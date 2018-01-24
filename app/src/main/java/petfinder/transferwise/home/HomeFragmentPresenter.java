package petfinder.transferwise.home;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import petfinder.transferwise.PetfinderApplication;
import petfinder.transferwise.dao.Catalogue;
import petfinder.transferwise.rest.RxErrorHandlingCallAdapterFactory;
import petfinder.transferwise.utils.Connectivity;
import petfinder.transferwise.utils.GlobalConstants;
import petfinder.transferwise.utils.RetrofitApi;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static petfinder.transferwise.dao.managers.MyOpenHelper.getCatalogue;

public class HomeFragmentPresenter {

    private String TAG = "CATALOGUE";
    private IHomeFragment mainView;
    Context mContext;

    public HomeFragmentPresenter(IHomeFragment mainView, Context ctx) {
        this.mainView = mainView;
        this.mContext = ctx;
    }

    public void getBreedList(final String animal) {

        if (!Connectivity.isConnected(mContext)) {
            try {
                List<Catalogue> list = getCatalogue();
                mainView.refreshResult(list);
            } catch (Exception ex) {
                ex.printStackTrace();
                //Toast.makeText(mContext, "Error: " + ex.getMessage() + "\n" + ex.getStackTrace().toString(), Toast.LENGTH_SHORT).show();
            }
        } else {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalConstants.SERVER_SSL_URL)
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                    //.addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            RetrofitApi authApi = retrofit.create(RetrofitApi.class);

            //authApi.getBreedList(GlobalConstants.PETFINDER_APIKEY, "json", "cat")
            authApi.getBreedListScalar(GlobalConstants.PETFINDER_APIKEY, "json", animal)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String json) {
                            try {
                                String name = "";
                                List<Catalogue> list = new ArrayList<Catalogue>();
                                PetfinderApplication.get().getOrCreateDaoSession().getCatalogueDao().deleteAll();

                                JSONObject jsonObj = new JSONObject(json);

                                JSONObject jPetfinder = jsonObj.getJSONObject("petfinder");

                                JSONObject jBreeds = jPetfinder.getJSONObject("breeds");

                                JSONArray breedList = jBreeds.getJSONArray("breed");

                                for (int i = 0; i < breedList.length(); i++) {
                                    JSONObject c = breedList.getJSONObject(i);
                                    name = c.getString("$t");

                                    Catalogue resultCatalog = new Catalogue();
                                    resultCatalog.setBreed(name);
                                    resultCatalog.setAnimal(animal);
                                    list.add(resultCatalog);
                                    insertToCatalogueDb(name, animal);
                                }

                                mainView.refreshResult(list);
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

    private void insertToCatalogueDb(String breed, String animal) {

        long daoCount = 0;
        daoCount = PetfinderApplication.get().getOrCreateDaoSession().getCatalogueDao().count();
        daoCount += 1;

        Catalogue cat = new Catalogue(daoCount, breed, animal);

        PetfinderApplication.get().getOrCreateDaoSession().getCatalogueDao().insertOrReplace(cat);
    }

}