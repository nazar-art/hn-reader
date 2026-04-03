package letscode.hnreader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class HnReaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(HnReaderApplication.class, args);
    }

}
