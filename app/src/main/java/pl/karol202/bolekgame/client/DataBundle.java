package pl.karol202.bolekgame.client;

import java8.util.stream.Stream;
import java8.util.stream.StreamSupport;

import java.util.HashMap;
import java.util.Map;

public class DataBundle
{
	public class Entry<T>
	{
		private String key;
		private T value;
		
		Entry(String key, T value)
		{
			this.key = key;
			this.value = value;
		}
		
		public String getKey()
		{
			return key;
		}
		
		public T getValue()
		{
			return value;
		}
	}
	
	private Map<String, String> stringEntries;
	private Map<String, Integer> intEntries;
	private Map<String, Float> floatEntries;
	
	public DataBundle()
	{
		stringEntries = new HashMap<>();
		intEntries = new HashMap<>();
		floatEntries = new HashMap<>();
	}
	
	public String getString(String key, String defaultValue)
	{
		return stringEntries.containsKey(key) ? stringEntries.get(key) : defaultValue;
	}
	
	public int getInt(String key, int defaultValue)
	{
		return intEntries.containsKey(key) ? intEntries.get(key) : defaultValue;
	}
	
	public float getFloat(String key, float defaultValue)
	{
		return floatEntries.containsKey(key) ? floatEntries.get(key) : defaultValue;
	}
	
	public void putString(String key, String value)
	{
		stringEntries.put(key, value);
	}
	
	public void putInt(String key, int value)
	{
		intEntries.put(key, value);
	}
	
	public void putFloat(String key, float value)
	{
		floatEntries.put(key, value);
	}
	
	public Stream<Entry<String>> getStringEntriesStream()
	{
		return getEntriesStream(stringEntries);
	}
	
	public Stream<Entry<Integer>> getIntEntriesStream()
	{
		return getEntriesStream(intEntries);
	}
	
	public Stream<Entry<Float>> getFloatEntriesStream()
	{
		return getEntriesStream(floatEntries);
	}
	
	private <T> Stream<Entry<T>> getEntriesStream(Map<String, T> entries)
	{
		return StreamSupport.stream(entries.entrySet()).map(e -> new Entry<>(e.getKey(), e.getValue()));
	}
}