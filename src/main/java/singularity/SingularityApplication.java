package singularity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(AppConfig.class)
@SpringBootApplication
public class SingularityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SingularityApplication.class, args);
    }
    
}
