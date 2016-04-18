package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;
import javax.management.openmbean.InvalidOpenTypeException;

/**
 * Represents a <code>String</code> value of the property.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueString implements Value {

    private final char escapeSymbol = '\\';
    private final String value;

    /**
     * Initializes a new instance of <code>ValueString</code>.
     *
     * @param value string to be parsed.
     * @throws InvalidValueFormat if the specified string has no correct format.
     */
    public ValueString(String value) throws InvalidValueFormat {
        for (ForbiddenSymbol symbol : ForbiddenSymbol.values()) {
            for (int i = 0; i < value.length(); i++) {
                if (symbol.equals(value.charAt(i))) {
                    if ((i == 0) || (escapeSymbol != value.charAt(i - 1))) {
                        throw new InvalidValueFormat("The specified string contains a forbidden symbol.");
                    }
                }
            }
        }
        this.value = value;
    }

    @Override
    public Object get() {
        return value;
    }

    /**
     * List of symbols that must be escaped with '\'. Then can not be used in
     * string within a <Code>ValueString</Code> independently.
     */
    public enum ForbiddenSymbol {
        COMMA,
        COLON,
        SEMICOLON;

        @Override
        public String toString() {
            switch (this) {
                case COMMA:
                    return ",";
                case COLON:
                    return ":";
                case SEMICOLON:
                    return ";";
                default:
                    throw new InvalidOpenTypeException();
            }
        }
    }

}
