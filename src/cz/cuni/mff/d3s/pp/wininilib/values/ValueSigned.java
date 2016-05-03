package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Constants;
import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;

/**
 * Represents a <code>Signed</code> value of the property.
 *
 * @author xxx
 */
public class ValueSigned implements Value {

    private final long value;
    private final boolean writeToIniFile;

    /**
     * Initializes a new instance of <code>ValueSigned</code>.
     *
     * @param value value to be parsed.
     * @param writeToIniFile a flag that determines whether the current value
     * has been written or will be written to INI file.
     * @throws InvalidValueFormat if the specified value is not correct.
     *
     */
    public ValueSigned(String value, boolean writeToIniFile) throws InvalidValueFormat {
        try {
            this.value = Long.parseLong(value, getRadix(value));
            this.writeToIniFile = writeToIniFile;
        } catch (NumberFormatException ex) {
            throw new InvalidValueFormat("Specified value is not correct number.");
        }
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
        return Long.toString(value);
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

    /**
     * Determines and returns numeric system (radix). Possible number systems
     * are "0x" for hex, "0" for oct, "0b" for binary.
     *
     * @param value value to be parsed.
     * @return a numberic system (radix) of the specified number.
     */
    private int getRadix(String value) {
        final int hex = 16;
        final int bin = 2;
        final int oct = 8;
        final int dec = 10;

        if (value.substring(0, 2).equals(Constants.HEX_PREFIX)) {
            return hex;
        }
        if (value.substring(0, 2).equals(Constants.BINARY_PREFIX)) {
            return bin;
        }
        if (value.substring(0, 1).equals(Constants.OCT_PREFIX)) {
            return oct;
        }
        return dec;
    }

}
