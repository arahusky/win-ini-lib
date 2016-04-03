package cz.cuni.mff.d3s.pp.wininilib.exceptions;

/**
 * This exception is thrown when some value has invalid format and can not be
 * parsed.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class InvalidValueFormat extends WinIniLibException {

    /**
     * Creates a new instance of <code>InvalidValueFormat</code> without detail
     * message.
     */
    public InvalidValueFormat() {
    }

    /**
     * Constructs an instance of <code>InvalidValueFormat</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidValueFormat(String msg) {
        super(msg);
    }
}