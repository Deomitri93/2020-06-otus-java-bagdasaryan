package ru.otus;

public enum BanknoteType {
    TEN(10),
    FIFTY(50),
    HUNDRED(100),
    FIVE_HUNDRED(500),
    THOUSAND(1000),
    FIVE_THOUSAND(5000);

    public final int faceValue;

    BanknoteType(int label) {
        this.faceValue = label;
    }
}
