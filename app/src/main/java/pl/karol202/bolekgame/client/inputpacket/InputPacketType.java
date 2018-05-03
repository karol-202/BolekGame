package pl.karol202.bolekgame.client.inputpacket;

import java8.util.function.Supplier;
import java8.util.stream.RefStreams;

enum InputPacketType
{
	LOGGEDIN(InputPacketLoggedIn::new),
	LOGGEDOUT(InputPacketLoggedOut::new),
	FAILURE(InputPacketFailure::new),
	USERSUPDATE(InputPacketUsersUpdate::new),
	SERVERSTATUS(InputPacketServerStatus::new),
	MESSAGE(InputPacketMessage::new),
	GAMESTART(InputPacketGameStart::new),
	ROLEASSIGNED(InputPacketRoleAssigned::new),
	COLLABORATORSREVEALMENT(InputPacketCollaboratorsRevealment::new),
	STACKREFILLED(InputPacketStackRefilled::new),
	PRESIDENTASSIGNED(InputPacketPresidentAssigned::new),
	CHOOSEPRIMEMINISTER(InputPacketChoosePrimeMinister::new),
	PRIMEMINISTERCHOSEN(InputPacketPrimeMinisterChosen::new),
	VOTEONPRIMEMINISTER(InputPacketVoteOnPrimeMinister::new),
	VOTINGRESULT(InputPacketVotingResult::new),
	PRIMEMINISTERASSIGNED(InputPacketPrimeMinisterAssigned::new),
	POLLINDEXCHANGE(InputPacketPollIndexChange::new),
	RANDOMACT(InputPacketRandomAct::new),
	CHOOSEACTSPRESIDENT(InputPacketChooseActsPresident::new),
	PRESIDENTCHOOSINGACTS(InputPacketPresidentChoosingActs::new),
	CHOOSEACTSPRIMEMINISTER(InputPacketChooseActsPrimeMinister::new),
	CHOOSEACTSORVETOPRIMEMINISTER(InputPacketChooseActsOrVetoPrimeMinister::new),
	PRIMEMINISTERCHOOSINGACTS(InputPacketPrimeMinisterChoosingActs::new),
	VETOREQUEST(InputPacketVetoRequest::new),
	VETORESPONSE(InputPacketVetoResponse::new),
	ACTPASSED(InputPacketActPassed::new),
	PRESIDENTCHECKINGPLAYER(InputPacketPresidentCheckingPlayer::new),
	CHECKPLAYERPRESIDENT(InputPacketCheckPlayerPresident::new),
	PLAYERCHECKINGRESULT(InputPacketPlayerCheckingResult::new),
	PRESIDENTCHECKEDPLAYER(InputPacketPresidentCheckedPlayer::new),
	PRESIDENTCHECKINGPLAYERORACTS(InputPacketPresidentCheckingPlayerOrActs::new),
	CHOOSEPLAYERORACTSCHECKINGPRESIDENT(InputPacketChoosePlayerOrActsCheckingPresident::new),
	PRESIDENTCHECKINGACTS(InputPacketPresidentCheckingActs::new),
	CHECKACTSPRESIDENT(InputPacketCheckActsPresident::new),
	ACTSCHECKINGRESULT(InputPacketActsCheckingResult::new),
	PRESIDENTCHECKEDACTS(InputPacketPresidentCheckedActs::new),
	PRESIDENTCHOOSINGPRESIDENT(InputPacketPresidentChoosingPresident::new),
	CHOOSEPRESIDENT(InputPacketChoosePresident::new),
	PRESIDENTLUSTRATING(InputPacketPresidentLustrating::new),
	LUSTRATEPRESIDENT(InputPacketLustratePresident::new),
	YOUARELUSTRATED(InputPacketYouAreLustrated::new),
	PRESIDENTLUSTRATED(InputPacketPresidentLustrated::new),
	WIN(InputPacketWin::new),
	GAMEEXITED(InputPacketGameExited::new),
	PLAYERSUPDATED(InputPacketPlayersUpdated::new),
	TOOFEWPLAYERS(InputPacketTooFewPlayers::new),
	PING(InputPacketPing::new),
	SPECTATINGSTART(InputPacketSpectatingStart::new);
	
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