package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;

/**
 * Represents a <code>Signed</code> value of the property.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueSigned implements Value {

    private final long value;

    /**
     * Initializes a new instance of <code>ValueSigned</code>.
     *
     * @param value value to be parsed.
     */
    public ValueSigned(long value) {
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
        return Long.toString(value);
    }
}
