package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.IniFile.SaveType;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.TooManyValuesException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
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
    private boolean isSingleValue;
    private ValueDelimiter delimiter;
    private Value implicitValue;
    private ValueRestriction valueRestriction;
    private String comment;
    private Value valueType;

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
    }

    /**
     * Sets the value type of the current property.
     *
     * @param restriction From this restriction will be recognized the value
     * type.
     */
    private void setValueType(ValueRestriction restriction) {
        //TODO set valueType
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
        return false;
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
        values.add(value);
        // TODO: RESTRICTION CHECK
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

    }

    /**
     * Adds the specified value to the current property. Value is specified by
     * reference to another property.
     *
     * @param identifier identifier of any section.
     * @param nameOfProperty property that will be linked to this one.
     * @throws TooManyValuesException if someone tries to add multiple values to
     * single value property.
     * @throws ViolatedRestrictionException if restriction of the current
     * property has been violated.
     */
    public void addValue(String identifier, String nameOfProperty) throws TooManyValuesException, ViolatedRestrictionException {

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
     * Returns value of the current property.
     *
     * @return value of the current property.
     */
    public Value getValueType() {
        return valueType;
    }

    /**
     * Value delimiter that is used for multi-valued properties.
     */
    public enum ValueDelimiter {
        COMMA,
        COLON
    }
    
    /**
     * Returns a string representation of the current <code>Property</code>.
     * Always saved with FULL save mode.
     * @return a string representation of the current <code>Property</code>.
     */
    @Override
    public String toString() {
        return null;
    }
    
    /**
     * Returns a string representation of the current <code>Property</code>.
     * Property is represented with the specified save mode.
     * @param type save mode to be returned with.
     * @return a string representation of the current <code>Property</code>.
     */
    public String toString(SaveType type) {
        return null;
    }
    
    /**
     * Compares the current object with the specified one.
     * @param obj object to be compared with.
     * @return true if objects are same; otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        return hashCode() == obj.hashCode();
    }

    /**
     * Evaluates a hash code of the current property.
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
}
