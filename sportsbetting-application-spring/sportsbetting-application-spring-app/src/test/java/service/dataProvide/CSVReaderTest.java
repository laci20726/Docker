package service.dataProvide;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CSVReaderTest {
    CSVReader csvReader;
    String fileCorrectFilePath;
    String fileInvalidFilePath;
    String fileEmptyFilePath;
    List<List<String>> readResult;

    @BeforeEach
    void setUp(){
        csvReader = new CSVReader();
        fileCorrectFilePath = "C:\\Prog_Java\\GitEpam\\mep\\sportsbetting-application-maven\\sportsbetting-application-app\\src\\test\\java\\service\\dataProvide\\SampleCSVCorrect.csv";
        fileInvalidFilePath = "C:\\Prog_Java\\GitEpam\\mep\\sportsbetting-application-maven\\sportsbetting-application-app\\src\\test\\java\\service\\dataProvide\\SampleCSVInvalid.png";
        fileEmptyFilePath = "C:\\Prog_Java\\GitEpam\\mep\\sportsbetting-application-maven\\sportsbetting-application-app\\src\\test\\java\\service\\dataProvide\\SampleCSVEmpty.csv";
    }

    @Test
    void testReadShouldReturnEmptyArrayWhenEmptyFile(){
        readResult = csvReader.read(fileEmptyFilePath);
        assertTrue(readResult.isEmpty());
    }

    @Test
    void testReadShouldReturnEmptyArrayWhenInvalidFile(){
        readResult = csvReader.read(fileInvalidFilePath);
        assertTrue(readResult.isEmpty());
    }

    @Test
    void testReadShouldReturnEmptyArrayWhenMissingFile(){
        readResult = csvReader.read("");
        assertTrue(readResult.isEmpty());
    }

    @Test
    void testReadShouldReturnTwoArrayWithTwoItemsWhenCorrectFileContainsTwoRowAndTwoColumn(){
        readResult = csvReader.read(fileCorrectFilePath);
        assertEquals(2,readResult.size(),"number of Arrays");
        assertEquals(2,readResult.get(0).size(),"number of items");
        assertEquals(2,readResult.get(1).size(),"number of items");
    }

}
