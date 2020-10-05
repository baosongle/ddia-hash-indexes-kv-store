package baosongle.hashindexkvstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:kv.properties")
public class HashIndexKvStoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(HashIndexKvStoreApplication.class, args);
    }
}
