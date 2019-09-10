package hr.ferit.tomislavrekic.cnnanimals.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;

import hr.ferit.tomislavrekic.cnnanimals.R;
import hr.ferit.tomislavrekic.cnnanimals.utils.Constants;
import hr.ferit.tomislavrekic.cnnanimals.utils.HelpItem;

public class HelpFragment extends Fragment {
    public static HelpFragment newInstance(HelpItem data){
        HelpFragment fragment = new HelpFragment();

        Bundle args = new Bundle();

        args.putSerializable(Constants.F_IMAGE_KEY, bitmapToByteArray(data.getImage()));
        args.putSerializable(Constants.F_TEXT_KEY, data.getText());

        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);

        Bundle args = getArguments();

        ImageView ivImage;
        TextView tvBody;

        ivImage = view.findViewById(R.id.ivHelpImage);
        tvBody = view.findViewById(R.id.tvHelpText);

        Bitmap tempImage = byteArrayToBitmap(args.getByteArray(Constants.F_IMAGE_KEY));
        String tempBody = args.getString(Constants.F_TEXT_KEY);

        ivImage.setImageBitmap(tempImage);
        tvBody.setText(tempBody);

        return view;
    }

    private static byte[] bitmapToByteArray(Bitmap input){
        if(input == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        input.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private static Bitmap byteArrayToBitmap(byte[] input){
        if(input == null) return null;
        return BitmapFactory.decodeByteArray(input, 0, input.length);
    }
}
