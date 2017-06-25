package com.cl.spatial.vo;

import lombok.Data;

import java.util.LinkedHashMap;

@Data
public class SpatialDataRow {

    private String partitionColumn;
    private LinkedHashMap<String, String> attributeMap;
    private String geometryInWKT;

}
