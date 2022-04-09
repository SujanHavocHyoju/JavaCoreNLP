package com.havocinc.restnlp.corenlpner.service;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class Pipeline {
    private static Properties properties;
    // NLP pipeline annotators
    private static String propertyName = "tokenize, ssplit, pos, lemma, ner, parse, depparse, coref, sentiment";
    private static StanfordCoreNLP stanfordCoreNLP;

    private Pipeline() {
    }

    static {
        properties = new Properties();
        properties.setProperty("annotators", propertyName);
    }

    @Bean(name = "stanfordCoreNLP")
    public static StanfordCoreNLP getInstance() {
        if (stanfordCoreNLP == null) {
            stanfordCoreNLP = new StanfordCoreNLP(properties);
        }
        return stanfordCoreNLP;
    }
}
