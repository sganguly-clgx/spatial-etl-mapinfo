package com.cl.spatial.sink;

import com.cl.spatial.vo.CustomTerritory;
import com.google.common.collect.Lists;
import lombok.Cleanup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by sganguly on 6/12/2017.
 */
@Component
public class CustomTerritoryDBWriter {

    private final Logger logger = LoggerFactory.getLogger(CustomTerritoryDBWriter.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate batchTemplate;

    @Value("${batch_size}")
    private int batchSize;

    @Value("${insert_sql}")
    private String insertSQL;


    public void writeToDB(List<CustomTerritory> customTerritories) {
        List<List<CustomTerritory>> batches = Lists.partition(customTerritories, batchSize);

        for (List<CustomTerritory> itemBatch : batches) {
            SqlParameterSource[] batch = SqlParameterSourceUtils.createBatch(itemBatch.toArray());
            int[] updateCounts = batchTemplate.batchUpdate(insertSQL, batch);

        }
    }

    public void debugDBInsert(List<CustomTerritory> customTerritories) {
        try {
            DataSource ds = jdbcTemplate.getDataSource();
            @Cleanup Connection connection = ds.getConnection();
            connection.setAutoCommit(true);
            PreparedStatement ps = connection.prepareStatement(insertSQL);

            int count = 0;

            for (CustomTerritory rowData : customTerritories) {
                ps.setString(1, rowData.getCustomLayerTag());
                ps.setString(2, rowData.getPartitionColumn());
                ps.setString(3, rowData.getGeomWKT());
                ps.setString(4, rowData.getAttrib());
                try {
                    ps.execute();
                    ps.clearParameters();
                    count++;
                } catch (SQLException se) {
                    logger.error("Custom Layer Tag: " + rowData.getCustomLayerTag());
                    logger.error("Attributes: " + rowData.getAttrib());
                    logger.error("GEOM in WKT: " + rowData.getGeomWKT());
                    logger.error(se.getMessage());
                    ps.clearParameters();
                    ps.close();
                    ds = jdbcTemplate.getDataSource();
                    connection = ds.getConnection();
                    connection.setAutoCommit(true);
                    ps = connection.prepareStatement(insertSQL);

                    continue;
                }
            }
            ps.close();
        } catch (SQLException se) {
            logger.error(se.getMessage());

        }

    }


}
