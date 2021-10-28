package service.dataProvide;

import domain.SportEvent;
import domain.TennisSportEvent;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SportEventParserTest {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    SportEventParser sportEventParser = new SportEventParser();
    List<String> testLineEmpty = new ArrayList<>();
    List<String> testLineTennis = new ArrayList<>(Arrays.asList("1", "", ""));
    List<String> testLineCorrect = new ArrayList<>(Arrays.asList("0", "Arsenal vs Chelsea", "2018-10-27 19:00:00"));
    List<String> testLineMissingDateValue = new ArrayList<>(Arrays.asList("0", "Arsenal vs Chelsea"));
    List<String> testLineMissingEventNameValue = new ArrayList<>(Collections.singletonList("0"));
    List<String> testLineInvalidDateValue = new ArrayList<>(Arrays.asList("0", "Arsenal vs Chelsea", "2016-10-27"));

    List<List<String>> testLineList = new ArrayList<>(Arrays.asList(
            testLineEmpty
            , testLineTennis
            , testLineCorrect
            , testLineMissingDateValue
            , testLineMissingEventNameValue
            , testLineInvalidDateValue

    ));

    @Test
    public void testParseLineToGenericShouldReturnCorrectSportEventWhenTestLineCorrect() {
        SportEvent sportEvent = sportEventParser.parseLineToGeneric(testLineCorrect);
        assertEquals("Arsenal vs Chelsea", sportEvent.getTitle());
        assertEquals("2018-10-27 19:00:00", sportEvent.getStartDate().format(formatter));
    }

    @Test
    public void testParseLineToGenericShouldReturnCorrectSportEventWhenTestLineTennis() {
        SportEvent sportEvent = sportEventParser.parseLineToGeneric(testLineTennis);
        assertEquals(TennisSportEvent.class, sportEvent.getClass());
    }

    @Test
    public void testParseLineToGenericShouldReturnSportEventWithDefaultValuesWhenTestLineEmpty() {
        SportEvent sportEvent = sportEventParser.parseLineToGeneric(testLineEmpty);
        assertEquals("Missing Values", sportEvent.getTitle());
        assertEquals("2016-10-27 19:00:00", sportEvent.getStartDate().format(formatter));
    }

    @Test
    public void testParseLineToGenericShouldReturnSportEventWithEmptyValuesAtTheEmptyPlacesWhenTestLineHasMissingDateValues() {
        SportEvent sportEvent = sportEventParser.parseLineToGeneric(testLineMissingDateValue);
        assertEquals("Missing Values", sportEvent.getTitle());
        assertEquals("2016-10-27 19:00:00", sportEvent.getStartDate().format(formatter));
    }

    @Test
    public void testParseLineToGenericShouldReturnSportEventWithEmptyValuesAtTheEmptyPlacesWhenTestLineHasMissingDateAndEventNameValues() {
        SportEvent sportEvent = sportEventParser.parseLineToGeneric(testLineMissingEventNameValue);
        assertEquals("Missing Values", sportEvent.getTitle());
        assertEquals("2016-10-27 19:00:00", sportEvent.getStartDate().format(formatter));
    }

    @Test
    public void testParseLineToGenericShouldReturnSportEventWithEmptyValuesAtTheEmptyPlacesWhenTestLineHasInvalidDateValues() {
        SportEvent sportEvent = sportEventParser.parseLineToGeneric(testLineInvalidDateValue);
        assertEquals("Arsenal vs Chelsea", sportEvent.getTitle());
        assertEquals("2016-10-27 19:00:00", sportEvent.getStartDate().format(formatter));
    }

    @Test
    public void testParseGenericToListShouldReturnSportEventListWithDedicatedValuesWhenTestLinesHasMissingAndEmptyAndCorrectValues() {
        List<SportEvent> sportEventList = sportEventParser.parseGenericToList(testLineList);

        assertEquals("Arsenal vs Chelsea", sportEventList.get(2).getTitle());
        assertEquals("2018-10-27 19:00:00", sportEventList.get(2).getStartDate().format(formatter));

        assertEquals(TennisSportEvent.class, sportEventList.get(1).getClass());

        assertEquals("Arsenal vs Chelsea", sportEventList.get(5).getTitle());
        assertEquals("2016-10-27 19:00:00", sportEventList.get(5).getStartDate().format(formatter));
    }
}
