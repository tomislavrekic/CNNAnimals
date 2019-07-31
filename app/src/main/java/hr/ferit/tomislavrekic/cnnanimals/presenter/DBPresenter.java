package hr.ferit.tomislavrekic.cnnanimals.presenter;

import java.util.List;

import hr.ferit.tomislavrekic.cnnanimals.descriptiondb.DescriptionDbSingleUnit;
import hr.ferit.tomislavrekic.cnnanimals.model.DBModel;
import hr.ferit.tomislavrekic.cnnanimals.utils.DBCallback;
import hr.ferit.tomislavrekic.cnnanimals.utils.DBContract;
import hr.ferit.tomislavrekic.cnnanimals.utils.NNContract;
import hr.ferit.tomislavrekic.cnnanimals.utils.VoidCallback;

public class DBPresenter implements DBContract.Presenter {

    private DBContract.Model mModel;
    private DBContract.View mView;
    private NNContract.Presenter mNNPresenter;

    public DBPresenter() {
        mModel = new DBModel(new VoidCallback() {
            @Override
            public void processFinished() {
                mNNPresenter.hideLoading();
            }
        });
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

    @Override
    public void addPresenter(NNContract.Presenter presenter) {
        mNNPresenter = presenter;
    }

    @Override
    public void initDB() {
        if(mModel.dBIsEmpty())
        {
            mNNPresenter.showLoading();
            mModel.initDB();
        }

    }
}
