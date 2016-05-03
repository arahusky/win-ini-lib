package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.exceptions.FileFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormat;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.TooManyValuesException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueString;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueStringRestriction;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author xxx
 */
public class IniFileUtils {

    /**
     * Validates and fills the current instance of IniFile with the specified
     * data.
     *
     * @param iniFile IniFile to fill in.
     * @param text Text containing data to be parsed and filled into iniFile.
     * @param type data load mode.
     */
    public static void parseAndValidate(IniFile iniFile, String text, IniFile.LoadingMode type) throws FileFormatException {
        List<Section> iniFileSections = new ArrayList<>();
        for (int i = 0; i < iniFile.getNumberOfSections(); i++) {
            iniFileSections.add(iniFile.getSection(i));
        }

        List<RawSection> dataOfSections = divideToSections(text);

        for (Section section : iniFileSections) {
            boolean failedToCombine = true;
            for (int i = 0; i < dataOfSections.size(); i++) {
                /*if (tryToCombineSection(section, dataOfSections.get(i), type)) {
                    failedToCombine = false;
                    dataOfSections.remove(i); // Each section is usable only once
                    break;
                }*/
            }
            if (failedToCombine && type == IniFile.LoadingMode.STRICT) {
                throw new FileFormatException("File is not applicable to the current IniFile instance.");
            }
        }

        if (type == IniFile.LoadingMode.RELAXED) {
            // Remaining data from file... 
            /*for (String rawSection : dataOfSections) {
                // TODO.
            }*/
        }
    }

    /**
     * Splits given text into sections. Each section is represented by String
     * instance.
     *
     * @param text
     * @return
     * @throws FileFormatException when there's no identifier for the first
     * section or if there are multiple sections with same name.
     */
    protected static List<RawSection> divideToSections(String text) throws FileFormatException {
        String[] data = text.split(Constants.NEW_LINE);
        List<RawSection> result = new ArrayList<>();
        boolean sectionStarted = false;
                
        StringBuilder sectionBody = new StringBuilder();
        String sectionIdentifier = "";
        
        for (int i = 0; i < data.length; i++) {            
            //ignore empty lines
            if (data[i].trim().isEmpty()) {
                continue;
            }

            //each line may contain one or more comment signs
            String[] parts = data[i].split(Constants.COMMENT_DELIMITER, 2);
            String identif = parts[0].trim();

            if ((identif.charAt(0) == '[') && (identif.charAt(identif.length() - 1) == ']')) {
                // new section found
                if (!sectionIdentifier.isEmpty()) {                    
                    result.add(new RawSection(sectionIdentifier, sectionBody.toString()));
                }
                sectionBody = new StringBuilder();
                sectionIdentifier = identif.substring(1, identif.length() - 1);

                sectionStarted = true;
                continue;
            }

            if (sectionStarted) {
                if (! sectionBody.toString().isEmpty()) {
                    sectionBody.append(Constants.NEW_LINE);
                }
                
                sectionBody.append(data[i].trim());       
            } else {
                throw new FileFormatException("Invalid file format.");
            }
        }
        result.add(new RawSection(sectionIdentifier, sectionBody.toString()));
        
        // every section must have an unique identifier       
        List<String> sectionIdentifiers = new ArrayList<>();
        for (RawSection rs : result) {
            if (sectionIdentifiers.contains(rs.getIdentifier())) {
                throw new FileFormatException("Duplicite section ID in file detected.");
            }
            
            sectionIdentifiers.add(rs.getIdentifier());
        }
        
        return result;
    }

    /**
     * Try to fit Section with the specified string-represented section. Returns
     * true if combination is specified property. In this case, the specified
     * section is also filled with data from string-represented section.
     *
     * @param section Section to fit.
     * @param rawSection A string-represented section.
     * @param type loading mode type.
     * @return true if the specified section combination is right.
     */
    private static boolean tryToCombineSection(Section section, String rawSection, IniFile.LoadingMode type) {
        String[] parts = rawSection.split(Constants.COMMENT_DELIMITER, 2);
        String identif = parts[0].substring(1, parts[0].length());
        String comment = parts.length > 1 ? parts[1] : "";

        if (!section.getIdentifier().equals(identif)) {
            return false;
        }
        section.setComment(comment);

        // TODO.
        return false;
    }

