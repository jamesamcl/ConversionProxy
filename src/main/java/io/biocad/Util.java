package io.biocad;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by james on 10/08/17.
 */
public class Util {

    // https://stackoverflow.com/a/37064468
    //
    public static ServletInputStream getInputStream(spark.Request request) throws IOException {
        final HttpServletRequest raw = request.raw();
        if (raw instanceof ServletRequestWrapper) {
            return ((ServletRequestWrapper) raw).getRequest().getInputStream();
        }

        return raw.getInputStream();
    }

}
