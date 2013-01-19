package com.tangpian.sna.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
	@RequestMapping("test")
	public String test(ModelMap model) {
		Map<String, Double> map = new HashMap<String, Double>();
		map.put("aaa", 12.2);
		map.put("bbb", 22.31);
		model.put("map", map);
		return "test";
	}

}
