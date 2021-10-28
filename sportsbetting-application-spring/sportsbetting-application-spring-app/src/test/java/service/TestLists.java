package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestLists {
    private final List<List<String>> sportEventSample = new ArrayList<>(Arrays.asList(
            Arrays.asList("0", "Arsenal vs Chelsea", "2016-10-27 19:00:00"),
            Arrays.asList("0", "Real Madrid vs Manchester", "2016-11-27 20:00:00")));

    private final List<List<String>> outcomeSample = new ArrayList<>(Arrays.asList(
            Arrays.asList("0", "1", "2"),
            Arrays.asList("0", "2", "4"),
            Arrays.asList("1", "3", "3"),
            Arrays.asList("2", "Arsenal", "2"),
            Arrays.asList("2", "Chelsea", "3"),
            Arrays.asList("3", "2", "4"),
            Arrays.asList("4", "2", "2"),
            Arrays.asList("5", "Real Madrid", "2"),
            Arrays.asList("5", "Manchester", "3")));

    private final List<List<String>> betSample = new ArrayList<>(Arrays.asList(
            Arrays.asList("0", "player Oliver Giroud score"),
            Arrays.asList("0", "number of scored goals"),
            Arrays.asList("0", "winner"),
            Arrays.asList("1", "player Ronaldo score"),
            Arrays.asList("1", "number of scored goals"),
            Arrays.asList("1", "winner")));

    public List<List<String>> getSportEventSample() {
        return sportEventSample;
    }

    public List<List<String>> getOutcomeSample() {
        return outcomeSample;
    }

    public List<List<String>> getBetSample() {
        return betSample;
    }
}
