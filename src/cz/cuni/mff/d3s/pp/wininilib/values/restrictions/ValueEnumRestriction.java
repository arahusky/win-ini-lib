package cz.cuni.mff.d3s.pp.wininilib.values.restrictions;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.ValueRestriction;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;

/**
 * Provides restriction rules for enum type values.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueEnumRestriction implements ValueRestriction {

    private final Class<? extends Enum> clazz;

    /**
     * Initializes a new instance of <code>ValueEnumRestriction</code>.
     *
     * @param clazz specified Enum.
     */
    public ValueEnumRestriction(Class<? extends Enum> clazz) {
        this.clazz = clazz;
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
        for (Enum enumValue : clazz.getEnumConstants()) {
            if (enumValue.equals(value) || enumValue.toString().equals(value.toString())) {
                return;
            }
        }
        throw new ViolatedRestrictionException("Specified value is not from correct enum.");
    }

}
