/*
 * Copyright © 2021 EPAM Systems, Inc. All Rights Reserved. All information contained herein is, and remains the
 * property of EPAM Systems, Inc. and/or its suppliers and is protected by international intellectual
 * property law. Dissemination of this information or reproduction of this material is strictly forbidden,
 * unless prior written permission is obtained from EPAM Systems, Inc
 */

package com.epam.training;

import java.util.Arrays;
import java.util.regex.Pattern;

public class Calculator {

    private String[] selectOperatorsFromExpression(String expression) {
        return Arrays.stream(expression.split("[0-9]+"))
                .map(String::trim)
                .toArray(String[]::new);
    }

    private int[] selectValuesFromExpression(String expression) {
        String[] values = expression.split("[" + Pattern.quote("+-*/") + "]");
        return convert(values);
    }

    public Result calculate(String expression) {
        String[] operators = selectOperatorsFromExpression(expression);
        int[] numbers = selectValuesFromExpression(expression);

        int result = calculate(operators, numbers);
        return new Result(operators, numbers, result);
    }

    private int[] convert(String[] numbers) {
        int[] numbersConverted = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            numbersConverted[i] = Integer.parseInt(numbers[i].trim());
        }
        return numbersConverted;
    }



    private int calculate(String[] operatorsParam, int[] numbersParam) {
        String[] operators = Arrays.copyOf(operatorsParam, operatorsParam.length);
        int[] numbers = Arrays.copyOf(numbersParam, numbersParam.length);

        int length = operators.length;
        int index = 1;
        length = divisionAndMultiply(operators, numbers, length, index);
        addAndExtract(operators, numbers, length, index);
        return numbers[0];
    }

    private void addAndExtract(String[] operators, int[] numbers, int length, int index) {
        while (index < length) {
            boolean isAdd = "+".equals(operators[index]);
            boolean isExtract = "-".equals(operators[index]);
            if (isAdd) {
                length = addition(operators, numbers, length, index);
            } else if (isExtract) {
                length = extract(operators, numbers, length, index);
            } else {
                index++;
            }
        }
    }

    private int divisionAndMultiply(String[] operators, int[] numbers, int length, int index) {
        while (index < length) {
            boolean isMultiply = "*".equals(operators[index]);
            boolean isDivision = "/".equals(operators[index]);
            if (isMultiply) {
                length = multiply(operators, numbers, length, index);
            } else if (isDivision) {
                length = division(operators, numbers, length, index);
            } else {
                index++;
            }
        }
        return length;
    }

    private int extract(String[] operators, int[] numbers, int length, int index) {
        numbers[index - 1] -= numbers[index];
        return removeUsedElement(operators, numbers, length, index);
    }

    private int addition(String[] operators, int[] numbers, int length, int index) {
        numbers[index - 1] += numbers[index];
        return removeUsedElement(operators, numbers, length, index);
    }

    private int division(String[] operators, int[] numbers, int length, int index) {
        numbers[index - 1] /= numbers[index];
        return removeUsedElement(operators, numbers, length, index);
    }

    private int multiply(String[] operators, int[] numbers, int length, int index) {
        numbers[index - 1] *= numbers[index];
        return removeUsedElement(operators, numbers, length, index);
    }

    private int removeUsedElement(String[] operators, int[] numbers, int length, int index) {
        for (int j = index; j < length - 1; j++) {
            numbers[j] = numbers[j + 1];
            operators[j] = operators[j + 1];
        }
        length--;
        return length;
    }
}
