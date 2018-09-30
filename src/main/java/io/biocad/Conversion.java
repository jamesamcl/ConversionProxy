package io.biocad;

import org.sbolstandard.core2.SBOLConversionException;
import org.sbolstandard.core2.SBOLDocument;
import org.sbolstandard.core2.SBOLReader;
import org.sbolstandard.core2.SBOLValidationException;

import java.io.IOException;
import java.io.InputStream;

public class Conversion {

    public static SBOLDocument sbolFromAnything(InputStream anything) throws SBOLValidationException, SBOLConversionException, IOException {

        return SBOLReader.read(anything);

    }

}
