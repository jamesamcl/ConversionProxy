package io.biocad.shipment;


import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLValidationException;
import org.sbolstandard.core2.Sequence;

import java.io.IOException;

public class FASTAProteinPartShipment extends PartShipment {

    String fasta;

    FASTAProteinPartShipment(String source) {
        this.fasta = source;
    }

    public Shipment convert(ShipmentEncoding encoding) throws IOException, ShipmentEncodingException {

        try {
            switch (encoding) {

                case PART_FASTA_PROTEIN:
                    return this;

                case PART_GENBANK_PROTEIN:
                    return (new SBOLPartShipment(this.fasta, SBOLDocument.FASTAformat, Sequence.IUPAC_PROTEIN)).convert(ShipmentEncoding.PART_GENBANK_PROTEIN);

                case PART_SBOL:
                    return new SBOLPartShipment(this.fasta, SBOLDocument.FASTAformat, Sequence.IUPAC_PROTEIN);

                case PART_RAW_PROTEIN:

                    StringBuilder seq = new StringBuilder();

                    boolean gotMetadataLine = false;

                    for(String line : this.fasta.split("\n")) {

                        String trimmedLine = line.trim();

                        if(trimmedLine.charAt(0) == '>') {

                            if(gotMetadataLine) {
                                throw new ShipmentEncodingException("cannot convert a fasta shipment with multiple sequences to a raw sequence");
                            } else {
                                gotMetadataLine = true;
                                continue;
                            }

                        } else {

                            seq.append(trimmedLine);
                        }

                    }

                    return new RawProteinSequencePartShipment(seq.toString());

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

        return fasta;

    }
}
