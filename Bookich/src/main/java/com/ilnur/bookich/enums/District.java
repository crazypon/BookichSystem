package com.ilnur.bookich.enums;

// I assume that this service will work only in Tashkent
public enum District {
    OLMAZOR("Almazar"),
    BEKTEMIR("Bektermir"),
    CHILANZAR("Chilanzar"),
    MIRABAD("Mirabad"),
    MIRZO_ULUGBEK("Mirzo-ulugbek"),
    SERGELI("Sergeli"),
    SHAYHANTAHUR("Shayhantahur"),
    UCHTEPA("Uchtepa"),
    YAKKASARAY("Yakkasaroy"),
    YANGIHAYOT("Yangihayot"),
    YASHNOBOD("Yashnobod"),
    YUNUSABAD("Yunusabad");

    private final String displayName;

    District(String displayName) {
        this.displayName = displayName;
    }
}
