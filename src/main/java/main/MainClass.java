package main;

import controller.CRUDController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import repository.ClientRepository;

@SpringBootApplication(scanBasePackageClasses = {ClientRepository.class, CRUDController.class})
@EntityScan("entity")
@EnableJpaRepositories("repository")
public class MainClass {
    public static void main(String[] args) {
        SpringApplication.run(MainClass.class, args);
    }
}
