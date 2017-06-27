package com.cl.spatial.cleanup;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sganguly on 6/27/2017.
 */
@Component
public class CleanupTable {

    private final Logger logger = LoggerFactory.getLogger(CleanupTable.class);

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Value("${delete_sql_withState}")
    private String deleteSQLWithStateClause;

    @Value("${delete_sql_noState}")
    private String deleteSQLNoStateClause;


    public boolean performCleanup(String customLayerTag, String statecd) {
        if (Strings.isNullOrEmpty(statecd))
            logger.info("Cleanup data for CustomLayerTag = " + customLayerTag);
        else
            logger.info("Cleanup data for CustomLayerTag = " + customLayerTag + " where state = " + statecd);

        List<Object> parameters = new ArrayList<>();
        String sql;
        if (Strings.isNullOrEmpty(statecd)) {
            sql = deleteSQLNoStateClause;
            parameters.add(customLayerTag);
        } else {
            sql = deleteSQLWithStateClause;
            parameters.add(customLayerTag);
            parameters.add(statecd);
        }

        jdbcTemplate.update(sql, parameters.toArray());

        return true;
    }

}

