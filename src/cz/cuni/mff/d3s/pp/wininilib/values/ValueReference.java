package cz.cuni.mff.d3s.pp.wininilib.values;

import cz.cuni.mff.d3s.pp.wininilib.IniFile;
import cz.cuni.mff.d3s.pp.wininilib.Property;
import cz.cuni.mff.d3s.pp.wininilib.Value;
import cz.cuni.mff.d3s.pp.wininilib.ValueRestriction;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import java.util.ArrayList;
import java.util.List;

/**
 * Used for encapsulation of value reference.
 *
 * @author xxx
 */
public class ValueReference implements Value {

    private final IniFile iniFile;
    private final String identifier;
    private final String nameOfProperty;
    private final ValueRestriction restriction;

    /**
     * Initializes a new instance of <code>ValueReference</code>. Represents a
     * reference to value of another property.
     *
     * @param iniFile INI file where is property stored.
     * @param identifier identifier of section where property is stored.
     * @param nameOfProperty name of property to use.
     * @param restriction restriction for referenced values.
     */
    public ValueReference(IniFile iniFile, String identifier, String nameOfProperty, ValueRestriction restriction) {
        this.iniFile = iniFile;
        this.identifier = identifier;
        this.nameOfProperty = nameOfProperty;
        this.restriction = restriction;
    }

    /**
     * Returns an IniFile where the current <code>ValueReference</code>
     * references.
     *
     * @return an IniFile where the current <code>ValueReference</code>
     * references.
     */
    public IniFile getIniFile() {
        return iniFile;
    }

    /**
     * Returns a identifier of section where the current
     * <code>ValueReference</code> references.
     *
     * @return a identifier of section where the current
     * <code>ValueReference</code> references.
     *
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns a name of property where the current <code>ValueReference</code>
     * references.
     *
     * @return a name of property where the current <code>ValueReference</code>
     * references.
     */
    public String getNameOfProperty() {
        return nameOfProperty;
    }

    /**
     * Retruns values that are referenced by the current reference.
     *
     * @return values that are references by the current reference.
     */
    @Override
    public Object get() {
        List<Value> referencedValues = new ArrayList<>();
        Property referenced = iniFile.getSection(identifier).getProperty(nameOfProperty);
        for (int i = 0; i < referenced.getNumberOfValues(); i++) {
            referencedValues.add(referenced.getValue(i));
        }
        return referencedValues;
    }

    /**
     * Checks if referenced values do not violate referencing property.
     *
     * @throws ViolatedRestrictionException if referenced values violate
     * referencing property.
     */
    public void checkReferenceRestriction() throws ViolatedRestrictionException {
        Property referenced = iniFile.getSection(identifier).getProperty(nameOfProperty);
        for (int i = 0; i < referenced.getNumberOfValues(); i++) {
            Value value = referenced.getValue(i);
            restriction.checkRestriction(value);
        }
    }

    /**
     * Provides a string representation of the current reference.
     *
     * @return a string representation of the current reference.
     */
    @Override
    public String toString() {
        return "${" + identifier + "#" + nameOfProperty + "}";
    }
}
