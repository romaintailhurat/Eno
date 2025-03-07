package fr.insee.eno.test;

import java.io.File;

import javax.xml.transform.stream.StreamSource;

import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.Diff;
import org.xmlunit.input.CommentLessSource;

/**
 * Created by I6VWID on 15/01/18.
 */
public class XMLDiff { 

    public XMLDiff() {
        
    }
      
    public Diff getDiff(File input, File expected) throws Exception {
        System.out.println(String.format("Diff  %s with %s", input.getAbsolutePath(), expected.getAbsolutePath()));
        CommentLessSource inputStream = null;
        CommentLessSource expectedStream = null; 
        try {
        	inputStream = new CommentLessSource(new StreamSource(input));
        	expectedStream = new CommentLessSource(new StreamSource(expected));
        	
            return DiffBuilder
                    .compare(expectedStream)
                    .withAttributeFilter(attr -> !attr.getName().equals("enoCoreVersion"))
                    .withTest(inputStream)
                    .ignoreWhitespace()
                    .normalizeWhitespace()
                    .checkForIdentical()
                    .build();
        } catch (Exception e) {
            throw e;
        } 
    }

    public Diff getDiff(String inputFilePath, String expectedFilePath) throws Exception {
        File inputFile = new File(inputFilePath);
        File expectedFile = new File(expectedFilePath);
        try {
            return getDiff(inputFile, expectedFile);
        } catch (Exception e) {
            throw e;
        }
    }

}
