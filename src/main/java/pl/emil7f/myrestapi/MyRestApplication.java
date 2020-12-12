package pl.emil7f.myrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

///**
// * Dzięki adnotacji @SpringBootApplication, która zawiera w środku @ComponentScan
// * Spring przeskanuje nam cały pakiet w poszukiwaniu klas z których może utworzyć Beany
// * <p>
// * Spring stworzy Beany dla:
// *
// * RestController
// * Controller
// * Service
// * Component
// * Repository
// * Configuration
// */
@SpringBootApplication
public class MyRestApplication {

    public static void main(String[] args) {
        fillDataSqlDummyCode();

//       spring.jpa.show-sql=true  -> aby zobaczyć zapytatnia które wykonuje hibernate
//        w properties należy dodać zależność
//        spring.jpa.hibernate.ddl-auto=none
//        dlatego że HIBERNATE próbuje nam stworzyć tabele, a ona już została utworzona
//        przez nas w pliku data.sql  && schema.sql
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
                writer.write("");
                //posty
                for (int i = 1; i <= 100; i++) {
                    writer.write(
                            "INSERT INTO POST (id, title, content, created) " +
                                    "values (" + i + ", 'Test Post " + i + "', 'Content " + i +
                                    "', '" + LocalDateTime.now().minusDays(120 - i) + "');\n"
                    );
                }
                // komentarze
                for (int i = 1; i <= 100; i++) {
                    int postId = 1 + i / 10;
                    writer.write(
                            "insert into comment(id, post_id, content, created) " +
                                    "values (" + i + "," + postId + ", 'Comment " + i +
                                    "', '" + LocalDateTime.now().minusDays(100 - i) + "');\n"
                    );
                }


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
