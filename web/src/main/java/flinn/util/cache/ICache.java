package flinn.util.cache;

public interface ICache<K, V> {
	void put(K key, V value);

	V get(K key);

	void delete(K key);
	
	void deleteAll();

	void flush();
}