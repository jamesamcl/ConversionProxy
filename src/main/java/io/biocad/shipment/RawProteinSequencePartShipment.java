package io.biocad.shipment;

import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.Sequence;

import java.io.IOException;

public class RawProteinSequencePartShipment extends PartShipment {

    String seq;

    public RawProteinSequencePartShipment(String body) {

        this.seq = body.toLowerCase();

    }


    public Shipment convert(ShipmentEncoding encoding) throws IOException, ShipmentEncodingException {

        try {
            switch (encoding) {

                case PART_FASTA_PROTEIN:
                    StringBuilder fasta = new StringBuilder();
                    fasta.append("> biocad_raw_protein_sequence_to_fasta_conversion\n");
                    fasta.append(this.seq);
                    return new FASTAProteinPartShipment(fasta.toString());

                case PART_GENBANK_PROTEIN:
                    return this.convert(ShipmentEncoding.PART_SBOL).convert(ShipmentEncoding.PART_GENBANK_PROTEIN);

                case PART_SBOL:
                    SBOLDocument sbol = new SBOLDocument();
                    sbol.createSequence("biocad_raw_protein_sequence_to_sbol_conversion", this.seq, Sequence.IUPAC_PROTEIN);
                    return new SBOLPartShipment(sbol);

                case PART_RAW_PROTEIN:
                    return this;

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

        return seq;

    }

}
