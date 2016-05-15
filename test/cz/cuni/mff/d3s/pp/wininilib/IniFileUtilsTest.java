package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.IniFile.LoadingMode;
import cz.cuni.mff.d3s.pp.wininilib.IniFileUtils.RawSection;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.DuplicateNameException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.FileFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;
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
        Assert.assertEquals("Sekce 1", rs.getIdentifier());
        Assert.assertTrue(rs.getBody().startsWith("Option 1 = value 1"));
        Assert.assertTrue(rs.getBody().endsWith(Constants.NEW_LINE + "oPtion 1 = 15, 16"));

        rs = sections.get(1);
        Assert.assertEquals("Cisla", rs.getIdentifier());
        Assert.assertTrue(rs.getBody().startsWith("cele = -1285:134" + Constants.NEW_LINE));
        Assert.assertTrue(rs.getBody().endsWith(Constants.NEW_LINE + "cele_bin = 0b01101001"));
    }

    @Test(expected = FileFormatException.class)
    public void testDivideToSectionsDupliciteSectionID() throws IOException, FileFormatException {
        String config1 = SampleConfigurationsLoader.getConfigDupliciteIDSection();
        List<RawSection> sections = IniFileUtils.divideToSections(config1);
    }

    @Test
    public void testPairSectionsSimple() throws IOException, DuplicateNameException, FileFormatException, InvalidValueFormatException {
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
        Assert.assertEquals(firstSection, pairedSections.get(0).getKey());
        Assert.assertEquals(firstSectionBody, pairedSections.get(0).getValue());

        Section secondSection = iniFile.getSection(1);
        String secondSectionBody = "cele = -1285:134" + Constants.NEW_LINE + "cele_bin = 0b01101001";
        Assert.assertEquals(secondSection, pairedSections.get(1).getKey());
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

            IniFileUtils.combineSections(iniFile, iniFileSection, rawSection, LoadingMode.STRICT);
        }

        testSimpleConfiguration(iniFile);
    }

    @Test
    public void testParseAndValidateSimpleStrict() throws IOException, WinIniLibException {
        String config1 = SampleConfigurationsLoader.getConfigSimple();
        IniFile iniFile = SampleConfigurationsLoader.getIniFileSimple();
        IniFileUtils.parseAndValidate(iniFile, config1, LoadingMode.STRICT);

        testSimpleConfiguration(iniFile);
    }

    @Test(expected = FileFormatException.class)
    public void parseAndValidateSimpleMoreSectionShouldExceptionAgainstSimpleConfiguration() throws IOException, WinIniLibException {
        String config1 = SampleConfigurationsLoader.getConfigSimpleMoreSections();
        IniFile iniFile = SampleConfigurationsLoader.getIniFileSimple();
        IniFileUtils.parseAndValidate(iniFile, config1, LoadingMode.STRICT);
    }

    @Test
    public void testParseAndValidateSimpleMoreSectionRelaxed() throws IOException, WinIniLibException {
        String config1 = SampleConfigurationsLoader.getConfigSimpleMoreSections();
        IniFile iniFile = SampleConfigurationsLoader.getIniFileSimple();
        IniFileUtils.parseAndValidate(iniFile, config1, LoadingMode.RELAXED);

        testRelaxedSimpleMoreSectionsAgainstSimple(iniFile);
    }

    @Test
    public void testStaticSimpleConfiguration() throws IOException, WinIniLibException {
        String config1 = SampleConfigurationsLoader.getConfigSimple();
        IniFile iniFile = IniFileUtils.createIniFile(config1);

        testStaticSimpleConfiguration(iniFile);
    }

    protected static void testSimpleConfiguration(IniFile iniFile) throws InvalidValueFormatException {
        Section section1 = iniFile.getSection(0);
        Property section1Property1 = section1.getProperty(0);
        Assert.assertEquals(1, section1Property1.getNumberOfValues());
        Assert.assertEquals(new ValueString("value 1"), section1Property1.getValue(0));
        Assert.assertEquals("volba 'Option 1' ma hodnotu 'value 1'", section1Property1.getComment());

        Property section1Property2 = section1.getProperty(1);
        Assert.assertEquals(2, section1Property2.getNumberOfValues());
        Assert.assertEquals(new ValueUnsigned("15"), section1Property2.getValue(0));
        Assert.assertEquals(new ValueUnsigned("16"), section1Property2.getValue(1));

        Section section2 = iniFile.getSection(1);
        Property section2Property1 = section2.getProperty(0);
        Assert.assertEquals(2, section2Property1.getNumberOfValues());
        Assert.assertEquals(new ValueSigned("-1285"), section2Property1.getValue(0));
        Assert.assertEquals(new ValueSigned("134"), section2Property1.getValue(1));

        Property section2Property2 = section2.getProperty(1);
        Assert.assertEquals(1, section2Property2.getNumberOfValues());
        Assert.assertEquals(new ValueSigned("0b01101001"), section2Property2.getValue(0));
    }
    
    protected static void testMusterConfiguration(IniFile iniFile) throws InvalidValueFormatException, IOException {
        Assert.assertEquals(SampleConfigurationsLoader.getConfigMusterToString(), iniFile.toString());
    }

    protected static void testRelaxedSimpleMoreSectionsAgainstSimple(IniFile iniFile) throws InvalidValueFormatException {
        testSimpleConfiguration(iniFile);

        Section section3 = iniFile.getSection(2);
        Assert.assertEquals("Obrazky", section3.getIdentifier());

        Property section3Property1 = section3.getProperty(0);
        Assert.assertEquals("zero", section3Property1.getKey());
        Assert.assertEquals(1, section3Property1.getNumberOfValues());
        Assert.assertEquals(new ValueString("simple"), section3Property1.getValue(0));
        Assert.assertEquals("simple comment", section3Property1.getComment());

        Property section3Property2 = section3.getProperty(1);
        Assert.assertEquals("first", section3Property2.getKey());
        Assert.assertEquals(2, section3Property2.getNumberOfValues());
        Assert.assertEquals(new ValueString("dog"), section3Property2.getValue(0));
        Assert.assertEquals(new ValueString("cat"), section3Property2.getValue(1));

        Property section3Property3 = section3.getProperty(2);
        Assert.assertEquals("second", section3Property3.getKey());
        Assert.assertEquals(2, section3Property3.getNumberOfValues());
        Assert.assertEquals(new ValueString("house with windows"), section3Property3.getValue(0));
        Assert.assertEquals(new ValueString("another house"), section3Property3.getValue(1));

        Property section3Property4 = section3.getProperty(3);
        Assert.assertEquals("third", section3Property4.getKey());
        Assert.assertEquals(2, section3Property4.getNumberOfValues());

        Assert.assertEquals(new ValueString("ball \\: cat"), section3Property4.getValue(0));
        Assert.assertEquals(new ValueString("dog"), section3Property4.getValue(1));
    }

    protected static void testStaticSimpleConfiguration(IniFile iniFile) throws InvalidValueFormatException {
        Section section1 = iniFile.getSection(0);
        Property section1Property1 = section1.getProperty(0);
        Assert.assertEquals(1, section1Property1.getNumberOfValues());
        Assert.assertEquals(new ValueString("value 1"), section1Property1.getValue(0));
        Assert.assertEquals("volba 'Option 1' ma hodnotu 'value 1'", section1Property1.getComment());

        Property section1Property2 = section1.getProperty(1);
        Assert.assertEquals(2, section1Property2.getNumberOfValues());
        Assert.assertEquals(new ValueString("15"), section1Property2.getValue(0));
        Assert.assertEquals(new ValueString("16"), section1Property2.getValue(1));

        Section section2 = iniFile.getSection(1);
        Property section2Property1 = section2.getProperty(0);
        Assert.assertEquals(2, section2Property1.getNumberOfValues());
        Assert.assertEquals(new ValueString("-1285"), section2Property1.getValue(0));
        Assert.assertEquals(new ValueString("134"), section2Property1.getValue(1));

        Property section2Property2 = section2.getProperty(1);
        Assert.assertEquals(1, section2Property2.getNumberOfValues());
        Assert.assertEquals(new ValueString("0b01101001"), section2Property2.getValue(0));
    }

    @Test
    public void testGetWhiteSpaces() {
        Assert.assertEquals("    ", IniFileUtils.getSpacesToWrite(" \\  \\ \\ \\ "));
        Assert.assertEquals("", IniFileUtils.getSpacesToWrite("     \\"));
        Assert.assertEquals("  ", IniFileUtils.getSpacesToWrite("\\ \\ \\"));
    }

    @Test
    public void testTrim1() {
        String value = "  \\ value 2\\ \\ \\        ";
        String expected = " value 2   ";
        Assert.assertEquals(expected, IniFileUtils.trim(value));
    }

    @Test
    public void testTrim2() {
        String value = "This is test string";
        Assert.assertEquals(value, IniFileUtils.trim(value));
    }

    @Test
    public void testTrim3() {
        String value = " This is test";
        String expected = "This is test";
        Assert.assertEquals(expected, IniFileUtils.trim(value));
    }

    @Test
    public void testTrim4() {
        String value = "Test ";
        String expected = "Test";
        Assert.assertEquals(expected, IniFileUtils.trim(value));
    }

    @Test
    public void testTrim5() {
        String value = " Test\\";
        String expected = "Test\\";
        Assert.assertEquals(expected, IniFileUtils.trim(value));
    }

    @Test
    public void testTrim6() {
        String value = "Test\\ ";
        String expected = "Test ";
        Assert.assertEquals(expected, IniFileUtils.trim(value));
    }

    @Test
    public void testTrim7() {
        String value = "\\ \\      Test\\ string\". \\ ";
        String expected = "  Test\\ string\". ";
        Assert.assertEquals(expected, IniFileUtils.trim(value));
    }

    @Test
    public void testTrim8() {
        String value = " 1";
        String expected = "1";
        Assert.assertEquals(expected, IniFileUtils.trim(value));
    }

    @Test
    public void testTrim9() {
        String value = "f";
        Assert.assertEquals(value, IniFileUtils.trim(value));
    }

    @Test
    public void testTrim10() {
        String value = "\\      ";
        String expected = " ";
        Assert.assertEquals(expected, IniFileUtils.trim(value));
    }

    @Test
    public void testTrim11() {
        String value = " ";
        String expected = "";
        Assert.assertEquals(expected, IniFileUtils.trim(value));
    }

    @Test
    public void testTrim12() {
        String value = " \\ \\ ";
        String expected = "  ";
        Assert.assertEquals(expected, IniFileUtils.trim(value));
    }

    @Test
    public void testTrim13() {
        String value = "\\ ";
        String expected = " ";
        Assert.assertEquals(expected, IniFileUtils.trim(value));
    }

    @Test
    public void testTrim14() {
        String value = "\\ x";
        String expected = " x";
        Assert.assertEquals(expected, IniFileUtils.trim(value));
    }

    @Test
    public void testSplitNoSplit() {
        String str = "aaa";
        Assert.assertArrayEquals(new String[]{str}, IniFileUtils.split(str, ','));
    }

    @Test
    public void testSplitCommaSimple() {
        String str = "aa a , b ,c";
        Assert.assertArrayEquals(new String[]{"aa a ", " b ", "c"}, IniFileUtils.split(str, ','));
    }

    @Test
    public void testSplitCommaWithBackslashes() {
        String str = "aa a \\, b ,c,d\\,e";
        Assert.assertArrayEquals(new String[]{"aa a \\, b ", "c", "d\\,e"}, IniFileUtils.split(str, ','));
    }
}
