package com.havocinc.restnlp.corenlpner.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.havocinc.restnlp.corenlpner.model.EntityType;
import com.havocinc.restnlp.corenlpner.model.Sentence;
import com.havocinc.restnlp.corenlpner.service.NERService;
import com.havocinc.restnlp.corenlpner.service.NLPService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Demo NLP controller", description = "Stanford Core NLP practise.")
@RestController
@RequestMapping("/api/nlp")
@CrossOrigin
public class NLPController {

    @Autowired
    private StanfordCoreNLP stanfordCoreNLP;

    @Autowired
    private NERService nerService;

    @Autowired
    private NLPService nlpService;

    @ApiOperation(value = "NER to Check for specific entity in the text.", response = Iterable.class, tags = "lookForEntity")
    @PostMapping(value = "/ner")
    public Set<String> getNERTokens(@RequestBody String input, @RequestParam EntityType type) {
        CoreDocument coreDocument = new CoreDocument(input);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreLabel> labels = coreDocument.tokens();
        return new HashSet<>(nerService.resultList(labels, type));
    }

    @ApiOperation(value = "NER to Check for all available entities in the text.", response = Iterable.class, tags = "getAllNEREntities")
    @PostMapping(value = "/ner/full")
    public HashMap<String, List<String>> getAllNERTokens(@RequestBody String input) {
        LinkedHashMap<String, List<String>> fullTokenMap = new LinkedHashMap<>();
        CoreDocument coreDocument = new CoreDocument(input);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreLabel> labels = coreDocument.tokens();
        Arrays.stream(EntityType.values())
                .forEach(entityType -> nerService.fullResultList(labels, entityType, fullTokenMap));
        return fullTokenMap;
    }

    @ApiOperation(value = "Get POS details", response = Iterable.class, tags = "getPOSTagging")
    @PostMapping("/pos")
    public HashMap<String, String> getPOSTagging(@RequestBody String input) {
        LinkedHashMap<String, String> posMap = new LinkedHashMap<>();
        // create an empty Annotation just with the given text
        Annotation document = stanfordCoreNLP.process(input); // run all Annotators on this text
        // a CoreMap is essentially a Map that uses class objects as keys and has values
        // with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // the POS tag of the token
                String pos = token.get(PartOfSpeechAnnotation.class);
                posMap.put(token.value(), pos);
            }
        }
        return posMap;
    }

    @ApiOperation(value = "Get complete POS details", response = Iterable.class, tags = "getFullPosTags")
    @PostMapping(value = "/pos/full")
    public List<Sentence> getFullPosTags(@RequestBody String input) {
        List<Sentence> sentencesWithPOS = new ArrayList<>();
        Annotation document = stanfordCoreNLP.process(input);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            sentencesWithPOS.add(nlpService.getSentenceWithPOS(sentence));
        }
        return sentencesWithPOS;
    }

    @ApiOperation(value = "Detect sentiment of the text", response = Iterable.class, tags = "getSentiment")
    @PostMapping("/sentiment")
    public List<String> getSentiment(@RequestBody String input) {
        LinkedList<String> sentimentList = new LinkedList<>();
        // create an empty Annotation just with the given text
        Annotation document = stanfordCoreNLP.process(input); // run all Annotators on this text
        // a CoreMap is essentially a Map that uses class objects as keys and has values
        // with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            sentimentList.add(sentiment + " : " + sentence);
        }
        return sentimentList;
    }

    @PostMapping("/parse-tree")
    public List<String> getParseTree(@RequestBody String input) {
        List<String> tList = new ArrayList<>();
        CoreDocument coreDocument = new CoreDocument(input);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreSentence> coreSentences = coreDocument.sentences();
        for (CoreSentence sentence : coreSentences) {
            tList.add(sentence.coreMap().get(TreeAnnotation.class).toString());
        }
        return tList;
    }

    @PostMapping("/dependency-graph")
    public List<SemanticGraph> getDependencyGraph(@RequestBody String input) {
        List<SemanticGraph> dgList = new ArrayList<>();
        CoreDocument coreDocument = new CoreDocument(input);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreSentence> coreSentences = coreDocument.sentences();
        for (CoreSentence sentence : coreSentences) {
            dgList.add(sentence.coreMap().get(CollapsedCCProcessedDependenciesAnnotation.class));
        }
        return dgList;
    }

    // @PostMapping("/coref")
    // public Map<Integer, CorefChain> getCoreferenceLinkGraph(@RequestBody String
    // input) {
    // Annotation document = new Annotation(input);
    // Map<Integer, CorefChain> corefMap = document.get(CorefChainAnnotation.class);
    // return corefMap;
    // }
}
