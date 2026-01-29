package com.ilnur.bookich.enums;

public enum Condition {
    NEW("New"),
    USED_GOOD("Used-good"),
    USED_POOR("Used-poor");

    private final String displayName;

    Condition(String displayName) {
        this.displayName = displayName;
    }
}
