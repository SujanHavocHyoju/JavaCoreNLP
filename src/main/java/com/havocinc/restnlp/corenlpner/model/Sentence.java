package com.havocinc.restnlp.corenlpner.model;

import java.util.List;

public class Sentence {
    private String text;
    private List<POSObject> pos;

    public Sentence() {
    }

    public List<POSObject> getPos() {
        return pos;
    }

    public void setPos(List<POSObject> pos) {
        this.pos = pos;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Sentence [text=" + text + ", pos=" + pos + "]";
    }

}
