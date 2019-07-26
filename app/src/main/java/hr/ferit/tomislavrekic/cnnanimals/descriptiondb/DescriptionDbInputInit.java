package hr.ferit.tomislavrekic.cnnanimals.descriptiondb;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import hr.ferit.tomislavrekic.cnnanimals.utils.Constants;
import hr.ferit.tomislavrekic.cnnanimals.utils.CreateFileFromBitmap;

import static hr.ferit.tomislavrekic.cnnanimals.utils.Constants.TAG;

public final class DescriptionDbInputInit {


    private static List<String> initLabels(Context context) {
        List<String> labels = new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(Constants.TF_LABEL_PATH)));
            String line;
            while((line = reader.readLine()) != null){
                labels.add(line);
            }
            return labels;
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private static List<String> initDesc(Context context){
        /*List<String> descs = new ArrayList<>();
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(Constants.DF_DESC_PATH)));
            String line;
            while((line = reader.readLine()) != null){
                descs.add(line);
            }
            return descs;
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }*/
        return initLabels(context);
    }

    //TODO: method for reading descriptions

    public static void initDb(Context context){
        DescriptionDbController tempController = new DescriptionDbController(context);

        if(tempController.readAll().size() != 0){
            return;
        }

        List<String> labels = initLabels(context);
        List<String> descs = initDesc(context);

        InputStream stream = null;
        AssetManager assetManager = context.getAssets();

        try{
            stream = assetManager.open(Constants.QUESTION_ICON);
        }
        catch (IOException e){
            Log.e(TAG, "initDb: " + e.toString());
        }

        Bitmap scaled = null;

        if(stream != null){
            Bitmap temp = BitmapFactory.decodeStream(stream);
            scaled = Bitmap.createScaledBitmap(temp, Constants.DB_IMG_DIM_X,Constants.DB_IMG_DIM_Y,true);
        }

        CreateFileFromBitmap.createFileFromBitmap(scaled, context, Constants.NO_IMG_KEY);

        for(int i=0; i<labels.size();i++){
            DescriptionDbSingleUnit tempUnit = new DescriptionDbSingleUnit(labels.get(i), descs.get(i), Constants.NO_IMG_KEY, 0.0f, 0, "01/01/1000" );

            tempController.insertRow(tempUnit);
        }


    }




}
