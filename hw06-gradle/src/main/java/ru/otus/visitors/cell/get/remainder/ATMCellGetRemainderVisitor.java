package ru.otus.visitors.cell.get.remainder;

import ru.otus.BanknoteStack;
import ru.otus.Cell;

import java.util.Set;
import java.util.TreeSet;

public class ATMCellGetRemainderVisitor implements CellGetRemainderVisitor {
    @Override
    public BanknoteStack cellGetRemainder(Cell cell) {
        return new BanknoteStack(cell.getBanknoteType(), cell.getAmount());
    }

    public Set<BanknoteStack> getRemainder(Iterable<Cell> cells) {
        Set<BanknoteStack> res = new TreeSet<>();

        for (Cell cell : cells) {
            res.add(cell.accept(this));
        }

        return res;
    }
}
