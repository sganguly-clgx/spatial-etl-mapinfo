package com.cl.spatial.validator;

import com.cl.spatial.vo.LoadingStatusInfo;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sganguly on 6/20/2017.
 */
@Component
public class Validator {
    private final Logger logger = LoggerFactory.getLogger(Validator.class);

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Value("${count_sql_withState}")
    private String countSQLWithStateClause;

    @Value("${count_sql_noState}")
    private String countSQLNoStateClause;


    public LoadingStatusInfo validate(int expectedRows, String customLayerTag, String statecd) {

        LoadingStatusInfo statusInfo = new LoadingStatusInfo();

        statusInfo.setCustomLayerTag(customLayerTag);
        statusInfo.setExpectedCount(expectedRows);

        if (expectedRows == 0) {
            statusInfo.setStatus("No data found.");
            return statusInfo;
        }

        List<Object> parameters = new ArrayList<>();
        String sql;
        if (Strings.isNullOrEmpty(statecd)) {
            sql = countSQLNoStateClause;
            parameters.add(customLayerTag);
        } else {
            sql = countSQLWithStateClause;
            parameters.add(customLayerTag);
            parameters.add(statecd);
        }

        int actualCount = jdbcTemplate.queryForObject(sql, parameters.toArray(), Integer.class);

        statusInfo.setActualCount(actualCount);
        statusInfo.setStatus("SUCCESS");
        if (actualCount == expectedRows)
            return statusInfo;
        else {
            String msg = MessageFormat.format("Expected {0} found {1}", expectedRows, actualCount);

            statusInfo.setStatus(msg);
            logger.error(msg);
            return statusInfo;
        }

    }
}
