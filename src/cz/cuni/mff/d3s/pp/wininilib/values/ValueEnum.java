package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;

/**
 * Represents a <code>Enum</code> value of the property.
 *
 * @author xxx
 */
public class ValueEnum implements Value {

    private final Object value;

    /**
     * Initializes a new instance of <code>ValueEnum</code>.
     *
     * @param value value of some enum.
     */
    public ValueEnum(Object value) {
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
        return value.toString();
    }
}
