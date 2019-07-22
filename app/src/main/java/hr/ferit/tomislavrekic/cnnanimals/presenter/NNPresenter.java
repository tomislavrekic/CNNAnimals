package hr.ferit.tomislavrekic.cnnanimals.presenter;

import android.util.Log;

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

    public NNPresenter() {
        mModel = new NNModel();
        mModel.initClassifier(Constants.TF_MODEL_PATH, Constants.TF_LABEL_PATH, MainActivity.getContext());

        mDBPresenter = new DBPresenter();
    }

    @Override
    public void runThroughNN(final String imageKey) {
        mView.updatePicture(Constants.TEMP_IMG_KEY);
        mModel.runThroughNN(imageKey, new ClassifierCallback() {
            @Override
            public void processFinished(int guessedLabelIndex, float guessedActivation) {
                mView.updateText("act: " + guessedActivation + "\n" + "ind: " + guessedLabelIndex);
                Log.d(TAG, "processFinished: " + guessedActivation + " ind " + guessedLabelIndex);

                mDBPresenter.updateRow(guessedLabelIndex, guessedActivation, imageKey);
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
}
