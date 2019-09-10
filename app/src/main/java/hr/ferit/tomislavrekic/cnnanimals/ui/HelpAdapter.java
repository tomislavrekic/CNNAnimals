package hr.ferit.tomislavrekic.cnnanimals.ui;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import hr.ferit.tomislavrekic.cnnanimals.utils.HelpItem;

public class HelpAdapter extends FragmentPagerAdapter {

    private List<HelpItem> mData;

    public void setmData(List<HelpItem> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public HelpAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return HelpFragment.newInstance(mData.get(i));
    }

    @Override
    public int getCount() {
        return mData.size();
    }
}
