package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;

/**
 * Represents a <code>Boolean</code> value of the property.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueBoolean implements Value {

    /**
     * Initializes a new instance of <code>BooleanValue</code>.
     *
     * @param string value to be parsed.
     */
    public ValueBoolean(String string) {
        if (!Boolean.parseBoolean(string)) {

        }
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
