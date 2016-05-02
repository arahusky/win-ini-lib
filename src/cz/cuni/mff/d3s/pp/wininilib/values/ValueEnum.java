package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;

/**
 * Represents a <code>Enum</code> value of the property.
 *
 * @author xxx
 */
public class ValueEnum implements Value {

    private final Object value;
    private final boolean writeToIniFile;

    /**
     * Initializes a new instance of <code>ValueEnum</code>.
     *
     * @param value value of some enum.
     * @param writeToIniFile a flag value that determines whether this value has
     * already been written or will be written in INI file.
     * @throws InvalidValueFormat if the specified value can not be parsed.
     */
    public ValueEnum(Object value, boolean writeToIniFile) throws InvalidValueFormat {
        this.value = value;
        this.writeToIniFile = writeToIniFile;
    }

    /**
     * Returns the inner value within the object.
     *
     * @return the inner value within the object.
     */
    @Override
    public Object get() {
        return value;
    }

    /**
     * Returns a string representation of the curernt object.
     *
     * @return a string representation of the current object.
     */
    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * Returns a flag value that determines whether this value has already been
     * written or will be written in INI file. Used only in ORIGIN saving mode.
     *
     * @return a flag value that determines whether this value has already been
     * written or will be written in INI file.
     */
    @Override
    public boolean writeToIniFile() {
        return writeToIniFile;
    }

}
