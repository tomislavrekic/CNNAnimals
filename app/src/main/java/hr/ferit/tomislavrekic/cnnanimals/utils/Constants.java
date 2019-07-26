package hr.ferit.tomislavrekic.cnnanimals.utils;

public final class Constants {
    public static final String TAG = "CNNTAG";

    public static final String TEMP_IMG_KEY = "tempImage.jpg";
    public static final String NO_IMG_KEY = "noImage";

    public static final int CNN_DIM_X = 224;
    public static final int CNN_DIM_Y = 224;
    public static final int DIM_BATCH_SIZE = 1;
    public static final int DIM_PIXEL_SIZE = 3;

    public static final int IMAGE_MEAN = 128;
    public static final float IMAGE_STD = 128.0f;

    public static final int DB_IMG_DIM_X = 100;
    public static final int DB_IMG_DIM_Y = 100;

    public static final String DF_NAME_KEY = "dbNameKey";
    public static final String DF_IMAGE_KEY = "dbImageKey";
    public static final String DF_GUESS_KEY = "dbGuessKey";
    public static final String DF_COUNT_KEY = "dbCountKey";
    public static final String DF_DATE_KEY = "dbDateKey";
    public static final String DF_INFO_KEY = "dbInfoKey";

    public static final String TF_MODEL_PATH = "Mobile2.tflite";
    public static final String TF_MODEL_KEY = "model.tflite";
    public static final String TF_LABEL_PATH = "labels.txt";

    public static final String DF_DESC_PATH = "desc.txt";

    public static final String BROADCAST_KEY1 = "hr.ferit.tomislavrekic.cnnanimals.sendbroadcast1";

    public static final String APP_LOGO = "FGMCLogo.png";
    public static final String QUESTION_ICON = "question_mark.png";

    public static final float[] GUESS_RATING_STAGES = {0.4f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f};

    public static final float MIN_GUESS_UPDATE_VALUE = 0.2f;

}
