package ru.otus;

import java.util.Objects;

public class BanknoteStack implements Comparable<BanknoteStack> {
    private final BanknoteType banknoteType;
    private final int amount;

    public BanknoteStack(BanknoteType banknoteType, int amount) {
        this.banknoteType = banknoteType;
        this.amount = amount;
    }

    public BanknoteType getBanknoteType() {
        return banknoteType;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "BanknoteStack{" +
                "banknoteType=" + banknoteType +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BanknoteStack that = (BanknoteStack) o;
        return amount == that.amount &&
                banknoteType == that.banknoteType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(banknoteType, amount);
    }

    public int compareTo(BanknoteStack b) {
        if (b != null) {
            return Integer.compare(b.getBanknoteType().faceValue, this.getBanknoteType().faceValue);
        } else {
            throw new NullPointerException("Banknote stack can not be compared to null");
        }
    }
}
