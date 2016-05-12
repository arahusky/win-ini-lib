# Win-ini-lib
This project provides simple API for handling Windows .ini configuration files. 

## Windows .ini file format 
Windows .ini files serve for saving programs' configuration. They consist of sections, which are blocks of related properties.
These properties are key-value pairs, with values of different type and arity. 
The values can be further restricted (e.g. it can only contain positive numbers), can contain comments, etc.
For bigger overview, we recommend you visiting <a href="https://en.wikipedia.org/wiki/INI_file" target="_blank">Wikipedia</a>.

## Project overview 
The project consists of 4 packages. All of them are prefixed with <i>cz.cuni.mff.d3s.pp.wininilib</i>.
The main package contains most important classes. These are:                    
  <ul>
    <li>IniFile: serves for specifying new format, loading existing configuration, accessing its internals as well as saving it back. </lI>
    <li>Section: represents one section and provides methods to work with its properties</li>
    <li>Property: represents one property and provides methods to work with its key and values</li>
    <li>Value: interface for all possible property values</li>
    <li>ValueRestriction: interface for Value restrictions</li>
  </ul>       
                    
Package <i>values</i> contains several implementations of <i>Value</i> interface (such as value for string, float or boolean).
Package <i>values.restrictions</i> then contains implementations of <i>ValueRestriction</i> interface; therefore classes, which check the validity of a specific value.
Finally, the last package <i>exceptions</i> contains all exceptions that are thrown by the library code.  

## Demo examples 
The most common tasks, user will use this library for are: 

###  Define own format of .ini file
```
IniFile format = new IniFile(); //create new format
Section section1 = new Section("Section1", true); //create new required section named 'Section1'
Property property1 = new Property("Option1", true, new ValueStringRestriction()); //create new required property named 'Option1' with string type
property1.addValue(new ValueString("value1")); //add new string value to defined property
section1.addProperty(property1);
format.addSection(section1);
```

###  Load .ini file, check it against specified format, modify it and save back
```
IniFile ini = loadFormat(); //load already defined format 
try {       
    ini.loadDataFromFile(fileName, LoadType.RELAXED); //try to fill in the ini format file with the configuration
    System.out.println("The file meets the specified format."); 
    Section sectionToBeModified = ini.getSection("Section1"); //we will modify section named 'Section1' 

    Property prop = sectionToBeModified.getProperty("Option1"); 
    prop.addValue(new ValueSigned(123)); //add new signed value to the property

    ini.saveToFile(fileName, IniFile.SaveType.FULL); // save the modified configuration back to file
} catch (FileFormatException ex) {  
    System.err.println("The file did not meet specified format."); 
} 
```

###  Load .ini file in a static-mode

<i>While loading in a static-mode, no ini-file format needs to be predefined, but it is derived from the configuration (note that all the property values are in this case strings). </i>

```
try {
    IniFile ini = IniFile.loadIniFromFile(fileName);
    System.out.println(ini.toString(IniFile.SavingMode.FULL));
} catch (FileFormatException ex) {
    System.err.println("This file is not valid ini file.");
}
```

To get more familiar with the library, we suggest you to inspect sample demo code, which is located in ExampleUsage java file. 

## Compilation

To compile the library, Java SDK >= 1.7.0. together with Apache Ant is required. Then these Ant tasks are available:
<ul>
  <li>compile: compiles all sources (into out/classes folder )</li>
  <li>refdoc: creates project documentation (in out/javadoc)</li>
  <li>create-jar: creates jar file of the library (out/jars)</li>
  <li>main: performs all previous tasks</li>
</ul>
               
               
