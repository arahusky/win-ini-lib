package cz.cuni.mff.d3s.pp.wininilib.values.restrictions;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.ValueRestriction;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import java.util.Arrays;
import java.util.List;

/**
 * Provides restriction rules for boolean type values.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueBooleanRestriction implements ValueRestriction {

    private List<String> falseValues = Arrays.asList(new String[]{"0", "f", "n", "off", "no", "disabled"});
    private List<String> trueValues = Arrays.asList(new String[]{"1", "t", "y", "on", "yes", "enabled"});

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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Returns a list of all values that can represent a <code>true</code>
     * value.
     *
     * @return a list of all values that can represent a <code>true</code>
     * value.
     */
    public static List<String> getAllTrueValues() {
        // return different instance of list...
        return null;
    }
    
    /**
     * Returns a list of all values that can represent a <code>false</code>
     * value.
     *
     * @return a list of all values that can represent a <code>false</code>
     * value.
     */
    public static List<String> getAllFalseValues() {
        // return different instance of list...
        return null;
    }

}
