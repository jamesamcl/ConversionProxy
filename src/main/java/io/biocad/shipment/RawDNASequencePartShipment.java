package io.biocad.shipment;

import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.Sequence;

import java.io.IOException;

public class RawDNASequencePartShipment extends PartShipment {

    String seq;

    public RawDNASequencePartShipment(String body) {

        this.seq = body.toLowerCase();

    }


    public Shipment convert(ShipmentEncoding encoding) throws IOException, ShipmentEncodingException {

        try {
            switch (encoding) {

                case PART_FASTA_DNA:
                    StringBuilder fasta = new StringBuilder();
                    fasta.append("> biocad_raw_dna_sequence_to_fasta_conversion\n");
                    fasta.append(this.seq);
                    return new FASTADNAPartShipment(fasta.toString());

                case PART_GENBANK_DNA:
                    return this.convert(ShipmentEncoding.PART_SBOL).convert(ShipmentEncoding.PART_GENBANK_DNA);

                case PART_SBOL:
                    SBOLDocument sbol = new SBOLDocument();
                    sbol.createSequence("biocad_raw_dna_sequence_to_sbol_conversion", this.seq, Sequence.IUPAC_DNA);
                    return new SBOLPartShipment(sbol);

                case PART_RAW_DNA:
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
