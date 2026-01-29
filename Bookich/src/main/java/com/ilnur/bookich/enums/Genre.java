package com.ilnur.bookich.enums;

import lombok.Getter;

@Getter
public enum Genre {
    FICTION("Fiction"),
    NON_FICTION("Non-fiction"),
    MYSTERY("Mystery"),
    THRILLER("Thriller"),
    SCIENCE_FICTION("Science Fiction"),
    FANTASY("Fantasy"),
    ROMANCE("Romance"),
    HISTORICAL("Historical Fiction"),
    HORROR("Horror"),
    BIOGRAPHY("Biography"),
    SELF_HELP("Self-help"),
    BUSINESS("Business & Economics"),
    TECHNOLOGY("Technology"),
    CHILDREN("Children's"),
    TEXTBOOK("Textbook / Education"),
    POETRY("Poetry"),
    RELIGION("Religion & Spirituality"),
    OTHER("Other");

    private final String displayName;

    Genre(String displayName) {
        this.displayName = displayName;
    }
}