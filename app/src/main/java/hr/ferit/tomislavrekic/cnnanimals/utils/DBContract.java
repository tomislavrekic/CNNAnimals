package hr.ferit.tomislavrekic.cnnanimals.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;

import hr.ferit.tomislavrekic.cnnanimals.descriptiondb.DescriptionDbSingleUnit;

public interface DBContract {
    interface View{
        void updateData(List<DescriptionDbSingleUnit> data);
    }
    interface Presenter {
        void updateRow(int index, float activation, String  imageKey);
        void readDB();
        void addView(DBContract.View view);
        void removeView();
        void addPresenter(NNContract.Presenter presenter);
        void initDB();
        void sendErrorNessage(String message);

    }
    interface Model {
        void initDB();
        void updateRow(int index, float activation, String imageKey);
        void readDB(DBCallback callback);
        boolean dBIsEmpty();
        void sendErrorMessage(String message);
        boolean dBNoDescs();
        void updateDescs();
    }
}
