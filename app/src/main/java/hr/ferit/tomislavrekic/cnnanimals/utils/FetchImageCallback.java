package hr.ferit.tomislavrekic.cnnanimals.utils;

import java.nio.ByteBuffer;

public interface FetchImageCallback {
    void processFinish(ByteBuffer output);
}
