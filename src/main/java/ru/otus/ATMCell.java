package ru.otus;

import ru.otus.visitors.cell.banknote.insert.CellBanknoteInsertVisitor;
import ru.otus.visitors.cell.get.remainder.CellGetRemainderVisitor;
import ru.otus.visitors.cell.give.cash.CellGiveCashVisitor;

public class ATMCell implements Cell {
    private final BanknoteType banknoteType;
    private int amount;

    public ATMCell(BanknoteType banknoteType, int amount) {
        this.banknoteType = banknoteType;
        this.amount = amount;
    }

    public ATMCell(BanknoteStack banknoteStack) {
        this.banknoteType = banknoteStack.getBanknoteType();
        this.amount = banknoteStack.getAmount();
    }

    @Override
    public BanknoteType getBanknoteType() {
        return banknoteType;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean insertBanknotes(BanknoteStack banknoteStack) {
        if (this.banknoteType.equals(banknoteStack.getBanknoteType())) {
            this.amount += banknoteStack.getAmount();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean accept(CellBanknoteInsertVisitor visitor) {
        return visitor.cellInsertBanknotes(this);
    }

    @Override
    public BanknoteStack accept(CellGetRemainderVisitor visitor) {
        return visitor.cellGetRemainder(this);
    }

    @Override
    public BanknoteStack accept(CellGiveCashVisitor visitor) {
        return visitor.cellGiveCash(this);
    }

    @Override
    public int compareTo(Cell c) {
        if (c != null) {
            return Integer.compare(c.getBanknoteType().faceValue, this.getBanknoteType().faceValue);
        } else {
            throw new NullPointerException("Cell can not be compared to null");
        }
    }
}
