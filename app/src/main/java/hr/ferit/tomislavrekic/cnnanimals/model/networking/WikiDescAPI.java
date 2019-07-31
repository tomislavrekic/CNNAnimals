package hr.ferit.tomislavrekic.cnnanimals.model.networking;

import hr.ferit.tomislavrekic.cnnanimals.model.networking.pojos.WikiDescResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WikiDescAPI {
    //https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=1&explaintext=1&titles=Dog
    @GET("w/api.php")
    Call<WikiDescResponse> getResponse(
            @Query("format") String format,
            @Query("action") String action,
            @Query("prop") String prop,
            @Query("exintro") String exintro,
            @Query("explaintext") String explaintext,
            @Query("titles") String title
    );
}
