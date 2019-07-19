package hr.ferit.tomislavrekic.cnnanimals.model;

import android.content.Context;

import hr.ferit.tomislavrekic.cnnanimals.ui.MainActivity;
import hr.ferit.tomislavrekic.cnnanimals.utils.ClassifierCallback;
import hr.ferit.tomislavrekic.cnnanimals.utils.NNContract;

public class NNModel implements NNContract.Model {
    NNContract.Presenter presenter;

    Classifier classifier;



    @Override
    public void runThroughNN(String imageKey, ClassifierCallback callback) {
        classifier.classify(callback, imageKey);
    }

    @Override
    public void initClassifier(String modelPath, String labelPath, Context context) {
        classifier = new Classifier(modelPath, labelPath, context);
    }
}
