package pl.karol202.bolekgame.client;

import pl.karol202.bolekgame.game.gameplay.Act;
import pl.karol202.bolekgame.game.gameplay.Role;
import pl.karol202.bolekgame.game.gameplay.WinCause;

import java.util.List;

public interface ClientListener
{
	void onDisconnect();
	
	void onFailure(int problem);
	
	void onLoggedIn(String serverName, int serverCode);
	
	void onLoggedOut();
	
	void onUsersUpdate(List<String> users, List<Boolean> readiness, List<String> addresses);
	
	void onServerStatusUpdate(boolean gameAvailable, int minUsers);
	
	void onMessage(String sender, String message, boolean newMessage);
	
	void onGameStart(List<String> players, byte[] imagesCode);
	
	void onRoleAssigned(Role role);
	
	void onCollaboratorsRevealment(List<String> ministers, List<String> collaborators, String bolek);
	
	void onStackRefill(int totalActs);
	
	void onPresidentAssignment(String president);
	
	void onChoosePrimeMinisterRequest(boolean update, List<String> candidates);
	
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
	
	void onCheckPlayerPresidentRequest(boolean update, List<String> checkablePlayers);
	
	void onPlayerCheckingResult(int result);
	
	void onPresidentCheckedPlayer(String checkedPlayer);
	
	void onPresidentCheckingPlayerOrActs();
	
	void onChoosePlayerOrActsCheckingPresidentRequest();
	
	void onPresidentCheckingActs();
	
	void onCheckActsPresidentRequest();
	
	void onActsCheckingResult(Act[] acts);
	
	void onPresidentCheckedActs();
	
	void onPresidentChoosingPresident();
	
	void onChoosePresidentRequest(boolean update, List<String> availablePlayers);
	
	void onPresidentLustrating();
	
	void onLustratePresidentRequest(boolean update, List<String> availablePlayers);
	
	void onYouAreLustrated();
	
	void onPresidentLustrated(String player, boolean wasBolek);
	
	void onWin(boolean ministers, WinCause cause);
	
	void onGameExited();
	
	void onPlayersUpdated(List<String> players);
	
	void onTooFewPlayers();
	
	void onSpectatingStart(byte[] imagesCode);
}