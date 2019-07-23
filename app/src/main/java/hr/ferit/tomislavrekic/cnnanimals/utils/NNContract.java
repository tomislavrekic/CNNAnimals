package hr.ferit.tomislavrekic.cnnanimals.utils;

import android.content.Context;

public interface NNContract {
    interface View{
        void updateText(String guessedLabel, float activation);
        void updatePicture(String uri);
    }

    interface Presenter{
        void runThroughNN(String imageKey);
        void addView(NNContract.View view);
        void removeView();
    }

    interface Model{
        void runThroughNN(String imageKey, ClassifierCallback callback);
        void initClassifier(String modelPath, String labelPath, Context context);
    }
}
