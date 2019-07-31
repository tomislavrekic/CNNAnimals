package hr.ferit.tomislavrekic.cnnanimals.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import hr.ferit.tomislavrekic.cnnanimals.R;
import hr.ferit.tomislavrekic.cnnanimals.presenter.NNPresenter;
import hr.ferit.tomislavrekic.cnnanimals.utils.Constants;
import hr.ferit.tomislavrekic.cnnanimals.utils.NNContract;

public class MainActivity extends AppCompatActivity implements NNContract.View {

    private ImageView ivPreview;
    private TextView tvGuess;
    private Button btnSwitch;
    private NNContract.Presenter presenter;

    private Intent intentCamera;

    private static Context context;

    private BroadcastReceiver receiver;
    private IntentFilter filter;

    private ActionBarDrawerToggle toggle;

    private Resources res;

    private ProgressDialog nDialog;

    public static Context getContext(){
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        res = getResources();

        context = getApplicationContext();

        presenter = new NNPresenter();
        presenter.addView(this);
        presenter.initDB();

        initViews();
        initIntents();

        configureReceiver();

        initNavBar();

    }

    @Override
    protected void onResume() {
        super.onResume();
        btnSwitch.setEnabled(true);
    }

    private void dispatchTakePictureIntent(){
        try {
            btnSwitch.setEnabled(false);
            startActivityIfNeeded(intentCamera,0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initViews(){
        ivPreview = findViewById(R.id.ivPreview);
        tvGuess = findViewById(R.id.tvGuess);
        btnSwitch = findViewById(R.id.btnSwitch);

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void initIntents() {
        intentCamera = new Intent(this, CameraActivity.class);
        intentCamera.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    }

    private void configureReceiver() {
        filter = new IntentFilter();
        filter.addAction(Constants.BROADCAST_KEY1);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                presenter.runThroughNN(Constants.TEMP_IMG_KEY);

                showLoadingText();
            }
        };
        receiverReg();
    }

    private void showLoadingText() {
        tvGuess.setText(getString(R.string.loadingText));
    }

    public void receiverReg(){
        registerReceiver(receiver,filter);
    }
    public void receiverUnreg(){
        unregisterReceiver(receiver);
    }

    @Override
    public void updateText(String guessedLabel, float activation) {
        tvGuess.setText(getString(R.string.guessText, guessedLabel, activation));
    }

    @Override
    public void updatePicture(String uri) {
        ivPreview.setImageBitmap(null);
        Picasso.get().load(new File(getFilesDir(), uri)).memoryPolicy(MemoryPolicy.NO_CACHE).into(ivPreview);
    }

    @Override
    public void showLoading() {
        nDialog = new ProgressDialog(this);
        nDialog.setMessage(res.getString(R.string.loading));
        nDialog.setTitle(res.getString(R.string.firstTimeInit));
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(false);
        nDialog.show();
    }

    @Override
    public void hideLoading() {
        if(nDialog.isShowing()){
            nDialog.hide();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        receiverUnreg();
    }

    @Override
    protected void onPause() {
        super.onPause();
        new DeleteImageTask().execute();
    }

    class DeleteImageTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            File dir = getFilesDir();
            File file = new File(dir, Constants.TEMP_IMG_KEY);
            if (!file.exists()) {
                return null;
            }
            file.delete();
            return null;
        }
    }

    private void initNavBar() {
        DrawerLayout drawerLayout = findViewById(R.id.dlDrawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.NavOpen, R.string.NavClose);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nvNav);

        ImageView image = navigationView.getHeaderView(0).findViewById(R.id.ivNavImage);

        InputStream stream = null;
        AssetManager assetManager = getAssets();



        try{
            stream = assetManager.open(Constants.APP_LOGO);
        }
        catch (IOException e){
            Log.e(Constants.TAG, "getImages: " + e.toString());
        }

        if(stream != null){
            Bitmap temp = BitmapFactory.decodeStream(stream);
            Bitmap scaled = Bitmap.createScaledBitmap(temp, temp.getWidth()/2,temp.getHeight()/2,true);
            image.setImageBitmap(scaled);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch(id)
                {
                    case R.id.Inav1:
                        Intent intent1 = new Intent(MainActivity.this, DatabaseActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivityIfNeeded(intent1, 0);
                        break;
                    case R.id.Inav2:
                        //Intent intent2 = new Intent(MainActivity.this, HelpActivity.class);
                        //intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        //startActivityIfNeeded(intent2, 0);
                        break;
                    case R.id.Inav3:
                        //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(FGMC_WEB_ADRESS));
                        //startActivity(browserIntent);

                        break;
                    default:
                        return true;
                }


                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(toggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
