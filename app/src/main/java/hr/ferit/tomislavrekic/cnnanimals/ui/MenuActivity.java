package hr.ferit.tomislavrekic.cnnanimals.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStreamWriter;

import hr.ferit.tomislavrekic.cnnanimals.R;
import hr.ferit.tomislavrekic.cnnanimals.presenter.NNPresenter;
import hr.ferit.tomislavrekic.cnnanimals.utils.Constants;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        int currentState = NNPresenter.readSettings();

        Spinner dropdown = findViewById(R.id.sDropdown);
        String[] items = new String[]{Constants.MODEL_NAMES[0], Constants.MODEL_NAMES[1]};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(currentState);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OutputStreamWriter outputStreamWriter = null;
                try {
                    outputStreamWriter = new OutputStreamWriter(MainActivity.getContext().openFileOutput(Constants.SETTINGS_FILENAME, Context.MODE_PRIVATE));
                    outputStreamWriter.write(String.valueOf(position));
                    outputStreamWriter.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


}
