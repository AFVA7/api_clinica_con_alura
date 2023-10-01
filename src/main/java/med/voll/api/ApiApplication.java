package med.voll.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	static String nombre="";

	public static void main(String[] args) {

		SpringApplication.run(ApiApplication.class, args);
	}
	//java -DDATASOURCE_URL=jdbc:mysql://localhost/vollmed_api -DDATASOURCE_USERNAME=root -DDATASOURCE_PASSWORD=12345 -jar target/api-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

}
