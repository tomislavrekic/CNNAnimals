package hr.ferit.tomislavrekic.cnnanimals.descriptiondb;

import android.content.Context;
import android.util.Log;

import java.io.File;

import hr.ferit.tomislavrekic.cnnanimals.utils.Constants;

public class DescriptionDbUpdateManager {
    DescriptionDbController controller;
    private static float minGuessValue = Constants.MIN_GUESS_UPDATE_VALUE;
    private Context mContext;

    public DescriptionDbUpdateManager(Context context){
        controller = new DescriptionDbController(context);
        mContext = context;
    }

    public void UpdateRow(DescriptionDbSingleUnit input){
        if(input.getGuess() < minGuessValue){
            return;
        }

        DescriptionDbSingleUnit tempUnit = controller.readDb(input.getName()).get(0);
        input.setGuessCount(1 + tempUnit.getGuessCount());

        //TODO: For possible full update, check if Info is different, do that inside DescriptionDbSingleUnit class

        if(input.getGuess() > tempUnit.getGuess()){
            File temp = new File(mContext.getFilesDir() , input.getPicture());

            temp.renameTo(new File(mContext.getFilesDir() , input.getName()));
            input.setPicture(input.getName());

            UpdatePic(input);
        }
        else {
            UpdateGuess(input);
        }
    }

    private void UpdateFull(DescriptionDbSingleUnit input){
        controller.updateRow(input, DescriptionDbController.Mode.UPDATE_FULL);
    }

    private void UpdatePic(DescriptionDbSingleUnit input){
        controller.updateRow(input, DescriptionDbController.Mode.UPDATE_PICTURE);
    }

    private void UpdateGuess(DescriptionDbSingleUnit input){
        controller.updateRow(input, DescriptionDbController.Mode.UPDATE_COUNTER);
    }


}
