package com.tangpian.sna.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.lucene.analysis.cn.smart.hhmm.HHMMSegmenter;
import org.apache.lucene.analysis.cn.smart.hhmm.SegToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

		// SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer(
		// Version.LUCENE_29);
		// Reader reader = new StringReader(inputstring);
		// TokenStream tokenStream = analyzer.tokenStream(null, reader);
		//
		// List<String> words = new ArrayList<String>();
		//
		// TermAttribute termAttribute = (TermAttribute) tokenStream
		// .getAttribute(TermAttribute.class);
		// words.add(termAttribute.toString());
		// return words;

		// StringReader reader = new StringReader(inputstring);
		//
		// SmartChineseAnalyzer ss = new
		// SmartChineseAnalyzer(Version.LUCENE_40);
		//
		// TokenStream tokenStream = ss.tokenStream("", reader);
		// try {
		// while (tokenStream.incrementToken()) {
		// TermAttribute termAttribute = (TermAttribute) tokenStream
		// .getAttribute(TermAttribute.class);
		// words.add(termAttribute.toString());
		// }
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		HHMMSegmenter segmenter = new HHMMSegmenter();
		List<SegToken> tokens = segmenter.process(inputstring);
		for (SegToken segToken : tokens) {
			words.add(new String(segToken.charArray));
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
