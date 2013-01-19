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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chenlb.mmseg4j.analysis.ComplexAnalyzer;
import com.tangpian.sna.dao.PostDao;
import com.tangpian.sna.model.Post;
import com.tangpian.sna.utils.ConfigUtil;

@Service
public class AnalysisService {
	private static final Logger LOGGER = Logger.getLogger(AnalysisService.class
			.getName());

	@Autowired
	private PostDao postDao;

	public Map<String, Double> analysisHotWords(String profileId) {
		List<Post> posts = postDao.getPostsByProfileId(profileId);
		List<String> words = new ArrayList<String>();
		for (Post post : posts) {
			words.addAll(segmentation(post.getContent()));
		}
		Map<String, Double> hotWords = getHotWords(words);
		log(hotWords.keySet());

		return hotWords;
	}

	public Double analyzeTrendency(String profileid) {
		List<Post> posts = postDao.getPostsByProfileId(profileid);
		List<String> words = new ArrayList<String>();
		for (Post post : posts) {
			words.addAll(segmentation(post.getContent()));
		}
		return analyzeContentsTrendency(words);
	}

	private Map<String, Double> getHotWords(List<String> allWords) {
		Map<String, Double> map = new HashMap<String, Double>();
		for (String string : allWords) {
			if (string.length() <= 1) {
				continue;
			}

			if (map.containsKey(string)) {
				map.put(string, map.get(string) + 7);
			} else {
				map.put(string, 7.0);
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

	private String preProcessing(String string) {
		String regex = "\\<.*?\\>|(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(string);
		return matcher.replaceAll("");
	}

	public List<String> segmentation(String inputstring) {
		inputstring = preProcessing(inputstring);
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

	public static void main(String[] args) {
		AnalysisService as = new AnalysisService();

		String string = "#听着听着我们就老了 <b>To be Included in our shared circle</b>:<br /><b>Share</b> the circle here: <a href=\"http://goo.gl/bLF7Y\" class=\"ot-anchor\">http://goo.gl/bLF7Y</a>";
		System.out.println(as.segmentation(string));
	}

	private double analyzeContentsTrendency(List<String> words) {
		Map<String, Double> trendencyWordMap = ConfigUtil
				.initTrendencyWordMap();
		double score = 0;

		for (String word : words) {
			score += trendencyWordMap.get(word);
		}

		return score;
	}

}
