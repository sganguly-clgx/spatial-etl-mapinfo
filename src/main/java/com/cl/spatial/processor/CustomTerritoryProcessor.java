package com.cl.spatial.processor;

import com.cl.spatial.vo.CustomTerritory;
import com.cl.spatial.vo.SpatialDataRow;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sganguly on 6/12/2017.
 */
@Component
public class CustomTerritoryProcessor implements SpatialDataProcessor {

    private final Logger logger = LoggerFactory.getLogger(CustomTerritoryProcessor.class);


    @Override
    public <T> List<T> process(List<SpatialDataRow> spatialDataRows, String customLayerTag) {
        List<CustomTerritory> territories = new ArrayList<>();

        for (SpatialDataRow row : spatialDataRows) {
            CustomTerritory custTer = new CustomTerritory();
            custTer.setCustomLayerTag(customLayerTag);
            custTer.setPartitionColumn(row.getPartitionColumn());

            Gson gson = new Gson();
            String attribJSON = gson.toJson(row.getAttributeMap());
            custTer.setAttrib(attribJSON);

            custTer.setGeomWKT(row.getGeometryInWKT());

            territories.add(custTer);
        }

        return (List<T>) territories;
    }
}
