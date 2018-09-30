package io.biocad;

import io.biocad.shipment.Shipment;
import io.biocad.shipment.ShipmentEncodingException;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLValidationException;
import spark.Request;
import spark.Response;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

public class ForwardPool {

    PoolingHttpClientConnectionManager connectionManager;
    CloseableHttpClient client;

    ForwardPool() {
        connectionManager = new PoolingHttpClientConnectionManager();
        client = HttpClients.custom().setConnectionManager(connectionManager).build();
    }

    public String post(Request req, Response res) {

        try {
            BiocadRequest sfReq = new BiocadRequest(req);

            URIBuilder uriBuilder = new URIBuilder(sfReq.getTargetURL());
            uriBuilder.setCustomQuery(req.queryString());


            HttpPost httpReq = new HttpPost();
            httpReq.setURI(new URI(uriBuilder.toString()));

            Shipment shipmentForTarget = sfReq.getInputShipment().convert(sfReq.getTargetWants());
            httpReq.setEntity(new ByteArrayEntity(shipmentForTarget.serialize().getBytes()));


            HttpResponse response = client.execute(httpReq);

            Shipment shipmentFromTarget = Shipment.fromBody(inputStreamToString(response.getEntity().getContent()), sfReq.getTargetProvides(), sfReq.getClientWants());

            res.status(response.getStatusLine().getStatusCode());

            /*
            for(Header header : response.getAllHeaders()) {
                res.header(header.getName(), header.getValue());
            }
            */

            return shipmentFromTarget.convert(sfReq.getClientWants()).serialize();

        } catch(Exception e) {

            res.status(500);
            return getStackTrace(e);


        }
    }


    private String inputStreamToString(InputStream inputStream) throws IOException
    {
        StringWriter writer = new StringWriter();

        IOUtils.copy(inputStream, writer);

        return writer.toString();
    }

    private String getStackTrace(final Throwable throwable) {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);

        throwable.printStackTrace(pw);

        return sw.getBuffer().toString();
    }
}
