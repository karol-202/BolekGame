package pl.karol202.bolekgame.client.inputpacket;

import java8.util.function.Supplier;
import java8.util.stream.RefStreams;

enum InputPacketType
{
	LOGGEDIN(InputPacketLoggedIn::new),
	LOGGEDOUT(InputPacketLoggedOut::new),
	FAILURE(InputPacketFailure::new),
	USERSUPDATE(InputPacketUsersUpdate::new),
	GAMESTART(InputPacketGameStart::new),
	SETREADY(InputPacketSetReady::new),
	ROLEASSIGNED(InputPacketRoleAssigned::new),
	COLLABORATORSREVEALMENT(InputPacketCollaboratorsRevealment::new),
	PRESIDENTASSIGNED(InputPacketPresidentAssigned::new),
	CHOOSEPRIMEMINISTER(InputPacketChoosePrimeMinister::new),
	PRIMEMINISTERCHOSEN(InputPacketPrimeMinisterChoosen::new),
	VOTEONPRIMEMINISTER(InputPacketVoteOnPrimeMinister::new),
	VOTINGRESULT(InputPacketVotingResult::new),
	PRIMEMINISTERASSIGNED(InputPacketPrimeMinisterAssigned::new),
	CHOOSEACTSPRESIDENT(InputPacketChooseActsPresident::new),
	PRESIDENTCHOOSINGACTS(InputPacketPresidentChoosingActs::new),
	CHOOSEACTSPRIMEMINISER(InputPacketChooseActsPrimeMinister::new),
	PRIMEMINISTERCHOOSINGACTS(InputPacketPrimeMinisterChoosingActs::new),
	ACTPASSED(InputPacketActPassed::new),
	WIN(InputPacketWin::new),
	LOSS(InputPacketLoss::new),
	GAMEEXITED(InputPacketGameExited::new);
	
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