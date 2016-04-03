package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;

/**
 * Each value restriction must be derived from this interface. Provides a basic
 * behaviour of restrictions.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public interface ValueRestriction {

    /**
     * Evaluates whether the specified value satisfies the restriction. If not,
     * exception is thrown.
     *
     * @param value value to be checked.
     * @throws ViolatedRestrictionException if this restriction has been
     * violated.
     */
    void checkRestriction(Value value) throws ViolatedRestrictionException;
}
