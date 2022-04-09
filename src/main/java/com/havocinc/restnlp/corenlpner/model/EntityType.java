package com.havocinc.restnlp.corenlpner.model;

public enum EntityType {
    PERSON("Person"),
    CITY("City"),
    STATE_OR_PROVINCE("State_Or_Province"),
    COUNTRY("Country"),
    EMAIL("Email"),
    TITLE("Title");

    private String entityType;

    EntityType(String type) {
        this.entityType = type;
    }

    public String getEntityType() {
        return entityType;
    }
}
