package com.mpay.ccd.service;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class CompareService {
	
	private final static String ADD_COMMENT_OPEN = 
			  "\n<!---------        ADD OPEN        ------------------------------>"
			+ "\n<div style='color: green'>";
	
	private final static String ADD_COMMENT_CLOSE = 
			  "\n</div>"
			+ "\n<!---------        ADD CLOSE        ------------------------------>";
	
	private final static String DELETE_COMMENT_OPEN = 
			  "\n<!---------        DELETE OPEN         ------------------------------>"
			+ "\n<div style='text-decoration: line-through;'>";
	
	private final static String DELETE_COMMENT_CLOSE = 
			  "\n</div>" 
			+ "\n<!---------        DELETE CLOSE         ------------------------------>";

    /**
     * Tim ra nhung element khac nhau giua 2 List. 
     * Xem them tai <a>https://rosettacode.org/wiki/Longest_common_subsequence#Java</a>
     * @param sourcesTags
     * @param newTags
     * @return 
     * 
     */
	public static List<String> diff(List<String> sourcesTags, List<String> newTags) {
	    int[][] lengths = new int[sourcesTags.size()+1][newTags.size()+1];
	    
//	    removeEmptyLines(sourcesTags);
//	    removeEmptyLines(newTags);
	 
	    // row 0 and column 0 are initialized to 0 already
	    // init the matrix checking for differences
	    for (int i = 0; i < sourcesTags.size(); i++)
	        for (int j = 0; j < newTags.size(); j++)
	            if (sourcesTags.get(i).equals(newTags.get(j)))
	                lengths[i+1][j+1] = lengths[i][j] + 1;
	            else
	                lengths[i+1][j+1] = Math.max(lengths[i+1][j], lengths[i][j+1]);
	 
	    // read the substring out from the matrix
	    LinkedList<String> diffs = new LinkedList<>(newTags);
	    for (int x = sourcesTags.size(), y = newTags.size(); x != 0 && y != 0; ) {
	        if (lengths[x][y] == lengths[x-1][y]) {
	        	x--;
	        	diffs.add(y, DELETE_COMMENT_OPEN + sourcesTags.get(x) + DELETE_COMMENT_CLOSE);
	        }
	        else if (lengths[x][y] == lengths[x][y-1]) {
	        	y--;
	        	diffs.set(y, ADD_COMMENT_OPEN + newTags.get(y) + ADD_COMMENT_CLOSE);
	        } else {
	        	x--;
	        	y--;
	        }
	    }
	    
	    removeTokenInputs(diffs);
	    
	    return diffs;
	}
	
//	private static void removeEmptyLines(List<String> lines) {
//	    Predicate<String> emptyLinefilter = new Predicate<String>() {
//			
//			@Override
//			public boolean test(String t) {
//				if (t == null) return false;
//				return t.equals("\r") || t.equals("");
//			}
//		};
//		
//		lines.removeIf(emptyLinefilter);
//	}
	
	private static void removeTokenInputs(List<String> lines) {
		// make filter check for input token
	    Predicate<String> filter = new Predicate<String>() {
			
			@Override
			public boolean test(String t) {
				if (t == null) return false;
				t = t.toLowerCase();
				return t.contains("token") && t.contains("<input ") && t.contains("type=hidden");
			}
		};
		
		lines.removeIf(filter);
	}
}
