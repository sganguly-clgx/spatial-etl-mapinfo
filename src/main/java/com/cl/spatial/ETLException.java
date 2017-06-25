package com.cl.spatial;

import org.springframework.boot.ExitCodeGenerator;

/**
 * Created by sganguly on 6/24/2017.
 */
public class ETLException extends RuntimeException implements ExitCodeGenerator {
    public ETLException(String message) {
        super(message);
    }

    public int getExitCode() {
        return 1

                ;
    }
}
