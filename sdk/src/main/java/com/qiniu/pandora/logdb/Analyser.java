package com.qiniu.pandora.logdb;

/**
 * Created by jemy on 2018/6/25.
 */
public interface Analyser {
    String StandardAnalyzer = "standard";
    String SimpleAnalyzer = "simple";
    String WhitespaceAnalyzer = "whitespace";
    String NoAnalyzer = "no";
    String AnsjAnalyzer = "index_ansj";
    String DicAnajAnalyzer = "dic_ansj";
    String SearchAnsjAnalyzer = "search_ansj";
    String ToAnsjAnalyzer = "to_ansj";
    String UserAnsjAnalyzer = "user_ansj";
    String KeyWordAnalyzer = "keyword";
    String PathAnalyzer = "path";
}
