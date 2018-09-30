package io.biocad.shipment;

public abstract class PartShipment extends Shipment {



    public ShipmentDisposition getDisposition() {

        return ShipmentDisposition.PART;

    }

}
