package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.TooManyValuesException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueBoolean;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueEnum;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueFloat;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueSigned;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueString;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueUnsigned;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueBooleanRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueEnumRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueFloatRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueSignedRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueStringRestriction;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueUnsignedRestriction;
import java.math.BigInteger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author xxx
 */
public class ValueAndRestrictionTest {

    //
    // ValueBoolean and ValueBooleanRestriction test methods
    //
    @Test
    public void testBoolean1() throws TooManyValuesException, ViolatedRestrictionException, InvalidValueFormatException {
        String value = "1";
        ValueBoolean vb = new ValueBoolean(value, true);
        Assert.assertEquals(value, vb.get().toString());
        ValueBooleanRestriction vbr = new ValueBooleanRestriction();
        vbr.checkRestriction(vb);
    }

    @Test(expected = InvalidValueFormatException.class)
    public void testBoolean2() throws TooManyValuesException, ViolatedRestrictionException, InvalidValueFormatException {
        String value = "invalid";
        ValueBoolean vb = new ValueBoolean(value, true);
        Assert.assertEquals(value, vb.get().toString());
    }

    //
    // ValueEnum and ValueEnumRestriction test methods
    //
    enum TestEnum {
        ONE,
        TWO,
        THREE
    }

    enum TestEnumAnother {
        ONE,
        FOUR,
        SIX
    }

    @Test(expected = ViolatedRestrictionException.class)
    public void testEnum1() throws ViolatedRestrictionException {
        Object value = new Object();
        ValueEnum ve = new ValueEnum(value, true);
        ValueEnumRestriction ver = new ValueEnumRestriction(TestEnum.class);
        ver.checkRestriction(ve);
    }

    @Test
    public void testEnum2() throws ViolatedRestrictionException {
        TestEnum value = TestEnum.TWO;
        ValueEnum ve = new ValueEnum(value, false);
        ValueEnumRestriction ver = new ValueEnumRestriction(TestEnum.class);
        ver.checkRestriction(ve);
    }

    @Test(expected = ViolatedRestrictionException.class)
    public void testEnum3() throws ViolatedRestrictionException {
        TestEnum value = TestEnum.THREE;
        ValueEnum ve = new ValueEnum(value, true);
        ValueEnumRestriction ver = new ValueEnumRestriction(TestEnumAnother.class);
        ver.checkRestriction(ve);
    }

    //
    // ValueFloat and ValueFloatRestriction test methods
    //
    @Test
    public void testFloat1() throws ViolatedRestrictionException {
        double value = 147.571;
        ValueFloat vf = new ValueFloat(value, true);
        ValueFloatRestriction vfr = new ValueFloatRestriction();
        vfr.checkRestriction(vf);
    }

    @Test(expected = InvalidValueFormatException.class)
    public void testFloat2() throws InvalidValueFormatException {
        ValueFloatRestriction vfr = new ValueFloatRestriction(0, -1);
    }

    @Test(expected = ViolatedRestrictionException.class)
    public void testFloat3() throws ViolatedRestrictionException, InvalidValueFormatException {
        double value = 1000.1;
        ValueFloat vf = new ValueFloat(value, true);
        ValueFloatRestriction vfr = new ValueFloatRestriction(value - 10, value - 5);
        vfr.checkRestriction(vf);
    }

    //
    // ValueSigned and ValueSignedRestriction test methods
    //
    @Test
    public void testSigned1() throws InvalidValueFormatException, ViolatedRestrictionException {
        long innerValue = 1234567890;
        String value = "1234567890";
        ValueSigned vs = new ValueSigned(value, true);
        ValueSignedRestriction vsr = new ValueSignedRestriction();
        vsr.checkRestriction(vs);

        vsr = new ValueSignedRestriction(0, 1234567899);
        vsr.checkRestriction(vs);

        Assert.assertEquals(innerValue, vs.get());
    }

    @Test
    public void testSigned2() throws InvalidValueFormatException, ViolatedRestrictionException {
        long innerValue = 362;
        String value = "0x16A";
        ValueSigned vs = new ValueSigned(value, true);
        ValueSignedRestriction vsr = new ValueSignedRestriction();
        vsr.checkRestriction(vs);

        vsr = new ValueSignedRestriction(0, 2000);
        vsr.checkRestriction(vs);

        Assert.assertEquals(innerValue, vs.get());
    }

    @Test(expected = ViolatedRestrictionException.class)
    public void testSigned3() throws InvalidValueFormatException, ViolatedRestrictionException {
        long innerValue = 102;
        String value = "0b1100110";
        ValueSigned vs = new ValueSigned(value, true);
        ValueSignedRestriction vsr = new ValueSignedRestriction(-10, 10);

        Assert.assertEquals(innerValue, vs.get());

        vsr.checkRestriction(vs);
    }

    @Test(expected = ViolatedRestrictionException.class)
    public void testSigned4() throws InvalidValueFormatException, ViolatedRestrictionException {
        long innerValue = 4528;
        String value = "010660";
        ValueSigned vs = new ValueSigned(value, true);
        ValueSignedRestriction vsr = new ValueSignedRestriction(innerValue + 1, innerValue + 2);

        Assert.assertEquals(innerValue, vs.get());

        vsr.checkRestriction(vs);
    }

    //
    // ValueUnsigned and ValueUnsignedRestriction test methods
    //
    @Test(expected = InvalidValueFormatException.class)
    public void testUnsigned1() throws InvalidValueFormatException, ViolatedRestrictionException {
        String value = "-9876543210";
        ValueUnsigned vu = new ValueUnsigned(value, true);
    }

    @Test(expected = ViolatedRestrictionException.class)
    public void testUnsigned2() throws InvalidValueFormatException, ViolatedRestrictionException {
        String value = "0x15AB4F";
        ValueUnsigned vu = new ValueUnsigned(value, true);
        ValueUnsignedRestriction vur = new ValueUnsignedRestriction(BigInteger.ZERO, BigInteger.TEN);
        vur.checkRestriction(vu);
    }

    //
    // ValueString and ValueStringRestriction test methods
    //
    @Test
    public void testString1() throws InvalidValueFormatException, ViolatedRestrictionException {
        String value = "test";
        ValueString vstr = new ValueString(value, true);

        Assert.assertEquals(value, vstr.get());

        ValueStringRestriction restr = new ValueStringRestriction();
        restr.checkRestriction(vstr);
    }

    @Test(expected = ViolatedRestrictionException.class)
    public void testString2() throws InvalidValueFormatException, ViolatedRestrictionException {
        String value = "invalid;string";
        ValueString vstr = new ValueString(value, true);

        Assert.assertEquals(value, vstr.get());

        ValueStringRestriction restr = new ValueStringRestriction();
        restr.checkRestriction(vstr);
    }

    @Test
    public void testString3() throws InvalidValueFormatException, ViolatedRestrictionException {
        String value = "valid\\;string\\,";
        ValueString vstr = new ValueString(value, true);
        ValueStringRestriction restr = new ValueStringRestriction();
        restr.checkRestriction(vstr);
    }

    @Test(expected = ViolatedRestrictionException.class)
    public void testString4() throws InvalidValueFormatException, ViolatedRestrictionException {
        String value = "valid\\;string\\," + Constants.NEW_LINE;
        ValueString vstr = new ValueString(value, true);
        ValueStringRestriction restr = new ValueStringRestriction();
        restr.checkRestriction(vstr);
    }
}
