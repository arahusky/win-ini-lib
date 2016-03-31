package cz.cuni.mff.d3s.pp.wininilib.values;

import com.sun.media.sound.InvalidFormatException;
import cz.cuni.mff.d3s.pp.wininilib.Value;

/**
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class ValueBoolean implements Value{

    public ValueBoolean(String string) {
        if (!Boolean.parseBoolean(string)) {
            
        }
    }
    
    @Override
    public Object get() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
