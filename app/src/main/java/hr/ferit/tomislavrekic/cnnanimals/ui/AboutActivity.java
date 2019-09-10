package hr.ferit.tomislavrekic.cnnanimals.ui;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

import hr.ferit.tomislavrekic.cnnanimals.R;
import hr.ferit.tomislavrekic.cnnanimals.utils.Constants;

import static hr.ferit.tomislavrekic.cnnanimals.utils.Constants.TAG;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Resources res = getResources();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView image = findViewById(R.id.ivAboutImage);
        TextView title = findViewById(R.id.tvAboutTitle);
        TextView body = findViewById(R.id.tvAboutText);
        TextView author = findViewById(R.id.tvAboutAuthor);

        title.setText(res.getString(R.string.app_name));
        body.setText(res.getString(R.string.AboutText));
        author.setText("Author: Tomislav Rekić");

        InputStream stream = null;
        AssetManager assetManager = getAssets();

        try{
            stream = assetManager.open(Constants.APP_LOGO);
        }
        catch (IOException e){
            Log.e(TAG, "getImages: " + e.toString());
        }

        if(stream != null){
            Bitmap temp = BitmapFactory.decodeStream(stream);
            Bitmap scaled = Bitmap.createScaledBitmap(temp, temp.getWidth()/2,temp.getHeight()/2,true);
            image.setImageBitmap(scaled);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
