package com.cl.spatial.service;

import com.cl.spatial.processor.SpatialDataProcessor;
import com.cl.spatial.reader.MapInfoLayerReader;
import com.cl.spatial.reader.MapInfoReader;
import com.cl.spatial.sink.CustomTerritoryDBWriter;
import com.cl.spatial.validator.Validator;
import com.cl.spatial.vo.CustomTerritory;
import com.cl.spatial.vo.DataLayerDetails;
import com.cl.spatial.vo.LoadingStatusInfo;
import com.cl.spatial.vo.SpatialDataRow;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.gdal.ogr.Layer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sganguly on 6/12/2017.
 */

@Component
public class SpatialDataService {

    private final Logger logger = LoggerFactory.getLogger(SpatialDataService.class);

    @Autowired
    private MapInfoReader reader;

    @Autowired
    private MapInfoLayerReader layerReader;

    @Autowired
    private SpatialDataProcessor customTerritoryProcessor;

    @Autowired
    private CustomTerritoryDBWriter dbWriter;

    @Autowired
    private Validator validator;

    public int startLoad(File csvFile, boolean debugMode) {
        logger.info("Loading started...");
        List<LoadingStatusInfo> statusInfoList = new ArrayList<>();
        int exitCode = 1; // Valid Values: 0-Success; 1-Fail
        List<DataLayerDetails> dataLayersToLoad = loadDataLayerList(csvFile);
        for (DataLayerDetails dataLayer : dataLayersToLoad) {
            statusInfoList.addAll(loadDataToDB(dataLayer.getDataPath(), dataLayer.getCustomLayerTag(), dataLayer.getStateCode(), debugMode));
        }

        // Get the exit code
        for (LoadingStatusInfo statusInfo : statusInfoList) {
            if (!"SUCCESS".equalsIgnoreCase(statusInfo.getStatus()))
                return 1;
        }
        logger.info("Exit Code = 0. All data successfully loaded !!!");
        return 0;

    }

    private List<LoadingStatusInfo> loadDataToDB(String mapInfoFileDir, String customLayerTag, String stateCode, boolean debug) {
        List<LoadingStatusInfo> statusInfoList = new ArrayList<>();

        List<Layer> layerList = reader.readAllTabFile(mapInfoFileDir);
        for (Layer layer : layerList) {
            logger.debug("Processing layer: " + layer.GetName());
            List<SpatialDataRow> rows = layerReader.readLayer(layer, stateCode);
            List<CustomTerritory> customTerritories = customTerritoryProcessor.process(rows, customLayerTag);
            if (debug)
                dbWriter.debugDBInsert(customTerritories);
            else
                dbWriter.writeToDB(customTerritories);

            LoadingStatusInfo statusInfo = validator.validate(rows.size(), customLayerTag, stateCode);
            statusInfo.setFileName(layer.GetName());
            statusInfoList.add(statusInfo);

        }

        // Log all the data load status - Helps ETL team to validate after load
        for (LoadingStatusInfo statusInfo : statusInfoList) {
            logger.info(statusInfo.toString());
        }

        return statusInfoList;

    }

    private List<DataLayerDetails> loadDataLayerList(File file) {
        try {
            CsvMapper mapper = new CsvMapper();
            CsvSchema csvSchema = mapper.typedSchemaFor(DataLayerDetails.class).withNullValue("").withoutHeader();
            if (file.exists()) {
                // Try to load from classpath
                List list = new CsvMapper().readerFor(DataLayerDetails.class)
                        .with(csvSchema.withColumnSeparator(CsvSchema.DEFAULT_COLUMN_SEPARATOR))
                        .readValues(file)
                        .readAll();
                return list;
            } else {
                return Collections.emptyList();
            }

        } catch (Exception e) {
            logger.error("Error occurred while loading object list from file " + file.getName(), e);
            return Collections.emptyList();

        }
    }
}
