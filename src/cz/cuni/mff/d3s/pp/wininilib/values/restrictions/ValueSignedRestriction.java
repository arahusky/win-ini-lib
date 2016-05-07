package cz.cuni.mff.d3s.pp.wininilib.values.restrictions;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.ValueRestriction;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueSigned;

/**
 * Provides restriction rules for signed type values.
 *
 * @author xxx
 */
public class ValueSignedRestriction implements ValueRestriction {

    private final long lowerBound;
    private final long upperBound;

    /**
     * Initializes a new instance of <code>ValueSignedRestriction</code> with no
     * interval restriction.
     */
    public ValueSignedRestriction() {
        lowerBound = Long.MIN_VALUE;
        upperBound = Long.MAX_VALUE;
    }

    /**
     * Initializes a new instance of <code>ValueSignedRestriction</code> with
     * the specified interval.
     *
     * @param lowerBound lower bound of the interval.
     * @param upperBound upper bound of the interval.
     * @throws InvalidValueFormatException if any of bounds is not correct value.
     */
    public ValueSignedRestriction(long lowerBound, long upperBound) throws InvalidValueFormatException {
        if (lowerBound > upperBound) {
            throw new InvalidValueFormatException("Upper bound must be greater or equal than lower bound.");
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

        double innerValue = (long) (value.get());
        if ((lowerBound > innerValue) || (upperBound < innerValue)) {
            throw new ViolatedRestrictionException("Value is not in correct interval.");
        }
    }
}
