package pl.karol202.bolekgame.ui.game;

import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.game.Act;
import pl.karol202.bolekgame.game.Role;
import pl.karol202.bolekgame.game.WinCause;

import java.util.List;

public class GameUI
{
	private ActivityGame activityGame;
	private Client client;
	
	public GameUI(ActivityGame activityGame, Client client)
	{
		this.activityGame = activityGame;
		this.client = client;
	}
	
	public void onFailure()
	{
	
	}
	
	public void onUsersUpdate(List<String> username)
	{
	
	}
	
	public void onRoleAssigned(Role role)
	{
	
	}
	
	public void onCollaboratorsRevealment(List<String> collaborators, String bolek)
	{
	
	}
	
	public void onStackRefill(int totalActs)
	{
	
	}
	
	public void onPresidentAssignment(String president)
	{
	
	}
	
	public void onChoosePrimeMinisterRequest(List<String> candidates)
	{
	
	}
	
	public void onPrimeMinisterChoose(String primeMinister)
	{
	
	}
	
	public void onVoteOnPrimeMinisterRequest()
	{
	
	}
	
	public void onVotingResult(int upvotes, int totalVotes, boolean passed, List<String> upvoters)
	{
	
	}
	
	public void onPrimeMinisterAssigment(String primeMinister)
	{
	
	}
	
	public void onPollIndexChange(int pollIndex)
	{
	
	}
	
	public void onRandomActPassed()
	{
	
	}
	
	public void onChooseActsPresidentRequest(Act[] acts)
	{
	
	}
	
	public void onPresidentChoosingActs()
	{
	
	}
	
	public void onChooseActsPrimeMinisterRequest(Act[] acts)
	{
	
	}
	
	public void onChooseActsOrVetoPrimeMinisterRequest(Act[] acts)
	{
	
	}
	
	public void onPrimeMinisterChoosingActs()
	{
	
	}
	
	public void onVetoRequest()
	{
	
	}
	
	public void onVetoResponse(boolean accepted)
	{
	
	}
	
	public void onActPass(int lustrationPassed, int antilustrationPassed)
	{
	
	}
	
	public void onPresidentCheckingPlayer()
	{
	
	}
	
	public void onCheckPlayerPresidentRequest(List<String> chechablePlayers)
	{
	
	}
	
	public void onPlayerCheckingResult(int result)
	{
	
	}
	
	public void onPresidentCheckedPlayer(String checkedPlayer)
	{
	
	}
	
	public void onPresidentCheckingPlayerOrActs()
	{
	
	}
	
	public void onChoosePlayerOrActsCheckingPresidentRequest()
	{
	
	}
	
	public void onActsCheckingResult(Act[] acts)
	{
	
	}
	
	public void onPresidentCheckedActs()
	{
	
	}
	
	public void onPresidentChoosingPresident()
	{
	
	}
	
	public void onChoosePresidentRequest()
	{
	
	}
	
	public void onPresidentLustrating()
	{
	
	}
	
	public void onLustratePresidentRequest()
	{
	
	}
	
	public void onYouAreLustrated()
	{
	
	}
	
	public void onPresidentLustrated(String player, boolean wasBolek)
	{
	
	}
	
	public void onWin(WinCause cause)
	{
	
	}
	
	public void onLoss(WinCause cause)
	{
	
	}
	
	public void onGameExited()
	{
	
	}
	
	public void onPlayersUpdated(List<String> players)
	{
	
	}
	
	public void onTooFewPlayers()
	{
	
	}
}
