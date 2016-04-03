package cz.cuni.mff.d3s.pp.wininilib;

import java.util.List;

/**
 * Represents a section of the Format.
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class Section {

    private String identifier;
    private boolean required;
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
    
    public Property getProperty(String identifier) {
        return null;
    }

    /**
     * Removes first occurrence of given property.
     *
     * @param property property to be removed.
     * @return true if any property was removed; otherwise false.
     */
    public boolean removeProperty(Property property) {
        return false;
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
}
