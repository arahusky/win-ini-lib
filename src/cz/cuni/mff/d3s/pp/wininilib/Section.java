package cz.cuni.mff.d3s.pp.wininilib;

import java.util.List;

/**
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class Section {
    private String identifier;
    private boolean required;
    private List<Property> properties;    
    private String comment;

    public Section(String identifier, boolean required) {
        this.identifier = identifier;
        this.required = required;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public Property getProperty(int i) {
        return properties.get(i);
    }    
    
    public int getNumberOfProperties() {
        return properties.size();
    }
    
    public void addProperty(Property property) {
        properties.add(property);
    }
}
