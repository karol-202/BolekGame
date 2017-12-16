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
	
	public void onCollaboratorsRevealment(List<String> collaborators)
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
	
	public void onChooseActsPresidentRequest(Act[] acts)
	{
	
	}
	
	public void onPresidentChoosingActs()
	{
	
	}
	
	public void onChooseActsPrimeMinisterRequest(Act[] acts)
	{
	
	}
	
	public void onPrimeMinisterChoosingActs()
	{
	
	}
	
	public void onActPass(int lustrationPassed, int antilustrationPassed)
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
}
