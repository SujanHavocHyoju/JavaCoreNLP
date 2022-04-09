package com.havocinc.restnlp.corenlpner.model;

import org.apache.commons.lang3.EnumUtils;

public class POSObject {
    private String term;
    private String posTag;
    private String posName;

    public POSObject() {
    }

    public POSObject(String term, String posTag) {
        this.term = term;
        this.posTag = posTag;
        if (EnumUtils.isValidEnum(POSType.class, posTag)) {
            this.posName = POSType.valueOf(posTag).getPosDetail();
        } else
            this.posName = posTag;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getPosTag() {
        return posTag;
    }

    public void setPosTag(String posTag) {
        this.posTag = posTag;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    @Override
    public String toString() {
        return "POSObject [term=" + term + ", posTag=" + posTag + ",  posName=" + posName + "]";
    }
}
