package hr.ferit.tomislavrekic.cnnanimals.presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import hr.ferit.tomislavrekic.cnnanimals.model.NNModel;
import hr.ferit.tomislavrekic.cnnanimals.ui.MainActivity;
import hr.ferit.tomislavrekic.cnnanimals.utils.ClassifierCallback;
import hr.ferit.tomislavrekic.cnnanimals.utils.Constants;
import hr.ferit.tomislavrekic.cnnanimals.utils.DBContract;
import hr.ferit.tomislavrekic.cnnanimals.utils.NNContract;

import static hr.ferit.tomislavrekic.cnnanimals.utils.Constants.TAG;

public class NNPresenter implements NNContract.Presenter {
    private NNContract.View mView;
    private NNContract.Model mModel;

    private DBContract.Presenter mDBPresenter;
    private int currentMode;

    public NNPresenter() {
        initSettingsFile();
        currentMode = readSettings();
        Log.d(TAG, "NNPresenter: " + currentMode);
        initModel(currentMode);
    }

    private void initModel(int mode){
        mModel = new NNModel();
        mModel.initClassifier(Constants.TF_MODEL_PATHS[mode], Constants.TF_LABEL_PATH, MainActivity.getContext());
    }

    public static int readSettings() {
        int settingsValue = 0;

        File file = new File(MainActivity.getContext().getFilesDir(), Constants.SETTINGS_FILENAME);
        if (file.exists() && (file.length() != 0)) {
            InputStream inputStream;
            try {
                inputStream = MainActivity.getContext().openFileInput(Constants.SETTINGS_FILENAME);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                settingsValue = Integer.valueOf(builder.toString());
                reader.close();
                return settingsValue;
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        }
        return 0;
    }

    private void initSettingsFile() {
        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(MainActivity.getContext().openFileOutput(Constants.SETTINGS_FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(Constants.SETTINGS_DEFAULT);
            outputStreamWriter.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void runThroughNN(final String imageKey) {
        if(currentMode != readSettings()){
            currentMode = readSettings();
            initModel(currentMode);
        }
        mView.updatePicture(Constants.TEMP_IMG_KEY);
        mModel.runThroughNN(imageKey, new ClassifierCallback() {
            @Override
            public void processFinished(String label, int labelIndex, float activation) {
                mView.updateText(label, activation);

                mDBPresenter.updateRow(labelIndex, activation, imageKey);
            }
        });
    }

    @Override
    public void addView(NNContract.View view) {
        mView=view;
    }

    @Override
    public void removeView() {
        mView = null;
    }

    @Override
    public void showLoading() {
        mView.showLoading();
    }

    @Override
    public void hideLoading() {
        mView.hideLoading();
    }

    @Override
    public void initDB() {
        mDBPresenter = new DBPresenter();
        mDBPresenter.addPresenter(this);
        mDBPresenter.initDB();
    }
}
