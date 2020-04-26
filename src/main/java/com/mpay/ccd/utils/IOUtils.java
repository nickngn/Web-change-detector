package com.mpay.ccd.utils;

import com.mpay.ccd.exception.FileNotExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * The Class IOUtils.
 */
@Service
public class IOUtils {
	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger("IOUtils"); 

	/** The Constant I. */
	private static final String I = File.separator;
	
	/** The Constant STORE_DIRECTORY. */
	private static final String STORE_DIRECTORY = "website_content";
	
	/** The Constant OLD_VERSION_DIR. */
	private static final String OLD_VERSION_DIR = "old_versions";
	
	/** The Constant DIFFERENCES_DIRECTORY. */
	private static final String DIFFERENCES_DIRECTORY = "differences";
	
	/** The Constant LOG_DIRECTORY. */
	private static final String LOG_DIRECTORY = "log";

	/**
	 * Gets the file path.
	 *
	 * @param extension the extension
	 * @param dirs the dirs
	 * @return the file path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String getFilePath(String extension, String... dirs) throws IOException {
		try {			
			String path = new File(".").getCanonicalPath();
			StringBuilder linkToFile = new StringBuilder(path);
			for (String dir : dirs) {
				linkToFile.append(I).append(dir);
			}
			linkToFile.append(".").append(extension);
			
			return linkToFile.toString();
		} catch (Exception e) {
			throw new IOException("Khong lay duoc duong dan.");
		}
	}
	
	/**
	 * Gets the config file path.
	 *
	 * @return the config file path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String getConfigFilePath() throws IOException {
		String path = new File(".").getCanonicalPath();
		return path + I + "config" + I + "config.json";
	}
	
	/**
	 * Save property.
	 *
	 * @param property the property
	 * @param value the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void saveProperty(String property, String value) throws IOException {
		String path = new File(".").getCanonicalPath() + I + "config" + I + "application.properties";
		FileInputStream fos = new FileInputStream(path);
		Properties props = new Properties();
		props.load(fos);
		props.setProperty(property, value);
		props.store(new FileOutputStream(path), "Property: " + property);
		fos.close();
	}
	
	
	/** The simple date format. */
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hhmmss_yyyyMMdd");

	/**
	 * Inits the directories.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void initDirectories () throws IOException {
		createDirIfNotExist(Paths.get(STORE_DIRECTORY));
		createDirIfNotExist(Paths.get(OLD_VERSION_DIR));
		createDirIfNotExist(Paths.get(DIFFERENCES_DIRECTORY));
		createDirIfNotExist(Paths.get(LOG_DIRECTORY));
	}
	
	/**
	 * Creates the dir if not exist.
	 *
	 * @param path the path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void createDirIfNotExist(Path path) throws IOException {
		if (path == null) return;
		
		if (!path.toFile().exists()) {			
			Files.createDirectory(path);
		}
	}

	/**
	 * Lay file duoc luu lai tu lan thay doi truoc.
	 *
	 * @param title the title
	 * @return content of file; if file is not exist return "FILE_NEVER_STORED_BEFORE"
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws FileNotExistException file not found
	 */
	public String getLatestFileContent(String title) throws IOException, FileNotExistException {
		String link = IOUtils.getFilePath("html", STORE_DIRECTORY, title);
		File file = new File(link);
		if (!file.exists()) {
		  throw new FileNotExistException("Khong tim thay file: " + link);
		}
		try {
			return readFile(link);
		} catch (IOException e) {
			throw new IOException("Khong doc duoc noi dung file " + STORE_DIRECTORY + "/" + title + ".html");
		}
	}

	/**
	 * Read file as string.
	 *
	 * @param fileName the file name
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private String readFile(String fileName) throws IOException{
	    String data = "";
	    data = new String(Files.readAllBytes(Paths.get(fileName)));
	    return data;
	}

	/**
	 * Chuyen file sang thu muc version cu.
	 *
	 * @param title the title
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void storeFileAsOldVersion(String title) throws IOException {
		String currentPath = IOUtils.getFilePath("html", STORE_DIRECTORY, title);
		File file = new File(currentPath);
		if (file.exists()) {
			String currentTime = simpleDateFormat.format(new Date());
			String toFileName = title + "_" + currentTime;
			String targetlink = IOUtils.getFilePath("html", OLD_VERSION_DIR, toFileName);
			Path sourcePath = FileSystems.getDefault().getPath(currentPath);
			Path targetPath = FileSystems.getDefault().getPath(targetlink);
			Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
		}
	}
	
	/**
	 * Store file as latest.
	 *
	 * @param title the title
	 * @param content the content
	 */
	public void storeFileAsLatest(String title, String content) {
		if (title == null) {return;}
		if (content == null) {content = "";}  
		
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
	
	/**
	 * Store file as difference.
	 *
	 * @param title the title
	 * @param content the content
	 * @return the path
	 */
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
	
}