    /**
     * Creates an INI file using the specified data.
     *
     * @param data data of the ini file.
     * @return an INI file using the specified data.
     */
    public static IniFile createIniFile(String data) throws FileFormatException {
        IniFile iniFile = new IniFile();
        /*try {
            for (String section : divideToSections(data)) {
                iniFile.addSection(parseSection(section));
            }
        } catch (DupliciteNameException ex) {
            throw new FileFormatException("Invalid file format. Duplicite name detected,", ex);
        }*/
        return iniFile;
    }

    /**
     * As a parameter gets a section a parses it to a instance of Section class.
     * Always used in RELAXED mode.
     *
     * @param section section to be parsed.
     * @return a parsed section.
     */
    private static Section parseSection(String section) throws FileFormatException {
        Section result = null;
        boolean required = false;

        String[] lines = section.split(Constants.NEW_LINE);
        if (lines.length == 0) {
            throw new FileFormatException("Invalid format.");
        }

        for (int i = 0; i < lines.length; i++) {
            String[] parts = lines[i].split(Constants.COMMENT_DELIMITER, 2);
            String value = parts[0];
            String comment = "";
            if (parts.length > 1) {
                comment = parts[1];
            }

            if (i == 0) {
                result = new Section(value.substring(1, value.length()), required);
                result.setComment(comment);
                continue;
            }
            result.addProperty(parseProperty(lines[i]));
        }
        return result;
    }

    /**
     * TODO: Rict, ze defaultne to vemu jako string a hotovo (nebo teda udelat
     * to nejspecificnejsi...?)
     *
     * @param property
     * @return
     */
    private static Property parseProperty(String property) throws FileFormatException {
        Property prop;

        String[] parts = property.split(Constants.COMMENT_DELIMITER, 2);
        String leftSide = parts[0];
        String comment = "";
        if (parts.length > 1) {
            comment = parts[1];
        }

        parts = leftSide.split(Constants.EQUAL_SIGN);
        Property.ValueDelimiter valueDelimiter = null;
        switch (parts.length) {
            // Using string-value type. 'Real' type can not be decided.
            case 2:
                String[] values = null;

                // If we have both 'comma' and 'colon' in line, we use comma as delimiter
                if (parts[1].contains(Property.ValueDelimiter.COLON.toString())) {
                    values = parts[1].split(Property.ValueDelimiter.COLON.toString());
                    valueDelimiter = Property.ValueDelimiter.COLON;
                }
                if (parts[1].contains(Property.ValueDelimiter.COMMA.toString())) {
                    values = parts[1].split(Property.ValueDelimiter.COMMA.toString());
                    valueDelimiter = Property.ValueDelimiter.COMMA;
                }

                boolean isSingleValue = true;
                if (values.length > 1) {
                    isSingleValue = false;
                }
                prop = new Property(parts[0], isSingleValue, new ValueStringRestriction());
                try {
                    for (String val : values) {
                        prop.addValue(new ValueString(val));
                    }
                } catch (TooManyValuesException | ViolatedRestrictionException | InvalidValueFormat ex) {
                    throw new FileFormatException("Invalid file format.", ex);
                }
                break;
            case 1:
                prop = new Property(parts[0], true, new ValueStringRestriction());
                valueDelimiter = Property.ValueDelimiter.COMMA;
            default:
                throw new FileFormatException("Invalid file format.");
        }

        if (!comment.isEmpty()) {
            prop.setComment(comment);
        }
        prop.setDelimiter(valueDelimiter);

        return prop;
    }

    /**
     * Immutable class, which represents each section as an identifier and a
     * (String) body.
     */
    protected static class RawSection {

        private final String identifier;
        private final String body;

        public RawSection(String identifier, String body) {
            this.identifier = identifier;
            this.body = body;
        }

        public String getIdentifier() {
            return identifier;
        }

        public String getBody() {
            return body;
        }
    }
}
