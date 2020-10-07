package baosongle.hashindexkvstore;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KVStoreServiceImpl implements KVStoreService {
    private final KVStoreConfig config;
    private final Map<String, Long> map;

    @Autowired
    public KVStoreServiceImpl(KVStoreConfig config) {
        this.config = config;
        this.map = new HashMap<>();
        this.loadFile();
    }

    private void loadFile() {
        try {
            File file = createFileIfNotExists();
            String line;
            long offset = 0;
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                while ((line = reader.readLine()) != null) {
                    String[] keyValue = parseKeyAndValue(line);
                    String key = keyValue[0];
                    String value = keyValue[1];
                    if (value.isBlank()) {
                        map.remove(key);
                    } else {
                        map.put(key, offset);
                    }
                    // 1 每一行末尾的 \n
                    offset += line.length() + 1;
                }
            }
        } catch (IOException | KVStoreException e) {
            log.error("read file {} failed with error", config.getDataFile(), e);
        }
    }

    private String[] parseKeyAndValue(String line) {
        if (line == null || line.isEmpty()) {
            throw new KVStoreException("line is empty");
        } else {
            String[] splitOut = line.split(",", 2);
            if (splitOut.length < 2) {
                throw new KVStoreException("line [" + line + "] don't contains separator comma");
            }
            return splitOut;
        }
    }

    @Override
    public synchronized String set(String key, String value) {
        if (key == null || key.isBlank()) {
            return null;
        }
        if (value == null || value.isBlank()) {
            return delete(key);
        }
        var old = get(key);
        map.put(key, append(key, value));
        return old;
    }

    private Long append(String key, String value) {
        try {
            File file = createFileIfNotExists();
            long offset = file.length();
            try (FileOutputStream outputStream = new FileOutputStream(file, true)) {
                String line = key + "," + value + "\n";
                byte[] bytes = line.getBytes(StandardCharsets.UTF_8);
                outputStream.write(bytes, 0, bytes.length);
                return offset;
            }
        } catch (IOException e) {
            String msg = String.format("append %s,%s to %s failed with error", key, value, config.getDataFile());
            throw new KVStoreException(msg, e);
        }
    }

    private File createFileIfNotExists() {
        File file = new File(config.getDataFile());
        try {
            if (!file.exists() && !file.getParentFile().mkdirs() && !file.createNewFile()) {
                throw new KVStoreException("create file " + config.getDataFile() +" failed with error");
            }
        } catch (IOException e) {
            throw new KVStoreException("create file " + config.getDataFile() +" failed with error", e);
        }
        return file;
    }

    @Override
    public synchronized String get(String key) {
        Long offset = map.get(key);
        if (offset == null) {
            return null;
        }
        try {
            File file = createFileIfNotExists();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                var skipped = reader.skip(offset);
                if (skipped < offset) {
                    log.error("skip {} failed with error", offset);
                    return null;
                }
                var line = reader.readLine();
                var keyValue = parseKeyAndValue(line);
                return keyValue[1];
            }
        } catch (IOException | KVStoreException e) {
            log.error("read file {} at {} failed with error", config.getDataFile(), offset, e);
            return null;
        }
    }

    @Override
    public synchronized String delete(String key) {
        var old = get(key);
        if (old == null || old.isBlank()) {
            return null;
        } else {
            try {
                append(key, "");
                map.remove(key);
                return old;
            } catch (KVStoreException e) {
                log.error("delete key {} failed with error", key, e);
                return null;
            }
        }
    }

    @Override
    public void clear() {
        try {
            File file = createFileIfNotExists();
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.print("");
                map.clear();
            }
        } catch (IOException | KVStoreException e) {
            log.error("clear file {} failed with error", config.getDataFile(), e);
        }
    }
}
