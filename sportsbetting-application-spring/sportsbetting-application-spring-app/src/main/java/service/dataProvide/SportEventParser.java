package service.dataProvide;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import domain.FootballSportEvent;
import domain.SportEvent;
import domain.TennisSportEvent;

@Component
public class SportEventParser implements DataParser<SportEvent> {
    public static final int POSITION_OF_SPORT_EVENT_TYPE = 0;
    public static final int POSITION_OF_DESCRIPTION = 1;
    public static final int POSITION_OF_SPORT_EVENT_START_OR_ODD = 2;
    public static final String DEFAULT_DATE = "2016-10-27 19:00:00";


    private SportEvent sportEventTypeSelect(String type) {
        SportEvent sportEvent;
        switch (Integer.parseInt(type)) {
        case 0:
            sportEvent = new FootballSportEvent();
            break;
        case 1:
            sportEvent = new TennisSportEvent();
            break;
        default:
            sportEvent = new FootballSportEvent();
        }
        return sportEvent;
    }

    @Override
    public SportEvent parseLineToGeneric(List<String> line) {
        SportEvent currentSportEvent;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (line.size() > 2) {
            currentSportEvent = sportEventTypeSelect(line.get(POSITION_OF_SPORT_EVENT_TYPE));
            currentSportEvent.setTitle(line.get(POSITION_OF_DESCRIPTION));
            try {
                currentSportEvent.setStartDate(LocalDateTime.parse(line.get(POSITION_OF_SPORT_EVENT_START_OR_ODD), formatter));
            } catch (DateTimeException e) {
                currentSportEvent.setStartDate(LocalDateTime.parse(DEFAULT_DATE, formatter));
            }
        } else {
            currentSportEvent = sportEventTypeSelect("0");
            currentSportEvent.setTitle("Missing Values");
            currentSportEvent.setStartDate(LocalDateTime.parse(DEFAULT_DATE, formatter));
        }

        return currentSportEvent;
    }

    @Override
    public List<SportEvent> parseGenericToList(List<List<String>> lines) {
        List<SportEvent> sportEvents = new ArrayList<>();

        for (List<String> line : lines) {
            sportEvents.add(parseLineToGeneric(line));
        }

        return sportEvents;
    }
}
