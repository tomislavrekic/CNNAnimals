package hr.ferit.tomislavrekic.cnnanimals.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

import hr.ferit.tomislavrekic.cnnanimals.descriptiondb.DescriptionDbController;
import hr.ferit.tomislavrekic.cnnanimals.descriptiondb.DescriptionDbInputInit;
import hr.ferit.tomislavrekic.cnnanimals.descriptiondb.DescriptionDbUpdater;
import hr.ferit.tomislavrekic.cnnanimals.ui.MainActivity;
import hr.ferit.tomislavrekic.cnnanimals.utils.Constants;
import hr.ferit.tomislavrekic.cnnanimals.utils.DBCallback;
import hr.ferit.tomislavrekic.cnnanimals.utils.DBContract;
import hr.ferit.tomislavrekic.cnnanimals.utils.VoidCallback;

import static hr.ferit.tomislavrekic.cnnanimals.utils.Constants.DB_IMG_DIM_X;
import static hr.ferit.tomislavrekic.cnnanimals.utils.Constants.DB_IMG_DIM_Y;

public class DBModel implements DBContract.Model {

    private DescriptionDbUpdater mUpdater;
    private Context mContext;
    private DescriptionDbInputInit dbInputInit;

    public DBModel(VoidCallback callback) {
        mContext = MainActivity.getContext();
        mUpdater = new DescriptionDbUpdater(mContext, Classifier.initLabels(Constants.TF_LABEL_PATH, mContext));
        dbInputInit = new DescriptionDbInputInit(mContext, callback);
    }

    @Override
    public void initDB() {
        dbInputInit.initDbData();
    }

    @Override
    public void updateRow(int index, float activation, String imageKey) {
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(mContext.openFileInput(imageKey));
            Bitmap.createScaledBitmap(bitmap, DB_IMG_DIM_X, DB_IMG_DIM_Y, true);

        } catch (IOException e) {
            e.printStackTrace();
        }

        mUpdater.updateDb(index, activation, bitmap);
    }

    @Override
    public void readDB(DBCallback callback) {
        DescriptionDbController controller = new DescriptionDbController(mContext);
        callback.processFinished(controller.readAll());
    }

    @Override
    public boolean dBIsEmpty() {
        return dbInputInit.dBIsEmpty();
    }
}
