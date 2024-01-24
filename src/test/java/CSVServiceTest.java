import com.heshanthenura.teamrocket.Entity.Person;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class CSVServiceTest {
    static Logger logger = Logger.getLogger("info");

    static ObservableList<Person> personList = FXCollections.observableArrayList();

    public static ObservableList<Person> getPersonData(String filePath) throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(filePath));
        List<String[]> data = reader.readAll();
        for (String[] d : data) {
            try {
                String name = d[0];
                String netWorth = (d[1].replace("\"", "") );
                int age = Integer.parseInt(d[2]);
                String c = d[3];
                String s = d[4];
                String i = d[5];
                logger.info(netWorth);
                personList.add(new Person(name, (netWorth), age, c, s, i));
            } catch (Exception e) {
                logger.info(String.valueOf(e));
            }
        }
        return personList;
    }


    public static void main(String[] args) throws IOException, CsvException {
        getPersonData("E:\\Coding\\Java\\Projects\\TeamRocket\\src\\main\\resources\\com\\heshanthenura\\teamrocket\\TopRichestInWorld.csv");
    }
}
