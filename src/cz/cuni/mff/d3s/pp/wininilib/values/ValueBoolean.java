package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueBooleanRestriction;

/**
 * Represents a <code>Boolean</code> value of the property.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueBoolean implements Value {

    private final String value;

    /**
     * Initializes a new instance of <code>BooleanValue</code>.
     *
     * @param value value to be parsed.
     * @throws InvalidValueFormat if the specified value can not be parsed.
     */
    public ValueBoolean(String value) throws InvalidValueFormat {
        if (!ValueBooleanRestriction.getAllTrueValues().contains(value)
                && !ValueBooleanRestriction.getAllFalseValues().contains(value)) {
            throw new InvalidValueFormat("Invalid format of the specified value (" + value + ")");
        }
        this.value = value;
    }

    /**
     * Returns the real value within the object.
     *
     * @return the real value within the object.
     */
    @Override
    public Object get() {
        return value;
    }
}
