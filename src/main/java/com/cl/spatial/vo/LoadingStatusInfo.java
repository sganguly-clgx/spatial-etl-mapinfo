package com.cl.spatial.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * Created by sganguly on 6/23/2017.
 */
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoadingStatusInfo {

    private String fileName;
    private String customLayerTag;
    private int expectedCount;
    private int actualCount;
    private String status;

    public String toString() {
        String msg = MessageFormat.format("<FileName> {0} <CustomLayerTag> {1} <Rows> {2} of {3} <Status> {4}", fileName, customLayerTag, expectedCount, actualCount, status);
        return msg;
    }
}
