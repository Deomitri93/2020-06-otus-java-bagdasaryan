package ru.otus.visitors.cell.get.remainder;

import ru.otus.BanknoteStack;
import ru.otus.Cell;

public interface CellGetRemainderVisitor {
    BanknoteStack cellGetRemainder(Cell cell);
}