package com.mpay.ccd.controller;

import com.mpay.ccd.CCD;
import com.mpay.ccd.service.ConfigService;
import com.mpay.ccd.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The Class AdminController.
 */
@Controller
public class AdminController {
	
	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(getClass()); 

	/** The port. */
	@Value("${server.port}")
	private int port;
	
	/** The looping time. */
	@Value("${time}")
	private long loopingTime;

	/** The sender. */
	@Autowired
	private EmailService sender;

	/**
	 * Index.
	 *
	 * @param map the map
	 * @return the string
	 */
	@GetMapping(value={"","/admin"})
	public String index(ModelMap map) {
		map.addAttribute("config", ConfigService.getConfig());
		map.addAttribute("port", port);
		map.addAttribute("loopingTime", loopingTime);
		return "admin";
	}
	
	/**
	 * Restart.
	 *
	 * @return the string
	 */
	@GetMapping(value= {"/restart"})
	public @ResponseBody String restart() {
	  logger.info("Do restarting");
		CCD.restart();
		return "OK";
	}
	
	/**
	 * Check running.
	 *
	 * @return the string
	 */
	@GetMapping(value= {"/check-running"})
	public @ResponseBody String checkRunning() {
		return "The application is running";
	}


	/**
	 * Check sending mail.
	 *
	 * @return the string
	 */
	@GetMapping("/check-sending-email") 
	public @ResponseBody String checkSendingMail() {
		sender.sendSimpleMessage("Test sending email from CCD", "This is the content of mail sent to you to test check sending mail");
		return "Sending email finishes";
	}
}
