package pl.karol202.bolekgame.client;

import pl.karol202.bolekgame.game.Act;
import pl.karol202.bolekgame.game.Role;
import pl.karol202.bolekgame.game.WinCause;

import java.util.List;

public interface ClientListener
{
	void onDisconnect();
	
	void onFailure(int problem);
	
	void onLoggedIn(String serverName, int serverCode);
	
	void onLoggedOut();
	
	void onUsersUpdate(List<String> users, List<Boolean> readiness);
	
	void onServerStatusUpdate(boolean gameAvailable);
	
	void onMessage(String sender, String message);
	
	void onGameStart(List<String> players);
	
	void onRoleAssigned(Role role);
	
	void onCollaboratorsRevealment(List<String> collaborators, String bolek);
	
	void onStackRefill(int totalActs);
	
	void onPresidentAssignment(String president);
	
	void onChoosePrimeMinisterRequest(List<String> candidates);
	
	void onPrimeMinisterChoose(String primeMinister);
	
	void onVoteOnPrimeMinisterRequest();
	
	void onVotingResult(int upvotes, int totalVotes, boolean passed, List<String> upvoters);
	
	void onPrimeMinisterAssigment(String primeMinister);
	
	void onPollIndexChange(int pollIndex);
	
	void onRandomActPassed();
	
	void onChooseActsPresidentRequest(Act[] acts);
	
	void onPresidentChoosingActs();
	
	void onChooseActsPrimeMinisterRequest(Act[] acts);
	
	void onChooseActsOrVetoPrimeMinisterRequest(Act[] acts);
	
	void onPrimeMinisterChoosingActs();
	
	void onVetoRequest();
	
	void onVetoResponse(boolean accepted);
	
	void onActPass(int lustrationPassed, int antilustrationPassed);
	
	void onPresidentCheckingPlayer();
	
	void onCheckPlayerPresidentRequest(List<String> chechablePlayers);
	
	void onPlayerCheckingResult(int result);
	
	void onPresidentCheckedPlayer(String checkedPlayer);
	
	void onPresidentCheckingPlayerOrActs();
	
	void onChoosePlayerOrActsCheckingPresidentRequest();
	
	void onActsCheckingResult(Act[] acts);
	
	void onPresidentCheckedActs();
	
	void onPresidentChoosingPresident();
	
	void onChoosePresidentRequest();
	
	void onPresidentLustrating();
	
	void onLustratePresidentRequest();
	
	void onYouAreLustrated();
	
	void onPresidentLustrated(String player, boolean wasBolek);
	
	void onWin(WinCause cause);
	
	void onLoss(WinCause cause);
	
	void onGameExited();
	
	void onPlayersUpdated(List<String> players);
	
	void onTooFewPlayers();
}