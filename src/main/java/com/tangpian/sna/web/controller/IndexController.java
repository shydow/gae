package com.tangpian.sna.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tangpian.sna.service.GplusService;

@Controller
public class IndexController {
	
	@Autowired
	private GplusService gplusService;

	@RequestMapping("/")
	public String index(HttpServletRequest request, ModelMap model) {
		return "index";
	}
}
