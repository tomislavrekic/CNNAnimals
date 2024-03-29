package hr.ferit.tomislavrekic.cnnanimals.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.io.InputStream;

import hr.ferit.tomislavrekic.cnnanimals.R;
import hr.ferit.tomislavrekic.cnnanimals.descriptiondb.DescriptionDbSingleUnit;
import hr.ferit.tomislavrekic.cnnanimals.utils.Constants;

public class DBDialogFragment extends DialogFragment {

    public static  DBDialogFragment newInstance(DescriptionDbSingleUnit data){
        DBDialogFragment f = new DBDialogFragment();


        Bundle args = new Bundle();
        args.putSerializable(Constants.DF_NAME_KEY, data.getName());
        args.putSerializable(Constants.DF_IMAGE_KEY, data.getPicture());
        args.putSerializable(Constants.DF_GUESS_KEY, data.getGuess());
        args.putSerializable(Constants.DF_COUNT_KEY, data.getGuessCount());
        args.putSerializable(Constants.DF_INFO_KEY, data.getInfo());
        args.putSerializable(Constants.DF_DATE_KEY, data.getLastSeen());

        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_db_fragment, container, false);

        Bundle args = getArguments();

        ImageView ivImage;
        TextView tvName;
        TextView tvDate;
        TextView tvGuess;
        TextView tvCount;
        TextView tvInfo;
        Button btnMore;

        ivImage = v.findViewById(R.id.ivDetailImage);
        tvName = v.findViewById(R.id.tvDetailName);
        tvDate = v.findViewById(R.id.tvDetailDate);
        tvGuess = v.findViewById(R.id.tvDetailGuess);
        tvCount = v.findViewById(R.id.tvDetailCount);
        tvInfo = v.findViewById(R.id.tvDetailInfo);
        btnMore = v.findViewById(R.id.btnMore);

        final String tempName = args.getString(Constants.DF_NAME_KEY);
        String tempDate = args.getString(Constants.DF_DATE_KEY);
        String tempInfo = args.getString(Constants.DF_INFO_KEY);
        String tempImage = args.getString(Constants.DF_IMAGE_KEY);
        float tempGuess = args.getFloat(Constants.DF_GUESS_KEY);
        int tempCount = args.getInt(Constants.DF_COUNT_KEY);

        InputStream tempStream = null;

        try {
            tempStream = v.getContext().openFileInput(tempImage);
        }
        catch (IOException e){
            e.printStackTrace();
        }

        int[] colorArray = v.getContext().getResources().getIntArray(R.array.ratingColors);

        for (int j=0; j<Constants.GUESS_RATING_STAGES.length; j++){
            if(tempGuess > Constants.GUESS_RATING_STAGES[j]){
                continue;
            }
            else {
                tvGuess.setTextColor(colorArray[j]);
                break;
            }
        }

        ivImage.setImageBitmap(BitmapFactory.decodeStream(tempStream));
        tvName.setText(tempName);
        tvInfo.setText(tempInfo);
        tvDate.setText(tempDate);
        tvGuess.setText(String.valueOf(tempGuess));
        tvCount.setText(String.valueOf(tempCount));

        tvInfo.setMovementMethod(new ScrollingMovementMethod());
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.WIKI_PAGE_BASE + tempName));
                startActivity(browserIntent);
            }
        });

        return v;
    }
}
