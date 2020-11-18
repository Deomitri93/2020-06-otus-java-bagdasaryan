package ru.otus;

import java.util.Set;
import java.util.TreeSet;

public class Executor {
    public static void main(String[] args) {
        ATM atm = new ATM();

        System.out.println();
        System.out.println("ATM remainder just after construction");
        for (BanknoteStack banknoteStack : atm.getRemainderByBanknoteTypes()) {
            System.out.println(banknoteStack);
        }

        Set<BanknoteStack> banknoteStacks = new TreeSet<>();
        banknoteStacks.add(new BanknoteStack(BanknoteType.FIFTY, 5));
        banknoteStacks.add(new BanknoteStack(BanknoteType.TEN, 5));
        banknoteStacks.add(new BanknoteStack(BanknoteType.THOUSAND, 5));
        banknoteStacks.add(new BanknoteStack(BanknoteType.FIVE_HUNDRED, 5));

        atm.insertBanknotes(banknoteStacks);

        System.out.println();
        System.out.println("ATM remainder after banknotes insertion");
        for (BanknoteStack banknoteStack : atm.getRemainderByBanknoteTypes()) {
            System.out.println(banknoteStack);
        }

        Set<BanknoteStack> cashDemanded = new TreeSet<>();
        cashDemanded.add(new BanknoteStack(BanknoteType.TEN, 3));
        cashDemanded.add(new BanknoteStack(BanknoteType.FIFTY, 2));
        cashDemanded.add(new BanknoteStack(BanknoteType.HUNDRED, 4));
        cashDemanded.add(new BanknoteStack(BanknoteType.FIVE_HUNDRED, 1));
        cashDemanded.add(new BanknoteStack(BanknoteType.THOUSAND, 2));

        int cashGiven = atm.giveCash(1460);

        System.out.println();
        if (cashGiven >= 0) {
            System.out.println("Cash given: " + cashGiven);
            System.out.println();
            System.out.println("ATM remainder after cash given");
            for (BanknoteStack banknoteStack : atm.getRemainderByBanknoteTypes()) {
                System.out.println(banknoteStack);
            }
        } else {
            System.out.println("ATM could not give demanded amount of cash");
        }
    }
}
