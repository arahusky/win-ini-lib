package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.IniFile.LoadingMode;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.FileFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.WinIniLibException;
import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author xxx
 */
public class IniFileTest {

    @Test
    public void testLoadSimpleConfiguration() throws IOException, WinIniLibException {
        String config1 = SampleConfigurationsLoader.getConfigSimple();
        IniFile iniFile = SampleConfigurationsLoader.getIniFileSimple();
        iniFile.loadDataFromString(config1, LoadingMode.STRICT);
        
        IniFileUtilsTest.testSimpleConfiguration(iniFile);
    }
    
    @Test(expected = FileFormatException.class)
    public void loadStrictShouldRaiseExceptionWhenLoadedHasMoreSections() throws IOException, WinIniLibException {
        String config1 = SampleConfigurationsLoader.getConfigSimpleMoreSections();
        IniFile iniFile = SampleConfigurationsLoader.getIniFileSimple();
        iniFile.loadDataFromString(config1, LoadingMode.STRICT);
    }
    
    @Test
    public void testLoadSimpleMoreSectionRelaxed() throws IOException, WinIniLibException {
        String config1 = SampleConfigurationsLoader.getConfigSimpleMoreSections();
        IniFile iniFile = SampleConfigurationsLoader.getIniFileSimple();
        iniFile.loadDataFromString(config1, LoadingMode.RELAXED);
        
        IniFileUtilsTest.testRelaxedSimpleMoreSectionsAgainstSimple(iniFile);
    }
    
    @Test
    public void loadStaticSimpleConfiguration() throws IOException, WinIniLibException{
        String config1 = SampleConfigurationsLoader.getConfigSimple();
        IniFile iniFile = IniFile.loadIniFromString(config1);
        
        IniFileUtilsTest.testStaticSimpleConfiguration(iniFile);
    }
    
    @Test
    public void testLoadMusterConfiguration() throws IOException, WinIniLibException {
        String config1 = SampleConfigurationsLoader.getConfigMuster();
        IniFile iniFile = SampleConfigurationsLoader.getIniFileMuster();
        iniFile.loadDataFromString(config1, LoadingMode.STRICT);
        
        //IniFileUtilsTest.testSimpleConfiguration(iniFile);
    }
    
    @Test(expected = FileFormatException.class)
    public void loadMalformedFileShouldRaiseException() throws IOException, WinIniLibException {
        String malformedConfig = SampleConfigurationsLoader.getConfigMalformed();
        IniFile iniFile = SampleConfigurationsLoader.getIniFileSimple();
        iniFile.loadDataFromString(malformedConfig, LoadingMode.STRICT);
    }
    
    @Test(expected = FileFormatException.class)
    public void loadDupliciteIDSectionFileShouldRaiseException() throws IOException, WinIniLibException {
        String duppliciteIDSection = SampleConfigurationsLoader.getConfigDupliciteIDSection();
        IniFile iniFile = SampleConfigurationsLoader.getIniFileSimple();
        iniFile.loadDataFromString(duppliciteIDSection, LoadingMode.RELAXED);
    }
    
    @Test(expected = FileFormatException.class)
    public void loadDupliciteIDPropertyFileShouldRaiseException() throws IOException, WinIniLibException {
        String dupliciteIDProperty = SampleConfigurationsLoader.getConfigDupliciteIDProperty();
        IniFile iniFile = SampleConfigurationsLoader.getIniFileSimple();
        iniFile.loadDataFromString(dupliciteIDProperty, LoadingMode.RELAXED);
        System.out.println(iniFile.toString());
    }
}
