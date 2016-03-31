package cz.cuni.mff.d3s.pp.wininilib;

import java.util.List;

/**
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class Property {

    private String key;
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
     * @param key
     * @param isSingleValue
     * @param valueRestriction
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
     * @param key
     * @param isSingleValue
     * @param implicitValue
     * @param valueRestriction
     */
    public Property(String key, boolean isSingleValue, Value implicitValue, ValueRestriction valueRestriction) {
        this.key = key;
        this.isSingleValue = isSingleValue;
        this.implicitValue = implicitValue;
        this.valueRestriction = valueRestriction;
    }

    private void setValueType(ValueRestriction restriction) {
        //TODO set valueType
    }

    public ValueDelimiter getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(ValueDelimiter delimiter) {
        this.delimiter = delimiter;
    }

    public Value getImplicitValue() {
        return implicitValue;
    }

    public void setImplicitValue(Value implicitValue) {
        this.implicitValue = implicitValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Value getValueType() {
        return valueType;
    }

    public enum ValueDelimiter {

        COMMA,
        COLON
    }
}
