package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.IniFile.SavingMode;
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
import java.util.List;
import java.util.Objects;

/**
 * Represents a property of a Section.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class Property {

    private final String key;
    private boolean required;
    private final boolean isSingleValue;
    private ValueDelimiter delimiter;
    private Value implicitValue;
    private ValueRestriction valueRestriction;
    private String comment;
    private Class<? extends Value> valueType;
    private List<Value> values;

    /**
     * Creates new instance of required property. ValueRestriction must not be
     * null, because value type is derived from it.
     *
     * @param key key of the property.
     * @param isSingleValue determines whether the property will be
     * single-valued; otherwise is multi-valued.
     * @param valueRestriction provides a value restriction for the property.
     * Could be empty, but not null.
     */
    public Property(String key, boolean isSingleValue, ValueRestriction valueRestriction) {
        this.key = key;
        this.isSingleValue = isSingleValue;
        this.valueRestriction = valueRestriction;

        setValueType(valueRestriction);
    }

    /**
     * Creates new instance of arbitrary property. Implicit value must be
     * defined.
     *
     * @param key key of the property.
     * @param isSingleValue determines whether the property is single-valued;
     * otherwise is multi-valued.
     * @param implicitValue defines an implicit value of the property.
     * @param valueRestriction provides a value restriction for the property.
     * Could be empty, but not null.
     */
    public Property(String key, boolean isSingleValue, Value implicitValue, ValueRestriction valueRestriction) {
        this.key = key;
        this.isSingleValue = isSingleValue;
        this.implicitValue = implicitValue;
        this.valueRestriction = valueRestriction;

        setValueType(valueRestriction);
    }

    /**
     * Sets a value type of the current property.
     */
    private void setValueType(ValueRestriction restriction) {
        if (restriction instanceof ValueBooleanRestriction) {
            valueType = ValueBoolean.class;
        } else if (restriction instanceof ValueEnumRestriction) {
            valueType = ValueEnum.class;
        } else if (restriction instanceof ValueFloatRestriction) {
            valueType = ValueFloat.class;
        } else if (restriction instanceof ValueSignedRestriction) {
            valueType = ValueSigned.class;
        } else if (restriction instanceof ValueStringRestriction) {
            valueType = ValueString.class;
        } else if (restriction instanceof ValueUnsignedRestriction) {
            valueType = ValueUnsigned.class;
        }
        throw new UnsupportedOperationException("Unsupported type of restriction.");
    }

    /**
     * Returns type of the current property value (or values).
     *
     * @return type of the current property value (or values).
     */
    public Class<? extends Value> getValueType() {
        return valueType;
    }

    /**
     * Returns key of the current property.
     *
     * @return key of the current property.
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns the element at the specified position in values.
     *
     * @param i index of the element to return.
     * @return the value at specified position.
     */
    public Value getValue(int i) {
        return values.get(i);
    }

    /**
     * Removes first occurrence of given value.
     *
     * @param value value to be removed.
     * @return true if any value was removed; otherwise false.
     */
    public boolean removeValue(Value value) {
        return values.remove(value);
    }

    /**
     * Sets new value to the value at the specified index.
     *
     * @param index index of old value.
     * @param value new value to set.
     * @throws ViolatedRestrictionException if restriction of the current
     * property has been violated.
     */
    public void setValue(int index, Value value) throws ViolatedRestrictionException {
        valueRestriction.checkRestriction(value);
        values.set(index, value);
    }

    /**
     * Returns a number of values within the current property.
     *
     * @return a number of values within the current property.
     */
    public int getNumberOfValues() {
        return values.size();
    }

    /**
     * Adds the specified value to the current property.
     *
     * @param value value to be added.
     * @throws TooManyValuesException if someone tries to add multiple values to
     * single value property.
     * @throws ViolatedRestrictionException if restriction of the current
     * property has been violated.
     */
    public void addValue(Value value) throws TooManyValuesException, ViolatedRestrictionException {
        valueRestriction.checkRestriction(value);
        if ((isSingleValue) && (values.size() > 0)) {
            throw new TooManyValuesException("Too many values in single value property.");
        }
        values.add(value);
    }

    /**
     * Adds the specified value to the current property.
     *
     * @param value value to be added.
     * @throws TooManyValuesException if someone tries to add multiple values to
     * single value property.
     * @throws ViolatedRestrictionException if restriction of the current
     * property has been violated.
     */
    public void addValue(Object value) throws TooManyValuesException, ViolatedRestrictionException {
        if (value instanceof ValueBoolean) {
            addValue((ValueBoolean) value);
        } else if (value instanceof ValueEnum) {
            addValue((ValueEnum) value);
        } else if (value instanceof ValueFloat) {
            addValue((ValueFloat) value);
        } else if (value instanceof ValueSigned) {
            addValue((ValueSigned) value);
        } else if (value instanceof ValueString) {
            addValue((ValueString) value);
        } else if (value instanceof ValueUnsigned) {
            addValue((ValueUnsigned) value);
        }
        throw new UnsupportedOperationException("Unsupported type of value.");
    }

    /**
     * Adds the specified value to the current property. Value is specified by
     * reference to another property.
     *
     * @param iniFile a reference to IniFile where the referenced value can be
     * found.
     * @param identifier identifier of any section.
     * @param nameOfProperty property that will be linked to this one.
     * @throws TooManyValuesException if someone tries to add multiple values to
     * single value property.
     * @throws ViolatedRestrictionException if restriction of the current
     * property has been violated.
     */
    public void addValue(IniFile iniFile, String identifier, String nameOfProperty) throws TooManyValuesException, ViolatedRestrictionException {
        List<Value> referencedValues = iniFile.getSection(identifier).getProperty(nameOfProperty).values;
        for (Value v : referencedValues) {
            // Restriction is checked in differend addValue method.
            addValue(v);
        }

        // TODO: Kdyz to udelame takto, tak zapomeneme informace o te referenci => 
        // do filu potom na vypis se bude davat to bez referenci?? (ono by 
        // to nevadilo... Ini file neni zrovna urceni pro cteni clovekem...)
    }

    /**
     * Returns delimiter of the current property.
     *
     * @return delimiter of the current property.
     */
    public ValueDelimiter getDelimiter() {
        return delimiter;
    }

    /**
     * Sets new value delimiter of the current property.
     *
     * @param delimiter new delimiter for the current property.
     */
    public void setDelimiter(ValueDelimiter delimiter) {
        this.delimiter = delimiter;
        // TODO: zkontrolovat jestli je delimiter nastavovan spravne z vnejsku!
    }

    /**
     * Returns implicit value of the current property.
     *
     * @return implicit value of the current property.
     */
    public Value getImplicitValue() {
        return implicitValue;
    }

    /**
     * Sets new implicit value of the current property.
     *
     * @param implicitValue new implicit value for the current property.
     */
    public void setImplicitValue(Value implicitValue) {
        this.implicitValue = implicitValue;
    }

    /**
     * Returns comment of the current property, or null if does not exist.
     *
     * @return comment of the current property.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets new comment of the current property.
     *
     * @param comment new comment of the current property.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Returns a string representation of the current <code>Property</code>.
     * Always saved with FULL save mode.
     *
     * @return a string representation of the current <code>Property</code>.
     */
    @Override
    public String toString() {
        return toString(SavingMode.FULL);
    }

    /**
     * Returns a string representation of the current <code>Property</code>.
     * Property is represented with the specified save mode.
     *
     * @param type save mode to be returned with.
     * @return a string representation of the current <code>Property</code>.
     */
    public String toString(SavingMode type) {
        StringBuilder result = new StringBuilder();
        result.append(key).append(IniFile.EQUAL_SIGN);
        for (int i = 0; i < values.size(); i++) {
            result.append(values.get(i).toString());
            if (i < values.size() - 1) {
                result.append(delimiter.toString());
            }
        }
        switch (type) {
            case FULL:
                if (values.isEmpty()) {
                    result.append(implicitValue.toString());
                }
                if (!comment.isEmpty()) {
                    result.append(" ;").append(comment);
                }
                break;
            case ORIGIN:
                // TODO: jak udelat ORIGIN?
                break;
            default:
                throw new AssertionError();
        }
        return result.toString();
    }

    /**
     * Compares the current object with the specified one.
     *
     * @param obj object to be compared with.
     * @return true if objects are same; otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Property other = (Property) obj;
        if (this.required != other.required) {
            return false;
        }
        if (this.isSingleValue != other.isSingleValue) {
            return false;
        }
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        if (this.delimiter != other.delimiter) {
            return false;
        }
        if (!Objects.equals(this.implicitValue, other.implicitValue)) {
            return false;
        }
        if (!Objects.equals(this.valueRestriction, other.valueRestriction)) {
            return false;
        }
        if (!Objects.equals(this.valueType, other.valueType)) {
            return false;
        }
        if (!Objects.equals(this.values, other.values)) {
            return false;
        }
        return true;
    }

    
    

    /**
     * Evaluates a hash code of the current property.
     *
     * @return a hash code of the current property.
     */
    @Override
    public int hashCode() {
        // This is automatic hashCode generated by NetBeans IDE.
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.key);
        hash = 73 * hash + (this.required ? 1 : 0);
        hash = 73 * hash + (this.isSingleValue ? 1 : 0);
        hash = 73 * hash + Objects.hashCode(this.delimiter);
        hash = 73 * hash + Objects.hashCode(this.implicitValue);
        hash = 73 * hash + Objects.hashCode(this.valueRestriction);
        hash = 73 * hash + Objects.hashCode(this.valueType);
        hash = 73 * hash + Objects.hashCode(this.values);
        return hash;
    }

    /**
     * Value delimiter that is used for multi-valued properties.
     */
    public enum ValueDelimiter {
        COMMA,
        COLON;

        /**
         * Returns a string representation of the current delimter.
         *
         * @return a string representation of the current delimiter.
         */
        @Override
        public String toString() {
            switch (this) {
                case COMMA:
                    return ",";
                case COLON:
                    return ":";
                default:
                    throw new UnsupportedOperationException("Invalid delimiter.");
            }
        }
    }

}
