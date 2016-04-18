package cz.cuni.mff.d3s.pp.wininilib.values.restrictions;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.ValueRestriction;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * Provides restriction rules for boolean type values.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueBooleanRestriction implements ValueRestriction {

    private static List<String> falseValues = Arrays.asList(new String[]{"0", "f", "n", "off", "no", "disabled"});
    private static List<String> trueValues = Arrays.asList(new String[]{"1", "t", "y", "on", "yes", "enabled"});

    /**
     * Initializes a new instance of <code>ValueBooleanRestriction</code>.
     */
    public ValueBooleanRestriction() {

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
        if (!(value instanceof ValueBoolean)) {
            throw new ViolatedRestrictionException("Specified value is not valid boolean value.");
        }
        Object innerValue = value.get();
        if (!falseValues.contains(innerValue) && !trueValues.contains(innerValue)) {
            throw new ViolatedRestrictionException("Specified value is not valid boolean value.");
        }
    }

    /**
     * Returns a list of all values that can represent a <code>true</code>
     * value.
     *
     * @return a list of all values that can represent a <code>true</code>
     * value.
     */
    public static List<String> getAllTrueValues() {
        // TODO: chceme jiny list? nebo dovolime ten samy...?
        return trueValues;
    }

    /**
     * Returns a list of all values that can represent a <code>false</code>
     * value.
     *
     * @return a list of all values that can represent a <code>false</code>
     * value.
     */
    public static List<String> getAllFalseValues() {
        // TODO: chceme jiny list? nebo dovolime ten samy...?
        return falseValues;
    }

}