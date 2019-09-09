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
import hr.ferit.tomislavrekic.cnnanimals.utils.DBContract;
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
    private DBContract.Model mModel;

    public DescriptionDbInputInit(Context context, VoidCallback callback, DBContract.Model model){
        mContext=context;
        descs = new Hashtable<>();
        labels = new ArrayList<>();
        mCallback = callback;
        mModel = model;
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

    public void initDesc(){
        if(labels.size() == 0){
            initLabels();
        }
        WikiDescService service = new WikiDescService();

        Log.d(TAG, "initDesc: enter");
        Log.d(TAG, "initDesc: " + labels.size());
        for (int j=0; j<labels.size(); j++){
            service.getResponse(labels.get(j), new Callback<WikiDescResponse>() {
                @Override
                public void onResponse(Call<WikiDescResponse> call, Response<WikiDescResponse> response) {
                    if(!response.isSuccessful()) return;
                    Log.d(TAG, "onResponse: test");
                    Page page = (Page)response.body().getQuery().getPageMap().values().toArray()[0];

                    String animName = call.request().url().queryParameter("titles");
                    String desc = page.getExtract();

                    descs.put(animName, desc);

                    counter++;

                    Log.d(TAG, "onResponse: " + counter);

                    if(counter == labels.size()){
                        updateDescs();
                    }
                }

                @Override
                public void onFailure(Call<WikiDescResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.toString());
                    sendError(t.getMessage());

                    String animName = call.request().url().queryParameter("titles");
                    String desc = "";

                    descs.put(animName, desc);

                    counter++;

                    if(counter == labels.size()){
                        updateDescs();
                    }
                }
            });
        }
    }

    private void updateDescs(){
        DescriptionDbController controller = new DescriptionDbController(mContext);
        List<DescriptionDbSingleUnit> data = controller.readAll();
        for(int i = 0; i<labels.size(); i++){
            DescriptionDbSingleUnit input = data.get(i);
            input.setInfo(descs.get(input.getName()));
            controller.updateRow(input, DescriptionDbController.Mode.UPDATE_FULL);
        }
        Log.d(TAG, "updateDescs: finished");
        mCallback.processFinished();
    }

    private void sendError(String message){
        mModel.sendErrorMessage(message);
    }

    public void initDbData(){
        initLabels();
        initDb();

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
            scaled = Bitmap.createScaledBitmap(
                    temp,
                    Constants.DB_IMG_DIM_X,
                    Constants.DB_IMG_DIM_Y,
                    true);
        }

        CreateFileFromBitmap.createFileFromBitmap(scaled, mContext, Constants.NO_IMG_KEY);

        for(int i=0; i<labels.size();i++){
            DescriptionDbSingleUnit tempUnit = new DescriptionDbSingleUnit(
                    labels.get(i),
                    "",
                    Constants.NO_IMG_KEY,
                    0.0f,
                    0,
                    "never" );

            tempController.insertRow(tempUnit);
        }
        initDesc();
    }
}
