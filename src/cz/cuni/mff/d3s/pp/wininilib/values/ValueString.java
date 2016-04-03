package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;

/**
 * Represents a <code>String</code> value of the property.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueString implements Value {

    /**
     * Initializes a new instance of <code>ValueString</code>.
     *
     * @param string string to be parsed.
     */
    public ValueString(String string) throws ViolatedRestrictionException {

    }

    @Override
    public Object get() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
