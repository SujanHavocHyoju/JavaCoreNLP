package com.havocinc.restnlp.corenlpner.service;

import org.springframework.stereotype.Service;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;
import java.util.stream.Collectors;

import com.havocinc.restnlp.corenlpner.model.POSObject;
import com.havocinc.restnlp.corenlpner.model.Sentence;

@Service
public class NLPService {
        public Map<String, String> getPOSMap(List<CoreLabel> labels) {
                List<String> posList = labels.stream()
                                .map(label -> label.get(CoreAnnotations.PartOfSpeechAnnotation.class))
                                .collect(Collectors.toList());

                return labels
                                .stream()
                                .collect(Collectors.toMap(CoreLabel::originalText,
                                                label -> label.get(CoreAnnotations.PartOfSpeechAnnotation.class)));
        }

        public Sentence getSentenceWithPOS(CoreMap sentence) {
                Sentence posSentence = new Sentence();
                List<POSObject> posObjects = new ArrayList<>();
                for (CoreLabel label : sentence.get(TokensAnnotation.class)) {
                        posObjects.add(new POSObject(label.value(),
                                        label.get(CoreAnnotations.PartOfSpeechAnnotation.class)));
                }
                posSentence.setText(sentence.toString());
                posSentence.setPos(posObjects);
                return posSentence;
        }
}

/*
 * Sentence
 * List<PosObjects>
 * term
 * posValue
 */