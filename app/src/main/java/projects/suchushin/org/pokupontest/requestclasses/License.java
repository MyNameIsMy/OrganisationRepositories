package projects.suchushin.org.pokupontest.requestclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class License {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("organisation_name")
    @Expose
    private String name;
    @SerializedName("spdx_id")
    @Expose
    private Object spdxId;
    @SerializedName("url")
    @Expose
    private Object url;
    @SerializedName("node_id")
    @Expose
    private String nodeId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getSpdxId() {
        return spdxId;
    }

    public void setSpdxId(Object spdxId) {
        this.spdxId = spdxId;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

}
