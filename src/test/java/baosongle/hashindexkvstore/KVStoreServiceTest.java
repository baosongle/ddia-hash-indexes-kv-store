package baosongle.hashindexkvstore;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.io.File;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    @Test
    @Order(3)
    public void testDelete() {
        setup(false);

        var name = kvStoreService.delete("name");
        Assertions.assertEquals("baosongle", name);
        name = kvStoreService.get("name");
        Assertions.assertNull(name);

        var base = kvStoreService.delete("base");
        Assertions.assertEquals("beijing", base);
        base = kvStoreService.get("base");
        Assertions.assertNull(base);
    }

    @Test
    @Order(4)
    public void testSetAfterDelete() {
        setup(false);

        var name = kvStoreService.set("name", "john");
        Assertions.assertNull(name);
        name = kvStoreService.get("name");
        Assertions.assertEquals("john", name);
    }

    @Test
    @Order(5)
    public void testSetAfterSet() {
        setup(false);

        var name = kvStoreService.set("name", "mike");
        Assertions.assertEquals("john", name);
        name = kvStoreService.get("name");
        Assertions.assertEquals("mike", name);
    }
}
