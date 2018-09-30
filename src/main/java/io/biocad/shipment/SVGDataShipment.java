package io.biocad.shipment;

public class SVGDataShipment extends DataShipment {

    String svg;

    SVGDataShipment(String source) {
        this.svg = source;
    }

    public Shipment convert(ShipmentEncoding encoding) throws ShipmentEncodingException {

        switch(encoding) {

            case DATA_SVG:
                return this;

            default:
                throw new ShipmentEncodingException("can't convert svg to " + encoding);

        }


    }

    public String serialize() {

        return svg;

    }
}
