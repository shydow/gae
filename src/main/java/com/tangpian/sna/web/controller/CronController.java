package com.tangpian.sna.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tangpian.sna.service.GplusService;

@Controller
public class CronController {
	
	@Autowired
	private GplusService gplusService;

	@RequestMapping("/cron/gatherPost")
	public String gatherPost() {
		gplusService.gatherPost();
		return "success";
	}

	@RequestMapping("/cron/gatherReply")
	public String gatherReply() {
		gplusService.gatherReply();
		return "success";
	}

//	@RequestMapping("/cron/clear")
//	public String clear() {
//		gplusService.clear();
//		return "success";
//	}
}
