package cruise.umple.fromjet;

import static org.junit.Assert.*;

import cruise.umple.util.SampleFileWriter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MainTest {
	private static final String testsBase = "test";
	private static final Path originalPath = Paths.get(testsBase, "JET_original");
	private static final Path convertedPath = Paths.get(testsBase, "JET_converted");
	private static final Path expectedPath = Paths.get(testsBase, "Umple_expected");
	
	private static final String phpTestFolder = "Php";
	private static final String javaTestFolder = "Java";
	private static final String rubyTestFolder = "Ruby";

	@BeforeClass
	public static void convertAll() {
		deleteFilesInDirectory(convertedPath);
		
		// Convert the base files
		DirectoryConverter converter = new DirectoryConverter(originalPath, "UnitTest");
		converter.convertDirectory(convertedPath);
	}
	
	private Path currentConvertedPath;
	private Path currentExpectedPath;
	
	@Before
	public void setupBasePath() {
		currentConvertedPath = convertedPath;
		currentExpectedPath = expectedPath;
	}
	
	// The following tests are special tests for specific setups
	@Test
	public void testTemplateIgnoreFinalBlankLine() {
		checkTemplate("constructor_StateMachineAssignDefault.ump");
	}
	
	@Test
	public void testTemplateConvertCodeStatements() {
		checkTemplate("members_AllHelpers.ump");
	}
	
	@Test
	public void testTemplateEndingBrace() {
		checkTemplate("attribute_Set_subclass.ump");
	}
	
	@Test
	public void testTemplateBracketFollowedByCode() {
		checkTemplate("braceFollowedByCode.ump");
	}
	
	@Test
	public void testTemplateCodeFollowedBySpaceThenText() {
		checkTemplate("association_GetOne.ump");
	}
	
	@Test
	public void testImmediateTemplateCodeWithSpace() {
		checkTemplate("association_SetUnidirectionalOptionalOne.ump");
	}
	
	@Test
	public void testTemplateCodeOrDirectiveToCode() {
		// This checks if the template will add an extra space between them
		checkTemplate("phpClassGenerator.ump");
		checkTemplate("JavaClassGenerator.ump");
	}
	
	// Mass tests
	@Test
	public void testTemplatesPhp() {
		testAllInFolder(phpTestFolder);
	}
	
	@Test
	public void testTemplatesJava() {
		testAllInFolder(javaTestFolder);
	}
	
	// Mass tests
	@Test
	public void testTemplatesRuby() {
		testAllInFolder(rubyTestFolder);
	}
	
	private void testAllInFolder(String folder) {
		currentConvertedPath = convertedPath.resolve(folder);
		currentExpectedPath = expectedPath.resolve(folder);
		
		deleteFilesInDirectory(currentConvertedPath);
		
		// Convert the folder
		DirectoryConverter converter = new DirectoryConverter(originalPath.resolve(folder), "UnitTest");
		converter.convertDirectory(currentConvertedPath);
		
		Set<String> templatesExpected = new HashSet<>();
		
		// Iterate through each file in expected directory
		File dir = currentExpectedPath.toFile();
		File[] directoryListing = dir.listFiles();
	  	if (directoryListing != null) {
	    	for (File child : directoryListing) {
	    		templatesExpected.add(child.getName());
	    		checkTemplate(child.getName());
	    	}
	    } else {
	    	fail("Directory should not be empty");
	    }
		
	  	// Ensure each file in the created directory was expected
	  	dir = currentConvertedPath.toFile();
		directoryListing = dir.listFiles();
	  	if (directoryListing != null) {
	    	for (File child : directoryListing) {
	    		assertTrue("Extra file " + child.getName(), templatesExpected.contains(child.getName()));
	    	}
	    } else {
	    	fail("Directory " + folder + " should exist");
	    }
	}
		
	private static void deleteFilesInDirectory(Path directory) {
		File baseDir = directory.toFile();
		File[] directoryListing = baseDir.listFiles();
		if (directoryListing != null) {
			for (File file: directoryListing)
				file.delete();
		}
	}

	private void checkTemplate(String templateName) {
		File actual = new File(currentConvertedPath.resolve(templateName).toString());
		File expected = new File(currentExpectedPath.resolve(templateName).toString());
		
		SampleFileWriter.assertFileContent(expected, actual, false);
	}
}
