package com.mpay.ccd.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mpay.ccd.utils.IOUtils;

@Service
public class ContentService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private final static String STORE_DIRECTORY = "website_content";
	private final static String OLD_VERSION_DIR = "old_versions";
	private final static String DIFFERENCES_DIRECTORY = "differences";
	private final static String LOG_DIRECTORY = "log";
	
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hhmmss_yyyyMMdd");

	public static void initDirectories () throws IOException {
		createDirIfNotExist(Paths.get(STORE_DIRECTORY));
		createDirIfNotExist(Paths.get(OLD_VERSION_DIR));
		createDirIfNotExist(Paths.get(DIFFERENCES_DIRECTORY));
		createDirIfNotExist(Paths.get(LOG_DIRECTORY));
	}
	
	public static void createDirIfNotExist(Path path) throws IOException {
		if (path == null) return;
		
		if (!path.toFile().exists()) {			
			Files.createDirectory(path);
		}
	}
	
	/**
	 * Lay file duoc luu lai tu lan thay doi truoc
	 * @param title
	 * @return content of file; if file is not exist return "FILE_NEVER_STORED_BEFORE"
	 * 
	 * @throws IOException
	 */
	public String getLastSavedContentOfTitle(String title) throws IOException {
		String link = IOUtils.getFilePath("html", STORE_DIRECTORY, title);
		File file = new File(link);
		if (file.exists()) {			
			return readFileAsString(link);
		} else {
			return "FILE_NEVER_STORED_BEFORE";
		}
	}
	
	private String readFileAsString(String fileName) throws IOException{
	    String data = "";
	    data = new String(Files.readAllBytes(Paths.get(fileName)));
	    return data;
	}
	
	/**
	 * Get difference of 2 String
	 * @param oldContent
	 * @param newContent
	 * @return if it is all same totally, return "NO_DIFFERENCE"
	 */
	public String getDifferent(String str1, String str2) {
		if ((str1 == null && str2 == null) || (str1 != null && str1.equals(str2))) {
			return "NO_DIFFERENCE";
		}
		List<String> htmlSourceTags = Arrays.asList(str1.split("\n"));
		List<String> htmlNewTags = Arrays.asList(str2.split("\n"));
		List<String> diff = CompareService.diff(htmlSourceTags, htmlNewTags);
		
		if (diff.isEmpty()) {
			return "NO_DIFFERENCE";
		}
		
		return StringUtils.collectionToDelimitedString(diff, "");
	}
	
	/**
	 * Chuyen file sang thu muc version cu
	 * @param title
	 * @throws IOException
	 */
	public void storeFileAsOldVersion(String title) throws IOException {
		try {
			String link = IOUtils.getFilePath("html", OLD_VERSION_DIR, title);
			File file = new File(link);
			if (file.exists()) {
				String currentTime = simpleDateFormat.format(new Date());
				String toFileName = title + "_" + currentTime;
				String targetlink = IOUtils.getFilePath("html", OLD_VERSION_DIR, toFileName);
				Path sourcePath = FileSystems.getDefault().getPath(link);
				Path targetPath = FileSystems.getDefault().getPath(targetlink);
				Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			throw new IOException("Khong lay duoc duong dan cua file " + title);
		}
	}
	
	public void storeFileAsLatest(String title, String content) {
		String link;
		try {
			link = IOUtils.getFilePath("html", STORE_DIRECTORY, title);
			Path path = Paths.get(link);
			Files.deleteIfExists(path);
			Files.createFile(path);
			Files.write(path, content.getBytes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
		} catch (IOException e) {
			logger.error("Khong cap nhat duoc file. {}/{}", STORE_DIRECTORY, title, e);
		}
	}
	
	public Path storeFileAsDifference(String title, String content) {
		String link;
		try {
			String currentTime = simpleDateFormat.format(new Date());
			link = IOUtils.getFilePath("html", DIFFERENCES_DIRECTORY, title + "_" + currentTime);
			Path path = Paths.get(link);
			Files.createFile(path);
			Files.write(path, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
			return path;
		} catch (IOException e) {
			logger.error("Khong cap nhat duoc file. {}/{}", DIFFERENCES_DIRECTORY, title, e);
		}
		return null;
	}
	
	
	public static void main(String[] args) throws IOException {
		Path path = Paths.get("ahihi");
		if (!path.toFile().exists()) {			
			Files.createDirectory(path);
		}
	}
}
