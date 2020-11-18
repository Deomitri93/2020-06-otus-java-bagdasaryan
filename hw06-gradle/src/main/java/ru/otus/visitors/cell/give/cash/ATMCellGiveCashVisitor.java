package ru.otus.visitors.cell.give.cash;

import ru.otus.BanknoteStack;
import ru.otus.Cell;

import java.util.Set;
import java.util.TreeSet;

public class ATMCellGiveCashVisitor implements CellGiveCashVisitor {
    private Set<BanknoteStack> cashDemanded;

    @Override
    public BanknoteStack cellGiveCash(Cell cell) {
        for (BanknoteStack banknoteStack : cashDemanded) {
            if (banknoteStack.getBanknoteType().equals(cell.getBanknoteType())) {
                int amountCashDemanded = banknoteStack.getAmount();
                int cashToGive = Math.min(cell.getAmount(), amountCashDemanded);

                cell.setAmount(cell.getAmount() - cashToGive);
                return new BanknoteStack(cell.getBanknoteType(), cashToGive);
            }
        }

        return new BanknoteStack(cell.getBanknoteType(), 0);
    }

    public Set<BanknoteStack> giveCash(Iterable<Cell> cells, Set<BanknoteStack> cashDemanded) {
        this.cashDemanded = cashDemanded;

        Set<BanknoteStack> res = new TreeSet<>();

        for (Cell cell : cells) {
            res.add(cell.accept(this));
        }

        return res;
    }
}
