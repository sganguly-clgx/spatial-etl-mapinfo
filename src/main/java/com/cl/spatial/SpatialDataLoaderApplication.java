package com.cl.spatial;

import com.cl.spatial.service.SpatialDataService;
import com.google.common.base.Splitter;
import org.gdal.ogr.ogr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

@SpringBootApplication
public class SpatialDataLoaderApplication implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(SpatialDataLoaderApplication.class);

    @Autowired
    private SpatialDataService dataService;

    @Override
    public void run(String... args) {
        HashMap<String, String> parameters = new HashMap<>();
        getParametersFromCommandLine(parameters, args);

        boolean debugMode = parameters.containsKey("--debug");
        File csvFile;
        if (parameters.containsKey("--data")) {
            csvFile = new File(parameters.get("--data"));
        } else {
            try {
                csvFile = new File(ClassLoader.getSystemResource("datalayer.csv").toURI());
            } catch (URISyntaxException e) {
                throw new ETLException("Cannot access datalayer.csv");
            }

        }

        if (csvFile.exists() == false) {
            throw new ETLException("Cannot access data input file -- " + csvFile.getAbsolutePath());
        }

        int exitCode = dataService.startLoad(csvFile, debugMode);

        if (exitCode == 1) {
            throw new ETLException("Failed: All data layers are not loaded successfully.");
        }
        System.exit(exitCode);

    }

    private void getParametersFromCommandLine(HashMap<String, String> parameters, String[] args) {
        // Get all the command line arguments
        for (int n = 0; n < args.length; ++n) {
            String arg = args[n].toLowerCase();
            List<String> kvList = Splitter.on("=").splitToList(arg);

            if (kvList.get(0).startsWith("--") && kvList.size() == 2) {
                parameters.put(kvList.get(0), kvList.get(1));
            }

        }
    }


    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpatialDataLoaderApplication.class);
        ogr.RegisterAll();
        app.run(args);

    }
}
