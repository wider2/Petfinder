package petfinder.transferwise.utils;

import okhttp3.ResponseBody;
import petfinder.transferwise.dao.Catalogue;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface RetrofitApi {

    //http://api.petfinder.com/breed.list?key=9b7dfd63eac20a8eb8cf5e183691d5f9&format=json&animal=cat
    @GET("/breed.list")
        //Observable<Catalogue> getBreedList (@Query("key") String key, @Query("format") String format, @Query("animal") String animal);
    Observable<String> getBreedListScalar(@Query("key") String key, @Query("format") String format, @Query("animal") String animal);


    //http://api.petfinder.com/pet.getRandom?key=b6ba5a567c1e4f543b3c8a4b7ae0cf76&animal=cat&breed=Abyssinian&output=basic&format=json
    @GET("/pet.getRandom")
    Observable<String> getPetRandom(@Query("key") String key, @Query("format") String format,
                                    @Query("animal") String animal, @Query("breed") String breed,
                                    @Query("output") String output);

}