package com.cl.spatial.processor;

import com.cl.spatial.vo.SpatialDataRow;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by sganguly on 6/12/2017.
 */
@Component
public interface SpatialDataProcessor {

    public <T> List<T> process(List<SpatialDataRow> spatialDataRows, String customLayerTag);

}
