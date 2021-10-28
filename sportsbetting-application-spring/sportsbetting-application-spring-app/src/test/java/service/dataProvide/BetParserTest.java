package service.dataProvide;

import domain.Bet;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BetParserTest {
    BetParser testBetParser = new BetParser();
    List<String> testLineEmpty = new ArrayList<>();
    List<String> testLineCorrect = new ArrayList<>(Arrays.asList("0", "player Oliver Giroud score"));
    List<String> testLineMissingValues = new ArrayList<>(Arrays.asList("0", ""));

    List<List<String>> testLineList = new ArrayList<>(Arrays.asList(
            testLineEmpty
            , testLineCorrect
            , testLineMissingValues
    ));

    @Test
    public void testParseLineToGenericShouldReturnCorrectBetWhenTestLineCorrect() {
        Bet bet = testBetParser.parseLineToGeneric(testLineCorrect);
        assertEquals("player Oliver Giroud score", bet.getDescription());
    }

    @Test
    public void testParseLineToGenericShouldReturnBetWithDefaultValuesWhenTestLineEmpty() {
        Bet bet = testBetParser.parseLineToGeneric(testLineEmpty);
        assertEquals("", bet.getDescription());
    }

    @Test
    public void testParseLineToGenericShouldReturnBetWithEmptyValuesAtTheEmptyPlacesWhenTestLineHasMissingValues() {
        Bet bet = testBetParser.parseLineToGeneric(testLineMissingValues);
        assertEquals("", bet.getDescription());
    }

    @Test
    public void testParseGenericToListShouldReturnBetListWithDedicatedValuesWhenTestLinesHasMissingAndEmptyAndCorrectValues() {
        List<Bet> testOutcomesList = testBetParser.parseGenericToList(testLineList);
        //EmptyLine
        assertEquals("", testOutcomesList.get(0).getDescription());
        //CorrectLine
        assertEquals("player Oliver Giroud score", testOutcomesList.get(1).getDescription());
        //LineWithMissingValues
        assertEquals("", testOutcomesList.get(2).getDescription());
    }
}

