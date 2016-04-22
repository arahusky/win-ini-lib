package cz.cuni.mff.d3s.pp.wininilib.values.restrictions;

import cz.cuni.mff.d3s.pp.wininilib.Constants;
import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.ValueRestriction;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueString;
import javax.management.openmbean.InvalidOpenTypeException;

/**
 * Provides restriction rules for string type values.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueStringRestriction implements ValueRestriction {

    private final char escapeSymbol = '\\';

    /**
     * Initializes a new instance of <code>ValueStringRestriction</code>.
     */
    public ValueStringRestriction() {
    }

    /**
     * Evaluates whether the specified value satisfies the restriction. If not,
     * exception is thrown.
     *
     * @param value value to be checked.
     * @throws ViolatedRestrictionException if this restriction has been
     * violated.
     */
    @Override
    public void checkRestriction(Value value) throws ViolatedRestrictionException {
        if (!(value instanceof ValueString)) {
            throw new ViolatedRestrictionException("Wrong value type.");
        }

        String innerValue = (String) value.get();
        for (ForbiddenSymbol symbol : ForbiddenSymbol.values()) {
            for (int i = 0; i < innerValue.length(); i++) {
                if (symbol.equals(innerValue.charAt(i))) {
                    if ((i == 0) || (escapeSymbol != innerValue.charAt(i - 1))) {
                        throw new ViolatedRestrictionException("The specified string contains a forbidden symbol.");
                    }
                }
            }
        }
    }

    /**
     * List of symbols that must be escaped with '\'. Then can not be used in
     * string within a <Code>ValueString</Code> independently.
     */
    public enum ForbiddenSymbol {
        COMMA,
        COLON,
        SEMICOLON,
        NEW_LINE;

        @Override
        public String toString() {
            switch (this) {
                case COMMA:
                    return ",";
                case COLON:
                    return ":";
                case SEMICOLON:
                    return ";";
                case NEW_LINE:
                    return Constants.NEW_LINE;
                default:
                    throw new InvalidOpenTypeException();
            }
        }
    }
}
