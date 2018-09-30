package io.biocad.shipment;

import org.sbolstandard.core2.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Set;

public class SBOLPartShipment extends PartShipment {

    SBOLDocument sbol;

    public SBOLPartShipment(SBOLDocument sbol) {
        this.sbol = sbol;
    }

    public SBOLPartShipment(String source, String fileType, URI defaultSeqEncoding) throws SBOLValidationException, SBOLConversionException, IOException {

        this.sbol = SBOLReadSynced.read(new ByteArrayInputStream(source.getBytes()), fileType, defaultSeqEncoding);

    }


    public Shipment convert(ShipmentEncoding encoding) throws IOException, ShipmentEncodingException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Set<Sequence> sequences;
        Sequence seq;

        try {
            switch (encoding) {

                case PART_FASTA_DNA:

                    for(Sequence sequence : sbol.getSequences()) {
                        if(sequence.getEncoding() != Sequence.IUPAC_DNA) {
                            throw new ShipmentEncodingException("All sequences need to be DNA for me to convert to FASTA DNA");
                        }
                    }

                    SBOLWriter.write(sbol, outputStream, SBOLDocument.FASTAformat);
                    return new FASTADNAPartShipment(outputStream.toString());

                case PART_GENBANK_DNA:

                    for(Sequence sequence : sbol.getSequences()) {
                        if(sequence.getEncoding() != Sequence.IUPAC_DNA) {
                            throw new ShipmentEncodingException("All sequences need to be DNA for me to convert to GenBank DNA");
                        }
                    }

                    SBOLWriter.write(sbol, outputStream, SBOLDocument.GENBANK);
                    return new GenBankDNAPartShipment(outputStream.toString());

                case PART_FASTA_PROTEIN:

                    for(Sequence sequence : sbol.getSequences()) {
                        if(sequence.getEncoding() != Sequence.IUPAC_PROTEIN) {
                            throw new ShipmentEncodingException("All sequences need to be protein for me to convert to FASTA protein");
                        }
                    }

                    SBOLWriter.write(sbol, outputStream, SBOLDocument.FASTAformat);
                    return new FASTADNAPartShipment(outputStream.toString());

                case PART_GENBANK_PROTEIN:

                    for(Sequence sequence : sbol.getSequences()) {
                        if(sequence.getEncoding() != Sequence.IUPAC_PROTEIN) {
                            throw new ShipmentEncodingException("All sequences need to be protein for me to convert to GenBank protein");
                        }
                    }

                    SBOLWriter.write(sbol, outputStream, SBOLDocument.GENBANK);
                    return new GenBankDNAPartShipment(outputStream.toString());

                case PART_SBOL:
                    return this;

                case PART_RAW_PROTEIN:

                    sequences = this.sbol.getSequences();

                    if(sequences.size() != 1) {
                        throw new ShipmentEncodingException("I need exactly one sequence to convert SBOL to a raw protein sequence");
                    }

                    seq = sequences.iterator().next();

                    if(seq.getEncoding() != Sequence.IUPAC_PROTEIN) {
                        throw new ShipmentEncodingException("Doesn't look like a protein sequence?");
                    }

                    return new RawDNASequencePartShipment(seq.getElements());

                case PART_RAW_DNA:

                    sequences = this.sbol.getSequences();

                    if(sequences.size() != 1) {
                        throw new ShipmentEncodingException("I need exactly one sequence to convert SBOL to a raw DNA sequence");
                    }

                    seq = sequences.iterator().next();

                    if(seq.getEncoding() != Sequence.IUPAC_DNA) {
                        throw new ShipmentEncodingException("Doesn't look like a DNA sequence?");
                    }

                    return new RawDNASequencePartShipment(seq.getElements());

                default:
                    throw new ShipmentEncodingException("unsupported conversion: PART_SBOL to " + encoding);
            }
        } catch(SBOLConversionException e) {
            throw new ShipmentEncodingException(e);
        }




    }


    public String serialize() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            SBOLWriter.write(sbol, outputStream);
        } catch (SBOLConversionException e) {
            throw new RuntimeException(e);
        }

        return outputStream.toString();


    }


}
