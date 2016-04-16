package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.exceptions.DupliciteNameException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.FileFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.TooManyValuesException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueString;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueStringRestriction;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Provides a representation for .ini configuration file.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class IniFile {

    public static final String NEW_LINE = System.getProperty("line.separator");
    public static final String CODING = "UTF-8";
    public static final String COMMENT_DELIMITER = ";";
    public static final String EQUAL_SIGN = "=";

    private final List<Section> sections;

    /**
     * Initializes a new instance of <code>IniFile</code>. Represents an .ini
     * file.
     */
    public IniFile() {
        sections = new ArrayList<>();
    }

    /**
     * Returns the section at the specified position.
     *
     * @param i index of the element to return.
     * @return the element at the specified position in sections.
     */
    public Section getSection(int i) {
        return sections.get(i);
    }

    /**
     * Returns the section with the specified identifier.
     *
     * @param identifier identifier of section to be returned.
     * @return the section with the specified identifier. Null if there is not a
     * section with the specified name.
     */
    public Section getSection(String identifier) {
        for (Section s : sections) {
            if (s.getIdentifier().equals(identifier)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Adds the specified element to the end of sections.
     *
     * @param section section to be added to sections.
     * @throws DupliciteNameException if there is already section with current
     * name.
     */
    public void addSection(Section section) throws DupliciteNameException {
        for (Section s : sections) {
            if (s.getIdentifier().equals(section.getIdentifier())) {
                throw new DupliciteNameException("Section with name " + section.getIdentifier() + " is already defined.");
            }
        }
        sections.add(section);
    }

    /**
     * Removes first occurrence of given section.
     *
     * @param section section to be removed.
     * @return true if any section was removed; otherwise false.
     */
    public boolean removeSection(Section section) {
        return sections.remove(section);
    }

    /**
     * Returns a number of sections within the current ini file.
     *
     * @return a numer of sections within the current ini file.
     */
    public int getNumberOfSections() {
        return sections.size();
    }

    /**
     * Provides a string representation of the current ini file. Using FULL save
     * type.
     *
     * @return a string representation of the current ini file.
     */
    @Override
    public String toString() {
        return toString(SaveType.FULL);
    }

    /**
     * Provides a string representation of the current ini file. Type of
     * representation is specified by parameter.
     *
     * @param type determines a type of way how to represent the current
     * IniFile.
     * @return a string representation of the current ini file with the
     * specified type.
     */
    public String toString(SaveType type) {
        StringBuilder sb = new StringBuilder();
        for (Section section : sections) {
            sb.append(section.toString(type));
            sb.append(NEW_LINE);
            sb.append(NEW_LINE);
        }
        return sb.toString();
    }

    /**
     * Provides a representation of the current ini file as a Stream. Type of
     * representation is specified by parameter.
     *
     * @param type determines a type of way how to represent the current
     * IniFile.
     * @return a representation of the current IniFile as a Stream with the
     * specified type.
     */
    public Stream toStream(SaveType type) {

        // TODO: tady moc nevim co s tim... resp. jak pouzit javosky stream
        String result = toString(type);
        Stream stream = sections.stream();

        return null;
    }

    /**
     * Saves the current ini file to a file with the specified type.
     *
     * @param fileName name of file where to save.
     * @param type type of ini file representantion.
     * @throws IOException if any IO operation could not be performed.
     */
    public void saveToFile(String fileName, SaveType type) throws IOException {
        File file = new File(fileName);

        // TODO: overridujeme stavajici file bez upozorneni?
        if (!file.exists()) {
            file.createNewFile();
        }

        try (PrintWriter writer = new PrintWriter(fileName, CODING)) {
            writer.write(toString(type));
        }

        // TODO: vyhazujeme IOException, original Java, budeme to obalovat nasi IOEx???
    }

    /**
     * Loads the ini file from the specified file. The file will be loaded to
     * the current instance of IniFile. File must be valid to this IniFile,
     * otherwise an exception is thrown.
     *
     * @param fileName name of the file to load from.
     * @param type type of way of load.
     * @throws FileFormatException if the loaded file does not have this ini
     * file structure or is not valid.
     */
    public void loadDataFromFile(String fileName, LoadType type) throws FileFormatException, IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            List<String> data = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
            validateAndFill(data, type);
        }
    }

    /**
     * Loads the ini file from the specified stream. The stream will be loaded
     * to the current instance of IniFile. Ini file within the stream must be
     * valid to this IniFile, otherwise an exception is thrown.
     *
     * @param stream stream that contains the ini file.
     * @param type type of way of load.
     * @throws FileFormatException if the loaded ini file from the stream does
     * not have this ini file structure or is not valid.
     */
    public void loadDataFromStream(Stream stream, LoadType type) throws FileFormatException {
        String[] arr = (String[]) stream.toArray();
        List<String> data = new ArrayList<>(Arrays.asList(arr));
        validateAndFill(data, type);
    }

    /**
     * Loads the ini file from the specified string. The string will be loaded
     * to the current instance of IniFile. Ini file within the string must be
     * valid to this IniFile, otherwise an exception is thrown.
     *
     * @param iniFile string that contains the ini file.
     * @param type type of way of load.
     * @throws FileFormatException if the loaded ini file from the string does
     * not have this ini file structure or is not valid.
     */
    public void loadDataFromString(String iniFile, LoadType type) throws FileFormatException {
        String[] data = iniFile.split(NEW_LINE);
        validateAndFill(Arrays.asList(data), type);
    }

    /**
     * Loads the ini file from the specified file with the specified type of
     * load. Always loaded in RELAXED mode.
     *
     * @param fileName name of the file to load from.
     * @return Loaded ini file from the file with the specified type of load.
     * @throws FileFormatException if the loaded ini file is not valid.
     */
    public static IniFile loadIniFromFile(String fileName) throws FileFormatException, IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            List<String> data = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
            return createIniFile(data);

            // TODO: poresit EXCEPTIONY, jake se budou pouzivat ... nase, nebo 
            // teda nechame nekdy IOEX... pokud treba nejde najit soubor nebo tak...
        }
    }

    /**
     * Loads the ini file from the specified stream with the specified type of
     * load. Always loaded in RELAXED mode.
     *
     * @param stream stream from which the IniFile will be loaded.
     * @return Loaded ini file from the specified stream with the specified type
     * of load.
     * @throws FileFormatException if the loaded ini file is not valid.
     */
    public static IniFile loadIniFromStream(Stream stream) throws FileFormatException {
        String[] arr = (String[]) stream.toArray();
        List<String> data = new ArrayList<>(Arrays.asList(arr));
        return createIniFile(data);
    }

    /**
     * Loads the ini file from the specified string with the specified type of
     * load. Always loaded in RELAXED mode.
     *
     * @param iniFile file string from which the IniFile will be loaded.
     * @return Loaded ini file from the specified string with the specified type
     * of load.
     * @throws FileFormatException if the loaded ini file is not valid.
     */
    public static IniFile loadIniFromString(String iniFile) throws FileFormatException {
        String[] data = iniFile.split(NEW_LINE);
        return createIniFile(Arrays.asList(data));
    }

    /**
     * Represents a type of ini file to load. STRICT loaded file must meet the
     * current ini file, no defaults. RELAXED tries to avoid errors as much as
     * possible.
     */
    public enum LoadType {

        STRICT,
        RELAXED
    }

    /**
     * Represents a type of ini file to save. FULL saves the whole IniFile with
     * all comments etc. ORIGIN save is based on original IniFile from which it
     * was loaded.
     *
     */
    public enum SaveType {

        FULL,
        ORIGIN
    }

    //
    //
    //
    
    /**
     * Validates and fills the current instance of IniFile with the specified
     * data.
     *
     * @param data data to fill with.
     * @param type data load mode.
     */
    private static void validateAndFill(List<String> data, LoadType type) {
        // TODO: Budeme zachovavat poradi nebo to muze byt v tom filu i na preskacku?

    }

    /**
     * Creates an INI file using the specified data.
     *
     * @param data data of the ini file.
     * @return an INI file using the specified data.
     */
    private static IniFile createIniFile(List<String> data) throws FileFormatException {

        IniFile iniFile = new IniFile();

        boolean sectionStarted = false;
        StringBuilder section = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isEmpty()) {
                data.remove(i);
            }

            // Divide line to value and a comment
            String[] parts = data.get(i).split(COMMENT_DELIMITER, 2);
            String value = parts[0];

            if ((value.charAt(0) == '[') && (value.charAt(value.length() - 1) == ']')) {
                // new section found
                if (!section.toString().isEmpty()) {
                    try {
                        iniFile.addSection(parseSection(section.toString()));
                    } catch (DupliciteNameException ex) {
                        throw new FileFormatException("Invalid file format. Duplicite name detected,", ex);
                    }
                }
                section = new StringBuilder();
                section.append(data.get(i));
                sectionStarted = true;
                continue;
            }

            if (sectionStarted) {
                section.append(data.get(i));
            } else {
                throw new FileFormatException("Invalid file format.");
            }
        }
        return iniFile;
    }

    /**
     * As a parameter gets a section a parses it to a instance of Section class.
     * Always used in RELAXED mode.
     *
     * @param section section to be parsed.
     * @return a parsed section.
     */
    private static Section parseSection(String section) throws FileFormatException {
        Section result = null;
        boolean required = false;

        String[] lines = section.split(NEW_LINE);
        if (lines.length == 0) {
            throw new FileFormatException("Invalid format.");
        }

        for (int i = 0; i < lines.length; i++) {
            String[] parts = lines[i].split(COMMENT_DELIMITER, 2);
            String value = parts[0];
            String comment = "";
            if (parts.length > 1) {
                comment = parts[1];
            }

            if (i == 0) {
                result = new Section(value, required);
                result.setComment(comment);
                continue;
            }
            result.addProperty(parseProperty(lines[i]));
        }
        return result;
    }

    /**
     * TODO: Rict, ze defaultne to vemu jako string a hotovo (nebo teda udelat
     * to nejspecificnejsi...?)
     *
     * @param property
     * @return
     */
    private static Property parseProperty(String property) throws FileFormatException {
        Property prop;

        String[] parts = property.split(COMMENT_DELIMITER, 2);
        String leftSide = parts[0];
        String comment = "";
        if (parts.length > 1) {
            comment = parts[1];
        }

        parts = leftSide.split(EQUAL_SIGN);
        Property.ValueDelimiter valueDelimiter = null;
        switch (parts.length) {
            // Using string-value type. 'Real' type can not be decided.
            case 2:
                String[] values = null;

                // If we have both 'comma' and 'colon' in line, we use comma as delimiter
                if (parts[1].contains(Property.ValueDelimiter.COLON.toString())) {
                    values = parts[1].split(Property.ValueDelimiter.COLON.toString());
                    valueDelimiter = Property.ValueDelimiter.COLON;
                }
                if (parts[1].contains(Property.ValueDelimiter.COMMA.toString())) {
                    values = parts[1].split(Property.ValueDelimiter.COMMA.toString());
                    valueDelimiter = Property.ValueDelimiter.COMMA;
                }

                boolean isSingleValue = true;
                if (values.length > 1) {
                    isSingleValue = false;
                }
                prop = new Property(parts[0], isSingleValue, new ValueStringRestriction());
                try {
                    for (String val : values) {
                        prop.addValue(new ValueString(val));
                    }
                } catch (TooManyValuesException | ViolatedRestrictionException ex) {
                    throw new FileFormatException("Invalid file format.", ex);
                }
                break;
            case 1:
                prop = new Property(parts[0], true, new ValueStringRestriction());
                valueDelimiter = Property.ValueDelimiter.COMMA;
            default:
                throw new FileFormatException("Invalid file format.");
        }

        if (!comment.isEmpty()) {
            prop.setComment(comment);
        }
        prop.setDelimiter(valueDelimiter);

        return prop;
    }
}
