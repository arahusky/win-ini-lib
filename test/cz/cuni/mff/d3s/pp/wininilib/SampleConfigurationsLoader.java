package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.exceptions.DuplicateNameException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.TooManyValuesException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueBoolean;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueFloat;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueReference;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueSigned;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueString;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueBooleanRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueFloatRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueSignedRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueStringRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueUnsignedRestriction;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author xxx
 */
public class SampleConfigurationsLoader {

    private static final String CONFIG_SIMPLE = "test/sample_configurations/config_simple.txt";
    private static final String CONFIG_SIMPLE_MORE_SECTIONS = "test/sample_configurations/config_simple_more_sections.txt";
    private static final String CONFIG_MUSTER = "test/sample_configurations/config_muster.txt";
    //required output of IniFile instance's (loaded as CONFIG_MUSTER) toString method
    private static final String CONFIG_MUSTER_TOSTRING = "test/sample_configurations/config_muster_tostring.txt";

    private static final String CONFIG_MALFORMED = "test/sample_configurations/config_malformed.txt";
    private static final String CONFIG_DUPLICITE_ID_SECTION = "test/sample_configurations/config_duplicite_id_section.txt";
    private static final String CONFIG_DUPLICITE_ID_PROPERTY = "test/sample_configurations/config_duplicite_id_property.txt";

    public static String getConfigSimple() throws IOException {
        return getFileContents(CONFIG_SIMPLE);
    }

    public static IniFile getIniFileSimple() throws DuplicateNameException, InvalidValueFormatException {
        IniFile iniFile = new IniFile();

        Section section1 = new Section("Sekce 1", true);
        section1.addProperty(new Property("Option 1", true, new ValueStringRestriction()));
        Property multivalued1 = new Property("oPtion 1", false, new ValueUnsignedRestriction());
        multivalued1.setDelimiter(Property.ValueDelimiter.COMMA);
        section1.addProperty(multivalued1);

        iniFile.addSection(section1);

        Section section2 = new Section("Cisla", true);
        Property multivalued2 = new Property("cele", false, new ValueSignedRestriction());
        multivalued2.setDelimiter(Property.ValueDelimiter.COLON);
        section2.addProperty(multivalued2);
        section2.addProperty(new Property("cele_bin", true, new ValueSignedRestriction()));
        iniFile.addSection(section2);

        return iniFile;
    }

