package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;

/**
 * Represents a <code>Signed</code> value of the property.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueSigned implements Value {

    private long value;

    /**
     * Initializes a new instance of <code>ValueSigned</code>.
     *
     * @param value value to be parsed.
     * @throws cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat
     */
    public ValueSigned(Object value) throws InvalidValueFormat, ViolatedRestrictionException {
        try {
            this.value = (long) (value);
        } catch (ClassCastException ex) {
            throw new InvalidValueFormat("TODO");
        }

    }

    @Override
    public Object get() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
