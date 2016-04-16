package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.IniFile.SaveType;
import java.util.List;
import java.util.Objects;

/**
 * Represents a section of the IniFile.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class Section {

    private final String identifier;
    private final boolean required;
    private List<Property> properties;
    private String comment;

    /**
     * Initializes a new instance of <code>Section</code>.
     *
     * @param identifier identifier of the section.
     * @param required determines whether the section is required.
     */
    public Section(String identifier, boolean required) {
        this.identifier = identifier;
        this.required = required;
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
     * Removes first occurrence of given property.
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
     * Returns a string representation of the current <code>Section</code>.
     *
     * @return a string representation of the current <code>Section</code>.
     */
    @Override
    public String toString() {
        return toString(SaveType.FULL);
    }

    /**
     * Returns a string representation of the current <code>Section</code>.
     * Represented with the specified save type.
     *
     * @param type save mode to be returned with.
     * @return a string representation of the current <code>Section</code>.
     */
    public String toString(SaveType type) {
        StringBuilder result = new StringBuilder();
        result.append("[").append(identifier).append("]");
        if (!comment.isEmpty()) {
            result.append(" ;").append(comment);
        }
        result.append(IniFile.NEW_LINE);

        for (Property property : properties) {
            result.append(property.toString(type));
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
        if (!obj.getClass().equals(Section.class)) {
            return false;
        }
        Section toComapre = (Section) obj;
        if (!identifier.equals(toComapre.identifier)) {
            return false;
        }
        if (required != toComapre.required) {
            return false;
        }

        // Compare properties one by one
        for (Property prop1 : properties) {
            for (Property prop2 : toComapre.properties) {
                if (prop1.equals(prop2)) {
                    return false;
                }
            }
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
        hash = 71 * hash + (this.required ? 1 : 0);
        hash = 71 * hash + Objects.hashCode(this.properties);
        return hash;
        //
    }
}
