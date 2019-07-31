
package hr.ferit.tomislavrekic.cnnanimals.model.networking.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Query {

    @SerializedName("pages")
    @Expose
    private Map<String, Page> pageMap;

    public Map<String, Page> getPageMap() {
        return pageMap;
    }

    public void setPageMap(Map<String, Page> pageMap) {
        this.pageMap = pageMap;
    }

}