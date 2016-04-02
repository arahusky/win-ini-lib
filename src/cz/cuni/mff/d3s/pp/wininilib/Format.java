package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.exceptions.FileFormatException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Provides a representation for .ini configuration file.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class Format {

    private List<Section> sections;

    /**
     * Initializes a new instance of <code>Format</code>. Represents an .ini
     * file.
     */
    public Format() {
        sections = new ArrayList<>();
    }

    /**
     * Returns the section at the specified position.
     *
     * @param i index of the element to return.
     * @return the element at the specified position in sections.
     */
    public Section getSection(int i) {
        return null;
    }

    /**
     * Adds the specified element to the end of sections.
     *
     * @param section section to be added to sections.
     */
    public void addSection(Section section) {

    }

    /**
     * Removes first occurrence of given section.
     *
     * @param section section to be removed.
     * @return true if any section was removed; otherwise false.
     */
    public boolean removeSection(Section section) {
        return false;
    }

    /**
     * Returns a number of sections within the current format.
     *
     * @return a numer of sections within the current format.
     */
    public int getNumberOfSections() {
        return sections.size();
    }

    /**
     * Provides a string representation of the current format.
     *
     * @return a string representation of the current format.
     */
    @Override
    public String toString() {
        return null;
    }

    /**
     * Provides a string representation of the current format. Type of
     * representation is specified by parameter.
     *
     * @param type determines a type of way how to represent the current Format.
     * @return a string representation of the current format with the specified
     * type.
     */
    public String toString(SaveType type) {
        return null;
    }

    /**
     * Provides a representation of the current format as a Stream. Type of
     * representation is specified by parameter.
     *
     * @param type determines a type of way how to represent the current Format.
     * @return a representation of the current Format as a Stream with the
     * specified type.
     */
    public Stream toStream(SaveType type) {
        return null;
    }

    /**
     * Saves the current format to a file with the specified type.
     *
     * @param fileName name of file where to save.
     * @param type type of format representantion.
     */
    public void saveToFile(String fileName, SaveType type) {
        // calls toString(type);
    }

    /**
     * Loads the format from the specified file. The file will be loaded to the
     * current instance of Format. File must be valid to this Format, otherwise
     * an exception is thrown.
     *
     * @param fileName name of the file to load from.
     * @throws FileFormatException if the loaded file does not have this format
     */
    public void loadDataFromFile(String fileName) throws FileFormatException{

    }

    /**
     * Loads the format from the specified file with the specified type of load.
     *
     * @param fileName name of the file to load from.
     * @param type type of way of load.
     * @return Loaded format from the file with the specified type of load.
     */
    public static Format loadFormatFromFile(String fileName, LoadType type) {
        return null;
    }

    /**
     * Loads the format from the specified string with the specified type of
     * load.
     *
     * @param format string from which the Format will be loaded.
     * @param type type of way of load.
     * @return Loaded format from the specified string with the specified type
     * of load.
     */
    public static Format loadFormatFromString(String format, LoadType type) {
        return null;
    }

    /**
     * Loads the format from the specified stream with the specified type of
     * load.
     *
     * @param stream stream from which the Format will be loaded.
     * @param type type of way of load.
     * @return Loaded format from the specified stream with the specified type
     * of load.
     */
    public static Format loadFormatFromStream(Stream stream, LoadType type) {
        return null;
    }

    /**
     * Represents a type of format to load. STRICT loaded file must meet 
     * the current format, no defaults. RELAXED tries to avoid errors as much
     * as possible.
     */
    public enum LoadType {

        STRICT,
        RELAXED
    }

    /**
     * Represents a type of format to save. FULL saves the whole Format with all
     * comments etc. ORIGIN save is based on original Format from which it was
     * loaded.
     *
     */
    public enum SaveType {

        FULL,
        ORIGIN
    }
}
