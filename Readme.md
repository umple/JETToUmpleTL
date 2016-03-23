# JETToUmpleTL

This project is meant to automatically convet JET templates to their equivalent in Umple.

This converter should handle around 95% of the conversion, although it will not handle everything. See (#Hand-Conversions) for suggestions on how to finish a conversion by hand. Due to the minute differences between Umple and JET, it is strongly recommeneded to have a solid test suite before converting. The converter is written in a mix of Umple and Java.

For an introduction to Umple templates, see the documentation for [Umple`s templates](http://cruise.eecs.uottawa.ca/umple/BasicTemplates.html) and a guide for the differences between Umple and JET (will be added by end of term).


## Converting from JET

1) Since this project relies on Java, you will need to have Java installed.

2) Then, download the [JETToUmpleTL release](). (Will be available once the repo has been setup)

3) Then, run the converter with:

```
java -jar JETToUmpleTL.jar /path/to/JET/template/dir /path/where/Umple/templates/will/be/placed <MainClassName>
```

The MainClassName will be the base class name shared among the templates. It does not need to be the outputted name of the generated templates. (I realize this is a bit vague, will improve later)

Note that the converter will only convert files in the current directory, so if you wish to convert for multiple directores it will need to be run multiple times.


Then, (once most of my changes are implemented), the templates can be run from [Umple](UmpleRelease), by running `java -jar /path/to/Umple Master.ump`.


## Hand Conversions

There are currently quite a few conversions that are not yet handled, but should be handled by the end of the term.

Some others I do not intend on implenting, due to taking too much time. As well, there are also likely to be other differences, due to the many special cases in JET that do not exist in Umple templates.

### Master.ump

This file will be the file that, when ran with the release version of Umple, will compile all the templates. It will be created automatically.

Until it is created automatically, hoewver, first create the file `Master.ump`. In it, add `generate Java "<directory to generate into>"`
    EG: `generate Java "../src-gen-UmpleTL"`

### `.jumpjet` files

For every .jumpjet file, will need to edit the corresponding .ump file. Most of the changes will correspond to the information in the JET header at the start of the .jumpjet file:

1) From the file corresponding to the `skeleton` argument, copy all of the code for it into the class.
    - Copy anything before the class declaration in the skeleton to before the declaration in the generated code (will be done automatically)
    - Copy everything inside of the class declaration in the skeleton to inside the class declaration. Do NOT overwrite the code that was placed in the template (will be done automatically)
2) Change the class name to be the value of the `class` argument in the header. (will be done automatically)
3) Add all of the `imports` inside the class, with the form `depend <import>`, one import per line. (will be done automatically)
4) If you have a `package` argument, write out `namespace <package-value>` before the class. (will be done automatically)
5) Handle any implements/exends -> isA <classname>. Note that will not handle the external issue. Will NOT be implemented
6) For the last function that you had copied from the `skeleton`, need to edit it. Change to emit <functionname>(arguments)(templateName).
    - (Will be done semi-automatically, with there being a template for the emit statement, with the templateName provided)
7) Add the line `use <jumpjet file name>.ump` to Master.ump, after the `generate Java` line. (Will be done automatically)
8) Next, you will probably need to remove the first blank line(s) that will be created by Umple
    - This is necessary since JET will remove the blank lines, while Umple will not specially handle them
    - The easiest way to do this is:
        1) Go to the template (will start with `<<!` in the .ump file)
        2) Find the first line that does not start with a <<, in some form. If you encounter a `<<@`, will need to go into the .ump file with that name, and look through its template.
        3) Then, remove the newline for it
    - This will NOT be handled specially, but I will try to create some examples of how to do this later


Some of the above may also appy to `.jet` templates that have a `class` argument in the header. If so, this will also be handled automatically.

### Additional recommendations

Will write these out as I come up with them. Most of our templates converted fine, but there may be difficulties for others.

- For some more advanced templates, they may define multiple functions in a single template. In this case, the first return will need to be changed from `return realSb.toString();` to `return realSb;`.
    - As well, the function that is meant to match the template will need to be changed as well. Here is an example, assuming it is never called:
```
/* This function is meant purely to properly end the emit code in Umple
   Should never be called
   Only purpose is to make sure the 'if (numSpaces > 0)' part of the template
   Doesn`t cause errors */
private StringBuilder endTemplate()
{
  GeneratorHelper.generator = null;
  int numSpaces = 0;
  StringBuilder newCode = null;
  StringBuilder sb = null;
  String spaces = "";

```
    - Finally, will need to change any references to StringBuffer in the function calls to StringBuilder.

- Currently, any references to `.jet` in the actual templates will be ignored (like determining which of the other templates should be used). I will likely add an option to convert them to be '.ump' in thefuture.

## Recompile JETToUmple

To be able to compile the converter into Java, you will need to have an [Umple distribution installed](UmpleRelease).

To compile the converter, go into the src folder, then run:

```
java -jar path/to/umple.jar -g Java Main.ump
```

It will be compiled into `cruise/umple/fromjet`

## Development

The easiest way to be able to change the code for this is to use Eclipse, after installing the [Umple Development Setup](https://github.com/umple/umple/wiki/DevelopmentSetUp) and the [Umple Eclipse Plugin](https://github.com/umple/umple/wiki/InstallEclipsePlugin)


### Tests

This converter currently has a relatively large test suite already. To view them, see the TestSource folder. In the base folders, are the tests that I wrote to check specific cases. Then, there is the folder `JavaTemplates`, `PhpTemplates`, and `RubyTemplates`, which are 'mass' tests ensruing that the conversion for the original Umple JET templates works. 

To run the tests, execute the file MainTest.java



[UmpleRelease]: https://github.com/umple/Umple/releases
