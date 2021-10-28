package com.epam.training;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {
    private Result testResult;

    @BeforeEach
    public void setUp() {
        Calculator testCa = new Calculator();
        testResult = testCa.calculate("10 / 2 + 3 + 6 * 2 - 5");
    }

    @Test
    public void testCalculate() {

        assertEquals(15, testResult.getResult());
        assertEquals("[, /, +, +, *, -]", Arrays.toString(testResult.getOperators()));
        assertEquals("[10, 2, 3, 6, 2, 5]", Arrays.toString(testResult.getOperands()));
    }

    @Test
    public void testResultToString() {
        assertEquals("Result{operators=[, /, +, +, *, -]," +
                " operands=[10, 2, 3, 6, 2, 5], result=15}",
                testResult.toString());
    }

    @Test
    public void testResultEquals() {
        Result sampleResult = new Result(null, null, 0);
        assertEquals(testResult, testResult);

        assertNotEquals(testResult, sampleResult);

        sampleResult = testResult;
        assertEquals(testResult, sampleResult);
    }
}
