package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.IniFile.LoadingMode;
import cz.cuni.mff.d3s.pp.wininilib.IniFileUtils.RawSection;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.DuplicateNameException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.FileFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.WinIniLibException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueSigned;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueString;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueUnsigned;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author xxx
 */
public class IniFileUtilsTest {
    
    @Test
    public void testDivideToSectionsSimple() throws IOException, FileFormatException {        
        String config1 = SampleConfigurationsLoader.getConfigSimple();
        List<RawSection> sections = IniFileUtils.divideToSections(config1);
        
        Assert.assertNotNull(sections);        
        Assert.assertEquals(2, sections.size());
        
        RawSection rs = sections.get(0);
        Assert.assertEquals("Sekce 1",rs.getIdentifier());
        Assert.assertTrue(rs.getBody().startsWith("Option 1 = value 1")); 
        Assert.assertTrue(rs.getBody().endsWith(Constants.NEW_LINE + "oPtion 1 = 15, 16"));
        
        rs = sections.get(1);
        Assert.assertEquals("Cisla",rs.getIdentifier());
        Assert.assertTrue(rs.getBody().startsWith("cele = -1285:134" + Constants.NEW_LINE));
        Assert.assertTrue(rs.getBody().endsWith(Constants.NEW_LINE + "cele_bin = 0b01101001"));
    }    
    
    @Test(expected = FileFormatException.class)
    public void testDivideToSectionsDupliciteSectionID() throws IOException, FileFormatException {        
        String config1 = SampleConfigurationsLoader.getConfigDupliciteIDSection();
        List<RawSection> sections = IniFileUtils.divideToSections(config1);
    }   
    
    @Test
    public void testPairSectionsSimple() throws IOException, DuplicateNameException, FileFormatException {
        String config1 = SampleConfigurationsLoader.getConfigSimple();
        IniFile iniFile = SampleConfigurationsLoader.getIniFileSimple();
        List<Section> iniFileSections = new ArrayList<>();
        
        for (int i = 0; i < iniFile.getNumberOfSections(); i++) {
            iniFileSections.add(iniFile.getSection(i));
        }
        
        List<RawSection> rawSections = IniFileUtils.divideToSections(config1);
        List<Pair<Section, String>> pairedSections = IniFileUtils.pairSections(iniFileSections, rawSections, IniFile.LoadingMode.STRICT);
        
        Assert.assertEquals(2, pairedSections.size());
        
        Section firstSection = iniFile.getSection(0);
        String firstSectionBody = "Option 1 = value 1  ; volba 'Option 1' ma hodnotu 'value 1'" + Constants.NEW_LINE + "oPtion 1 = 15, 16";
        Assert.assertEquals(firstSection,pairedSections.get(0).getKey());
        Assert.assertEquals(firstSectionBody, pairedSections.get(0).getValue());
        
        Section secondSection = iniFile.getSection(1);
        String secondSectionBody = "cele = -1285:134" + Constants.NEW_LINE + "cele_bin = 0b01101001";
        Assert.assertEquals(secondSection,pairedSections.get(1).getKey());
        Assert.assertEquals(secondSectionBody, pairedSections.get(1).getValue());
    }
    
    @Test
    public void testPairSectionsShouldRaiseExceptionWhenMissingRequiredSection() {
        //TODO
    }    
    
    @Test
    public void testCombineSectionsSimple() throws IOException, WinIniLibException {
        String config1 = SampleConfigurationsLoader.getConfigSimple();
        IniFile iniFile = SampleConfigurationsLoader.getIniFileSimple();
        List<Section> iniFileSections = new ArrayList<>();
        for (int i = 0; i < iniFile.getNumberOfSections(); i++) {
            iniFileSections.add(iniFile.getSection(i));
        }
        
        List<RawSection> rawSections = IniFileUtils.divideToSections(config1);
        List<Pair<Section, String>> pairedSections = IniFileUtils.pairSections(iniFileSections, rawSections, IniFile.LoadingMode.STRICT);
        
         for (Pair<Section, String> pair : pairedSections) {
            Section iniFileSection = pair.getKey();
            String rawSection = pair.getValue();

            IniFileUtils.combineSections(iniFileSection, rawSection, LoadingMode.STRICT);
        }
         
         Section section1 = iniFile.getSection(0);
         Property section1Property1 = section1.getProperty(0);
         Assert.assertEquals(section1Property1.getNumberOfValues(),1);
         Assert.assertEquals(section1Property1.getValue(0),new ValueString("value 1", true));
         Assert.assertEquals("volba 'Option 1' ma hodnotu 'value 1'", section1Property1.getComment());
         
         Property section1Property2 = section1.getProperty(1);
         Assert.assertEquals(section1Property2.getNumberOfValues(),2);
         Assert.assertEquals(section1Property2.getValue(0),new ValueUnsigned("15", true));
         Assert.assertEquals(section1Property2.getValue(1),new ValueUnsigned("16", true));
         
         Section section2 = iniFile.getSection(1);
         Property section2Property1 = section2.getProperty(0);
         Assert.assertEquals(section2Property1.getNumberOfValues(),2);
         Assert.assertEquals(section2Property1.getValue(0),new ValueSigned("-1285", true));
         Assert.assertEquals(section2Property1.getValue(1),new ValueSigned("134", true));
         
         Property section2Property2 = section2.getProperty(1);
         Assert.assertEquals(section2Property2.getNumberOfValues(),1);
         Assert.assertEquals(section2Property2.getValue(0),new ValueSigned("0b01101001", true));
    }
}