    public static IniFile getIniFileMuster() throws InvalidValueFormatException, TooManyValuesException, ViolatedRestrictionException, DuplicateNameException {
        IniFile iniFile = new IniFile();

        // section 1
        Section section1 = new Section("Sekce 1", true);
        section1.setComment("komentar");
        Property p11 = new Property("Option 1", true, new ValueStringRestriction());
        p11.addValue(new ValueString("value 1"));
        p11.setComment("volba 'Option 1' ma hodnotu 'value 1'");

        Property p12 = new Property("oPtion 1", true, new ValueStringRestriction());
        p12.addValue(new ValueString(" value 2   "));
        p12.setComment("volba 'oPtion 1' ma hodnotu ' value 2   ', 'oPtion 1' a 'Option 1' jsou ruzne volby");

        section1.addProperty(p11);
        section1.addProperty(p12);
        iniFile.addSection(section1);

        // section 2
        Section section2 = new Section("$Sekce::podsekce", true);
        iniFile.addSection(section2);
        Property p21 = new Property("Option 2", false, new ValueStringRestriction());
        p21.setDelimiter(Property.ValueDelimiter.COLON);
        p21.addValue(new ValueString("value 1"));
        p21.addValue(new ValueString("value 2"));
        p21.addValue(new ValueString("value 3"));
        p21.setComment("volba 'Option 2' je seznam hodnot 'value 1', 'value 2' a 'value 3'");
        section2.addProperty(p21);

        Property p22 = new Property("Option 3", false, new ValueStringRestriction());
        p22.setDelimiter(Property.ValueDelimiter.COMMA);
        p22.addValue(new ValueString("value 1"));
        p22.addValue(new ValueReference(iniFile, "Sekce 1", "Option 1", new ValueStringRestriction()));
        p22.setComment("volba 'Option 3' je seznam hodnot 'value 1' a 'value 1'");
        section2.addProperty(p22);

        Property p23 = new Property("Option 4", false, new ValueStringRestriction());
        p23.setDelimiter(Property.ValueDelimiter.COMMA);
        p23.addValue(new ValueString("v1"));
        p23.addValue(iniFile, "$Sekce::podsekce", "Option 3");
        p23.addValue(new ValueString("v2"));
        p23.setComment("volba 'Option 4' je seznam hodnot 'v1', 'value 1', 'value 1', 'v2'");
        section2.addProperty(p23);

        Property p24 = new Property("Option 5", false, new ValueStringRestriction());
        p24.setDelimiter(Property.ValueDelimiter.COMMA);
        p24.addValue(new ValueString("v1"));
        p24.addValue(new ValueString("v2\\:v3"));
        p24.setComment("volba 'Option 5' je seznam hodnot 'v1' a 'v2\\:v3', nebo 'v1, v2' a 'v3' podle zvoleneho oddelovace");

        section2.addProperty(p24);

        // section 3
        Section section3 = new Section("Cisla", true);
        Property p31 = new Property("cele", true, new ValueSignedRestriction());
        p31.addValue(new ValueSigned("-1285"));

        Property p32 = new Property("cele_bin", true, new ValueSignedRestriction());
        p32.addValue(new ValueSigned("0b01101001"));

        Property p33 = new Property("cele_hex", false, new ValueSignedRestriction());
        p33.setDelimiter(Property.ValueDelimiter.COMMA);
        p33.addValue(new ValueSigned("0x12ae"));
        p33.addValue(new ValueSigned("0xAc2B"));

        Property p34 = new Property("cele_oct", true, new ValueSignedRestriction());
        p34.addValue(new ValueSigned("01754"));

        Property p35 = new Property("float1", true, new ValueFloatRestriction());
        p35.addValue(new ValueFloat(-124.45667356));

        Property p36 = new Property("float2", true, new ValueFloatRestriction());
        p36.addValue(new ValueFloat(+4.1234565E+45));

        Property p37 = new Property("float3", true, new ValueFloatRestriction());
        p37.addValue(new ValueFloat(412.34565e45));

        Property p38 = new Property("float4", true, new ValueFloatRestriction());
        p38.addValue(new ValueFloat(-1.1245864E-6));

        section3.addProperty(p31);
        section3.addProperty(p32);
        section3.addProperty(p33);
        section3.addProperty(p34);
        section3.addProperty(p35);
        section3.addProperty(p36);
        section3.addProperty(p37);
        section3.addProperty(p38);

        iniFile.addSection(section3);

        // section 4
        Section section4 = new Section("Other", true);
        Property p41 = new Property("bool1", true, new ValueBooleanRestriction());
        p41.addValue(new ValueBoolean("1"));

        Property p42 = new Property("bool2", true, new ValueBooleanRestriction());
        p42.addValue(new ValueBoolean("on"));

        Property p43 = new Property("bool3", true, new ValueBooleanRestriction());
        p43.addValue(new ValueBoolean("f"));

        section4.addProperty(p41);
        section4.addProperty(p42);
        section4.addProperty(p43);

        iniFile.addSection(section4);

        return iniFile;
    }

    public static String getConfigSimpleMoreSections() throws IOException {
        return getFileContents(CONFIG_SIMPLE_MORE_SECTIONS);
    }

    public static String getConfigMuster() throws IOException {
        return getFileContents(CONFIG_MUSTER);
    }
    
    public static String getConfigMusterToString() throws IOException {
        return getFileContents(CONFIG_MUSTER_TOSTRING);
    }

    public static String getConfigMalformed() throws IOException {
        return getFileContents(CONFIG_MALFORMED);
    }

    public static String getConfigDupliciteIDSection() throws IOException {
        return getFileContents(CONFIG_DUPLICITE_ID_SECTION);
    }

    public static String getConfigDupliciteIDProperty() throws IOException {
        return getFileContents(CONFIG_DUPLICITE_ID_PROPERTY);
    }

    private static String getFileContents(String fileName) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);

        return text;
    }
}
