package cz.cuni.mff.d3s.pp.wininilib.values.restrictions;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.ValueRestriction;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueSigned;

/**
 * Provides restriction rules for signed type values.
 *
 * @author xxx
 */
public class ValueSignedRestriction implements ValueRestriction {

    private final double lowerBound;
    private final double upperBound;

    /**
     * Initializes a new instance of <code>ValueSignedRestriction</code> with
     * the specified interval.
     *
     * @param lowerBound lower bound of the interval.
     * @param upperBound upper bound of the interval.
     * @throws InvalidValueFormat if any of bounds is not correct value.
     */
    public ValueSignedRestriction(double lowerBound, double upperBound) throws InvalidValueFormat {
        if (lowerBound > upperBound) {
            throw new InvalidValueFormat("Upper bound must be greater or equal than lower bound.");
        }
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
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
        if (!(value instanceof ValueSigned)) {
            throw new ViolatedRestrictionException("Invalid signed value.");
        }

        double innerValue = (double) (value.get());
        if ((lowerBound > innerValue) || (upperBound < innerValue)) {
            throw new ViolatedRestrictionException("Value is not in correct interval.");
        }
    }
}
