package io.biocad.shipment;

public class JSONDataShipment extends DataShipment {

    String json;

    JSONDataShipment(String source) {
        this.json = source;
    }

    public Shipment convert(ShipmentEncoding encoding) throws ShipmentEncodingException {

        switch(encoding) {

            case DATA_JSON:
                return this;

            default:
                throw new ShipmentEncodingException("can't convert json to " + encoding);

        }


    }

    public String serialize() {

        return json;

    }
}
