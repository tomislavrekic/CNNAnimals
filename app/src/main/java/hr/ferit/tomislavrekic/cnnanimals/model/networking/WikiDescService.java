package hr.ferit.tomislavrekic.cnnanimals.model.networking;

import hr.ferit.tomislavrekic.cnnanimals.model.networking.pojos.WikiDescResponse;
import hr.ferit.tomislavrekic.cnnanimals.utils.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=1&explaintext=1&titles=Dog

public class WikiDescService {
    private Call<WikiDescResponse> mCallAsync;
    private WikiDescAPI wikiDescAPI;

    public WikiDescService(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(Constants.WIKI_API_BASE).
                addConverterFactory(GsonConverterFactory.create()).
                client(httpClient.build()).
                build();

        wikiDescAPI = retrofit.create(WikiDescAPI.class);
    }


    public void getResponse(String title, Callback<WikiDescResponse> callback){
        mCallAsync = wikiDescAPI.getResponse(
                "json",
                "query",
                "extracts",
                "1",
                "1",
                title);
        mCallAsync.clone().enqueue(callback);
    }
}
