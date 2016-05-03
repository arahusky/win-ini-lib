package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;

/**
 * Represents a <code>Float</code> value of the property. Internally represented
 * by more precise double.
 *
 * @author xxx
 */
public class ValueFloat implements Value {

    private final double value;
    private final boolean writeToIniFile;

    /**
     * Initializes a new instance of <code>ValueFloat</code>.
     *
     * @param value value to be parsed.
     * @param writeToIniFile a flag value that determines whether this value has
     * already been written or will be written in INI file.
     * @throws InvalidValueFormat if the specified value can not be parsed.
     */
    public ValueFloat(double value, boolean writeToIniFile) throws InvalidValueFormat {
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
        return Double.toString(value);
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
