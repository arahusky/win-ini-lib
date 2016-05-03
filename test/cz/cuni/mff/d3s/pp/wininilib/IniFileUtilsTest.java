package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.IniFileUtils.RawSection;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.FileFormatException;
import java.io.IOException;
import java.util.List;
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
        Assert.assertTrue(rs.getBody().endsWith(Constants.NEW_LINE + "oPtion 1 = 15"));
        
        rs = sections.get(1);
        Assert.assertEquals("Cisla",rs.getIdentifier());
        Assert.assertTrue(rs.getBody().startsWith("cele = -1285" + Constants.NEW_LINE));
        Assert.assertTrue(rs.getBody().endsWith(Constants.NEW_LINE + "cele_bin = 0b01101001"));
    }    
    
    @Test(expected = FileFormatException.class)
    public void testDivideToSectionsDupliciteSectionID() throws IOException, FileFormatException {        
        String config1 = SampleConfigurationsLoader.getConfigDupliciteIDSection();
        List<RawSection> sections = IniFileUtils.divideToSections(config1);
    }   
}
