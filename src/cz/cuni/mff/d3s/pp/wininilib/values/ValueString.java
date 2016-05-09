package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;
import java.util.Objects;

/**
 * Represents a <code>String</code> value of the property.
 *
 * @author xxx
 */
public class ValueString implements Value {

    private final String value;

    /**
     * Initializes a new instance of <code>ValueString</code>.
     *
     * @param value string to be parsed.
     * @throws InvalidValueFormatException if the specified string has no correct format.
     */
    public ValueString(String value) throws InvalidValueFormatException {
        this.value = value;
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
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 13 * hash + Objects.hashCode(this.value);
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
        final ValueString other = (ValueString) obj;
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }
}
