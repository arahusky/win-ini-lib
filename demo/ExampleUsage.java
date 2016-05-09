
import cz.cuni.mff.d3s.pp.wininilib.IniFile;
import cz.cuni.mff.d3s.pp.wininilib.IniFile.LoadingMode;
import cz.cuni.mff.d3s.pp.wininilib.Property;
import cz.cuni.mff.d3s.pp.wininilib.Section;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.DuplicateNameException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.FileFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.TooManyValuesException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueSigned;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueString;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueBooleanRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueEnumRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueFloatRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueStringRestriction;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This class shows basic examples of the library usage.
 *
 * @author xxx
 */
public class ExampleUsage {

    /**
     * Generates basic ini-file format, which consists of two sections with some
     * properties.
     *
     * @return created ini-file format.
     */
    public static IniFile getBasicFormat() throws TooManyValuesException, InvalidValueFormatException, ViolatedRestrictionException, DuplicateNameException {
        //First, we have to define the format
        IniFile format = new IniFile();

        //The core of the ini files are sections, let's define one named 'Section1', which is required.
        Section section1 = new Section("Section1", true);

        //Every section consists of several properties
        //let's create a required property named "Option1", whose values can be of type string and have no further restriction 
        Property property1 = new Property("Option1", true, new ValueStringRestriction());
        //and fill in the property with the one value "value1"
        property1.addValue(new ValueString("value1"));

        //second arbitrary property has identifier "Option2" and its values are of type boolean
        Property property2 = new Property("Option2", false, new ValueBooleanRestriction());
        property2.setComment("Option2 has value 'true'");
        
        //add Enum property
        Property property3 = new Property("Key", true, new ValueEnumRestriction(TestEnum.class));
        property3.addValue(TestEnum.ONE);
        
        //add float value property with interval restriction
        Property property4 = new Property("number", true, new ValueFloatRestriction(0, 1000));
        double value = 500;
        property4.addValue(value); // value out of the specified interval causes an exception
        
        section1.addProperty(property1);
        section1.addProperty(property2);
        section1.addProperty(property3);
        section1.addProperty(property4);

        format.addSection(section1);
        return format;
    }

    /**
     * Basic enum for test purposes.
     */
    enum TestEnum {
        ONE,
        TWO,
        THREE
    }
    
    /**
     * This method generates basic ini-file format and tries to fill it in with
     * configuration from a file. If the file contains configuration, which does
     * not meet the requirements of the defined format, an error message is
     * printed.
     *
     * @throws TooManyValuesException
     * @throws ViolatedRestrictionException
     * @throws InvalidValueFormatException
     */
    public static void loadIniFileAndCheckItAgainstGivenFormat() throws TooManyValuesException, ViolatedRestrictionException, InvalidValueFormatException, DuplicateNameException, IOException {
        String fileName = "sampleFile.ini";

        IniFile ini = getBasicFormat();
        try {
            //try to fill in the ini format file with the configuration
            ini.loadDataFromFile(fileName, LoadingMode.RELAXED);
            System.out.println("The file meets the specified format.");
        } catch (FileFormatException ex) {
            System.err.println("The file did not meet specified format.");
        }
    }

    /**
     * This method tries to load configuration, change some of its internals and
     * save it back.
     *
     */
    public static void loadModifyAndSaveIniFile() throws FileFormatException, TooManyValuesException, InvalidValueFormatException, ViolatedRestrictionException, DuplicateNameException, FileNotFoundException, IOException {
        String fileName = "sampleFile.ini";
        IniFile ini = getBasicFormat();

        ini.loadDataFromFile(fileName, LoadingMode.RELAXED);

        Section sectionToBeModified = ini.getSection("Section1");

        Property prop = sectionToBeModified.getProperty("Option1");
        prop.addValue(new ValueSigned("123"));

        ini.saveToFile(fileName, IniFile.SavingMode.FULL);
    }

    /**
     * This method loads the configuration of the given file in a static-mode.
     * This means, that no ini-file format needs to be predefined, but it is
     * derived from the configuration.
     */
    public static void loadFileStatic() throws IOException {
        String fileName = "sampleFile.ini";
        try {
            IniFile ini = IniFile.loadIniFromFile(fileName);
            System.out.println(ini.toString(IniFile.SavingMode.FULL));
        } catch (FileFormatException ex) {
            System.err.println("This file is not valid ini file.");
        }
    }
}
