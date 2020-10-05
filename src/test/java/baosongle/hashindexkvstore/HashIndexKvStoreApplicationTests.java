package baosongle.hashindexkvstore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HashIndexKvStoreApplicationTests {
    @Autowired
    private KVService kvService;

    @Test
    void contextLoads() {
        kvService.set("name", "baosongle");
        String name = kvService.get("name");
        Assertions.assertEquals("baosongle", name);

        kvService.set("age", "26");
        String age = kvService.get("age");
        Assertions.assertEquals("26", age);

        kvService.set("base", "beijing");
        String base = kvService.get("base");
        Assertions.assertEquals("beijing", base);

        age = kvService.get("age");
        Assertions.assertEquals("26", age);
        name = kvService.get("name");
        Assertions.assertEquals("baosongle", name);
        base = kvService.get("base");
        Assertions.assertEquals("beijing", base);

        kvService.delete("age");
        age = kvService.get("age");
        Assertions.assertNull(age);
    }

    @Test
    void testLoadFile() {
        var age = kvService.get("age");
        Assertions.assertNull(age);

        var base = kvService.get("base");
        Assertions.assertEquals("beijing", base);

        var name = kvService.get("name");
        Assertions.assertEquals("baosongle", name);
    }
}
