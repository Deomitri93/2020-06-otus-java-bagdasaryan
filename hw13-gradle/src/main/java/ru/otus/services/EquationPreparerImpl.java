package ru.otus.services;

import ru.otus.model.DivisionEquation;
import ru.otus.model.Equation;
import ru.otus.model.MultiplicationEquation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EquationPreparerImpl implements EquationPreparer {
    @Override
    public List<Equation> prepareEquationsFor(int base) {
        final int EQUATIONS_NUMBER = 2;

        List<Equation> equations = new ArrayList<>();
        for (int i = 1; i < EQUATIONS_NUMBER; i++) {
            var multiplicationEquation = new MultiplicationEquation(base, i);
            var divisionEquation = new DivisionEquation(multiplicationEquation.getResult(), base);
            equations.add(multiplicationEquation);
            equations.add(divisionEquation);
        }

        Collections.shuffle(equations);
        return equations;
    }
}
