/*
 *Copyright © 2021 EPAM Systems, Inc. All Rights Reserved. All information contained herein is, and remains the
property of EPAM Systems, Inc. and/or its suppliers and is protected by international intellectual
property law. Dissemination of this information or reproduction of this material is strictly forbidden,
unless prior written permission is obtained from EPAM Systems, Inc
 * */

package com.epam.training;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.regex.Pattern;

public class CalculatorApp {
    public static void main(String[] args) throws IOException {
        System.out.print("Enter an expression: ");

        String line = new BufferedReader(new InputStreamReader(System.in)).readLine();

        Calculator calculator = new Calculator();

        Result result = calculator.calculate(line);
        System.out.println(result);
    }
}

