package hr.ferit.tomislavrekic.cnnanimals.utils;

import android.content.Context;

public interface NNContract {
    interface View{
        void updateText(String guessedLabel, float activation);
        void updatePicture(String uri);
        void showLoading();
        void hideLoading();
        void showErrorMessage(String message);
    }

    interface Presenter{
        void runThroughNN(String imageKey);
        void addView(NNContract.View view);
        void removeView();
        void showLoading();
        void hideLoading();
        void initDB();
        void sendErrorMessage(String message);
    }

    interface Model{
        void runThroughNN(String imageKey, ClassifierCallback callback);
        void initClassifier(String modelPath, String labelPath, Context context);
    }
}
