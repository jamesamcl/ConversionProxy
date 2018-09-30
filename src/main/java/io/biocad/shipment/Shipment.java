package io.biocad.shipment;

import io.biocad.BiocadRequest;
import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.Sequence;

import java.io.IOException;

public abstract class Shipment {

    public static Shipment fromBody(String _body, ShipmentEncoding currentEncoding, ShipmentEncoding targetEncoding) throws ShipmentEncodingException, IOException, SBOLValidationException, SBOLConversionException {

        String body =_body.trim();

        Shipment shipment;

        switch(currentEncoding) {

            case PART_FASTA_DNA:
                shipment = new FASTADNAPartShipment(body);
                break;

            case PART_FASTA_PROTEIN:
                shipment = new FASTAProteinPartShipment(body);
                break;

            case PART_GENBANK_DNA:
                shipment = new GenBankDNAPartShipment(body);
                break;

            case PART_GENBANK_PROTEIN:
                shipment = new GenBankProteinPartShipment(body);
                break;

            case PART_SBOL:
                shipment = new SBOLPartShipment(body, SBOLDocument.RDF, /* irrelevant */ Sequence.IUPAC_DNA);
                break;

            case PART_RAW_PROTEIN:
                shipment = new RawProteinSequencePartShipment(body);
                break;

            case PART_RAW_DNA:
                shipment = new RawDNASequencePartShipment(body);
                break;

            case DATA_JSON:
                shipment = new JSONDataShipment(body);
                break;

            case DATA_SVG:
                shipment = new SVGDataShipment(body);
                break;

            default:
            case UNKNOWN:
                throw new ShipmentEncodingException("could not identify the type of your shipment");
        }


        return shipment.convert(targetEncoding);
    }

    public static Shipment fromRequest(BiocadRequest req) throws ShipmentEncodingException, IOException, SBOLValidationException, SBOLConversionException {

        return fromBody(req.getRequest().body().trim(), req.getClientProvides(), req.getTargetWants());
    }

    abstract public ShipmentDisposition getDisposition();


    abstract public Shipment convert(ShipmentEncoding encoding) throws IOException, ShipmentEncodingException, SBOLValidationException, SBOLConversionException;

    abstract public String serialize();

}
