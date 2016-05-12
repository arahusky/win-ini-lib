package cz.cuni.mff.d3s.pp.wininilib.values.restrictions;

import cz.cuni.mff.d3s.pp.wininilib.IniFileUtils;
import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.ValueRestriction;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueUnsigned;
import java.math.BigInteger;

/**
 * Provides restriction rules for string type values.
 *
 * @author xxx
 */
public class ValueUnsignedRestriction implements ValueRestriction {

    private final BigInteger lowerBound;
    private final BigInteger upperBound;
    private final boolean hasRestriction;

    /**
     * Initializes a new instance of <code>ValueUnsignedRestriction</code> with
     * no interval restriction.
     */
    public ValueUnsignedRestriction() {
        hasRestriction = false;

        // Just any constants - doesn't matter
        lowerBound = BigInteger.ZERO;
        upperBound = BigInteger.ZERO;
    }

    /**
     * Initializes a new instance of <code>ValueUnsignedRestriction</code> with
     * the specified interval.
     *
     * @param lowerBound lower bound of the interval.
     * @param upperBound upper bound of the interval.
     * @throws InvalidValueFormatException if any of bounds is not correct value.
     */
    public ValueUnsignedRestriction(BigInteger lowerBound, BigInteger upperBound) throws InvalidValueFormatException {
        hasRestriction = true;
        if ((lowerBound.signum() == -1) || (upperBound.signum() == -1)) {
            throw new InvalidValueFormatException("Unsigned values can be only non-negative integers.");
        }
        if (lowerBound.compareTo(upperBound) == 1) {
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
        IniFileUtils.checkRestrictionRecursive(value, this);
        
        if (!(value instanceof ValueUnsigned)) {
            throw new ViolatedRestrictionException("Invalid unsigned value.");
        }

        if (hasRestriction) {
            BigInteger innerValue = (BigInteger) (value.get());
            if ((innerValue.compareTo(lowerBound) == -1) || (innerValue.compareTo(upperBound) == 1)) {
                throw new ViolatedRestrictionException("Value is not in correct interval.");
            }
        }
    }
}
