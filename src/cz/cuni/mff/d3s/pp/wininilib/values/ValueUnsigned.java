package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;
import java.math.BigDecimal;

/**
 * Represents a <code>Unsigned</code> value of the property.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueUnsigned implements Value {

    private BigDecimal value;
    
    /**
     * Initializes a new instance of <code>ValueUnsigned</code>.
     *
     * @param value value to be parsed.
     * @throws InvalidValueFormat if the specified value can not be parsed.
     */
    public ValueUnsigned(Object value) throws InvalidValueFormat {
         try {
            this.value = new BigDecimal(value.toString());
        } catch (NumberFormatException ex) {
            throw new InvalidValueFormat("Specified value is not correct signed value");
        }

    }

    @Override
    public Object get() {
        return value;
    }
}
