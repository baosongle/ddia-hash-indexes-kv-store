package baosongle.hashindexkvstore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class KVStoreConfig {
    @Value("${kv.dataFile}")
    private String dataFile;
}
