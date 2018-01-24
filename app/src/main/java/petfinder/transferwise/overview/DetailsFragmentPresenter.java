package petfinder.transferwise.overview;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import petfinder.transferwise.PetfinderApplication;
import petfinder.transferwise.dao.Catalogue;
import petfinder.transferwise.dao.Pet;
import petfinder.transferwise.dao.Pet_Breed;
import petfinder.transferwise.dao.Pet_Contact;
import petfinder.transferwise.dao.Pet_Media;
import petfinder.transferwise.rest.RxErrorHandlingCallAdapterFactory;
import petfinder.transferwise.utils.Connectivity;
import petfinder.transferwise.utils.GlobalConstants;
import petfinder.transferwise.utils.RetrofitApi;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static petfinder.transferwise.dao.managers.MyOpenHelper.getBreedRandom;
import static petfinder.transferwise.dao.managers.MyOpenHelper.getCatalogue;
import static petfinder.transferwise.dao.managers.MyOpenHelper.getPetBreedById;
import static petfinder.transferwise.dao.managers.MyOpenHelper.getPetById;
import static petfinder.transferwise.dao.managers.MyOpenHelper.getPetContactById;
import static petfinder.transferwise.dao.managers.MyOpenHelper.getPetMediaById;
import static petfinder.transferwise.utils.Utilities.returnBooleanBasedOnJsonObjectName;

public class DetailsFragmentPresenter {

    private String TAG = "PET_DETAILS";
    private IDetailsFragment mainView;
    Context mContext;

    public DetailsFragmentPresenter(IDetailsFragment mainView, Context ctx) {
        this.mainView = mainView;
        this.mContext = ctx;
    }

