package io.biocad.shipment;

import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.Sequence;

import java.io.IOException;

/**
 * Created by james on 14/08/17.
 */
public class GenBankProteinPartShipment extends PartShipment {

    String genbank;

    GenBankProteinPartShipment(String source) {
        this.genbank = source;
    }

    public Shipment convert(ShipmentEncoding encoding) throws IOException, ShipmentEncodingException {

        try {
            switch (encoding) {

                case PART_FASTA_PROTEIN:
                    return (new SBOLPartShipment(this.genbank, SBOLDocument.GENBANK, Sequence.IUPAC_PROTEIN)).convert(ShipmentEncoding.PART_FASTA_PROTEIN);

                case PART_GENBANK_PROTEIN:
                    return this;

                case PART_SBOL:
                    return new SBOLPartShipment(this.genbank, SBOLDocument.GENBANK, Sequence.IUPAC_PROTEIN);

                case PART_RAW_PROTEIN:
                    return (new SBOLPartShipment(this.genbank, SBOLDocument.GENBANK, Sequence.IUPAC_PROTEIN)).convert(ShipmentEncoding.PART_RAW_PROTEIN);

                default:
                    throw new ShipmentEncodingException("unsupported conversion");
            }
        } catch (SBOLValidationException e) {
            throw new ShipmentEncodingException(e);
        } catch (SBOLConversionException e) {
            throw new ShipmentEncodingException(e);
        }

    }

    public String serialize() {

        return genbank;

    }
}
