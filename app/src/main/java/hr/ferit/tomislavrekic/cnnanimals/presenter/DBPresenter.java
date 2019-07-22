package hr.ferit.tomislavrekic.cnnanimals.presenter;

import java.util.List;

import hr.ferit.tomislavrekic.cnnanimals.descriptiondb.DescriptionDbSingleUnit;
import hr.ferit.tomislavrekic.cnnanimals.model.DBModel;
import hr.ferit.tomislavrekic.cnnanimals.utils.DBCallback;
import hr.ferit.tomislavrekic.cnnanimals.utils.DBContract;

public class DBPresenter implements DBContract.Presenter {

    private DBContract.Model mModel;
    private DBContract.View mView;

    public DBPresenter() {
        mModel = new DBModel();
    }

    @Override
    public void updateRow(int index, float activation, String imageKey) {
        mModel.updateRow(index,activation, imageKey);
    }

    @Override
    public void readDB() {
        mModel.readDB(new DBCallback() {
            @Override
            public void processFinished(List<DescriptionDbSingleUnit> data) {
                if(mView != null){
                    mView.updateData(data);
                }
            }
        });
    }

    @Override
    public void addView(DBContract.View view) {
        mView = view;
    }

    @Override
    public void removeView() {
        mView = null;
    }
}
