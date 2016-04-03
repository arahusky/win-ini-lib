package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;

/**
 * Represents a <code>Boolean</code> value of the property.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueBoolean implements Value {

    private String value;

    /**
     * Initializes a new instance of <code>BooleanValue</code>.
     *
     * @param value value to be parsed.
     * @throws InvalidValueFormat if the specified value can not be parsed.
     */
    public ValueBoolean(String value) throws InvalidValueFormat {

    }

    /**
     * Returns the real value within the object.
     *
     * @return the real value within the object.
     */
    @Override
    public Object get() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
