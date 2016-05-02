package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.TooManyValuesException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueSigned;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueSignedRestriction;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author xxx
 */
public class PropertyTest {

    @Test
    public void testToString1() throws TooManyValuesException, ViolatedRestrictionException, InvalidValueFormat {
        String key = "Key";
        String equalSign = "=";
        String value = "128";

        Property p = new Property(key, true, new ValueSignedRestriction(0, Integer.MAX_VALUE));
        p.addValue(new ValueSigned(value, false));

        String expected = key + equalSign + value;
        Assert.assertEquals(expected, p.toString());
    }

    @Test
    public void testToString2() throws InvalidValueFormat, TooManyValuesException, ViolatedRestrictionException {
        String key = "Key";
        String equalSign = "=";
        String value1 = "128";
        String value2 = "231";
        String value3 = "451";

        Property p = new Property(key, false, new ValueSignedRestriction(0, Integer.MAX_VALUE));
        p.addValue(new ValueSigned(value1, true));
        p.addValue(new ValueSigned(value2, true));
        p.addValue(new ValueSigned(value3, true));
        p.setDelimiter(Property.ValueDelimiter.COMMA);

        String expected = key
                + equalSign
                + value1
                + Property.ValueDelimiter.COMMA
                + value2
                + Property.ValueDelimiter.COMMA
                + value3;
        Assert.assertEquals(expected, p.toString());
    }
}
