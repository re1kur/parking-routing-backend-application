package re1kur.fs;

import io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication(exclude = S3AutoConfiguration.class)
public class FileServiceApplication {

    public static void main(String[] args) {
        System.out.println(System.getenv("PATH"));
        System.out.println(new File("/home/re1kur/Projects/parking-routing-backend-app/file-service/dev-compose.yml").exists());
        SpringApplication.run(FileServiceApplication.class, args);
    }

}
