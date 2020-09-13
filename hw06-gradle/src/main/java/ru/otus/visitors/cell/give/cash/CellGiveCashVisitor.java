package ru.otus.visitors.cell.give.cash;

import ru.otus.BanknoteStack;
import ru.otus.Cell;

public interface CellGiveCashVisitor {
    BanknoteStack cellGiveCash(Cell cell);
}