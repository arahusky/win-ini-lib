package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Constants;
import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;

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
     * @throws InvalidValueFormatException if the specified value is not correct.
     *
     */
    public ValueSigned(String value, boolean writeToIniFile) throws InvalidValueFormatException {
        try {
            value = value.trim();
            int prefixLength = 0;
            int radix = getRadix(value);
            switch (radix) {
                case 2:
                    prefixLength = Constants.BINARY_PREFIX.length();
                    break;
                case 8:
                    prefixLength = Constants.OCT_PREFIX.length();
                    break;
                case 16:
                    prefixLength = Constants.HEX_PREFIX.length();
                    break;
            }
            value = value.substring(prefixLength);
            this.value = Long.parseLong(value, radix);
            this.writeToIniFile = writeToIniFile;
        } catch (NumberFormatException ex) {
            throw new InvalidValueFormatException("Specified value is not correct number.");
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (int) (this.value ^ (this.value >>> 32));
        hash = 47 * hash + (this.writeToIniFile ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ValueSigned other = (ValueSigned) obj;
        if (this.value != other.value) {
            return false;
        }
        if (this.writeToIniFile != other.writeToIniFile) {
            return false;
        }
        return true;
    }
}