    public void getPetRandom(final String animal, final String breed) {

        if (!Connectivity.isConnected(mContext)) {
            List<Pet_Media> petMedia = new ArrayList<Pet_Media>();
            List<Pet_Breed> listBreed = new ArrayList<Pet_Breed>();
            Pet_Contact contact = new Pet_Contact();

            Pet pet = getBreedRandom(breed);
            if (pet.getPet_id() !=null ) {
                petMedia = getPetMediaById(pet.getPet_id());
                listBreed = getPetBreedById(pet.getPet_id());
                contact = getPetContactById(pet.getPet_id());
            }
            mainView.refreshResult(pet, petMedia, listBreed, contact);

        } else {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalConstants.SERVER_SSL_URL)
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            RetrofitApi authApi = retrofit.create(RetrofitApi.class);

            authApi.getPetRandom(GlobalConstants.PETFINDER_APIKEY, "json", animal, breed, "basic")
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String json) {
                            try {
                                String mediaSize = "", link = "", name = "", sex = "", age = "", size = "", id = "", breed2 = "", description = "", dateUpdate = "", shelterId = "";
                                String phone="", email="", city="", zip="", state="", address="";
                                Integer petId = 0;
                                List<Pet_Media> listMedia = new ArrayList<Pet_Media>();;
                                List<Pet_Breed> listBreed = new ArrayList<Pet_Breed>();
                                //PetfinderApplication.get().getOrCreateDaoSession().getPetDao().deleteAll();


                                JSONObject jsonObj = new JSONObject(json);

                                JSONObject jPetfinder = jsonObj.getJSONObject("petfinder");

                                JSONObject jPet = jPetfinder.getJSONObject("pet");

                                JSONObject jAge = jPet.getJSONObject("age");
                                age = jAge.getString("$t");

                                JSONObject jSize = jPet.getJSONObject("size");
                                size = jSize.getString("$t");

                                JSONObject jId = jPet.getJSONObject("id");
                                id = jId.getString("$t");
                                petId = Integer.parseInt(id);


                                Pet ppp = getPetById(petId);
                                if (ppp != null) {
                                    PetfinderApplication.get().getOrCreateDaoSession().getPetDao().deleteByKey(ppp.getId());
                                }


                                JSONObject jMedia = jPet.getJSONObject("media");
                                if (jMedia.has("photos")) {
                                    JSONObject jPhotos = jMedia.getJSONObject("photos");
                                    //JSONObject jPhoto = jPhotos.getJSONObject("photo");
                                    JSONArray jPhoto = jPhotos.getJSONArray("photo");
                                    for (int i = 0; i < jPhoto.length(); i++) {
                                        JSONObject c = jPhoto.getJSONObject(i);
                                        if (c != null) {
                                            if (c.has("$t")) {
                                                mediaSize = c.getString("@size");
                                                if (mediaSize.equals("x")) {
                                                    link = c.getString("$t");

                                                    Pet_Media petMedia = new Pet_Media();
                                                    petMedia.setSize(mediaSize);
                                                    petMedia.setPet_id(petId);
                                                    petMedia.setPhoto(link);
                                                    listMedia.add(petMedia);
                                                    insertToMediaDb(petMedia);
                                                }
                                            }
                                        }
                                    }
                                }
                                JSONObject jBreeds = jPet.getJSONObject("breeds");
                                //if (returnBooleanBasedOnJsonObject(jBreeds.getJSONObject("breed"))) {
                                if (returnBooleanBasedOnJsonObjectName(jBreeds, "breed")) {
                                    JSONObject jBreed = jBreeds.getJSONObject("breed");
                                    breed2 = jBreed.getString("$t");
                                    //listBreed.add(breed2);
                                    Pet_Breed petBreed = new Pet_Breed();
                                    petBreed.setPet_id(petId);
                                    petBreed.setBreed(breed2);
                                    listBreed.add(petBreed);
                                } else {
                                    JSONArray jBreedArray = jBreeds.getJSONArray("breed");
                                    for (int i = 0; i < jBreedArray.length(); i++) {
                                        JSONObject c = jBreedArray.getJSONObject(i);
                                        if (c != null) {
                                            if (c.has("$t")) {
                                                breed2 = c.getString("$t");

                                                Pet_Breed petBreed = new Pet_Breed();
                                                petBreed.setPet_id(petId);
                                                petBreed.setBreed(breed2);
                                                listBreed.add(petBreed);
                                                insertToBreedDb(petId, breed2);
                                            }
                                        }
                                    }
                                }

                                JSONObject jName = jPet.getJSONObject("name");
                                if (jName.has("$t")) {
                                    name = jName.getString("$t");
                                }

                                JSONObject jSex = jPet.getJSONObject("sex");
                                if (jSex.has("$t")) {
                                    sex = jSex.getString("$t");
                                }

                                JSONObject jDescription = jPet.getJSONObject("description");
                                if (jDescription.has("$t")) {
                                    description = jDescription.getString("$t");
                                }

                                JSONObject jDateUpdate = jPet.getJSONObject("lastUpdate");
                                if (jDateUpdate.has("$t")) {
                                    dateUpdate = jDateUpdate.getString("$t");
                                }

                                JSONObject jShelterId = jPet.getJSONObject("shelterPetId");
                                if (jShelterId.has("$t")) {
                                    shelterId = jShelterId.getString("$t");
                                }


                                JSONObject jContact = jPet.getJSONObject("contact");
                                JSONObject jPhone = jContact.getJSONObject("phone");
                                if (jPhone.has("$t")) {
                                    phone = jPhone.getString("$t");
                                }

                                JSONObject jState = jContact.getJSONObject("state");
                                if (jState.has("$t")) {
                                    state = jState.getString("$t");
                                }

                                JSONObject jEmail = jContact.getJSONObject("email");
                                if (jEmail.has("$t")) {
                                    email = jEmail.getString("$t");
                                }

                                JSONObject jCity = jContact.getJSONObject("city");
                                if (jCity.has("$t")) {
                                    city = jCity.getString("$t");
                                }

                                JSONObject jZip = jContact.getJSONObject("zip");
                                if (jZip.has("$t")) {
                                    zip = jZip.getString("$t");
                                }

                                JSONObject jAddress = jContact.getJSONObject("address1");
                                if (jAddress.has("$t")) {
                                    address = jAddress.getString("$t");
                                }
                                if (address.equals("")) {
                                    jAddress = jContact.getJSONObject("address2");
                                    if (jAddress.has("$t")) {
                                        address = jAddress.getString("$t");
                                    }
                                }

                                Pet_Contact contact = new Pet_Contact();
                                contact.setPet_id(petId);
                                contact.setAddress(address);
                                contact.setCity(city);
                                contact.setEmail(email);
                                contact.setPhone(phone);
                                contact.setState(state);
                                contact.setZip(zip);
                                insertToContactDb(contact);


                                Pet pet = new Pet();
                                pet.setBreed(breed);
                                pet.setAnimal(animal);
                                pet.setName(name);
                                pet.setPet_id(petId);
                                pet.setAge(age);
                                pet.setDescription(description);
                                pet.setLast_update(dateUpdate);
                                pet.setSex(sex);
                                pet.setSize(size);
                                //pet.setShelterId(shelterId);
                                insertToPetDb(pet);

                                mainView.refreshResult(pet, listMedia, listBreed, contact);

                            } catch (final JSONException e) {
                                Log.e(TAG, "Json parsing error: " + e.getMessage());
                            }
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            mainView.showErrorServerResponse(throwable);
                        }
                    });
        }
    }

    private void insertToPetDb(Pet pet) {
        long daoCount = 0;
        daoCount = PetfinderApplication.get().getOrCreateDaoSession().getPetDao().count();
        daoCount += 1;
        pet.setId(daoCount);

        PetfinderApplication.get().getOrCreateDaoSession().getPetDao().insertOrReplace(pet);
    }

    private void insertToBreedDb(Integer petId, String breed) {
        long daoCount = 0;
        daoCount = PetfinderApplication.get().getOrCreateDaoSession().getPet_BreedDao().count();
        daoCount += 1;

        Pet_Breed pet = new Pet_Breed(daoCount, petId, breed);
        PetfinderApplication.get().getOrCreateDaoSession().getPet_BreedDao().insertOrReplace(pet);
    }

    private void insertToMediaDb(Pet_Media petMedia) {
        long daoCount = 0;
        daoCount = PetfinderApplication.get().getOrCreateDaoSession().getPet_MediaDao().count();
        daoCount += 1;
        petMedia.setId(daoCount);
        PetfinderApplication.get().getOrCreateDaoSession().getPet_MediaDao().insertOrReplace(petMedia);
    }

    private void insertToContactDb(Pet_Contact contact) {
        long daoCount = 0;
        daoCount = PetfinderApplication.get().getOrCreateDaoSession().getPet_ContactDao().count();
        daoCount += 1;
        contact.setId(daoCount);
        PetfinderApplication.get().getOrCreateDaoSession().getPet_ContactDao().insertOrReplace(contact);
    }

}