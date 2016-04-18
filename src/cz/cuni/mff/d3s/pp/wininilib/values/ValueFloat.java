package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;

/**
 * Represents a <code>Float</code> value of the property.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueFloat implements Value {

    private double value;
    
    /**
     * Initializes a new instance of <code>ValueFloat</code>.
     *
     * @param value value to be parsed.
     * @throws InvalidValueFormat if the specified value can not be parsed.
     */
    public ValueFloat(double value) throws InvalidValueFormat {
        this.value = value;
    }

    @Override
    public Object get() {
        return value;
    }

}
