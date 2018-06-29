package com.qiniu.pandora.logdb.repo;

/**
 * 定义 LogDB 常用的分词器
 */
public interface Analyzer {
    String StandardAnalyzer = "standard";       // 标准分词
    String WhitespaceAnalyzer = "whitespace";   // 空白分词
    String NoAnalyzer = "no";                   // 不分词不索引
    String AnsjAnalyzer = "index_ansj";         // 中文分词
    String KeyWordAnalyzer = "keyword";         // 关键字分词
    String PathAnalyzer = "path";               // Path分词
}
