package service.dataProvide;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;
@Component
public class CSVReader {
    public CSVReader() {
    }

    public List<List<String>> read(String filePath) {
        String line;
        String[] data;
        List<List<String>> lines = new ArrayList<>();
        if (filePath.endsWith(".csv")){
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                while ((line = br.readLine()) != null) {
                    data = line.split(";");
                    lines.add(Arrays.asList(data));
                }
            } catch (IOException e) {
                //TODO logging
            }
        }
        return lines;
    }
}
