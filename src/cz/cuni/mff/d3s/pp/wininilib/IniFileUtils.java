package cz.cuni.mff.d3s.pp.wininilib;

import cz.cuni.mff.d3s.pp.wininilib.exceptions.DuplicateNameException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.FileFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.TooManyValuesException;
import cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueBoolean;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueEnum;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueFloat;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueReference;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueSigned;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueString;
import cz.cuni.mff.d3s.pp.wininilib.values.ValueUnsigned;
import cz.cuni.mff.d3s.pp.wininilib.values.restrictions.ValueStringRestriction;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.util.Pair;

/**
 * This class provides static methods (mainly) for file parsing, validating and
 * combining with IniFile.
 *
 * @author xxx
 */
public class IniFileUtils {

    //regex for matching identifier
    private static final String identifierRegex = "[a-zA-Z\\.,\\$\\:][a-zA-Z0-9_~\\-\\.\\:\\$ ]*";
    private static final Pattern identifierPattern = Pattern.compile(identifierRegex);

    /**
     * Validates and fills the current instance of IniFile with the specified
     * data.
     *
     * @param iniFile IniFile to fill in.
     * @param text Text containing data to be parsed and filled into iniFile.
     * @param type data load mode.
     */
    public static void parseAndValidate(IniFile iniFile, String text, IniFile.LoadingMode type) throws FileFormatException {
        //clear all previously loaded data
        iniFile.clearIniFile();

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
                combineSections(iniFile, iniFileSection, rawSection, type);
            } catch (InvalidValueFormatException | TooManyValuesException | ViolatedRestrictionException e) {
                throw new FileFormatException("File is not applicable to the current IniFile instance.");
            }
        }

        //in RELAXED mode, we must also process unknown sections (i.e. those rawsections that were not paired)
        if (type == IniFile.LoadingMode.RELAXED) {
            // Remaining data from file... 
            for (RawSection rs : dataOfSections) {
                boolean isPaired = false;
                for (Section section : iniFileSections) {
                    if (section.getIdentifier().equals(rs.getIdentifier())) {
                        isPaired = true;
                        break;
                    }
                }

                if (!isPaired) {
                    try {
                        iniFile.addSection(parseSection(rs));
                    } catch (DuplicateNameException ex) {
                        throw new FileFormatException("File contains multiple section with same ID.");
                    } catch (InvalidValueFormatException ex) {
                        throw new FileFormatException("File contains property in bad format.");
                    }
                }
            }
        }
    }

    /**
     * Splits given text into sections. Each section is represented as an
     * identifier and properties part.
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

            //each line may contain one or more comment symbols
            String[] parts = data[i].split(Constants.COMMENT_DELIMITER, 2);
            String identif = IniFileUtils.trim(parts[0]);
            if ((identif.length() > 2 && identif.charAt(0) == '[') && (identif.charAt(identif.length() - 1) == ']')) {
                // new section found
                if (!sectionIdentifier.isEmpty()) {
                    result.add(new RawSection(sectionIdentifier, sectionBody.toString()));
                }
                sectionBody = new StringBuilder();
                sectionIdentifier = IniFileUtils.trim(identif.substring(1, identif.length() - 1));

                sectionStarted = true;
                continue;
            }

            if (sectionStarted) {
                if (!sectionBody.toString().isEmpty()) {
                    sectionBody.append(Constants.NEW_LINE);
                }

                sectionBody.append(IniFileUtils.trim(data[i]));
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
     * Pairs sections with rawSections. This means that for each section in
     * sections, we try to find corresponding (with same section identifier)
     * rawSection and return all such pairs.
     *
     *
     * @param sections
     * @param rawSections
     * @param type
     * @return
     * @throws FileFormatException when the rawSections do not contain some
     * mandatory section from sections or if in STRICT mode and there is a
     * rawSection with ID that cannot be found in sections.
     */
    protected static List<Pair<Section, String>> pairSections(List<Section> sections, List<RawSection> rawSections, IniFile.LoadingMode type) throws FileFormatException {
        List<String> rawSectionIdentifiers = new ArrayList<>();
        for (RawSection rs : rawSections) {
            String identifier = rs.getIdentifier();

            //in STRICT mode, there may not be any undefined section
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
     * @param iniFile
     * @param section Section to fit.
     * @param rawSection A string-represented section body (without first line
     * containing identifier).
     * @param type loading mode type.
     * @return true if the specified section combination is right.
     * @throws cz.cuni.mff.d3s.pp.wininilib.exceptions.TooManyValuesException
     * @throws
     * cz.cuni.mff.d3s.pp.wininilib.exceptions.ViolatedRestrictionException
     * @throws
     * cz.cuni.mff.d3s.pp.wininilib.exceptions.InvalidValueFormatException
     * @throws cz.cuni.mff.d3s.pp.wininilib.exceptions.FileFormatException
     */
    protected static boolean combineSections(IniFile iniFile, Section section, String rawSection, IniFile.LoadingMode type) throws TooManyValuesException, ViolatedRestrictionException, InvalidValueFormatException, FileFormatException {
        //used to check, whether rawSection contains multiple properties with same ID -> error
        List<String> usedPropertyIDs = new ArrayList<>();

        String[] lines = rawSection.split(Constants.NEW_LINE);
        for (int i = 0; i < lines.length; i++) {
            lines[i] = IniFileUtils.trim(lines[i]);
        }

        for (int i = 0; i < lines.length; i++) {
            //the first line may contain section comment
            if (i == 0 && lines[0].charAt(0) == ';') {
                String comment = lines[0].substring(1);
                section.setComment(comment);
                continue;
            }
            String[] propertyParts = lines[i].split("=", 2);
            String propertyID = IniFileUtils.trim(propertyParts[0]);
            if (usedPropertyIDs.contains(propertyID)) {
                throw new FileFormatException("Duplicite property identifier in one section.");
            }
            usedPropertyIDs.add(propertyID);

            String propertyBody = propertyParts[1];

            String[] bodyParts = propertyBody.split(Constants.COMMENT_DELIMITER, 2);
            String bodyNoComment = bodyParts[0];
            String comment = bodyParts.length > 1 ? bodyParts[1] : "";
            comment = IniFileUtils.trim(comment);

            Property property = section.getProperty(propertyID);

            if (property == null) {
                //if we met uknown property in STRICT mode, it will lead to exception
                if (type == IniFile.LoadingMode.STRICT) {
                    throw new FileFormatException("Uknown property detected in STRICT mode.");
                }

                //otherwise (RELAXED mode), we create new String property
                property = new Property(propertyID, true, new ValueStringRestriction());
                section.addProperty(property);
            }

            Class<? extends Value> valueType = property.getValueType();
            String[] values = null;
            if (property.isSingleValue()) {
                values = new String[]{bodyNoComment};
            } else {
                values = split(bodyNoComment, property.getDelimiter().toString().charAt(0));
            }

            for (String value : values) {
                value = IniFileUtils.trim(value);
                if (isReference(value)) {
                    String referenceBody = value.substring(2, value.length() - 1);
                    String[] parts = referenceBody.split("#");
                    property.addValue(iniFile, parts[0], parts[1]);
                } else if (valueType.equals(ValueBoolean.class)) {
                    property.addValue(new ValueBoolean(value));
                } else if (valueType.equals(ValueEnum.class)) {
                    property.addValue(new ValueEnum(value));
                } else if (valueType.equals(ValueFloat.class)) {
                    property.addValue(new ValueFloat(Double.parseDouble(value)));
                } else if (valueType.equals(ValueSigned.class)) {
                    property.addValue(new ValueSigned(value));
                } else if (valueType.equals(ValueString.class)) {
                    property.addValue(new ValueString(value));
                } else if (valueType.equals(ValueUnsigned.class)) {
                    property.addValue(new ValueUnsigned(value));
                } else {
                    throw new UnsupportedOperationException("Unsupported type of value.");
                }
            }

            property.setComment(comment);
        }

        return true;
    }

    /**
     * Creates an INI file using the specified data.
     *
     * @param data data of the ini file.
     * @return an INI file using the specified data.
     * @throws cz.cuni.mff.d3s.pp.wininilib.exceptions.FileFormatException
     */
    public static IniFile createIniFile(String data) throws FileFormatException {
        IniFile iniFile = new IniFile();
        try {
            for (RawSection rs : divideToSections(data)) {
                iniFile.addSection(parseSection(rs));
            }
        } catch (DuplicateNameException | InvalidValueFormatException ex) {
            throw new FileFormatException("Invalid file format. Duplicite name detected,", ex);
        }
        return iniFile;
    }

    /**
     * As a parameter gets a section a parses it to a instance of Section class.
     * Always used in RELAXED mode.
     *
     * @param section section to be parsed.
     * @return a parsed section.
     */
    private static Section parseSection(RawSection section) throws InvalidValueFormatException, FileFormatException {
        String identifier = section.getIdentifier();

        String rawBody = section.getBody();
        Section result = new Section(identifier, true);

        //used to check, whether section contains multiple properties with same ID -> error
        List<String> usedPropertyIDs = new ArrayList<>();

        String[] lines = rawBody.split(Constants.NEW_LINE);
        for (int i = 0; i < lines.length; i++) {
            //the first line may contain section comment
            if (i == 0 && lines[0].charAt(0) == ';') {
                String comment = lines[0].substring(1);
                result.setComment(comment);
                continue;
            }

            Property prop = parseProperty(lines[i]);
            if (usedPropertyIDs.contains(prop.getKey())) {
                throw new FileFormatException("Duplicite property identifier in one section.");
            }
            usedPropertyIDs.add(prop.getKey());

            result.addProperty(prop);
        }
        return result;
    }

    /**
     * Tries to create property from the given String representation. All values
     * of the created property are instances of ValueString class. When line
     * contains comma (or colon) than it is considered as a multi-value property
     * (with corresponding delimiter), otherwise the returned instance is a
     * single-value property.
     *
     * @param property String representation of the property to be parsed
     * @return instance of Property that corresponds to given String
     * representation
     */
    private static Property parseProperty(String line) throws FileFormatException, InvalidValueFormatException {
        String[] propertyParts = line.split(Constants.EQUAL_SIGN, 2);
        String propertyID = IniFileUtils.trim(propertyParts[0]);

        String propertyBody = propertyParts[1];

        String[] bodyParts = propertyBody.split(Constants.COMMENT_DELIMITER, 2);
        String valuesPart = bodyParts[0];
        String comment = bodyParts.length > 1 ? bodyParts[1] : "";
        comment = IniFileUtils.trim(comment);

        Property.ValueDelimiter valueDelimiter = null;
        Property property = null;
        if (valuesPart.length() > 0) {
            String[] values = null;

            // If we have both 'comma' and 'colon' in line, we use comma as delimiter
            if (valuesPart.matches(".*[^\\\\]" + Property.ValueDelimiter.COMMA.toString() + ".*")) {
                values = split(valuesPart, Property.ValueDelimiter.COMMA.toString().charAt(0));
                valueDelimiter = Property.ValueDelimiter.COMMA;
            } else if (valuesPart.matches(".*[^\\\\]" + Property.ValueDelimiter.COLON.toString() + ".*")) {
                values = split(valuesPart, Property.ValueDelimiter.COLON.toString().charAt(0));
                valueDelimiter = Property.ValueDelimiter.COLON;
            } else {
                values = new String[]{valuesPart};
            }

            if (values.length > 1) {
                property = new Property(propertyID, false, new ValueStringRestriction());
                property.setDelimiter(valueDelimiter);
            } else {
                property = new Property(propertyID, true, new ValueStringRestriction());
            }

            for (String value : values) {
                value = IniFileUtils.trim(value);
                try {
                    property.addValue(new ValueString(value));
                } catch (InvalidValueFormatException | TooManyValuesException | ViolatedRestrictionException e) {
                    throw new FileFormatException("Invalid file format.", e);
                }
            }

        } else {
            property = new Property(propertyID, true, new ValueStringRestriction());
        }

        property.setComment(comment);
        return property;
    }

    /**
     * This method returns a copy of the string, with leading and trailing
     * whitespace omitted. Whitespace will be saved only if it's
     * pre-backslashed.
     *
     * @param toTrim string to be trimmed.
     * @return This method returns a copy of the string, with leading and
     * trailing whitespace omitted. Whitespace will be saved only if it's
     * pre-backslashed.
     */
    public static String trim(String toTrim) {
        if (toTrim.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        int startIndex = -1;
        int endIndex = -1;
        for (int i = 0; i < toTrim.length(); i++) {
            char ch = toTrim.charAt(i);
            if (i == toTrim.length() - 1) {
                if (!Character.isWhitespace(ch)) {
                    startIndex = i;
                }
                break;
            }
            char next = toTrim.charAt(i + 1);
            if (Character.isWhitespace(ch)
                    || (ch == Constants.ESCAPE_SYMBOL && Character.isWhitespace(next))) {
                continue;
            }
            startIndex = i;
            break;
        }

        for (int i = toTrim.length() - 1; i >= 0; i--) {
            char ch = toTrim.charAt(i);
            if (Character.isWhitespace(ch)) {
                continue;
            }
            if (i == toTrim.length() - 1) {
                endIndex = i;
                break;
            }
            char next = toTrim.charAt(i + 1);
            if (ch != Constants.ESCAPE_SYMBOL || !Character.isWhitespace(next)) {
                endIndex = i;
                break;
            }
        }

        if (startIndex == -1) {
            return getSpacesToWrite(toTrim);
        }
        // Append backslashed spaces at the beginning
        result.append(getSpacesToWrite(toTrim.substring(0, startIndex)));
        // Append body
        result.append(toTrim.substring(startIndex, endIndex + 1));
        // Append backslashed spaces at the end
        result.append(getSpacesToWrite(toTrim.substring(endIndex + 1, toTrim.length())));

        return result.toString();
    }

    /**
     * Counts all escaped whitespaces and returns them in a new
     * <code>String</code>.
     *
     * @param str to be analyzed.
     * @return all escaped whitespaces in a new <code>String</code>.
     */
    protected static String getSpacesToWrite(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length() - 1; i++) {
            if ((str.charAt(i) == Constants.ESCAPE_SYMBOL) && (Character.isWhitespace(str.charAt(i + 1)))) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    /**
     * Splits given string according to given delimiter that cannot be preceded
     * by an 'unbacklashed backslash'.
     *
     * @param str string to be split
     * @param delimiter
     * @return array containing splitted parts
     */
    protected static String[] split(String str, char delimiter) {
        List<String> result = new ArrayList<>();
        StringBuilder currentString = new StringBuilder();

        boolean lastSymbolBackslash = false;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\\') {
                if (lastSymbolBackslash) {
                    // backslash is backlashed -> normal symbol backlash
                    currentString.append('\\');
                    lastSymbolBackslash = false;
                } else {
                    lastSymbolBackslash = true;
                }
            } else if (str.charAt(i) == delimiter && !lastSymbolBackslash) {
                result.add(currentString.toString());
                currentString = new StringBuilder();
            } else {
                if (lastSymbolBackslash) {
                    currentString.append("\\"); //backslash must be written back
                }
                currentString.append(str.charAt(i));
                lastSymbolBackslash = false;
            }
        }

        result.add(currentString.toString());

        return result.toArray(new String[result.size()]);
    }

    public static boolean checkIdentifierValidity(String identifier) {
        Matcher identifierMatcher = identifierPattern.matcher(identifier);

        return identifierMatcher.matches();
    }

    /**
     * Checks whether given string represents a reference to some other field.
     *
     * @param str
     * @return true, if str is a reference to other property; otherwise false.
     */
    protected static boolean isReference(String str) {
        if (str == null || str.length() < 3) {
            return false;
        }

        if (!str.startsWith("${") || !str.endsWith("}")) {
            return false;
        }

        String body = str.substring(2, str.length() - 1);
        String[] parts = body.split("#");

        if (parts.length != 2) {
            return false;
        }

        if (!checkIdentifierValidity(parts[0]) || !checkIdentifierValidity(parts[1])) {
            return false;
        }

        return true;
    }

    /**
     * Checks restriction for reference values.
     *
     * @param value reference to be checked.
     * @param restriction restriction to check against.
     * @return true if restriction is not violated.
     * @throws ViolatedRestrictionException if restriction has been violated.
     */
    public static boolean checkRestrictionRecursive(Value value, ValueRestriction restriction) throws ViolatedRestrictionException {
        if (value instanceof ValueReference) {
            List<Value> referencedValues = (List<Value>) value.get();
            for (Value referencedValue : referencedValues) {
                restriction.checkRestriction(referencedValue);
            }
            return true;
        }
        return false;
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
