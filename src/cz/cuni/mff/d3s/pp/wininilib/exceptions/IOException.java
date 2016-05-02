package cz.cuni.mff.d3s.pp.wininilib.exceptions;

/**
 * Basically only wrapper for <i>java.io.Exception</i>.
 *
 * @author xxx
 */
public class IOException extends WinIniLibException {

    private static final long serialVersionUID = 1L;
    
    /**
     * Creates a new instance of <code>IOException</code> without detail
     * message.
     */
    public IOException() {
    }

    /**
     * Constructs an instance of <code>IOException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public IOException(String msg) {
        super(msg);
    }
}
