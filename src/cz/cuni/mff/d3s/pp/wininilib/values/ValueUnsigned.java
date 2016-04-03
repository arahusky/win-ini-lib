package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;

/**
 * Represents a <code>Unsigned</code> value of the property.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueUnsigned implements Value {

    /**
     * Initializes a new instance of <code>ValueUnsigned</code>.
     *
     * @param value value to be parsed.
     */
    public ValueUnsigned(Object value) throws ViolatedRestrictionException {

    }

    @Override
    public Object get() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
