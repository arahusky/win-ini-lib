package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.exceptions.DuplicateNameException;
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
    private static final String CONFIG_MUSTER = "test/sample_configurations/config_muster.txt";
    
    private static final String CONFIG_DUPLICITE_ID_SECTION = "test/sample_configurations/config_duplicite_id_section.txt";
    private static final String CONFIG_DUPLICITE_ID_PROPERTY = "test/sample_configurations/config_duplicite_id_property.txt";
    
    public static String getConfigSimple() throws IOException {
        return getFileContents(CONFIG_SIMPLE);
    }
    
    public static IniFile getIniFileSimple() throws DuplicateNameException {
        IniFile iniFile = new IniFile();
        
        Section section1 = new Section("Sekce 1", true);
        section1.addProperty(new Property("Option 1", true, new ValueStringRestriction()));
        section1.addProperty(new Property("oPtion", true, new ValueUnsignedRestriction()));
        iniFile.addSection(section1);
        
        Section section2 = new Section("Cisla", true);
        section2.addProperty(new Property("cele", true, new ValueSignedRestriction()));
        section2.addProperty(new Property("cele_bin", true, new ValueSignedRestriction()));
        iniFile.addSection(section2);
        
        return iniFile;
    }
    
    public static String getConfigMuster() throws IOException {
        return getFileContents(CONFIG_MUSTER);
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
