package cz.cuni.mff.d3s.pp.wininilib.exceptions;

/**
 *
 * @author Jakub Naplava
 */
public class ViolatedRestrictionException extends WinIniLibException {

    /**
     * Creates a new instance of <code>ViolatedRestrictionException</code>
     * without detail message.
     */
    public ViolatedRestrictionException() {
    }

    /**
     * Constructs an instance of <code>ViolatedRestrictionException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ViolatedRestrictionException(String msg) {
        super(msg);
    }
}
