package pl.karol202.bolekgame.utils;

import android.os.AsyncTask;
import java8.util.stream.RefStreams;
import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.outputpacket.OutputPacket;

public class NetworkingAsyncTask extends AsyncTask<OutputPacket, Void, Void>
{
	private Client client;
	
	NetworkingAsyncTask(Client client)
	{
		this.client = client;
	}
	
	@Override
	protected Void doInBackground(OutputPacket... outputPackets)
	{
		RefStreams.of(outputPackets).forEach(client::sendPacket);
		return null;
	}
}
