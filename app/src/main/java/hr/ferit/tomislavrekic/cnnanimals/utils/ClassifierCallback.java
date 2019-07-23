package hr.ferit.tomislavrekic.cnnanimals.utils;

public interface ClassifierCallback {
        void processFinished(String guessedLabel, int guessedLabelIndex, float guessedActivation);
    }
