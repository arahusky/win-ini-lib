package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.exceptions.DuplicateNameException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.TooManyValuesException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueBoolean;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueEnum;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueFloat;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueReference;
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
import java.util.List;
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
        ValueBoolean vb = new ValueBoolean(value);
        Assert.assertEquals(value, vb.toString());
        Assert.assertTrue((boolean) vb.get());
        ValueBooleanRestriction vbr = new ValueBooleanRestriction();
        vbr.checkRestriction(vb);
    }

    @Test(expected = InvalidValueFormatException.class)
    public void testBoolean2() throws TooManyValuesException, ViolatedRestrictionException, InvalidValueFormatException {
        String value = "invalid";
        ValueBoolean vb = new ValueBoolean(value);
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
        ValueEnum ve = new ValueEnum(value);
        ValueEnumRestriction ver = new ValueEnumRestriction(TestEnum.class);
        ver.checkRestriction(ve);
    }

    @Test
    public void testEnum2() throws ViolatedRestrictionException {
        TestEnum value = TestEnum.TWO;
        ValueEnum ve = new ValueEnum(value);
        ValueEnumRestriction ver = new ValueEnumRestriction(TestEnum.class);
        ver.checkRestriction(ve);
    }

    @Test(expected = ViolatedRestrictionException.class)
    public void testEnum3() throws ViolatedRestrictionException {
        TestEnum value = TestEnum.THREE;
        ValueEnum ve = new ValueEnum(value);
        ValueEnumRestriction ver = new ValueEnumRestriction(TestEnumAnother.class);
        ver.checkRestriction(ve);
    }

    //
    // ValueFloat and ValueFloatRestriction test methods
    //
    @Test
    public void testFloat1() throws ViolatedRestrictionException {
        double value = 147.571;
        ValueFloat vf = new ValueFloat(value);
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
        ValueFloat vf = new ValueFloat(value);
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
        ValueSigned vs = new ValueSigned(value);
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
        ValueSigned vs = new ValueSigned(value);
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
        ValueSigned vs = new ValueSigned(value);
        ValueSignedRestriction vsr = new ValueSignedRestriction(-10, 10);

        Assert.assertEquals(innerValue, vs.get());

        vsr.checkRestriction(vs);
    }

    @Test(expected = ViolatedRestrictionException.class)
    public void testSigned4() throws InvalidValueFormatException, ViolatedRestrictionException {
        long innerValue = 4528;
        String value = "010660";
        ValueSigned vs = new ValueSigned(value);
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
        ValueUnsigned vu = new ValueUnsigned(value);
    }

    @Test(expected = ViolatedRestrictionException.class)
    public void testUnsigned2() throws InvalidValueFormatException, ViolatedRestrictionException {
        String value = "0x15AB4F";
        ValueUnsigned vu = new ValueUnsigned(value);
        ValueUnsignedRestriction vur = new ValueUnsignedRestriction(BigInteger.ZERO, BigInteger.TEN);
        vur.checkRestriction(vu);
    }

    //
    // ValueString and ValueStringRestriction test methods
    //
    @Test
    public void testString1() throws InvalidValueFormatException, ViolatedRestrictionException {
        String value = "test";
        ValueString vstr = new ValueString(value);

        Assert.assertEquals(value, vstr.get());

        ValueStringRestriction restr = new ValueStringRestriction();
        restr.checkRestriction(vstr);
    }

    @Test(expected = ViolatedRestrictionException.class)
    public void testString2() throws InvalidValueFormatException, ViolatedRestrictionException {
        String value = "invalid;string";
        ValueString vstr = new ValueString(value);

        Assert.assertEquals(value, vstr.get());

        ValueStringRestriction restr = new ValueStringRestriction();
        restr.checkRestriction(vstr);
    }

    @Test
    public void testString3() throws InvalidValueFormatException, ViolatedRestrictionException {
        String value = "valid\\;string\\,";
        ValueString vstr = new ValueString(value);
        ValueStringRestriction restr = new ValueStringRestriction();
        restr.checkRestriction(vstr);
    }

    @Test(expected = ViolatedRestrictionException.class)
    public void testString4() throws InvalidValueFormatException, ViolatedRestrictionException {
        String value = "valid\\;string\\," + Constants.NEW_LINE;
        ValueString vstr = new ValueString(value);
        ValueStringRestriction restr = new ValueStringRestriction();
        restr.checkRestriction(vstr);
    }

    //
    // ValueReference test methods
    //
    @Test
    public void testValueReference1() throws InvalidValueFormatException, DuplicateNameException, TooManyValuesException, ViolatedRestrictionException {
        IniFile iniFile = new IniFile();
        String sectionIdentifier = "section 1";
        Section s = new Section(sectionIdentifier, true);

        String propertyName = "Energy";
        boolean isSingleValue = true;
        Property p1 = new Property(propertyName, isSingleValue, new ValueFloatRestriction());
        p1.addValue(new ValueFloat(8.2));
        s.addProperty(p1);

        iniFile.addSection(s);

        String name = "InheritedEnergy";
        Property p2 = new Property(name, isSingleValue, new ValueFloatRestriction());
        p2.addValue(new ValueReference(iniFile, sectionIdentifier, propertyName, p2.getRestriction()));

        Property p3 = new Property(name, isSingleValue, new ValueFloatRestriction());
        p3.addValue(iniFile, sectionIdentifier, propertyName);

        String expected = name + "=" + "${" + sectionIdentifier + "#" + propertyName + "}";

        Assert.assertEquals(expected, p2.toString());
        Assert.assertEquals(expected, p3.toString());

        List<Value> values = (List<Value>) p2.getValue(0).get();
        Assert.assertEquals(8.2, values.get(0).get());

        p1.setValue(0, new ValueFloat(10));

        Assert.assertEquals(expected, p2.toString());
        Assert.assertEquals(expected, p3.toString());

        values = (List<Value>) p2.getValue(0).get();
        Assert.assertEquals(10.0, values.get(0).get());
    }

    @Test(expected = ViolatedRestrictionException.class)
    public void testValueReference2() throws InvalidValueFormatException, DuplicateNameException, TooManyValuesException, ViolatedRestrictionException {
        IniFile iniFile = new IniFile();
        String sectionIdentifier = "section 1";
        Section s = new Section(sectionIdentifier, true);

        String propertyName = "Energy";
        boolean isSingleValue = true;
        Property p1 = new Property(propertyName, isSingleValue, new ValueFloatRestriction());
        p1.addValue(new ValueFloat(8.2));
        s.addProperty(p1);

        iniFile.addSection(s);

        String name = "InheritedEnergy";
        Property p2 = new Property(name, isSingleValue, new ValueFloatRestriction(-10, 0));
        p2.addValue(iniFile, sectionIdentifier, propertyName);
    }

    @Test
    public void testValueReference3() throws InvalidValueFormatException, DuplicateNameException, TooManyValuesException, ViolatedRestrictionException {
        IniFile iniFile = new IniFile();
        String sectionIdentifier = "section 1";
        Section s = new Section(sectionIdentifier, true);

        String propertyName = "Energy";
        Property p1 = new Property(propertyName, true, new ValueFloatRestriction());
        p1.addValue(new ValueFloat(8.2));
        s.addProperty(p1);

        iniFile.addSection(s);

        String name = "InheritedEnergy";
        Property p2 = new Property(name, false, new ValueFloatRestriction(-100, 100));
        p2.addValue(iniFile, sectionIdentifier, propertyName);
        p2.addValue(new ValueFloat(42));

        String expected = name + "=" + "${" + sectionIdentifier + "#" + propertyName + "}," + 42.0;
        Assert.assertEquals(expected, p2.toString());
    }

}
