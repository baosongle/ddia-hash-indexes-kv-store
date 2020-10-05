package baosongle.hashindexkvstore;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class KVConfig {
    @Value("${kv.dataFile}")
    private String dataFile;
}
