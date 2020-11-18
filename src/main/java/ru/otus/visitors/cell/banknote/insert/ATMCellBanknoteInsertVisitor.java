package ru.otus.visitors.cell.banknote.insert;

import ru.otus.BanknoteStack;
import ru.otus.Cell;

public class ATMCellBanknoteInsertVisitor implements CellBanknoteInsertVisitor {
    private BanknoteStack banknoteStack;

    public boolean insertBanknotes(Iterable<Cell> cells, BanknoteStack banknoteStack) {
        this.banknoteStack = banknoteStack;

        for (Cell cell : cells) {
            if (cell.accept(this)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean cellInsertBanknotes(Cell cell) {
        if (banknoteStack != null) {
            return cell.insertBanknotes(banknoteStack);
        } else {
            throw new NullPointerException("Can not insert null banknote stack into a cell");
        }
    }
}
