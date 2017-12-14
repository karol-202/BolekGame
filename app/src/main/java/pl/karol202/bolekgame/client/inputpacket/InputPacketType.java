package pl.karol202.bolekgame.client.inputpacket;

import java8.util.function.Supplier;
import java8.util.stream.RefStreams;

enum InputPacketType
{
	;
	
	private Supplier<InputPacket> packetSupplier;
	
	InputPacketType(Supplier<InputPacket> packetSupplier)
	{
		this.packetSupplier = packetSupplier;
	}
	
	InputPacket createPacket()
	{
		return packetSupplier.get();
	}
	
	static InputPacketType getPacketTypeByName(String name)
	{
		return RefStreams.of(values()).filter(t -> t.name().equals(name)).findAny().orElse(null);
	}
}