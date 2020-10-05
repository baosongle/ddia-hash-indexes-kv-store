package baosongle.hashindexkvstore;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.File;

@Slf4j
public class KVStoreServiceTest {
    private KVStoreService kvStoreService;

    private void setup(boolean delete) {
        var dataFile = "/Users/songle/kvstore.txt";
        var file = new File(dataFile);
        if (delete && file.exists() && !file.delete()) {
            log.warn("delete file {} failed with error", dataFile);
        }
        var config = new KVStoreConfig(dataFile);
        kvStoreService = new KVStoreServiceImpl(config);
    }

    @Test
    @Order(1)
    public void testSetGetDelete() {
        setup(true);
        kvStoreService.set("name", "baosongle");
        var name = kvStoreService.get("name");
        Assertions.assertEquals("baosongle", name);

        kvStoreService.set("base", "beijing");
        var base = kvStoreService.get("base");
        Assertions.assertEquals("beijing", base);
    }

    @Test
    @Order(2)
    public void testLoadFile() {
        setup(false);
        var name = kvStoreService.get("name");
        Assertions.assertEquals("baosongle", name);

        var base = kvStoreService.get("base");
        Assertions.assertEquals("beijing", base);
    }
}
