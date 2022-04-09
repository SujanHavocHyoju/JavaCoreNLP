package com.havocinc.restnlp.corenlpner.controller;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import com.havocinc.restnlp.corenlpner.model.EntityType;

@RestController
@RequestMapping(value = "/api")
@CrossOrigin
public class NERController {

    @Autowired
    private StanfordCoreNLP stanfordCoreNLP;

    @GetMapping(value = "/")
    public String getWelcomeString() {
        return "Enjoy the NLP";
    }

    @PostMapping(value = "/ner")
    public Set<String> getNERTokens(@RequestBody String input, @RequestParam EntityType type) {
        CoreDocument coreDocument = new CoreDocument(input);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreLabel> labels = coreDocument.tokens();
        return new HashSet<>(resultList(labels, type));
    }

    @PostMapping(value = "/ner/full")
    public HashMap<String, List<String>> getAllNERTokens(@RequestBody String input) {
        LinkedHashMap<String, List<String>> fullTokenMap = new LinkedHashMap<>();
        CoreDocument coreDocument = new CoreDocument(input);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreLabel> labels = coreDocument.tokens();
        Arrays.stream(EntityType.values()).forEach(entityType -> fullResultList(labels, entityType, fullTokenMap));
        return fullTokenMap;
    }

    private List<String> resultList(List<CoreLabel> labels, EntityType type) {
        return labels
                .stream()
                .filter(label -> type.name()
                        .equalsIgnoreCase(label.get(CoreAnnotations.NamedEntityTagAnnotation.class)))
                .map(CoreLabel::originalText)
                .collect(Collectors.toList());
    }

    private void fullResultList(List<CoreLabel> labels, EntityType type, HashMap<String, List<String>> map) {
        List<String> processedLabels = labels
                .stream()
                .filter(label -> type.name()
                        .equalsIgnoreCase(label.get(CoreAnnotations.NamedEntityTagAnnotation.class)))
                .map(CoreLabel::originalText)
                .collect(Collectors.toList());
        map.put(type.name(), processedLabels);
    }
}
