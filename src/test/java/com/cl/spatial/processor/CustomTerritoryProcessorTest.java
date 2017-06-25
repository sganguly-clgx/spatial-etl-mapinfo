package com.cl.spatial.processor;

import com.cl.spatial.reader.MapInfoLayerReader;
import com.cl.spatial.reader.MapInfoReader;
import com.cl.spatial.vo.CustomTerritory;
import com.cl.spatial.vo.SpatialDataRow;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by sganguly on 6/24/2017.
 */
public class CustomTerritoryProcessorTest {
    @Test
    public void process() throws Exception {
        ogr.RegisterAll();

        String expectedAttrib = "{\"OBJECTID\":\"1\",\"County\":\"Allegany County\",\"Terr_code\":\"020\",\"Type\":\"Auto\"}";
        MapInfoReader reader = new MapInfoReader();
        MapInfoLayerReader layerReader = new MapInfoLayerReader();
        CustomTerritoryProcessor processor = new CustomTerritoryProcessor();

        URL url = Thread.currentThread().getContextClassLoader().getResource("data");
        File f = new File(url.toURI());
        List<Layer> list = reader.readAllTabFile(f.getAbsolutePath());
        List<SpatialDataRow> rows = layerReader.readLayer(list.get(0), null);
        assertTrue(rows.size() == 57);

        List<CustomTerritory> customTerritories = processor.process(rows, "Test");

        assertTrue(customTerritories.size() == rows.size());

        CustomTerritory customTerritory = customTerritories.get(0);

        assertEquals(customTerritory.getCustomLayerTag(), "Test");
        assertEquals(customTerritory.getPartitionColumn(), null);
        assertEquals(customTerritory.getAttrib(), expectedAttrib);


    }

}