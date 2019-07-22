package hr.ferit.tomislavrekic.cnnanimals.utils;

import java.util.List;

import hr.ferit.tomislavrekic.cnnanimals.descriptiondb.DescriptionDbSingleUnit;

public interface DBCallback {
    void processFinished(List<DescriptionDbSingleUnit> data);
}
