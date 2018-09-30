package io.biocad;

import io.biocad.shipment.Shipment;
import io.biocad.shipment.ShipmentEncoding;
import io.biocad.shipment.ShipmentEncodingException;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLValidationException;
import spark.Request;

import java.io.IOException;

public class BiocadRequest {

    Request req;

    ShipmentEncoding clientProvides;
    ShipmentEncoding targetWants;
    ShipmentEncoding targetProvides;
    ShipmentEncoding clientWants;

    String targetURL;

    public Shipment getInputShipment() {
        return inputShipment;
    }

    Shipment inputShipment;



    public BiocadRequest(Request req) throws ShipmentEncodingException, IOException, SBOLValidationException, SBOLConversionException {

        this.req = req;

        this.targetWants = ShipmentEncoding.fromString(req.headers("X-Biocad-TargetWants"));
        this.targetProvides = ShipmentEncoding.fromString(req.headers("X-Biocad-TargetProvides"));
        this.clientProvides = ShipmentEncoding.fromString(req.headers("X-Biocad-ClientProvides"));
        this.clientWants = ShipmentEncoding.fromString(req.headers("X-Biocad-ClientWants"));

        if(this.clientProvides == ShipmentEncoding.UNKNOWN) {

            String body = req.body().trim();

            this.clientProvides = ShipmentEncoding.detectEncoding(body, this.targetWants);

            if(this.clientProvides == ShipmentEncoding.UNKNOWN) {

                throw new ShipmentEncodingException("I don't know what format you sent me and can't guess");

            }
        }

        if(this.clientWants == ShipmentEncoding.UNKNOWN) {

            this.clientWants = this.clientProvides;

        }

        this.targetURL = req.headers("X-Biocad-TargetURL");

        this.inputShipment = Shipment.fromRequest(this);

    }

    public Request getRequest() {

        return this.req;

    }

    public String getTargetURL() {
        return targetURL;
    }

    public ShipmentEncoding getClientWants() {
        return clientWants;
    }

    public ShipmentEncoding getClientProvides() {
        return clientProvides;
    }


    public ShipmentEncoding getTargetWants() {
        return targetWants;
    }

    public ShipmentEncoding getTargetProvides() {
        return targetProvides;
    }


}
