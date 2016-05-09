package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.exceptions.FileFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.TooManyValuesException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueBoolean;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueEnum;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueFloat;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueSigned;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueString;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueUnsigned;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueStringRestriction;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

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
        List<Pair<Section, String>> pairedSections = pairSections(iniFileSections, dataOfSections, type);

        for (Pair<Section, String> pair : pairedSections) {
            Section iniFileSection = pair.getKey();
            String rawSection = pair.getValue();

            try {
                combineSections(iniFileSection, rawSection, type);
            } catch (InvalidValueFormatException | TooManyValuesException | ViolatedRestrictionException e) {
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
                if (!sectionBody.toString().isEmpty()) {
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
     * TODO test me!!!
     *
     * TODO comment me!!!
     *
     *
     * @param sections
     * @param rawSections
     * @return
     * @throws FileFormatException
     */
    protected static List<Pair<Section, String>> pairSections(List<Section> sections, List<RawSection> rawSections, IniFile.LoadingMode type) throws FileFormatException {
        List<String> rawSectionIdentifiers = new ArrayList<>();
        for (RawSection rs : rawSections) {
            String identifier = rs.getIdentifier();

            //in strict mode, there may not be any previously undefined section
            if (type == IniFile.LoadingMode.STRICT) {
                boolean isSectionDefined = false;
                for (Section section : sections) {
                    if (section.getIdentifier().equals(identifier)) {
                        isSectionDefined = true;
                        break;
                    }
                }

                if (!isSectionDefined) {
                    throw new FileFormatException("Given file contains section, which does not exist in the specified format.");
                }
            }

            rawSectionIdentifiers.add(rs.getIdentifier());
        }

        List<Pair<Section, String>> result = new ArrayList<>();

        for (Section section : sections) {
            int rawSectionPosition = rawSectionIdentifiers.indexOf(section.getIdentifier());

            if (rawSectionPosition == -1) {
                if (section.isRequired()) {
                    throw new FileFormatException("Given file does not contain mandatory section with given ID: " + section.getIdentifier());
                }

                continue;
            }

            result.add(new Pair<>(section, rawSections.get(rawSectionPosition).body));
        }

        return result;
    }

    /**
     * Try to fit Section with the specified string-represented section. Returns
     * true if combination is specified properly. In this case, the specified
     * section is also filled with data from string-represented section.
     *
     * @param section Section to fit.
     * @param rawSection A string-represented section body (without first line
     * containing identifier).
     * @param type loading mode type.
     * @return true if the specified section combination is right.
     */
    protected static boolean combineSections(Section section, String rawSection, IniFile.LoadingMode type) throws TooManyValuesException, ViolatedRestrictionException, InvalidValueFormatException {

        String[] lines = rawSection.split(Constants.NEW_LINE);
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].trim();
        }

        for (int i = 0; i < lines.length; i++) {
            //the first line may contain section comment
            if (i == 0 && lines[0].charAt(0) == ';') {
                String comment = lines[0].substring(1);
                section.setComment(comment);
                continue;
            }
            String[] propertyParts = lines[i].split("=", 2);
            String propertyID = propertyParts[0];
            String propertyBody = propertyParts[1];

            String[] bodyParts = propertyBody.split(Constants.COMMENT_DELIMITER, 2);
            String bodyNoComment = bodyParts[0];
            String comment = bodyParts.length > 1 ? bodyParts[1] : "";

            Property property = section.getProperty(propertyID.trim());
            Class<? extends Value> valueType = property.getValueType();
            String[] values = bodyNoComment.split(property.getDelimiter().toString());

            //TODO what is the second parameter (what should I fill in?)
            for (String value : values) {
                value = value.trim();
                if (valueType.equals(ValueBoolean.class)) {
                    property.addValue(new ValueBoolean(value, true));
                } else if (valueType.equals(ValueEnum.class)) {
                    property.addValue(new ValueEnum(value, true));
                } else if (valueType.equals(ValueFloat.class)) {
                    property.addValue(new ValueFloat(Double.parseDouble(value), true));
                } else if (valueType.equals(ValueSigned.class)) {
                    property.addValue(new ValueSigned(value, true));
                } else if (valueType.equals(ValueString.class)) {
                    property.addValue(new ValueString(value, true));
                } else if (valueType.equals(ValueUnsigned.class)) {
                    property.addValue(new ValueUnsigned(value, true));
                } else {
                    throw new UnsupportedOperationException("Unsupported type of value.");
                }
            }

            property.setComment(comment.trim());
        }

        return true;
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
    private static Section parseSection(String section) throws FileFormatException, InvalidValueFormatException {
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
    private static Property parseProperty(String property) throws FileFormatException, InvalidValueFormatException {
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
                        prop.addValue(new ValueString(val, true)); // @TODO.
                    }
                } catch (TooManyValuesException | ViolatedRestrictionException | InvalidValueFormatException ex) {
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