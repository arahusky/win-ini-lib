package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.exceptions.FileFormatException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueBoolean;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueSigned;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueString;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueBooleanRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueStringRestriction;

/**
 *
 * @author Jakub Naplava
 */
public class ExampleUsage {

    public static Format getBasicFormat() {
        //First, we have to define the format

        Format format = new Format();

        //The core of the ini files are sections, let's define one named 'Section1', which is always required.
        Section section1 = new Section("Section1", true);

        //Every section consists of several properties
        Property property1 = new Property("Option1", true, new ValueStringRestriction());
        property1.addValue(new ValueString("value1"));
        
        Property property2 = new Property("Option2", true, new ValueBooleanRestriction());
        property2.addValue(new ValueBoolean("true"));
        property2.setComment("Option2 has value 'true'");
        
        section1.addProperty(property1);
        section1.addProperty(property2);
        
        format.addSection(section1);        
        return format;
    }
    
    public static void loadIniFileAndCheckItAgainstGivenFormat() {
        String fileName = "sampleFile.ini";
        
        Format format = getBasicFormat();
        try {
            format.loadDataFromFile(fileName);
        } catch (FileFormatException ex) {
            System.err.println("The file did not meet specified format.");
        }        
    }
    
    public static void loadModifyAndSaveIniFile() throws FileFormatException {
        String fileName = "sampleFile.ini";        
        Format format = getBasicFormat();
        
        format.loadDataFromFile(fileName);
        
        //TODO getSection(string identifier)
        Section sectionToBeModified = format.getSection(2);
        
        //TODO getProperty(string key)
        Property prop = sectionToBeModified.getProperty(2);
        prop.addValue(new ValueSigned(123));
        
        format.saveToFile(fileName, Format.SaveType.FULL);        
    }
}
