package com.qiniu.pandora.logdb.repo;

/**
 * Created by jemy on 2018/6/25.
 */
public interface Analyzer {
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
