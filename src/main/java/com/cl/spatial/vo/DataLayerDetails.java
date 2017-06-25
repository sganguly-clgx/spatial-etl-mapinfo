package com.cl.spatial.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by sganguly on 6/24/2017.
 */

@JsonPropertyOrder(value = {"dataPath", "customLayerTag", "stateCode"})
@JsonRootName("DataLayerDetails")
public class DataLayerDetails {

    @JsonProperty
    private String dataPath;
    @JsonProperty
    private String customLayerTag;
    @JsonProperty
    private String stateCode;

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getCustomLayerTag() {
        return customLayerTag;
    }

    public void setCustomLayerTag(String customLayerTag) {
        this.customLayerTag = customLayerTag;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
}
