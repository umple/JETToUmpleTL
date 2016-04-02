# JETToUmpleTL

This project is meant to automatically convet JET templates to their equivalent in Umple.

This converter should handle most of the conversion, although it will not handle everything. See [#Hand-Conversions]() for suggestions on how to finish a conversion by hand. Due to the minute differences between Umple and JET, it is strongly recommeneded to have a solid test suite before converting. The converter is written in a mix of Umple and Java.

For an introduction to Umple templates, see the documentation for [Umple`s templates](http://cruise.eecs.uottawa.ca/umple/BasicTemplates.html) and a guide for the differences between Umple and JET (will be added by end of term).


## Converting from JET

1. Since this project relies on Java, you will need to have Java installed.
2. Then, download the [JETToUmpleTL release](). (Will be available once the repo has been setup)
3. Then, run the converter with:

```
java -jar JETToUmpleTL.jar /path/to/JET/template/dir /path/where/Umple/templates/will/be/placed <MainClassName>
```

The MainClassName will be the base class name shared among the templates. It does not need to be the outputted name of the generated templates. (I realize this is a bit vague, will improve later)

Note that the converter will only convert files in the current directory, so if you wish to convert for multiple directores it will need to be run multiple times.


Then, (once most of my changes are implemented), the templates can be run from [Umple](UmpleRelease), by running `java -jar /path/to/Umple Master.ump`.


## Hand Conversions

There are currently quite a few conversions that are not yet handled, but should be handled by the end of the term.

Some others I do not intend on implenting, due to taking too much time. As well, there are also likely to be other differences, due to the many special cases in JET that do not exist in Umple templates.

Warning: If there is a template called "Master.ump" it will be overridden.

### Master.ump

This file will be the file that, when ran with the release version of Umple, will compile all the templates. It is created automatically. However, you will need to choose the path that the templates will be created into. Each class will be put into a subdirectory in the given path corresponding to the package.

    EG: `generate Java "../src-gen-UmpleTL"`

### `.jumpjet` files

For every .jumpjet file, will need to edit the corresponding .ump file. Most of the changes will correspond to the information in the JET header at the start of the .jumpjet file:

1. From the skeleton file, should ensure that all implements/extends are included. Most should be automatically.
  - They will be placed right before the class in the corresponding .ump file.
  - If is an interface, should be `external interface <InterfaceName> {}`
  - If is a class, should just be `external <ClassName> {}`
2. For the last function that occurs before the template, should copy its name and arguments into the following emit statement. There is a TODO to remind of this.
3. Next, you will probably need to remove the first blank line(s) that will be created by Umple
  - This is necessary since JET will remove the blank lines, while Umple will not specially handle them
  - The easiest way to do this is:
    1. Go to the template (will start with `<<!` in the .ump file)
    2. Find the first line that does not start with a `<<`, in some form. If you encounter a `<<@`, will need to go into the .ump file with that name, and look through its template.
    3. Then, remove the newline for it

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

The easiest way to be able to change the code for this is to use Eclipse, after installing the [Umple Development Setup](https://github.com/umple/umple/wiki/DevelopmentSetUp) and the [Umple Eclipse Plugin](https://github.com/umple/umple/wiki/InstallEclipsePlugin).

All issues should be reported to the main [umple repository](https://github.com/umple/Umple).


### Tests

This converter currently has a relatively large test suite already. To view them, see the TestSource folder. In the base folders, are the tests that I wrote to check specific cases. Then, there is the folder `JavaTemplates`, `PhpTemplates`, and `RubyTemplates`, which are 'mass' tests ensruing that the conversion for the original Umple JET templates works. 

To run the tests, execute the file MainTest.java



[UmpleRelease]: https://github.com/umple/Umple/releases
