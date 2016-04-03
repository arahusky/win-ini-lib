package cz.cuni.mff.d3s.pp.wininilib.exceptions;

/**
 * This exception is thrown when there is an attept to add multiple values to
 * single value property.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class TooManyValuesException extends WinIniLibException {

    /**
     * Creates a new instance of <code>TooManyValuesException</code> without
     * detail message.
     */
    public TooManyValuesException() {
    }

    /**
     * Constructs an instance of <code>TooManyValuesException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TooManyValuesException(String msg) {
        super(msg);
    }
}
