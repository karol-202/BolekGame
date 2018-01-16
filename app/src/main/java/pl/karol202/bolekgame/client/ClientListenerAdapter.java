package pl.karol202.bolekgame.client;

import pl.karol202.bolekgame.game.Act;
import pl.karol202.bolekgame.game.Role;
import pl.karol202.bolekgame.game.WinCause;

import java.util.List;

public class ClientListenerAdapter implements ClientListener
{
	@Override
	public void onDisconnect() { }
	
	@Override
	public void onFailure(int problem) { }
	
	@Override
	public void onLoggedIn(String serverName, int serverCode) { }
	
	@Override
	public void onLoggedOut() { }
	
	@Override
	public void onUsersUpdate(List<String> users, List<Boolean> readiness) { }
	
	@Override
	public void onServerStatusUpdate(boolean gameAvailable) { }
	
	@Override
	public void onMessage(String sender, String message) { }
	
	@Override
	public void onGameStart(List<String> players) { }
	
	@Override
	public void onRoleAssigned(Role role) { }
	
	@Override
	public void onCollaboratorsRevealment(List<String> collaborators, String bolek) { }
	
	@Override
	public void onStackRefill(int totalActs) { }
	
	@Override
	public void onPresidentAssignment(String president) { }
	
	@Override
	public void onChoosePrimeMinisterRequest(List<String> candidates) { }
	
	@Override
	public void onPrimeMinisterChoose(String primeMinister) { }
	
	@Override
	public void onVoteOnPrimeMinisterRequest() { }
	
	@Override
	public void onVotingResult(int upvotes, int totalVotes, boolean passed, List<String> upvoters) { }
	
	@Override
	public void onPrimeMinisterAssigment(String primeMinister) { }
	
	@Override
	public void onPollIndexChange(int pollIndex) { }
	
	@Override
	public void onRandomActPassed() { }
	
	@Override
	public void onChooseActsPresidentRequest(Act[] acts) { }
	
	@Override
	public void onPresidentChoosingActs() { }
	
	@Override
	public void onChooseActsPrimeMinisterRequest(Act[] acts) { }
	
	@Override
	public void onChooseActsOrVetoPrimeMinisterRequest(Act[] acts) { }
	
	@Override
	public void onPrimeMinisterChoosingActs() { }
	
	@Override
	public void onVetoRequest() { }
	
	@Override
	public void onVetoResponse(boolean accepted) { }
	
	@Override
	public void onActPass(int lustrationPassed, int antilustrationPassed) { }
	
	@Override
	public void onPresidentCheckingPlayer() { }
	
	@Override
	public void onCheckPlayerPresidentRequest(List<String> chechablePlayers) { }
	
	@Override
	public void onPlayerCheckingResult(int result) { }
	
	@Override
	public void onPresidentCheckedPlayer(String checkedPlayer) { }
	
	@Override
	public void onPresidentCheckingPlayerOrActs() { }
	
	@Override
	public void onChoosePlayerOrActsCheckingPresidentRequest() { }
	
	@Override
	public void onActsCheckingResult(Act[] acts) { }
	
	@Override
	public void onPresidentCheckedActs() { }
	
	@Override
	public void onPresidentChoosingPresident() { }
	
	@Override
	public void onChoosePresidentRequest() { }
	
	@Override
	public void onPresidentLustrating() { }
	
	@Override
	public void onLustratePresidentRequest() { }
	
	@Override
	public void onYouAreLustrated() { }
	
	@Override
	public void onPresidentLustrated(String player, boolean wasBolek) { }
	
	@Override
	public void onWin(WinCause cause) { }
	
	@Override
	public void onLoss(WinCause cause) { }
	
	@Override
	public void onGameExited() { }
	
	@Override
	public void onPlayersUpdated(List<String> players) { }
	
	@Override
	public void onTooFewPlayers() { }
}