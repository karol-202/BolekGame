package pl.karol202.bolekgame.game;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketExitGame;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketMessage;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketPong;
import pl.karol202.bolekgame.client.outputpacket.OutputPacketSetPrimeMinister;
import pl.karol202.bolekgame.game.gameplay.Act;
import pl.karol202.bolekgame.game.gameplay.Position;
import pl.karol202.bolekgame.game.gameplay.Role;
import pl.karol202.bolekgame.game.gameplay.WinCause;
import pl.karol202.bolekgame.game.main.ActionManager;
import pl.karol202.bolekgame.game.main.actions.ActionChoosePrimeMinister;
import pl.karol202.bolekgame.game.main.actions.ActionCollaboratorsRevealment;
import pl.karol202.bolekgame.game.main.actions.ActionPresidentAssigned;
import pl.karol202.bolekgame.game.main.actions.ActionRoleAssigned;
import pl.karol202.bolekgame.game.players.LocalPlayer;
import pl.karol202.bolekgame.game.players.Player;
import pl.karol202.bolekgame.game.players.Players;
import pl.karol202.bolekgame.server.Users;
import pl.karol202.bolekgame.utils.Logic;
import pl.karol202.bolekgame.utils.TextChat;

import java.util.List;
import java.util.Map;

public class GameLogic extends Logic<ActivityGame>
{
	private Players players;
	private TextChat textChat;
	private ActionManager actionManager;
	private boolean ignoreGameExit;
	
	GameLogic(Client client, TextChat textChat, String localPlayerName)
	{
		super(client);
		
		players = new Players(new LocalPlayer(localPlayerName));
		players.addOnPlayersUpdateListener(new Players.OnPlayersUpdateListener() {
			@Override
			public void onPlayerAdd() { }
			
			@Override
			public void onPlayerRemove(int position, Player player)
			{
				onPlayerLeaved(player);
			}
			
			@Override
			public void onPlayerUpdate(int position) { }
		});
		
		this.textChat = textChat;
		
		actionManager = new ActionManager();
	}
	
	private void choosePrimeMinister(Player primeMinister)
	{
		sendPacket(new OutputPacketSetPrimeMinister(primeMinister.getName()));
	}
	
	void exit()
	{
		sendPacket(new OutputPacketExitGame());
		suspend();
	}
	
	public void sendMessage(String message)
	{
		sendPacket(new OutputPacketMessage(message));
		textChat.addEntry(players.getLocalPlayerName(), message);
		activity.onTextChatUpdate();
	}
	
	public String getTextChatString()
	{
		return textChat.getTextChatString();
	}
	
	private void onPlayerLeaved(Player player)
	{
		activity.onPlayerLeaved(player);
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
		runInUIThread(() -> players.updatePlayersList(updatedPlayers));
	}
	
	@Override
	public void onMessage(String sender, String message)
	{
		textChat.addEntry(sender, message);
		runInUIThread(() -> activity.onTextChatUpdate());
	}
	
	@Override
	public void onRoleAssigned(Role role)
	{
		runInUIThread(() -> {
			actionManager.addAction(new ActionRoleAssigned(activity, role));
			activity.onRoleAssigned(role);
		});
	}
	
	@Override
	public void onCollaboratorsRevealment(List<String> collaborators, String bolek)
	{
		runInUIThread(() -> {
			Map<Player, Role> playerRoles = createRolesMap(collaborators, bolek);
			actionManager.addAction(new ActionCollaboratorsRevealment(activity, playerRoles));
		});
	}
	
	private Map<Player, Role> createRolesMap(List<String> collaboratorsNames, String bolekName)
	{
		return StreamSupport.stream(players.getPlayers()).collect(Collectors.toMap(p -> p, p -> {
			if(bolekName.equals(p.getName())) return Role.BOLEK;
			else if(collaboratorsNames.contains(p.getName())) return Role.COLLABORATOR;
			else return Role.MINISTER;
		}));
	}
	
	@Override
	public void onStackRefill(int totalActs)
	{
	
	}
	
	@Override
	public void onPresidentAssignment(String president)
	{
		runInUIThread(() -> {
			boolean amIPresident = president.equals(players.getLocalPlayerName());
			if(amIPresident) actionManager.addAction(new ActionPresidentAssigned(activity));
			else actionManager.addAction(new ActionPresidentAssigned(activity, president));
			players.setPlayerPositionAndResetRest(president, Position.PRESIDENT);
		});
	}
	
	@Override
	public void onChoosePrimeMinisterRequest(List<String> candidatesNames)
	{
		runInUIThread(() -> {
			List<Player> candidates = StreamSupport.stream(candidatesNames).map(players::findPlayer).collect(Collectors.toList());
			actionManager.addAction(new ActionChoosePrimeMinister(activity, this::choosePrimeMinister, candidates));
		});
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
	
	public ActionManager getActionManager()
	{
		return actionManager;
	}
	
	boolean willGameBeEndedAfterMyLeave()
	{
		return players.getPlayersAmount() == Users.MIN_USERS;
	}
}
