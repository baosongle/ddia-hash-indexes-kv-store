package baosongle.hashindexkvstore;

public interface KVService {
    String set(String key, String value);
    String get(String key);
    String delete(String key);
}
