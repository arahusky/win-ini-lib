package cz.cuni.mff.d3s.pp.wininilib.values.restrictions;

import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.ValueRestriction;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;

/**
 * Provides restriction rules for string type values.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueUnsignedRestriction implements ValueRestriction {

    /**
     * Evaluates whether the specified value satisfies the restriction. If not,
     * exception is thrown.
     *
     * @param value value to be checked.
     */
    @Override
    public void checkRestriction(Value value) throws ViolatedRestrictionException{
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
