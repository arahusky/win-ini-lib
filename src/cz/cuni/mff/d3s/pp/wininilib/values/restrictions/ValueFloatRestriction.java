package cz.cuni.mff.d3s.pp.wininilib.values.restrictions;

import cz.cuni.mff.d3s.pp.wininilib.IniFileUtils;
import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.ValueRestriction;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueFloat;

/**
 * Provides restriction rules for float type values.
 *
 * @author xxx
 */
public class ValueFloatRestriction implements ValueRestriction {

    private final double lowerBound;
    private final double upperBound;

    /**
     * Initializes a new instance of <code>ValueFloatRestriction</code> with no
     * interval restriction.
     */
    public ValueFloatRestriction() {
        lowerBound = Double.NEGATIVE_INFINITY;
        upperBound = Double.POSITIVE_INFINITY;
    }

    /**
     * Initializes a new instance of <code>ValueFloatRestriction</code> with the
     * specified interval.
     *
     * @param lowerBound lower bound of the interval.
     * @param upperBound upper bound of the interval.
     * @throws InvalidValueFormatException if any of bounds is not correct value.
     */
    public ValueFloatRestriction(double lowerBound, double upperBound) throws InvalidValueFormatException {
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
        IniFileUtils.checkRestrictionRecursive(value, this);
        
        if (!(value instanceof ValueFloat)) {
            throw new ViolatedRestrictionException("Invalid float value.");
        }
        double innerValue = (double) (value.get());
        if ((lowerBound > innerValue) || (upperBound < innerValue)) {
            throw new ViolatedRestrictionException("Value is not in correct interval.");
        }
    }

}
