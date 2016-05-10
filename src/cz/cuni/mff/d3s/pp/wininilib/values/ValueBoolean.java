package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueBooleanRestriction;

/**
 * Represents a <code>Boolean</code> value of the property.
 *
 * @author xxx
 */
public class ValueBoolean implements Value {

    private final String value;

    /**
     * Initializes a new instance of <code>BooleanValue</code>.
     *
     * @param value value to be parsed.
     * @throws InvalidValueFormatException if the specified value can not be parsed.
     */
    public ValueBoolean(String value) throws InvalidValueFormatException {
        if (!ValueBooleanRestriction.getAllTrueValues().contains(value)
                && !ValueBooleanRestriction.getAllFalseValues().contains(value)) {
            throw new InvalidValueFormatException("Invalid format of the specified value (" + value + ")");
        }
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
