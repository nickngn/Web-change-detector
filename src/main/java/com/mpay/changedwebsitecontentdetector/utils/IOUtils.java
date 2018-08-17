package com.mpay.changedwebsitecontentdetector.utils;

import java.io.File;
import java.io.IOException;

public class IOUtils {

	public static String getFilePath(String extension, String... dirs) throws IOException {
		try {			
			String path = new File(".").getCanonicalPath();
			String linkToFile = path;
			for (String dir : dirs) {
				linkToFile += File.separator + dir;
			}
			linkToFile += "." + extension;
			
			return linkToFile;
		} catch (Exception e) {
			throw new IOException("Khong lay duoc duong dan.");
		}
	}
}
