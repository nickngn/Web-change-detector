package com.nickng.ccd.controller;

import com.nickng.ccd.CCD;
import com.nickng.ccd.service.ConfigService;
import com.nickng.ccd.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The Class AdminController.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminController {

	/** The port. */
	@Value("${server.port}")
	private int port;
	
	/** The looping time. */
	@Value("${time}")
	private long loopingTime;

	/** The sender. */
	private final EmailService sender;

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
	  log.info("Do restarting");
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
