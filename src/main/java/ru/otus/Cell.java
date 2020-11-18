package ru.otus;

import ru.otus.visitors.cell.banknote.insert.CellBanknoteInsertVisitor;
import ru.otus.visitors.cell.get.remainder.CellGetRemainderVisitor;
import ru.otus.visitors.cell.give.cash.CellGiveCashVisitor;

public interface Cell extends Comparable<Cell> {
    boolean insertBanknotes(BanknoteStack banknoteStack);

    BanknoteType getBanknoteType();

    int getAmount();

    void setAmount(int amount);

    boolean accept(CellBanknoteInsertVisitor visitor);

    BanknoteStack accept(CellGetRemainderVisitor visitor);

    BanknoteStack accept(CellGiveCashVisitor visitor);
}
