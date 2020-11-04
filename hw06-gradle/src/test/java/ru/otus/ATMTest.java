package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ATMTest {
    ATM atm;
    Set<BanknoteStack> banknoteStackSet;

    @BeforeEach
    void createATMInstance() {
        atm = new ATM();

        banknoteStackSet = new TreeSet<>();
        System.out.println("Banknotes inserted:");
        for (int i = BanknoteType.values().length - 1; i >= 0; i--) {
            BanknoteStack banknoteStack = new BanknoteStack(BanknoteType.values()[i], (int) (Math.random() * 100));
            banknoteStackSet.add(banknoteStack);
            System.out.println("\t" + banknoteStack);
        }
        System.out.println();

        atm.insertBanknotes(banknoteStackSet);
    }

    @Test
    void insertBanknotes() {
        Set<BanknoteStack> remainderBanknoteStackSet = atm.getRemainderByBanknoteTypes();

        assertTrue((banknoteStackSet != null) && (remainderBanknoteStackSet != null));
        assertTrue(banknoteStackSet.size() == remainderBanknoteStackSet.size());

        Iterator<BanknoteStack> banknoteStackSetIterator = banknoteStackSet.iterator();
        Iterator<BanknoteStack> remainderBanknoteStackSetIterator = remainderBanknoteStackSet.iterator();

        System.out.println("Banknotes inserted ? Remainder banknotes");
        while ((banknoteStackSetIterator.hasNext()) && (remainderBanknoteStackSetIterator.hasNext())) {
            BanknoteStack banknoteStack = banknoteStackSetIterator.next();
            BanknoteStack remainderBanknoteStack = remainderBanknoteStackSetIterator.next();
            assertEquals(banknoteStack, remainderBanknoteStack);
            System.out.println("\t" + banknoteStack + " == " + remainderBanknoteStack);
        }
    }

    @Test
    void getRemainder() {
        int calcRemainder = 0;
        for (BanknoteStack banknoteStack : banknoteStackSet) {
            calcRemainder += banknoteStack.getBanknoteType().faceValue * banknoteStack.getAmount();
        }

        System.out.println("Expected remainder: " + calcRemainder);
        System.out.println("actual remainder: " + atm.getRemainder());

        assertEquals(atm.getRemainder(), calcRemainder);
    }

    @Test
    void getRemainderByBanknoteTypes() {
        Set<BanknoteStack> remainderBanknoteStackSet = atm.getRemainderByBanknoteTypes();

        assertTrue((banknoteStackSet != null) && (remainderBanknoteStackSet != null));
        assertTrue(banknoteStackSet.size() == remainderBanknoteStackSet.size());

        Iterator<BanknoteStack> banknoteStackSetIterator = banknoteStackSet.iterator();
        Iterator<BanknoteStack> remainderBanknoteStackSetIterator = remainderBanknoteStackSet.iterator();

        System.out.println("Banknotes inserted ? Remainder banknotes");
        while ((banknoteStackSetIterator.hasNext()) && (remainderBanknoteStackSetIterator.hasNext())) {
            BanknoteStack banknoteStack = banknoteStackSetIterator.next();
            BanknoteStack remainderBanknoteStack = remainderBanknoteStackSetIterator.next();
            assertEquals(banknoteStack, remainderBanknoteStack);
            System.out.println("\t" + banknoteStack + " == " + remainderBanknoteStack);
        }
    }

    @Test
    void giveCash() {
        final int cashDemanded = ((int) (Math.random() * (atm.getRemainder() / 10)) * 10);
        final int remainderBefore = atm.getRemainder();

        System.out.println("Cash demanded: " + cashDemanded);
        System.out.println("Remainder before getting cash: " + remainderBefore);

        final int cashGiven = atm.giveCash(cashDemanded);
        final int remainderAfter = atm.getRemainder();

        System.out.println("Cash given: " + cashGiven);
        System.out.println("Remainder after getting cash: " + remainderAfter);

        assertEquals(cashDemanded, cashGiven);
        assertEquals(cashGiven + remainderAfter, remainderBefore);
    }

    @Test
    void giveCashByBanknoteTypes() {
        final int cashDemanded = ((int) (Math.random() * (atm.getRemainder() / 10)) * 10);
        final int remainderBefore = atm.getRemainder();

        System.out.println("Cash demanded: " + cashDemanded);
        int cashDemandedDivider = cashDemanded;
        Set<BanknoteStack> cashDemandedByBanknoteTypes = new TreeSet<>();
        for (int i = BanknoteType.values().length - 1; i >= 0; i--) {
            cashDemandedByBanknoteTypes.add(new BanknoteStack(BanknoteType.values()[i], cashDemandedDivider / BanknoteType.values()[i].faceValue));
            cashDemandedDivider -= (cashDemandedDivider / BanknoteType.values()[i].faceValue) * BanknoteType.values()[i].faceValue;
        }
        for (BanknoteStack banknoteStack : cashDemandedByBanknoteTypes) {
            System.out.println("\t" + banknoteStack);
        }

        System.out.println("Remainder before getting cash: " + remainderBefore);
        Set<BanknoteStack> remainderBeforeByBanknoteTypes = atm.getRemainderByBanknoteTypes();
        for (BanknoteStack banknoteStack : remainderBeforeByBanknoteTypes) {
            System.out.println("\t" + banknoteStack);
        }

        Set<BanknoteStack> cashGivenByBanknoteTypes = atm.giveCashByBanknoteTypes(cashDemanded);
        int cashGiven = 0;
        for (BanknoteStack banknoteStack : cashGivenByBanknoteTypes) {
            cashGiven += banknoteStack.getBanknoteType().faceValue * banknoteStack.getAmount();
        }

        System.out.println();
        System.out.println("Cash given: " + cashGiven);
        for (BanknoteStack banknoteStack : cashGivenByBanknoteTypes) {
            System.out.println("\t" + banknoteStack);

        }
        System.out.println("Remainder after getting cash: " + atm.getRemainder());
        Set<BanknoteStack> remainderAfterByBanknoteTypes = atm.getRemainderByBanknoteTypes();
        for (BanknoteStack banknoteStack : remainderAfterByBanknoteTypes) {
            System.out.println("\t" + banknoteStack);
        }


        Iterator<BanknoteStack> cashGivenByBanknoteTypeIterator = cashGivenByBanknoteTypes.iterator();
        Iterator<BanknoteStack> remainderBeforeByBanknoteTypeIterator = remainderBeforeByBanknoteTypes.iterator();
        Iterator<BanknoteStack> remainderAfterByBanknoteTypeIterator = remainderAfterByBanknoteTypes.iterator();


        cashDemandedDivider = cashDemanded;
        while (cashGivenByBanknoteTypeIterator.hasNext() &&
                remainderBeforeByBanknoteTypeIterator.hasNext() &&
                remainderAfterByBanknoteTypeIterator.hasNext()) {
            BanknoteStack buf = cashGivenByBanknoteTypeIterator.next();
            BanknoteType curBanknoteType = buf.getBanknoteType();
            int cashGivenAmount = buf.getAmount();
            int remainderBeforeAmount = remainderBeforeByBanknoteTypeIterator.next().getAmount();
            int remainderAfterAmount = remainderAfterByBanknoteTypeIterator.next().getAmount();

            assertEquals(remainderBeforeAmount, remainderAfterAmount + cashGivenAmount);
            assertEquals(cashGivenAmount, Math.min(remainderBeforeAmount, cashDemandedDivider / curBanknoteType.faceValue));

            cashDemandedDivider -= Math.min(remainderBeforeAmount, cashDemandedDivider / curBanknoteType.faceValue) * curBanknoteType.faceValue;
        }

        for (int i = BanknoteType.values().length - 1; i >= 0; i--) {

            cashDemandedByBanknoteTypes.add(new BanknoteStack(BanknoteType.values()[i], cashDemandedDivider / BanknoteType.values()[i].faceValue));
            cashDemandedDivider -= (cashDemandedDivider / BanknoteType.values()[i].faceValue) * BanknoteType.values()[i].faceValue;
        }
    }
}