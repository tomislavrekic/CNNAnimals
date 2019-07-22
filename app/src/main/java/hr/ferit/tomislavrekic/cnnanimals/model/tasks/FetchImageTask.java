package hr.ferit.tomislavrekic.cnnanimals.model.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import hr.ferit.tomislavrekic.cnnanimals.utils.FetchImageCallback;

import static hr.ferit.tomislavrekic.cnnanimals.utils.Constants.DIM_BATCH_SIZE;
import static hr.ferit.tomislavrekic.cnnanimals.utils.Constants.DIM_PIXEL_SIZE;
import static hr.ferit.tomislavrekic.cnnanimals.utils.Constants.IMAGE_MEAN;
import static hr.ferit.tomislavrekic.cnnanimals.utils.Constants.IMAGE_STD;
import static hr.ferit.tomislavrekic.cnnanimals.utils.Constants.CNN_DIM_X;
import static hr.ferit.tomislavrekic.cnnanimals.utils.Constants.CNN_DIM_Y;


public class FetchImageTask extends AsyncTask<String, Void, ByteBuffer> {

    public FetchImageCallback mCallback;
    private Context mContext;

    public FetchImageTask(Context context, FetchImageCallback callback) {
        mCallback = callback;
        mContext = context;

    }

    @Override
    protected ByteBuffer doInBackground(String... strings) {
        Bitmap temp = GetImage(strings[0]);
        if (temp == null) return null;
        return convertBitmapToByteBuffer(temp);
    }

    @Override
    protected void onPostExecute(ByteBuffer buffer) {
        mCallback.processFinish(buffer);
    }


    private Bitmap GetImage(String imageKey) {
        Bitmap bitmap;

        try {
            bitmap = BitmapFactory.decodeStream(mContext.openFileInput(imageKey));
            return Bitmap.createScaledBitmap(bitmap, CNN_DIM_X, CNN_DIM_Y, true);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private ByteBuffer convertBitmapToByteBuffer(Bitmap bitmap) {

        ByteBuffer tempBuffer = ByteBuffer.allocateDirect(4 * DIM_BATCH_SIZE * CNN_DIM_X * CNN_DIM_Y * DIM_PIXEL_SIZE);
        tempBuffer.order(ByteOrder.nativeOrder());
        int[] intValues = new int[CNN_DIM_X * CNN_DIM_Y];


        tempBuffer.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // Convert the image to floating point.

        int pixel = 0;
        for (int i = 0; i < CNN_DIM_X; ++i) {
            for (int j = 0; j < CNN_DIM_Y; ++j) {
                final int val = intValues[pixel++];
                tempBuffer.putFloat(((((val >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD));
                tempBuffer.putFloat(((((val >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD));
                tempBuffer.putFloat(((((val) & 0xFF) - IMAGE_MEAN) / IMAGE_STD));
            }
        }
        return tempBuffer;
    }
}
