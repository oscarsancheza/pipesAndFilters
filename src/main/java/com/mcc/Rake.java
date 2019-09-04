package com.mcc;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class Rake {
  private String stopWordsPattern;

  Rake() {
    // Read the stop words file for the given language
    try {
      InputStream stream = new FileInputStream("./src/main/java/com/mcc/stopwords.txt");
      String line;

      ArrayList<String> stopWords = new ArrayList<>();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

      // Loop through each stop word and add it to the list
      while ((line = bufferedReader.readLine()) != null) stopWords.add(line.trim());

      ArrayList<String> regexList = new ArrayList<>();

      // Turn the stop words into an array of regex
      for (String word : stopWords) {
        String regex = "\\b" + word + "(?![\\w-])";
        regexList.add(regex);
      }

      // Join all regexes into global pattern
      this.stopWordsPattern = String.join("|", regexList);
    } catch (Exception e) {
      throw new Error(e.toString());
    }
  }

  /**
   * Returns a list of all sentences in a given string of text
   *
   * @param text
   * @return String[]
   */
  private String[] getSentences(String text) {
    return text.split("[.!?,;:\\t\\\\\\\\\"\\\\(\\\\)\\\\'\\u2019\\u2013]|\\\\s\\\\-\\\\s");
  }

  private String[] separateWords(String text, int size) {
    String[] split = text.split("[^a-zA-Z]");
    ArrayList<String> words = new ArrayList<>();

    for (String word : split) {
      String current = word.trim().toLowerCase();
      int len = current.length();

      if (len > size && !StringUtils.isNumeric(current)) {
        words.add(current);
      }
    }

    return words.toArray(new String[words.size()]);
  }

  /**
   * Generates a list of KeywordsGenerator by splitting sentences by their stop words
   *
   * @param sentences
   * @return
   */
  private String[] getKeywords(String[] sentences) {
    ArrayList<String> phraseList = new ArrayList<>();

    for (String sentence : sentences) {
      String temp = sentence.trim().replaceAll(this.stopWordsPattern, "|");
      String[] phrases = temp.split("\\|");

      for (String phrase : phrases) {
        phrase = phrase.trim().toLowerCase();

        if (phrase.length() > 0) phraseList.add(phrase);
      }
    }

    return phraseList.toArray(new String[phraseList.size()]);
  }

  /**
   * Calculates word scores for each word in a collection of phrases
   *
   * <p>Scores is calculated by dividing the word degree (collective length of phrases the word
   * appears in) by the number of times the word appears
   *
   * @param phrases
   * @return
   */
  private LinkedHashMap<String, Double> calculateWordScores(String[] phrases) {
    LinkedHashMap<String, Integer> wordFrequencies = new LinkedHashMap<>();
    LinkedHashMap<String, Integer> wordDegrees = new LinkedHashMap<>();
    LinkedHashMap<String, Double> wordScores = new LinkedHashMap<>();

    for (String phrase : phrases) {
      String[] words = this.separateWords(phrase, 3);
      int length = words.length;
      int degree = length - 1;

      for (String word : words) {
        wordFrequencies.put(word, wordDegrees.getOrDefault(word, 0) + 1);
        wordDegrees.put(word, wordFrequencies.getOrDefault(word, 0) + degree);
      }
    }

    for (String item : wordFrequencies.keySet()) {
      wordDegrees.put(item, wordDegrees.get(item) + wordFrequencies.get(item));
      wordScores.put(item, wordDegrees.get(item) / (wordFrequencies.get(item) * 1.0));
    }

    return wordScores;
  }

  /**
   * Returns a list of keyword candidates and their respective word scores
   *
   * @param phrases
   * @param wordScores
   * @return
   */
  private Set<String> getCandidateKeywordScores(
      String[] phrases, LinkedHashMap<String, Double> wordScores) {
    Set<String> keywordCandidates = new HashSet<>();

    for (String phrase : phrases) {
      double score = 0.0;

      String[] words = this.separateWords(phrase, 3);

      for (String word : words) {
        score += wordScores.get(word);
      }

      if (score > 0) {
        keywordCandidates.add(phrase);
      }
    }

    return keywordCandidates;
  }

  /**
   * Extracts KeywordsGenerator from the given text body using the RAKE algorithm
   *
   * @param text
   */
  public Set<String> getKeywordsFromText(String text) {
    String[] sentences = this.getSentences(text);
    String[] keywords = this.getKeywords(sentences);

    LinkedHashMap<String, Double> wordScores = this.calculateWordScores(keywords);
    return this.getCandidateKeywordScores(keywords, wordScores);
  }
}
