package pl.karol202.bolekgame.utils;

import java.util.ArrayList;
import java.util.List;

public class TextChat
{
	class Entry
	{
		private String sender;
		private String message;
		
		Entry(String sender, String message)
		{
			this.sender = sender;
			this.message = message;
		}
		
		void writeEntry(StringBuilder builder)
		{
			builder.append(sender);
			builder.append(": ");
			builder.append(message);
		}
	}
	
	private List<Entry> entries;
	
	public TextChat()
	{
		entries = new ArrayList<>();
	}
	
	public void addEntry(String sender, String message)
	{
		entries.add(new Entry(sender, message));
	}
	
	public String getTextChatString()
	{
		StringBuilder builder = new StringBuilder();
		for(Entry entry : entries)
		{
			entry.writeEntry(builder);
			builder.append("\n");
		}
		return builder.toString();
	}
}
