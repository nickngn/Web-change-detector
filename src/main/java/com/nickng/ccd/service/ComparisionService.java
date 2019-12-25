package com.nickng.ccd.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * The Class ComparisionService.
 */
@Service
public class ComparisionService {
	
	/** The Constant ADD_COMMENT_OPEN. */
	private static final String ADD_COMMENT_OPEN = 
			  "\n<!---------        ADD OPEN        ------------------------------>"
			+ "\n<div style='color: green'>";
	
	/** The Constant ADD_COMMENT_CLOSE. */
	private static final String ADD_COMMENT_CLOSE = 
			  "\n</div>"
			+ "\n<!---------        ADD CLOSE        ------------------------------>";
	
	/** The Constant DELETE_COMMENT_OPEN. */
	private static final String DELETE_COMMENT_OPEN = 
			  "\n<!---------        DELETE OPEN         ------------------------------>"
			+ "\n<div style='text-decoration: line-through;'>";
	
	/** The Constant DELETE_COMMENT_CLOSE. */
	private static final String DELETE_COMMENT_CLOSE = 
			  "\n</div>" 
			+ "\n<!---------        DELETE CLOSE         ------------------------------>";

	private static final String EMPTY = "";

	/**
	 * Get differences of one Text from one other Text.
	 *
	 * @param template the source
	 * @param comparedText the received content
	 * @return NO_DIFFERENCE if it is all same totally
	 * @return
	 */
	public String getDifference(final String template, final String comparedText) {
		if ((template == null && comparedText == null)
					|| (template != null && template.equals(comparedText))) {
			return EMPTY;
		}
		
		if (template == null) {
			return new StringBuilder()
					.append(ADD_COMMENT_OPEN)
					.append(comparedText)
					.append(ADD_COMMENT_CLOSE).toString();
		}
		
		List<String> templateLines = new ArrayList<>(Arrays.asList(template.split("\n")));
		List<String> comparedTextLines = new ArrayList<>(Arrays.asList(comparedText.split("\n")));

		removeEmptyLines(templateLines);
		removeEmptyLines(comparedTextLines);
	    removeTokenInputs(templateLines);
	    removeTokenInputs(comparedTextLines);
	    
		List<String> diffs = findDiff(templateLines, comparedTextLines);
		
		if (diffs.isEmpty()) {
			return EMPTY;
		}
		
		return StringUtils.collectionToDelimitedString(diffs, "");
	}
	
    /**
     * Find out different Strings between 2 List of Strings <br/>
     * @see <a href="https://rosettacode.org/wiki/Longest_common_subsequence#Java">Find Longest Increasement String</a>
     * @param sourcesTags the sources tags
     * @param newTags the new tags
     * @return List of not same Strings between 2 List
     */
	private List<String> findDiff(List<String> sourcesTags, List<String> newTags) {
	    boolean detectDifference = false;
	    int[][] compareResult = new int[sourcesTags.size()+1][newTags.size()+1];
	 
	    // row 0 and column 0 are initialized to 0 already
	    // init the matrix checking for differences
	    for (int i = 0; i < sourcesTags.size(); i++) {	    	
	    	for (int j = 0; j < newTags.size(); j++) {	    		
	    		if (sourcesTags.get(i).equals(newTags.get(j))) {	    			
	    			compareResult[i+1][j+1] = compareResult[i][j] + 1;
	    		} else {	    			
	    			compareResult[i+1][j+1] = Math.max(compareResult[i+1][j], compareResult[i][j+1]);
	    		}
	    	}
	    }
	 
	    // read the substring out from the matrix
	    LinkedList<String> diffs = new LinkedList<>(newTags);
	    int x = sourcesTags.size();
	    int y = newTags.size();
	    while (x != 0 && y != 0) {
	        if (compareResult[x][y] == compareResult[x-1][y]) {
	        	x--;
	        	diffs.add(y, DELETE_COMMENT_OPEN + sourcesTags.get(x) + DELETE_COMMENT_CLOSE);
    			detectDifference = true;
	        } else if (compareResult[x][y] == compareResult[x][y-1]) {
	        	y--;
	        	diffs.set(y, ADD_COMMENT_OPEN + newTags.get(y) + ADD_COMMENT_CLOSE);
    			detectDifference = true;
	        } else {
	        	x--;
	        	y--;
	        }
	    }
	    
	    if (!detectDifference) {
	    	return new LinkedList<>();
	    }
	    return diffs;
	}
	
	/**
	 * Removes the empty lines.
	 *
	 * @param lines the lines
	 * @return the list
	 */
	private static List<String> removeEmptyLines(List<String> lines) {
		Predicate<String> isEmptyLine = line -> ((line != null) && (line.equals("\r") || line.equals("")));
		
		lines.removeIf(isEmptyLine);
		return lines;
	}
	
	/**
	 * Removes the token inputs.
	 *
	 * @param lines the lines
	 * @return the list
	 */
	private static List<String> removeTokenInputs(List<String> lines) {
		Predicate<String> isTokenLine = 
				line -> ((line != null) 
						&& line.contains("<input ") 
						&& (line.contains("type=\"hidden\"") || line.contains("type='hidden'")));
		
		lines.removeIf(isTokenLine);
		return lines;
		
	}
	
}
