package io.biocad;

import static spark.Spark.*;

import org.sbolstandard.core2.*;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;

public class Main {

    public static void main(String[] args) {

        port(9995);


        SBOLReader.setURIPrefix("http://api.biocad.io/temp/");

        ForwardPool pool = new ForwardPool();

        post("*", (req, res) -> {

            return pool.post(req, res);

            /*
            try {

                return pool.post(req, res);

            } catch (Exception e) {

                res.status(500);
                return e.toString();
            }*/
        });

    }

}


