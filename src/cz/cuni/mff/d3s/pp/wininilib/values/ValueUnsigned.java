package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Constants;
import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;
import java.math.BigInteger;
import java.util.Objects;

/**
 * Represents a <code>Unsigned</code> value of the property.
 *
 * @author xxx
 */
public class ValueUnsigned implements Value {

    private final BigInteger value;
    private final boolean writeToIniFile;

    /**
     * Initializes a new instance of <code>ValueUnsigned</code>. Represents an
     * non-negative integer.
     *
     * @param value value to be parsed.
     * @param writeToIniFile a flag value that determines whether this value has
     * already been written or will be written in INI file.
     * @throws InvalidValueFormatException if the specified value can not be parsed.
     */
    public ValueUnsigned(String value, boolean writeToIniFile) throws InvalidValueFormatException {
        try {
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
            value = value.substring(prefixLength).trim();
            this.value = new BigInteger(value, radix);
            if (this.value.signum() == -1) {
                throw new InvalidValueFormatException("Specified value must be non-negative.");
            }
        } catch (NumberFormatException ex) {
            throw new InvalidValueFormatException("Specified value is not correct signed value");
        }
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
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.value);
        hash = 37 * hash + (this.writeToIniFile ? 1 : 0);
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
        final ValueUnsigned other = (ValueUnsigned) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (this.writeToIniFile != other.writeToIniFile) {
            return false;
        }
        return true;
    }    
}
