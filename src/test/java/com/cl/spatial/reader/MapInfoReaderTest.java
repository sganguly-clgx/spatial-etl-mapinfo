package com.cl.spatial.reader;

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
public class MapInfoReaderTest {
    @Test
    public void readOneMapInfoFile() throws Exception {
        ogr.RegisterAll();
        MapInfoReader reader = new MapInfoReader();
        URL url = Thread.currentThread().getContextClassLoader().getResource("data");
        File f = new File(url.toURI());
        List<Layer> list = reader.readAllTabFile(f.getAbsolutePath());
        assertTrue(list.size() == 1);
    }

    @Test
    public void testInvalidDataPath() throws Exception {
        ogr.RegisterAll();
        MapInfoReader reader = new MapInfoReader();
        List<Layer> list = reader.readAllTabFile("c:/invlidDir");
        assertTrue(list.size() == 0);
    }

}