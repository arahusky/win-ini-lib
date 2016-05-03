package cz.cuni.mff.d3s.pp.wininilib;

/**
 * Each value of any property is derived from this interface. Provides a basic
 * behaviour for values.
 *
 * @author xxx
 */
public interface Value {

    /**
     * Returns the real value within the object. This is value for some
     * property.
     *
     * @return the real value within the object.
     */
    Object get();

    /**
     * Returns a flag value that determines whether this value has already been
     * written or will be written in INI file. Used only in ORIGIN saving mode.
     *
     * @return a flag value that determines whether this value has already been
     * written or will be written in INI file.
     */
    boolean writeToIniFile();
}
