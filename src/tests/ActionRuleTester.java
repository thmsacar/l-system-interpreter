package tests;

import fractal.ActionRule;

import java.util.Arrays;

public class ActionRuleTester {

    public static void main(String[] args) {
        ActionRuleTester.getDoubleFromWordTester();
    }

    public static void getDoubleFromWordTester () {
        System.out.println("Testing function getDoubleFromWord...");
        Double tmp = 0.0;
        for (int i = 0; i < 100; i++) {
            tmp = Math.random() * 100;
            String[] word = {"move", tmp.toString()};
            Double value = ActionRule.getDoubleFromWord(word);
            assert value == tmp;
        }
        System.out.println("Test passed.");
    }

}
