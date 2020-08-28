package ProgettoPO.ProgettoProgrammazione;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProgettoProgrammazioneApplication {

	public static void main(String[] args) throws LifecycleException {
		SpringApplication.run(ProgettoProgrammazioneApplication.class, args);
		
	}

}
