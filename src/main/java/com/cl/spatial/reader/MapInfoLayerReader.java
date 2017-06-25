package com.cl.spatial.reader;

import com.cl.spatial.vo.SpatialDataRow;
import org.gdal.ogr.Feature;
import org.gdal.ogr.FieldDefn;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by sganguly on 6/13/2017.
 */
@Component
public class MapInfoLayerReader {

    private final Logger logger = LoggerFactory.getLogger(MapInfoLayerReader.class);

    public List<SpatialDataRow> readLayer(Layer layer, String partionColValue) {
        List<SpatialDataRow> rows = new ArrayList<>();

        if (layer == null) {
            return rows;
        }

        int featureCnt = layer.GetFeatureCount();

        logger.debug("Number of Features = " + featureCnt);

        // Convert all features to JSON
        for (int i = 1; i <= featureCnt; i++) {
            Feature f = layer.GetNextFeature();
            if (f == null) {
                logger.debug("No more features found.");
                break;
            }
            LinkedHashMap<String, String> content = new LinkedHashMap<String, String>();

            Geometry geom = f.GetGeometryRef();
            String wkt = geom.ExportToWkt();
            int fldCnt = f.GetFieldCount();
            for (int j = 0; j < fldCnt; j++) {
                FieldDefn fldDef = f.GetFieldDefnRef(j);
                content.put(fldDef.GetName(), f.GetFieldAsString(j));
            }

            SpatialDataRow row = new SpatialDataRow();
            row.setAttributeMap(content);
            row.setGeometryInWKT(wkt);
            row.setPartitionColumn(partionColValue);
            rows.add(row);
        }

        return rows;
    }
}
