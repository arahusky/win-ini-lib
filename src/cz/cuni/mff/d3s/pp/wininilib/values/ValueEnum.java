package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;
import java.util.List;

/**
 * Represents a <code>Enum</code> value of the property.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueEnum implements Value {

    Object value;

    /**
     * Initializes a new instance of <code>ValueEnum</code>.
     *
     * @param value value of some enum.
     * @throws InvalidValueFormat if the specified value can not be parsed.
     */
    public ValueEnum(Object value) throws InvalidValueFormat {
        this.value = value;
    }

    @Override
    public Object get() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
