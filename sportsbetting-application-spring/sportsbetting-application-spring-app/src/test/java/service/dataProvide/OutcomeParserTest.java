package service.dataProvide;

import domain.Outcome;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OutcomeParserTest {
    OutcomeParser testOutcomeParser = new OutcomeParser();
    List<String> testLineEmpty = new ArrayList<>();
    List<String> testLineCorrect = new ArrayList<>(Arrays.asList("0", "1", "2"));
    List<String> testLineEmptyValues = new ArrayList<>(Arrays.asList("0", "", ""));
    List<String> testLineMissingValues = new ArrayList<>(Arrays.asList("0",""));
    List<String> testLineInvalidValues = new ArrayList<>(Arrays.asList("0", "Winner", "a"));

    List<List<String>> testLineList = new ArrayList<>(Arrays.asList(
            testLineEmpty
            , testLineCorrect
            , testLineEmptyValues
            , testLineInvalidValues
            , testLineMissingValues
    ));

    @Test
    public void testParseLineToGenericShouldReturnCorrectOutcomeWhenTestLineCorrect() {
        Outcome testOutcome = testOutcomeParser.parseLineToGeneric(testLineCorrect);
        assertEquals("1", testOutcome.getDescription());
        assertEquals(BigDecimal.valueOf(2), testOutcome.getOdd());
    }

    @Test
    public void testParseLineToGenericShouldReturnOutcomeWithDefaultValuesWhenTestLineEmpty() {
        Outcome testOutcome = testOutcomeParser.parseLineToGeneric(testLineEmpty);
        assertEquals("Missing Values", testOutcome.getDescription());
        assertEquals(BigDecimal.valueOf(0), testOutcome.getOdd());
    }

    @Test
    public void testParseLineToGenericShouldReturnOutcomeWithEmptyValuesAtTheEmptyPlacesWhenTestLineHasEmptyValues() {
        Outcome testOutcome = testOutcomeParser.parseLineToGeneric(testLineEmptyValues);
        assertEquals("", testOutcome.getDescription());
        assertEquals(BigDecimal.valueOf(-1), testOutcome.getOdd());
    }

    @Test
    public void testParseLineToGenericShouldReturnOutcomeWithErrorCodeWhenTestLineHasInvalidValues() {
        Outcome testOutcome = testOutcomeParser.parseLineToGeneric(testLineInvalidValues);
        assertEquals("Winner", testOutcome.getDescription());
        assertEquals(BigDecimal.valueOf(-1), testOutcome.getOdd());
    }

    @Test
    public void testParseGenericToListShouldReturnOutcomeListWithDedicatedValuesWhenTestLinesHasInvalidAndMissingAndEmptyAndCorrectValues() {
        List<Outcome> testOutcomesList = testOutcomeParser.parseGenericToList(testLineList);
        //EmptyLine
        assertEquals("Missing Values", testOutcomesList.get(0).getDescription());
        assertEquals(BigDecimal.valueOf(0), testOutcomesList.get(0).getOdd());
        //CorrectLine
        assertEquals("1", testOutcomesList.get(1).getDescription());
        assertEquals(BigDecimal.valueOf(2), testOutcomesList.get(1).getOdd());
        //LineWithEmptyValues
        assertEquals("", testOutcomesList.get(2).getDescription());
        assertEquals(BigDecimal.valueOf(-1), testOutcomesList.get(2).getOdd());
        //LineWithInvalidValues
        assertEquals("Winner", testOutcomesList.get(3).getDescription());
        assertEquals(BigDecimal.valueOf(-1), testOutcomesList.get(3).getOdd());
        //LineWithMissingValues
        assertEquals("Missing Values", testOutcomesList.get(4).getDescription());
        assertEquals(BigDecimal.valueOf(0), testOutcomesList.get(4).getOdd());
    }
}
