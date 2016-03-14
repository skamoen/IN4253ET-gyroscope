package nl.tudelft.gyroscope;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GyroscopeServiceApplication {

    @Bean
    CommandLineRunner init(DataRepository dataRepository) {

        return (evt) -> {


        };

    }

	public static void main(String[] args) {
		SpringApplication.run(GyroscopeServiceApplication.class, args);
	}
}
