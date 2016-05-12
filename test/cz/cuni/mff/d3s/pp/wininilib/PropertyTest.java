package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.TooManyValuesException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueFloat;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueSigned;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueString;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueFloatRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueSignedRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueStringRestriction;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author xxx
 */
public class PropertyTest {

    @Test
    public void testToString1() throws TooManyValuesException, ViolatedRestrictionException, InvalidValueFormatException {
        String key = "Key";
        String equalSign = "=";
        String value = "128";
        Property p = new Property(key, true, new ValueSignedRestriction());
        p.addValue(new ValueSigned(value));
        String expected = key + equalSign + value;

        Assert.assertEquals(expected, p.toString());
    }

    @Test
    public void testToString2() throws InvalidValueFormatException, TooManyValuesException, ViolatedRestrictionException {
        String key = "Key";
        String equalSign = "=";
        String value1 = "128";
        String value2 = "231";
        String value3 = "451";

        Property p = new Property(key, false, new ValueSignedRestriction());
        p.addValue(new ValueSigned(value1));
        p.addValue(new ValueSigned(value2));
        p.addValue(new ValueSigned(value3));
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

    @Test
    public void testToString3() throws InvalidValueFormatException, TooManyValuesException, ViolatedRestrictionException {
        String key = "Key";
        String equalSign = "=";
        ValueSigned implicit = new ValueSigned("42");
        Property p = new Property(key, true, implicit, new ValueSignedRestriction());
        String expected = key + equalSign + implicit.toString();

        Assert.assertEquals(expected, p.toString());
        Assert.assertEquals(expected, p.toString(IniFile.SavingMode.FULL));
        Assert.assertEquals(key + equalSign, p.toString(IniFile.SavingMode.ORIGINAL));
    }

    @Test
    public void testToString4() throws InvalidValueFormatException, TooManyValuesException, ViolatedRestrictionException {
        String key = "Key";
        String equalSign = "=";
        String comment = "this is comment";
        String value = "42";
        Property p = new Property(key, true, new ValueSignedRestriction());
        p.addValue(new ValueSigned(value));
        p.setComment(comment);
        String expectedWithComment = key + equalSign + value + " " + Constants.COMMENT_DELIMITER + comment;
        String expectedNoComment = key + equalSign + value;

        Assert.assertEquals(expectedWithComment, p.toString());
        Assert.assertEquals(expectedWithComment, p.toString(IniFile.SavingMode.FULL));
        Assert.assertEquals(expectedNoComment, p.toString(IniFile.SavingMode.ORIGINAL));
    }

    @Test(expected = TooManyValuesException.class)
    public void testSingleValue() throws TooManyValuesException, ViolatedRestrictionException, InvalidValueFormatException {
        boolean isSingleValue = true;
        Property p = new Property("key", isSingleValue, new ValueStringRestriction());
        ValueString value = new ValueString("Hi.");
        p.addValue(value);
        p.addValue(value);
    }

    @Test
    public void testValueType() throws InvalidValueFormatException {
        Property p = new Property("key", true, new ValueFloatRestriction());
        Assert.assertEquals(ValueFloat.class, p.getValueType());
    }
    
    @Test(expected = InvalidValueFormatException.class)
    public void testInvalidID1() throws InvalidValueFormatException {
        String malformedID = "9section";
        Property p = new Property(malformedID, true, new ValueStringRestriction());
    }
    
    @Test(expected = InvalidValueFormatException.class)
    public void testInvalidID2() throws InvalidValueFormatException {
        String malformedID = "section{asd}";
        Property p = new Property(malformedID, true, new ValueStringRestriction());
    }
}
