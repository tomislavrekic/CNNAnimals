package hr.ferit.tomislavrekic.cnnanimals.utils;

public interface ClassifierCallback {
        void processFinished(int guessedLabelIndex, float guessedActivation);
    }
