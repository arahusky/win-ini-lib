package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.IniFile.SavingMode;
import static cz.cuni.mff.d3s.pp.wininilib.IniFileUtils.checkIdentifierValidity;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a section of the IniFile.
 *
 * @author xxx
 */
public class Section {

    private final String identifier;
    private final boolean isRequired;
    private final List<Property> properties;
    private String comment;

    /**
     * Initializes a new instance of <code>Section</code>.
     *
     * @param identifier identifier of the section.
     * @param required determines whether the section is required.
     */
    public Section(String identifier, boolean required) throws InvalidValueFormatException {
        this.identifier = identifier;
        if (!checkIdentifierValidity(identifier)) {
            throw new InvalidValueFormatException("Malformed section identifier.");
        }
        
        this.isRequired = required;
        properties = new ArrayList<>();
    }

    /**
     * Removes all values within the properties in the current section.
     */
    public void clearSection() {
        for (Property property : properties) {
            property.clearProperty();
        }
    }

    /**
     * Returns identifier of the current section.
     *
     * @return identifier of the current section.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns comment of the current section, or null if does not exist.
     *
     * @return comment of the current section.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets new comment of the current section.
     *
     * @param comment new comment of the current section.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Returns the element at the specified position in properties.
     *
     * @param i index of the element to return.
     * @return the element at the specified position in properties.
     */
    public Property getProperty(int i) {
        return properties.get(i);
    }

    /**
     * Returns the property with the specified identifier.
     *
     * @param key identifier of the property to return.
     * @return the property with the specified identifier.
     */
    public Property getProperty(String key) {
        for (Property property : properties) {
            if (property.getKey().equals(key)) {
                return property;
            }
        }
        return null;
    }

    /**
     * Removes given property.
     *
     * @param property property to be removed.
     * @return true if any property was removed; otherwise false.
     */
    public boolean removeProperty(Property property) {
        return properties.remove(property);
    }

    /**
     * Returns a number of properties within the current section.
     *
     * @return a numer of properties within the current section.
     */
    public int getNumberOfProperties() {
        return properties.size();
    }

    /**
     * Adds the specified property to the current section.
     *
     * @param property property to be added.
     */
    public void addProperty(Property property) {
        properties.add(property);
    }

    /**
     * Check, whether this section is mandatory.
     *
     * @return true, if the section is required, otherwise false.
     */
    public boolean isRequired() {
        return isRequired;
    }

    /**
     * Returns a string representation of the current <code>Section</code>.
     *
     * @return a string representation of the current <code>Section</code>.
     */
    @Override
    public String toString() {
        return toString(SavingMode.FULL);
    }

    /**
     * Returns a string representation of the current <code>Section</code>.
     * Represented with the specified save type.
     *
     * @param type save mode to be returned with.
     * @return a string representation of the current <code>Section</code>.
     */
    public String toString(SavingMode type) {
        StringBuilder result = new StringBuilder();
        result.append("[").append(identifier).append("]");
        boolean hasComment = (comment != null && !comment.isEmpty());
        if (hasComment) {
            result.append(Constants.NEW_LINE);
            result.append(";").append(comment);
        }
        result.append(Constants.NEW_LINE);

        for (int i = 0; i < properties.size(); i++) {
            result.append(properties.get(i).toString(type));
            if (i != properties.size() - 1) {
                result.append(Constants.NEW_LINE);
            }
        }
        if (properties.isEmpty() && !hasComment) {
            result.append(Constants.NEW_LINE);
        }
        return result.toString();
    }

    /**
     * Compares the current object with the specified object.
     *
     * @param obj to compare with.
     * @return true if objects are same; otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Section other = (Section) obj;
        if (!Objects.equals(this.identifier, other.identifier)) {
            return false;
        }
        if (this.isRequired != other.isRequired) {
            return false;
        }
        if (!Objects.equals(this.properties, other.properties)) {
            return false;
        }
        return true;
    }

    /**
     * A hash code of the current section.
     *
     * @return a hash code of the current section.
     */
    @Override
    public int hashCode() {
        // This is automatically generated hashCode by NetBeans IDE.
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.identifier);
        hash = 71 * hash + (this.isRequired ? 1 : 0);
        hash = 71 * hash + Objects.hashCode(this.properties);
        return hash;
        //
    }
}
