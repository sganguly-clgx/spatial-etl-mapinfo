package com.cl.spatial.reader;

import com.cl.spatial.vo.SpatialDataRow;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by sganguly on 6/24/2017.
 */
public class MapInfoLayerReaderTest {
    @Test
    public void readLayer() throws Exception {
        ogr.RegisterAll();
        MapInfoReader reader = new MapInfoReader();
        MapInfoLayerReader layerReader = new MapInfoLayerReader();
        URL url = Thread.currentThread().getContextClassLoader().getResource("data");
        File f = new File(url.toURI());
        List<Layer> list = reader.readAllTabFile(f.getAbsolutePath());
        List<SpatialDataRow> rows = layerReader.readLayer(list.get(0), "MD");
        assertTrue(rows.size() == 57);
    }

    @Test
    public void readLayerWithNull() {
        MapInfoLayerReader layerReader = new MapInfoLayerReader();
        List<SpatialDataRow> rows = layerReader.readLayer(null, "MD");
        assertTrue(rows.size() == 0);
    }

    @Test
    public void readLayerWithNullPartitionColValue() throws Exception {
        ogr.RegisterAll();
        MapInfoReader reader = new MapInfoReader();
        MapInfoLayerReader layerReader = new MapInfoLayerReader();
        URL url = Thread.currentThread().getContextClassLoader().getResource("data");
        File f = new File(url.toURI());
        List<Layer> list = reader.readAllTabFile(f.getAbsolutePath());
        List<SpatialDataRow> rows = layerReader.readLayer(list.get(0), null);
        assertTrue(rows.size() == 57);
    }

}