package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;

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
     * @throws InvalidValueFormat if the specified string has no correct format.
     */
    public ValueString(String value) throws InvalidValueFormat {
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
}
