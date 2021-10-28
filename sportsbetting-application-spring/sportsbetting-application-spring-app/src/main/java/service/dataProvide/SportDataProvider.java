package service.dataProvide;

import java.util.List;

public interface SportDataProvider<E> {

    List<E> provideSportData(List<List<String>> sportEventLines,
                             List<List<String>> betLines,
                             List<List<String>> outcomeLines);
}
