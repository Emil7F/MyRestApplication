package pl.emil7f.myrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Dzięki adnotacji @SpringBootApplication, która zawiera w środku @ComponentScan
 * Spring przeskanuje nam cały pakiet w poszukiwaniu klas z których może utworzyć Beany
 * <p>
 * Spring stworzy Beany dla:
 *
 * RestController
 * Controller
 * Service
 * Component
 * Repository
 * Configuration
 */
@SpringBootApplication
public class MyRestApplication {

    public static void main(String[] args) {
        fillDataSqlDummyCode();
    SpringApplication.run(MyRestApplication.class, args);
    }

    private static void fillDataSqlDummyCode() {
        FileWriter dataFile = null;
        try {
            dataFile = new FileWriter("src/main/resources/data.sql");
        } catch (IOException e) {
            System.out.println("Problem with created FileWriter data.sql file");
            e.printStackTrace();

        }

        if (dataFile != null) {


            BufferedWriter writer = new BufferedWriter(dataFile);

            try {
                writer.write(" ");

            } catch (IOException e) {
                System.out.println("Problem with BufferdReader: data.sql file");
                System.out.println(e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
