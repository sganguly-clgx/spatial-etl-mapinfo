package com.cl.spatial.reader;

import org.gdal.ogr.DataSource;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sganguly on 6/12/2017.
 */
@Component
public class MapInfoReader {

    private final Logger logger = LoggerFactory.getLogger(MapInfoReader.class);


    public List<Layer> readAllTabFile(String mapInfoFilesDir) {
        List<Layer> layerList = new ArrayList<>();

        // Open the data source
        DataSource ds = ogr.Open(mapInfoFilesDir);

        if (ds == null) {
            logger.error("No Layer found !!!");
            return layerList;
        }
        // Get all layers
        int layerCnt = ds.GetLayerCount();
        for (int i = 0; i < layerCnt; i++) {
            layerList.add(ds.GetLayer(i));

        }
        return layerList;
    }
}
