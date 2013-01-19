package com.tangpian.sna;

import java.util.Map;

import com.tangpian.sna.utils.ConfigUtil;

public class Test {
	public static void main(String[] args) {
		Map<String, Double> map = ConfigUtil.initTrendencyWordMap();
		for (String string : map.keySet()) {
			System.out.println(string + "|" + map.get(string));
		}
	}
}
