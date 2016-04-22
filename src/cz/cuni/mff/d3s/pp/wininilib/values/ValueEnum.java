package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;

/**
 * Represents a <code>Enum</code> value of the property.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueEnum implements Value {

    Object value;

    /**
     * Initializes a new instance of <code>ValueEnum</code>.
     *
     * @param value value of some enum.
     * @throws InvalidValueFormat if the specified value can not be parsed.
     */
    public ValueEnum(Object value) throws InvalidValueFormat {
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
