package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.exceptions.DuplicateNameException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.FileFormatException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Provides a representation for .ini configuration file.
 *
 * @author xxx
 */
public class IniFile {

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
     * @throws DuplicateNameException if there is already section with current
     * name.
     */
    public void addSection(Section section) throws DuplicateNameException {
        for (Section s : sections) {
            if (s.getIdentifier().equals(section.getIdentifier())) {
                throw new DuplicateNameException("Section with name " + section.getIdentifier() + " is already defined.");
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
        return toString(SavingMode.FULL);
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
    public String toString(SavingMode type) {
        StringBuilder sb = new StringBuilder();
        for (Section section : sections) {
            sb.append(section.toString(type));
            sb.append(Constants.NEW_LINE);
            sb.append(Constants.NEW_LINE);
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
    public Stream toStream(SavingMode type) {

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
    public void saveToFile(String fileName, SavingMode type) throws IOException {
        File file = new File(fileName);

        // TODO: overridujeme stavajici file bez upozorneni?
        if (!file.exists()) {
            file.createNewFile();
        }

        try (PrintWriter writer = new PrintWriter(fileName, Constants.CODING)) {
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
    public void loadDataFromFile(String fileName, LoadingMode type) throws FileFormatException, IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            IniFileUtils.parseAndValidate(this, sb.toString(), type);
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
    public void loadDataFromStream(Stream stream, LoadingMode type) throws FileFormatException {
        //TODO netusim
        //String[] data = (String[]) stream.toArray();
        //IniFileParser.parseAndValidate(this, iniFile, type);
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
    public void loadDataFromString(String iniFile, LoadingMode type) throws FileFormatException {
        IniFileUtils.parseAndValidate(this, iniFile, type);
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
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return IniFileUtils.createIniFile(sb.toString());

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
        //String[] data = (String[]) stream.toArray();
        //return IniFileUtils.createIniFile(data);
        
        //TODO nevim jak
        return null;
    }

    /**
     * Loads the ini file from the specified string. Always loaded in RELAXED
     * mode.
     *
     * @param iniFile file string from which the IniFile will be loaded.
     * @return Loaded ini file from the specified string with the specified type
     * of load.
     * @throws FileFormatException if the loaded ini file is not valid.
     */
    public static IniFile loadIniFromString(String iniFile) throws FileFormatException {
        return IniFileUtils.createIniFile(iniFile);
    }

    /**
     * Represents a type of ini file to load. STRICT loaded file must meet the
     * current ini file, no defaults. RELAXED tries to avoid errors as much as
     * possible.
     */
    public enum LoadingMode {

        STRICT,
        RELAXED
    }

    /**
     * Represents a type of ini file to save. FULL saves the whole IniFile with
     * all comments etc. ORIGIN save is based on original IniFile from which it
     * was loaded.
     *
     */
    public enum SavingMode {

        FULL,
        ORIGINAL
    }
}
