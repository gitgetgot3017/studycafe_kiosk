package lhj.studycafekiosk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class StudycafeKioskApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudycafeKioskApplication.class, args);
	}

}
