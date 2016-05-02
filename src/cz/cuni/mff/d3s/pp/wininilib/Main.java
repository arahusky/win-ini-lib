package cz.cuni.mff.d3s.pp.wininilib;

/**
 *
 * @author xxx
 */
public class Main {

    public static void main(String[] args) {

        /**
         * DESCRIPTION OF WINDOWS INI LIBRARY. WinIniLib provides a
         * representation for Formats. They contains some sections, which
         * consists of properties. Property is key-value pair, basically.
         * Property can be multi-valued. The library reflects the actual/real
         * Windows .ini files. For more informations about .ini files visit
         * https://en.wikipedia.org/wiki/INI_file.
         * 
         * Format, Section and Property provides methods for basic operations
         * with them. Value of the property is always instance of some class
         * that derives from Value interface. On each value can be applied
         * value restriction.
         * 
         * Supported value types are:
         * - Boolean
         * - Enum
         * - Float
         * - Signed
         * - String
         * - Unsigned
         * 
         * This library uses WinIniLibException for indication of errors, for
         * example, file format error, general IO error or violated restriction
         * error.
         * 
         * 
         * USAGE OF WINDOWS INI LIBRARY (example):
         * Create new format:
         * Format format = new Format();
         * 
         * or load it from the file (stream or string):
         * format = Format.loadFormatFromFile(...);
         * 
         * then modify it:
         * Section s = format.getSection(index);
         * Property p = new Property(...);
         * p.addValue(new ValueFloat(...));
         * 
         * s.addProperty(p);
         * s.setComment(...);
         * 
         * Save back to the file:
         * format.saveToFile(...);
         * 
         * For debugging purposes you can use toString or toStream methods to
         * get representation (output) of the current format, without saving it
         * to a file.
         * 
         */
    }
}
