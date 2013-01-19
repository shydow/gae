package com.tangpian.sna.web.controller;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tangpian.sna.model.Post;
import com.tangpian.sna.model.User;
import com.tangpian.sna.service.AnalysisService;
import com.tangpian.sna.service.GplusService;

@Controller
public class MainController {

	private static final Logger LOGGER = Logger.getLogger(MainController.class
			.getName());

	@Autowired
	private GplusService gplusService;
	
	@Autowired
	private AnalysisService analysisService;

	@RequestMapping("summary")
	public String summary(@RequestParam String profileid, ModelMap model) {
		User user = gplusService.getUserProfile(profileid);
		model.put("user", user);
		
		return "summary";
	}

	@RequestMapping("main")
	public String main(@RequestParam String profileid, ModelMap model) {

		LOGGER.info("User Profile id:" + profileid);

		List<Post> posts = gplusService.listAndUpdateLastestPosts(profileid);

		model.put("posts", posts);

		return "show";

	}
	
	@RequestMapping("analysis")
	public String analysis(@RequestParam String profileid, ModelMap model) {
		LOGGER.info("User Profile id:" + profileid);
		Map<String, Double> result = analysisService.analysisPosts(profileid);
		model.put("hotWords", result);
		return "words";
	}
}
