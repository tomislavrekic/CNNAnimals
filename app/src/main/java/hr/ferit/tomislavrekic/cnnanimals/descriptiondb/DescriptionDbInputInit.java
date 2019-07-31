package hr.ferit.tomislavrekic.cnnanimals.descriptiondb;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import hr.ferit.tomislavrekic.cnnanimals.model.networking.WikiDescService;
import hr.ferit.tomislavrekic.cnnanimals.model.networking.pojos.Page;
import hr.ferit.tomislavrekic.cnnanimals.model.networking.pojos.WikiDescResponse;
import hr.ferit.tomislavrekic.cnnanimals.utils.Constants;
import hr.ferit.tomislavrekic.cnnanimals.utils.CreateFileFromBitmap;
import hr.ferit.tomislavrekic.cnnanimals.utils.VoidCallback;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static hr.ferit.tomislavrekic.cnnanimals.utils.Constants.TAG;

public class DescriptionDbInputInit {

    private List<String> labels;
    private Dictionary<String, String> descs;
    private Context mContext;
    private int counter = 0;
    private VoidCallback mCallback;

    public DescriptionDbInputInit(Context context, VoidCallback callback){
        mContext=context;
        descs = new Hashtable<>();
        labels = new ArrayList<>();
        mCallback = callback;
    }

    private void initLabels() {
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(Constants.TF_LABEL_PATH)));
            String line;
            while((line = reader.readLine()) != null){
                labels.add(line);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void initDesc(){
        WikiDescService service = new WikiDescService();

        for (int j=0; j<labels.size(); j++){
            service.getResponse(labels.get(j), new Callback<WikiDescResponse>() {
                @Override
                public void onResponse(Call<WikiDescResponse> call, Response<WikiDescResponse> response) {
                    if(!response.isSuccessful()) return;

                    Page page = (Page)response.body().getQuery().getPageMap().values().toArray()[0];

                    String animName = page.getTitle();
                    animName = animName.replace(" ", "_");
                    String desc = page.getExtract();

                    descs.put(animName, desc);

                    counter++;

                    if(counter == labels.size()){
                        initDb();
                    }
                }

                @Override
                public void onFailure(Call<WikiDescResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.toString());
                }
            });
        }

    }

    public boolean dBIsEmpty(){
        DescriptionDbController tempController = new DescriptionDbController(mContext);

        if(tempController.readAll().size() != 0){
            return false;
        }
        return true;
    }

    public void initDbData(){
        initLabels();
        initDesc();

    }

    private void initDb(){
        DescriptionDbController tempController = new DescriptionDbController(mContext);
        InputStream stream = null;
        AssetManager assetManager = mContext.getAssets();

        try{
            stream = assetManager.open(Constants.QUESTION_ICON);
        }
        catch (IOException e){
            Log.e(TAG, "initDbData: " + e.toString());
        }

        Bitmap scaled = null;

        if(stream != null){
            Bitmap temp = BitmapFactory.decodeStream(stream);
            scaled = Bitmap.createScaledBitmap(temp, Constants.DB_IMG_DIM_X,Constants.DB_IMG_DIM_Y,true);
        }

        CreateFileFromBitmap.createFileFromBitmap(scaled, mContext, Constants.NO_IMG_KEY);

        for(int i=0; i<labels.size();i++){
            DescriptionDbSingleUnit tempUnit = new DescriptionDbSingleUnit(labels.get(i), descs.get(labels.get(i)), Constants.NO_IMG_KEY, 0.0f, 0, "never" );

            tempController.insertRow(tempUnit);
        }
        mCallback.processFinished();
    }
}
