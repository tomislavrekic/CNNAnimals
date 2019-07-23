package hr.ferit.tomislavrekic.cnnanimals.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import hr.ferit.tomislavrekic.cnnanimals.R;
import hr.ferit.tomislavrekic.cnnanimals.descriptiondb.DescriptionDbSingleUnit;
import hr.ferit.tomislavrekic.cnnanimals.utils.Constants;
import hr.ferit.tomislavrekic.cnnanimals.utils.OnClickListener;

import static hr.ferit.tomislavrekic.cnnanimals.utils.Constants.TAG;

public class DbListAdapter extends RecyclerView.Adapter<DbListAdapter.DbListViewHolder> {
    private List<DescriptionDbSingleUnit> mData;
    private Context mContext;
    private OnClickListener listener;
    private Resources resources;
    private int[] colorArray;


    @NonNull
    @Override
    public DbListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_listitem, viewGroup, false);

        resources = mContext.getResources();

        colorArray = resources.getIntArray(R.array.ratingColors);
        DbListViewHolder vh = new DbListViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull DbListViewHolder dbListViewHolder, int i) {
        InputStream tempStream = null;
        try {
             tempStream = mContext.openFileInput(mData.get(i).getPicture());
        }
        catch (IOException e){
            e.printStackTrace();
        }

        float guess = mData.get(i).getGuess();

        for (int j=0; j<Constants.GUESS_RATING_STAGES.length; j++){
            if(guess > Constants.GUESS_RATING_STAGES[j]){
                continue;
            }
            else {
                dbListViewHolder.tvGuess.setTextColor(colorArray[j]);
                break;
            }

        }

        dbListViewHolder.ivThumbnail.setImageBitmap(BitmapFactory.decodeStream(tempStream));
        dbListViewHolder.tvName.setText(mData.get(i).getName());
        dbListViewHolder.tvGuess.setText(String.format("%.3f", guess));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class DbListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivThumbnail;
        public TextView tvName;
        public TextView tvGuess;

        public DbListViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvName = itemView.findViewById(R.id.tvName);
            tvGuess = itemView.findViewById(R.id.tvGuess2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(getAdapterPosition());
        }
    }

    public DbListAdapter(List<DescriptionDbSingleUnit> input, Context context, OnClickListener listener){
        mData=input;
        mContext = context;
        this.listener=listener;
    }



}


