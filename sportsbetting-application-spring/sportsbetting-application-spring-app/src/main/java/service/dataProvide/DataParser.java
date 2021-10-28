package service.dataProvide;

import java.util.List;

public interface DataParser<E> {
    E parseLineToGeneric(List<String> line);

    List<E> parseGenericToList(List<List<String>> lines);
}
