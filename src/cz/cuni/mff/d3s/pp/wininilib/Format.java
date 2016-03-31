package cz.cuni.mff.d3s.pp.wininilib;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author Jakub Naplava; Jan Kluj
 */
public class Format {

    private List<Section> sections;
    
    public Format() {
        sections = new ArrayList<>();
    }

    public Section getSection(int i) {
        return null;
    }

    public void addSection(Section section) {
        
    }

    /**
     * Removes first occurrence of given section.
     *
     * @param section Section to be removed.
     * @return true if any section was removed; otherwise false.
     */
    public boolean removeSection(Section section) {
        return false;
    }

    public int getNumberOfSections() {
        return sections.size();
    }    
    
    @Override
    public String toString() {
        return null;
    }
    
    public String toString(SaveType type) {
        return null;
    }
    
    public Stream toStream(SaveType type) {
        return null;
    }
    
    public void loadDataFromFile(String fileName) {
        
    }
    
    public static Format loadFormatFromFile(String fileName, LoadType type) {
        return null;
    }
    
    public static Format loadFormatFromString(String format, LoadType type) {
        return null;
    }
    
    public static Format loadFormatFromStream(Stream stream, LoadType type) {
        return null;
    }
    
    public enum LoadType{
        STRICT,
        RELAXED
    }
    
    public enum SaveType{
        FULL,
        ORIGIN
    }
}
