package pl.karol202.bolekgame.game;

import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketExitGame;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketPong;
import pl.karol202.bolekgame.server.Users;
import pl.karol202.bolekgame.utils.Logic;

import java.util.List;

public class GameLogic extends Logic<ActivityGame>
{
	private Players players;
	private boolean ignoreGameExit;
	
	GameLogic(Client client, String localPlayerName)
	{
		this.client = client;
		
		players = new Players(new LocalPlayer(localPlayerName));
		players.addOnPlayersUpdateListener(new Players.OnPlayersUpdateListener() {
			@Override
			public void onPlayerAdd()
			{ }
			
			@Override
			public void onPlayerRemove(int position, Player player)
			{
				onPlayerLeaved(player);
			}
		});
	}
	
	void exit()
	{
		sendPacket(new OutputPacketExitGame());
		suspend();
	}
	
	private void onPlayerLeaved(Player player)
	{
		runInUIThread(() -> activity.onPlayerLeaved(player));
	}
	
	@Override
	public void onDisconnect()
	{
		runInUIThread(activity::onDisconnect);
	}
	
	@Override
	public void onPing()
	{
		sendPacket(new OutputPacketPong());
	}
	
	@Override
	public void onFailure(int problem)
	{
		runInUIThread(activity::onError);
	}
	
	@Override
	public void onPlayersUpdated(List<String> updatedPlayers)
	{
		players.updatePlayersList(updatedPlayers);
	}
	
	@Override
	public void onRoleAssigned(Role role)
	{
	
	}
	
	@Override
	public void onCollaboratorsRevealment(List<String> collaborators, String bolek)
	{
	
	}
	
	@Override
	public void onStackRefill(int totalActs)
	{
	
	}
	
	@Override
	public void onPresidentAssignment(String president)
	{
	
	}
	
	@Override
	public void onChoosePrimeMinisterRequest(List<String> candidates)
	{
	
	}
	
	@Override
	public void onPrimeMinisterChoose(String primeMinister)
	{
	
	}
	
	@Override
	public void onVoteOnPrimeMinisterRequest()
	{
	
	}
	
	@Override
	public void onVotingResult(int upvotes, int totalVotes, boolean passed, List<String> upvoters)
	{
	
	}
	
	@Override
	public void onPrimeMinisterAssigment(String primeMinister)
	{
	
	}
	
	@Override
	public void onPollIndexChange(int pollIndex)
	{
	
	}
	
	@Override
	public void onRandomActPassed()
	{
	
	}
	
	@Override
	public void onChooseActsPresidentRequest(Act[] acts)
	{
	
	}
	
	@Override
	public void onPresidentChoosingActs()
	{
	
	}
	
	@Override
	public void onChooseActsPrimeMinisterRequest(Act[] acts)
	{
	
	}
	
	@Override
	public void onChooseActsOrVetoPrimeMinisterRequest(Act[] acts)
	{
	
	}
	
	@Override
	public void onPrimeMinisterChoosingActs()
	{
	
	}
	
	@Override
	public void onVetoRequest()
	{
	
	}
	
	@Override
	public void onVetoResponse(boolean accepted)
	{
	
	}
	
	@Override
	public void onActPass(int lustrationPassed, int antilustrationPassed)
	{
	
	}
	
	@Override
	public void onPresidentCheckingPlayer()
	{
	
	}
	
	@Override
	public void onCheckPlayerPresidentRequest(List<String> chechablePlayers)
	{
	
	}
	
	@Override
	public void onPlayerCheckingResult(int result)
	{
	
	}
	
	@Override
	public void onPresidentCheckedPlayer(String checkedPlayer)
	{
	
	}
	
	@Override
	public void onPresidentCheckingPlayerOrActs()
	{
	
	}
	
	@Override
	public void onChoosePlayerOrActsCheckingPresidentRequest()
	{
	
	}
	
	@Override
	public void onActsCheckingResult(Act[] acts)
	{
	
	}
	
	@Override
	public void onPresidentCheckedActs()
	{
	
	}
	
	@Override
	public void onPresidentChoosingPresident()
	{
	
	}
	
	@Override
	public void onChoosePresidentRequest()
	{
	
	}
	
	@Override
	public void onPresidentLustrating()
	{
	
	}
	
	@Override
	public void onLustratePresidentRequest()
	{
	
	}
	
	@Override
	public void onYouAreLustrated()
	{
		ignoreGameExit = true;
	}
	
	@Override
	public void onPresidentLustrated(String player, boolean wasBolek)
	{
	
	}
	
	@Override
	public void onWin(WinCause cause)
	{
		ignoreGameExit = true;
	}
	
	@Override
	public void onLoss(WinCause cause)
	{
		ignoreGameExit = true;
	}
	
	@Override
	public void onGameExited()
	{
		if(ignoreGameExit) return;
		runInUIThread(() -> activity.onGameExit());
	}
	
	@Override
	public void onTooFewPlayers()
	{
		ignoreGameExit = true;
		runInUIThread(activity::onTooFewPlayers);
	}
	
	public Players getPlayers()
	{
		return players;
	}
	
	boolean willGameBeEndedAfterMyLeave()
	{
		return players.getPlayersAmount() == Users.MIN_USERS;
	}
}
