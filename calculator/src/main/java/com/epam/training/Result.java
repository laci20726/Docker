/*
 * Copyright © 2021 EPAM Systems, Inc. All Rights Reserved. All information contained herein is, and remains the
 * property of EPAM Systems, Inc. and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from EPAM Systems, Inc
 */

package com.epam.training;

import java.util.Arrays;
import java.util.Objects;

public class Result {
    private final String[] operators;
    private final int[] operands;
    private final double result;

    public Result(String[] operators, int[] operands, double result) {
        this.operators = operators;
        this.operands = operands;
        this.result = result;
    }

    public String[] getOperators() {
        return operators;
    }

    public int[] getOperands() {
        return operands;
    }

    public double getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result1 = (Result) o;
        return Double.compare(result1.result, result) == 0 && Arrays.equals(operators, result1.operators) && Arrays.equals(operands, result1.operands);
    }

    @Override
    public int hashCode() {
        int result1 = Objects.hash(result);
        result1 = 31 * result1 + Arrays.hashCode(operators);
        result1 = 31 * result1 + Arrays.hashCode(operands);
        return result1;
    }

    @Override
    public String toString() {
        return "Result{" +
                "operators=" + Arrays.toString(operators) +
                ", operands=" + Arrays.toString(operands) +
                ", result=" + result +
                '}';
    }
}
