package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.IniFile.LoadType;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.FileFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.TooManyValuesException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueBoolean;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueSigned;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueString;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueBooleanRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueStringRestriction;

/**
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ExampleUsage {

    public static IniFile getBasicFormat() throws TooManyValuesException, ViolatedRestrictionException {
        //First, we have to define the format

        IniFile format = new IniFile();

        //The core of the ini files are sections, let's define one named 'Section1', which is always required.
        Section section1 = new Section("Section1", true);

        //Every section consists of several properties
        Property property1 = new Property("Option1", true, new ValueStringRestriction());
        property1.addValue(new ValueString("value1"));

        Property property2 = new Property("Option2", true, new ValueBooleanRestriction());
        property2.addValue(new ValueBoolean("y"));
        property2.setComment("Option2 has value 'true'");

        section1.addProperty(property1);
        section1.addProperty(property2);

        format.addSection(section1);
        return format;
    }

    public static void loadIniFileAndCheckItAgainstGivenFormat() throws TooManyValuesException, ViolatedRestrictionException {
        String fileName = "sampleFile.ini";

        IniFile format = getBasicFormat();
        try {
            format.loadDataFromFile(fileName, LoadType.RELAXED);
        } catch (FileFormatException ex) {
            System.err.println("The file did not meet specified format.");
        }
    }

    public static void loadModifyAndSaveIniFile() throws FileFormatException, TooManyValuesException, InvalidValueFormat, ViolatedRestrictionException {
        String fileName = "sampleFile.ini";
        IniFile format = getBasicFormat();

        format.loadDataFromFile(fileName, LoadType.RELAXED);

        //TODO getSection(string identifier)
        Section sectionToBeModified = format.getSection(2);

        //TODO getProperty(string key)
        Property prop = sectionToBeModified.getProperty(2);
        prop.addValue(new ValueSigned(123));

        format.saveToFile(fileName, IniFile.SaveType.FULL);
    }
}
