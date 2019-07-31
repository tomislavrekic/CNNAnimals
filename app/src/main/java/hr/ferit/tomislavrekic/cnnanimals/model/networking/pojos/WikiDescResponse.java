package hr.ferit.tomislavrekic.cnnanimals.model.networking.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WikiDescResponse {

    @SerializedName("batchcomplete")
    @Expose
    private String batchcomplete;
    @SerializedName("query")
    @Expose
    private Query query;

    public String getBatchcomplete() {
        return batchcomplete;
    }

    public void setBatchcomplete(String batchcomplete) {
        this.batchcomplete = batchcomplete;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

}
