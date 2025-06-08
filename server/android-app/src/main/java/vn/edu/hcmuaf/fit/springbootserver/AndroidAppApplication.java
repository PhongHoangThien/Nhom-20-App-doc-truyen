package vn.edu.hcmuaf.fit.springbootserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "vn.edu.hcmuaf.fit.springbootserver.repository")
@EntityScan(basePackages = "vn.edu.hcmuaf.fit.springbootserver.entity")
public class AndroidAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AndroidAppApplication.class, args);
    }

}
