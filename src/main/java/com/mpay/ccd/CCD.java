package com.mpay.ccd;

import com.mpay.ccd.utils.IOUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

/**
 * The Class CCD.
 */
@SpringBootApplication
@EnableScheduling
public class CCD {

	private static ConfigurableApplicationContext context;
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		IOUtils.initDirectories();
		context = SpringApplication.run(CCD.class, args);
	}
	public static void restart() {
		ApplicationArguments args = context.getBean(ApplicationArguments.class);

		Thread thread = new Thread(() -> {
			context.close();
			context = SpringApplication.run(CCD.class, args.getSourceArgs());
		});

		thread.setDaemon(false);
		thread.start();
	}
}
