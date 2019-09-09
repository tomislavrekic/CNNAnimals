package hr.ferit.tomislavrekic.cnnanimals.descriptiondb;

import android.content.Context;

import java.io.File;

import hr.ferit.tomislavrekic.cnnanimals.utils.Constants;

public class DescriptionDbUpdateManager {
    private DescriptionDbController controller;
    private static float minGuessValue = Constants.MIN_GUESS_UPDATE_VALUE;
    private Context mContext;

    public DescriptionDbUpdateManager(Context context){
        controller = new DescriptionDbController(context);
        mContext = context;
    }

    public void updateRow(DescriptionDbSingleUnit input){
        if(input.getGuess() < minGuessValue){
            return;
        }

        DescriptionDbSingleUnit tempUnit = controller.readDb(input.getName()).get(0);
        input.setGuessCount(1 + tempUnit.getGuessCount());

        if(input.getGuess() > tempUnit.getGuess()){
            File temp = new File(mContext.getFilesDir() , input.getPicture());

            temp.renameTo(new File(mContext.getFilesDir() , input.getName()));
            input.setPicture(input.getName());

            updatePic(input);
        }
        else {
            updateGuess(input);
        }
    }

    private void updateFull(DescriptionDbSingleUnit input){
        controller.updateRow(input, DescriptionDbController.Mode.UPDATE_FULL);
    }

    private void updatePic(DescriptionDbSingleUnit input){
        controller.updateRow(input, DescriptionDbController.Mode.UPDATE_PICTURE);
    }

    private void updateGuess(DescriptionDbSingleUnit input){
        controller.updateRow(input, DescriptionDbController.Mode.UPDATE_COUNTER);
    }


}
