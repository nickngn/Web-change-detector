package com.projects.ccd.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

	

	/**
	 * Get difference of str2 from str1.
	 *
	 * @param source the source
	 * @param receivedContent the received content
	 * @return if it is all same totally, return "NO_DIFFERENCE"
	 */
	public String getDifference(String source, String receivedContent) {
		if ((source != null && source.equals(receivedContent)) || (source == null && receivedContent == null)) {
			return "";
		}
		
		if (source == null) {
			return ADD_COMMENT_OPEN +
					receivedContent +
					ADD_COMMENT_CLOSE;
		}
		
		List<String> htmlSourceTags = new ArrayList<>(Arrays.asList(source.split("\n")));
		List<String> htmlNewTags = new ArrayList<>(Arrays.asList(receivedContent.split("\n")));

		removeEmptyLines(htmlSourceTags);
		removeEmptyLines(htmlNewTags);
	    removeTokenInputs(htmlSourceTags);
	    removeTokenInputs(htmlNewTags);
	    
		List<String> diff = diff(htmlSourceTags, htmlNewTags);
		
		if (diff.isEmpty()) {
			return "";
		}
		
		return StringUtils.collectionToDelimitedString(diff, "");
	}
	
    /**
     * Tim ra nhung element khac nhau giua 2 List. 
     * Xem them tai <a>https://rosettacode.org/wiki/Longest_common_subsequence#Java</a>
     *
     * @param sourcesTags the sources tags
     * @param newTags the new tags
     * @return the list
     */
	private List<String> diff(List<String> sourcesTags, List<String> newTags) {
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
	 */
	private static void removeEmptyLines(List<String> lines) {
		Predicate<String> isEmptyLine = line -> ((line != null) && (line.equals("\r") || line.equals("")));
		
		lines.removeIf(isEmptyLine);
	}
	
	/**
	 * Removes the token inputs.
	 *
	 * @param lines the lines
	 */
	private static void removeTokenInputs(List<String> lines) {
		Predicate<String> isTokenLine = 
				line -> ((line != null) 
						&& line.contains("<input ") 
						&& (line.contains("type=\"hidden\"") || line.contains("type='hidden'")));
		
		lines.removeIf(isTokenLine);

	}
	
}
