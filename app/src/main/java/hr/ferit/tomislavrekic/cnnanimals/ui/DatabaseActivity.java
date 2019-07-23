package hr.ferit.tomislavrekic.cnnanimals.ui;


import android.os.Bundle;

import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hr.ferit.tomislavrekic.cnnanimals.R;
import hr.ferit.tomislavrekic.cnnanimals.descriptiondb.DescriptionDbController;
import hr.ferit.tomislavrekic.cnnanimals.descriptiondb.DescriptionDbInputInit;
import hr.ferit.tomislavrekic.cnnanimals.descriptiondb.DescriptionDbSingleUnit;
import hr.ferit.tomislavrekic.cnnanimals.utils.OnClickListener;

public class DatabaseActivity extends AppCompatActivity {
    DescriptionDbController controller;
    List<DescriptionDbSingleUnit> items;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        DescriptionDbInputInit.initDb(this);

        controller = new DescriptionDbController(this);

        items = controller.readAll();

        recyclerView = (RecyclerView) findViewById(R.id.rvDescriptionDb);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new DbListAdapter(items, this,   new OnClickListener() {
            @Override
            public void onClick(int pos) {
                //show dialog
                showDialogFragment(pos);
            }
        });
        recyclerView.setAdapter(mAdapter);


    }

    void showDialogFragment(int pos) {
        DialogFragment df = DBDialogFragment.newInstance(items.get(pos));
        df.show(this.getSupportFragmentManager(), "DBD");
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}


