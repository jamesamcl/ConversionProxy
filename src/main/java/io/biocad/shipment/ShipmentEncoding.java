package io.biocad.shipment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by james on 10/08/17.
 */
public enum ShipmentEncoding {
    UNKNOWN,
    PART_FASTA_DNA,
    PART_FASTA_PROTEIN,
    PART_GENBANK_DNA,
    PART_GENBANK_PROTEIN,
    PART_SBOL,
    PART_RAW_PROTEIN,
    PART_RAW_DNA,
    DATA_JSON,
    DATA_SVG;


    public static ShipmentEncoding fromString(String s) {

        if(s == null)
            return UNKNOWN;

        try {
            return ShipmentEncoding.valueOf(s);
        } catch(IllegalArgumentException e) {
            return UNKNOWN;
        }

    }


    static Pattern bpLenPattern = Pattern.compile(".*[ \t][0-9]+[ \t]bp.*");
    static Pattern aaLenPattern = Pattern.compile(".*[ \t][0-9]+[ \t]aa.*");

    public static ShipmentEncoding detectEncoding(String body, ShipmentEncoding targetEncoding /* for a hint */) throws ShipmentEncodingException {

        if(body.length() == 0) {
            throw new ShipmentEncodingException("empty body");
        }

        if(body.startsWith("LOCUS")) {

            String firstLine = body.substring(0, body.indexOf("\n"));

            Matcher bpMatcher = bpLenPattern.matcher(firstLine);

            if(bpMatcher.matches()) {
                return ShipmentEncoding.PART_GENBANK_DNA;
            }

            Matcher aaMatcher = aaLenPattern.matcher(firstLine);

            if(aaMatcher.matches()) {
                return ShipmentEncoding.PART_GENBANK_PROTEIN;
            }

            throw new ShipmentEncodingException("You gave me GenBank, but I don't know whether it's a DNA or protein sequence");
        }

        if (body.startsWith(">")) {

            if(targetEncoding == ShipmentEncoding.PART_RAW_PROTEIN ||
                    targetEncoding == ShipmentEncoding.PART_FASTA_PROTEIN) {

                return ShipmentEncoding.PART_FASTA_PROTEIN;

            } else if(targetEncoding == ShipmentEncoding.PART_RAW_DNA ||
                    targetEncoding == ShipmentEncoding.PART_FASTA_DNA) {

                return ShipmentEncoding.PART_FASTA_DNA;

            } else {
                throw new ShipmentEncodingException("You gave me a FASTA sequence, but this endpoint accepts multiple types of sequence and I don't know what type of sequence you gave me");
            }


        } else if (body.startsWith("<")) {

            return ShipmentEncoding.PART_SBOL;

        } else if (Character.isAlphabetic(body.charAt(0))) {

            // they've given us a raw sequence.  obviously it's ambiguous as to whether this is a dna, rna,
            // protein sequence.  if the target is looking for a specific type of sequence we'll assume it's the type the
            // target wanted, otherwise we'll bail

            if(targetEncoding== ShipmentEncoding.PART_RAW_PROTEIN ||
                    targetEncoding == ShipmentEncoding.PART_FASTA_PROTEIN) {

                return ShipmentEncoding.PART_RAW_PROTEIN;

            } else if(targetEncoding== ShipmentEncoding.PART_RAW_DNA ||
                    targetEncoding == ShipmentEncoding.PART_FASTA_DNA) {

                return ShipmentEncoding.PART_RAW_DNA;

            } else {
                throw new ShipmentEncodingException("You gave me a raw sequence, but this endpoint accepts multiple types of sequence and I don't know what type of sequence you gave me");
            }

        }

        return ShipmentEncoding.UNKNOWN;

    }



}
