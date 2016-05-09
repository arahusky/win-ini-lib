package cz.cuni.mff.d3s.pp.wininilib;

/**
 * Each value of any property is derived from this interface. Provides a basic
 * behaviour for values.
 *
 * @author xxx
 */
public interface Value {

    /**
     * Returns the real value within the object. This is value for some
     * property.
     *
     * @return the real value within the object.
     */
    Object get();
}
