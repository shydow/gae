package com.tangpian.sna.service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chenlb.mmseg4j.analysis.ComplexAnalyzer;
import com.tangpian.sna.dao.PostDao;
import com.tangpian.sna.model.Post;

@Service
public class AnalysisService {
	private static final Logger LOGGER = Logger.getLogger(AnalysisService.class
			.getName());

	@Autowired
	private PostDao postDao;

	public Map<String, Double> analysisPosts(String profileId) {
		List<Post> posts = postDao.getPostsByProfileId(profileId);
		List<String> words = new ArrayList<String>();
		for (Post post : posts) {
			words.addAll(segmentation(post.getContent()));
		}
		Map<String, Double> hotWords = getHotWords(words);
		log(hotWords.keySet());

		return hotWords;
	}

	private Map<String, Double> getHotWords(List<String> allWords) {
		Map<String, Double> map = new HashMap<String, Double>();
		for (String string : allWords) {
			if (string.length() <= 1) {
				continue;
			}
			if ("始##始".equals(string) || "末##末".equals(string) || string.contains("#")) {
				continue;
			}
			if (map.containsKey(string)) {
				map.put(string, map.get(string) + 10);
			} else {
				map.put(string, 10.0);
			}
		}

		List<Map.Entry<String, Double>> keyList = new ArrayList<Map.Entry<String, Double>>(
				map.entrySet());
		// 词频排序（选择前30个）
		Collections.sort(keyList, new Comparator<Map.Entry<String, Double>>() {

			@Override
			public int compare(Entry<String, Double> o1,
					Entry<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}

		});

		Map<String, Double> hotWords = new HashMap<String, Double>();
		for (int i = 0; i < keyList.size() && i < 30; i++) {
			hotWords.put(keyList.get(i).getKey(), keyList.get(i).getValue());
		}
		return hotWords;
	}

	public List<String> segmentation(String inputstring) {
		List<String> words = new ArrayList<String>();
		Reader reader = new StringReader(inputstring);

		Analyzer analyzer = new ComplexAnalyzer();
		
		
		TokenStream tokenStream;
		try {
			tokenStream = analyzer.tokenStream(null, reader);
			OffsetAttribute offsetAttribute = tokenStream
					.addAttribute(OffsetAttribute.class);
			CharTermAttribute charTermAttribute = tokenStream
					.addAttribute(CharTermAttribute.class);

			while (tokenStream.incrementToken()) {
				int startOffset = offsetAttribute.startOffset();
				int endOffset = offsetAttribute.endOffset();
				words.add(charTermAttribute.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		log(words);

		return words;
	}

	private void log(Collection<String> words) {
		LOGGER.info("start print words!");
		for (String word : words) {
			LOGGER.info("word:" + word);
		}
		LOGGER.info("stop print words!");
	}

}
