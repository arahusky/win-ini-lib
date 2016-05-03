package cz.cuni.mff.d3s.pp.wininilib.values.restrictions;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.ValueRestriction;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueUnsigned;
import java.math.BigDecimal;

/**
 * Provides restriction rules for string type values.
 *
 * @author xxx
 */
public class ValueUnsignedRestriction implements ValueRestriction {

    private final BigDecimal lowerBound;
    private final BigDecimal upperBound;
    private final boolean hasRestriction;

    /**
     * Initializes a new instance of <code>ValueUnsignedRestriction</code> with
     * no interval restriction.
     */
    public ValueUnsignedRestriction() {
        hasRestriction = false;

        // Just any constants - doesn't matter
        lowerBound = BigDecimal.ZERO;
        upperBound = BigDecimal.ZERO;
    }

    /**
     * Initializes a new instance of <code>ValueUnsignedRestriction</code> with
     * the specified interval.
     *
     * @param lowerBound lower bound of the interval.
     * @param upperBound upper bound of the interval.
     * @throws InvalidValueFormat if any of bounds is not correct value.
     */
    public ValueUnsignedRestriction(BigDecimal lowerBound, BigDecimal upperBound) throws InvalidValueFormat {
        hasRestriction = true;
        if ((lowerBound.signum() == -1) || (upperBound.signum() == -1)) {
            throw new InvalidValueFormat("Unsigned values can be only non-negative integers.");
        }
        if (lowerBound.compareTo(upperBound) == 1) {
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
        if (!(value instanceof ValueUnsigned)) {
            throw new ViolatedRestrictionException("Invalid unsigned value.");
        }

        if (hasRestriction) {
            BigDecimal innerValue = (BigDecimal) (value.get());
            if ((innerValue.compareTo(lowerBound) == -1) || (innerValue.compareTo(upperBound) == 1)) {
                throw new ViolatedRestrictionException("Value is not in correct interval.");
            }
        }
    }
}
