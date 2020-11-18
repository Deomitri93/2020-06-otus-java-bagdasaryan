package ru.otus;

import ru.otus.visitors.cell.banknote.insert.ATMCellBanknoteInsertVisitor;
import ru.otus.visitors.cell.get.remainder.ATMCellGetRemainderVisitor;
import ru.otus.visitors.cell.give.cash.ATMCellGiveCashVisitor;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ATM {
    private SortedSet<Cell> cells;
    private ATMCellBanknoteInsertVisitor banknoteInsertVisitor;
    private ATMCellGetRemainderVisitor getRemainderVisitor;
    private ATMCellGiveCashVisitor giveCashVisitor;

    public ATM() {
        cells = new TreeSet<Cell>();

        for (int i = 0; i < BanknoteType.values().length; i++) {
            cells.add(new ATMCell(BanknoteType.values()[i], 0));
        }

        banknoteInsertVisitor = new ATMCellBanknoteInsertVisitor();
        getRemainderVisitor = new ATMCellGetRemainderVisitor();
        giveCashVisitor = new ATMCellGiveCashVisitor();
    }

    public void insertBanknotes(Set<BanknoteStack> banknoteStacks) {
        for (BanknoteStack banknoteStack : banknoteStacks) {
            banknoteInsertVisitor.insertBanknotes(cells, banknoteStack);
        }
    }

    public int getRemainder() {
        int res = 0;

        Set<BanknoteStack> remainder = getRemainderVisitor.getRemainder(cells);
        for (BanknoteStack banknoteStack : remainder) {
            res += banknoteStack.getBanknoteType().faceValue * banknoteStack.getAmount();
        }

        return res;
    }

    public Set<BanknoteStack> getRemainderByBanknoteTypes() {
        return getRemainderVisitor.getRemainder(cells);
    }

    private Set<BanknoteStack> possibleToGiveCash(int cashDemanded) {
        Set<BanknoteStack> remainder = getRemainderVisitor.getRemainder(cells);
        Set<BanknoteStack> cashToGive = new TreeSet<>();

        for (BanknoteStack banknoteStack : remainder) {
            int div = Math.min(cashDemanded / banknoteStack.getBanknoteType().faceValue, banknoteStack.getAmount());

            cashDemanded -= div * banknoteStack.getBanknoteType().faceValue;
            cashToGive.add(new BanknoteStack(banknoteStack.getBanknoteType(), div));
        }

        if (cashDemanded == 0) {
            return cashToGive;
        } else {
            return null;
        }
    }

    public int giveCash(int cashDemanded) {
        Set<BanknoteStack> cashToGive = possibleToGiveCash(cashDemanded);

        if (cashToGive != null) {
            cashDemanded = 0;
            for (BanknoteStack banknoteStack : giveCashVisitor.giveCash(cells, cashToGive)) {
                cashDemanded += banknoteStack.getBanknoteType().faceValue * banknoteStack.getAmount();
            }
            return cashDemanded;
        } else {
            return -1;
        }
    }

    public Set<BanknoteStack> giveCashByBanknoteTypes(int cashDemanded) {
        Set<BanknoteStack> cashToGive = possibleToGiveCash(cashDemanded);

        if (cashToGive != null) {
            return giveCashVisitor.giveCash(cells, cashToGive);
        } else {
            return null;
        }
    }
}
