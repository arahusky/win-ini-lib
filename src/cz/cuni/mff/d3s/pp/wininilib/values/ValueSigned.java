package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;

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
     * @throws InvalidValueFormat if the specified value can not be parsed.
     */
    public ValueSigned(Object value) throws InvalidValueFormat {
        try {
            this.value = (long) (value);
        } catch (ClassCastException ex) {
            throw new InvalidValueFormat("Specified value is not correct signed value");
        }

    }

    @Override
    public Object get() {
        return value;
    }
}
