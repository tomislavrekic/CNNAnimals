package hr.ferit.tomislavrekic.cnnanimals.presenter;

import hr.ferit.tomislavrekic.cnnanimals.model.NNModel;
import hr.ferit.tomislavrekic.cnnanimals.ui.MainActivity;
import hr.ferit.tomislavrekic.cnnanimals.utils.ClassifierCallback;
import hr.ferit.tomislavrekic.cnnanimals.utils.Constants;
import hr.ferit.tomislavrekic.cnnanimals.utils.DBContract;
import hr.ferit.tomislavrekic.cnnanimals.utils.NNContract;

public class NNPresenter implements NNContract.Presenter {
    private NNContract.View mView;
    private NNContract.Model mModel;

    private DBContract.Presenter mDBPresenter;

    public NNPresenter() {
        mModel = new NNModel();
        mModel.initClassifier(Constants.TF_MODEL_PATH, Constants.TF_LABEL_PATH, MainActivity.getContext());
    }

    @Override
    public void runThroughNN(final String imageKey) {
        mView.updatePicture(Constants.TEMP_IMG_KEY);
        mModel.runThroughNN(imageKey, new ClassifierCallback() {
            @Override
            public void processFinished(String guessedLabel, int guessedLabelIndex, float guessedActivation) {
                mView.updateText(guessedLabel, guessedActivation);

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
