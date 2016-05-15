package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.TooManyValuesException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueSigned;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueSignedRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueStringRestriction;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author xxx
 */
public class SectionTest {

    @Test
    public void testToString1() throws TooManyValuesException, ViolatedRestrictionException, InvalidValueFormatException {

        String key = "Key";
        String equalSign = "=";
        String value1 = "128";
        String value2 = "231";
        String comment = "this is comment";

        Property p1 = new Property(key, true, new ValueSignedRestriction());
        p1.addValue(new ValueSigned(value1));
        p1.setComment(comment);
        String expected = key + equalSign + value1 + " " + Constants.COMMENT_DELIMITER + comment;
        Assert.assertEquals(expected, p1.toString());

        Property p2 = new Property(key, false, new ValueSignedRestriction());
        p2.addValue(new ValueSigned(value1));
        p2.addValue(new ValueSigned(value2));
        p2.setDelimiter(Property.ValueDelimiter.COMMA);

        expected = key
                + equalSign
                + value1
                + Property.ValueDelimiter.COMMA
                + value2;
        Assert.assertEquals(expected, p2.toString());

        String identifier = "Identif";
        Section s = new Section(identifier, true);
        s.addProperty(p1);
        expected = "[" + identifier + "]" + Constants.NEW_LINE + p1.toString();
        Assert.assertEquals(expected, s.toString());

        s.addProperty(p2);

        expected += Constants.NEW_LINE + p2.toString();
        Assert.assertEquals(expected, s.toString());
    }

    @Test
    public void testToString2() throws InvalidValueFormatException {
        String identifier = "hi";
        String comment = "this is comment";

        Section s = new Section(identifier, true);
        s.setComment(comment);
        String expected = "[" + identifier + "]" + Constants.NEW_LINE +  Constants.COMMENT_DELIMITER + comment + Constants.NEW_LINE;
        Assert.assertEquals(expected, s.toString());
    }
    
    @Test(expected = InvalidValueFormatException.class)
    public void testInvalidID1() throws InvalidValueFormatException {
        String malformedID = "9section";
        Section s = new Section(malformedID, true);
    }
    
    @Test(expected = InvalidValueFormatException.class)
    public void testInvalidID2() throws InvalidValueFormatException {
        String malformedID = "section{asd}";
        Section s = new Section(malformedID, true);
    }
}
