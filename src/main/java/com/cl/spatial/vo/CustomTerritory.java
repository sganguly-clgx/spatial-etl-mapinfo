package com.cl.spatial.vo;

import lombok.Data;

/**
 * Created by sganguly on 6/12/2017.
 */
@Data
public class CustomTerritory {
    private String customLayerTag;
    private String partitionColumn;
    private String attrib;
    private String geomWKT;
}
