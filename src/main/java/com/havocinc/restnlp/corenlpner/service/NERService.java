package com.havocinc.restnlp.corenlpner.service;

import org.springframework.stereotype.Service;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import java.util.*;
import java.util.stream.Collectors;
import com.havocinc.restnlp.corenlpner.model.EntityType;

@Service
public class NERService {
    public List<String> resultList(List<CoreLabel> labels, EntityType type) {
        return labels
                .stream()
                .filter(label -> type.name()
                        .equalsIgnoreCase(label.get(CoreAnnotations.NamedEntityTagAnnotation.class)))
                .map(CoreLabel::originalText)
                .collect(Collectors.toList());
    }

    public void fullResultList(List<CoreLabel> labels, EntityType type, HashMap<String, List<String>> map) {
        List<String> processedLabels = labels
                .stream()
                .filter(label -> type.name()
                        .equalsIgnoreCase(label.get(CoreAnnotations.NamedEntityTagAnnotation.class)))
                .map(CoreLabel::originalText)
                .collect(Collectors.toList());
        map.put(type.name(), processedLabels);
    }
}
