package io.biocad.shipment;

import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLReader;
import org.sbolstandard.core2.SBOLValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class SBOLReadSynced {


    public static SBOLDocument read(InputStream inputStream, String fileType, URI defaultSeqEncoding) throws SBOLValidationException, SBOLConversionException, IOException {

        if(fileType.equals(SBOLDocument.FASTAformat)) {

            synchronized(SBOLReader.class) {
                SBOLReader.setDefaultSequenceEncoding(defaultSeqEncoding);
                return SBOLReader.read(inputStream);
            }

        } else {

            return SBOLReader.read(inputStream);
        }
    }

}
