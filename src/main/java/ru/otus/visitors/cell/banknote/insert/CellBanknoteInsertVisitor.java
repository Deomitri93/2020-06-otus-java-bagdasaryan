package ru.otus.visitors.cell.banknote.insert;

import ru.otus.Cell;

public interface CellBanknoteInsertVisitor {
    boolean cellInsertBanknotes(Cell cell);
}
