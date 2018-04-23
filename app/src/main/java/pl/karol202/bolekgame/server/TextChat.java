package pl.karol202.bolekgame.server;

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
	private Entry lastEntry;
	private boolean notification;
	
	TextChat()
	{
		entries = new ArrayList<>();
	}
	
	public void addEntry(String sender, String message, boolean newMessage)
	{
		Entry entry = new Entry(sender, message);
		entries.add(entry);
		if(newMessage) lastEntry = entry;
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
	
	public String getLastEntryString()
	{
		if(lastEntry == null) return null;
		StringBuilder builder = new StringBuilder();
		lastEntry.writeEntry(builder);
		return builder.toString();
	}
	
	public boolean isNotificationSet()
	{
		return notification;
	}
	
	public void setNotification(boolean enabled)
	{
		this.notification = enabled;
	}
}
