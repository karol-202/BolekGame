package pl.karol202.bolekgame.game;

import com.vdurmont.emoji.EmojiParser;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import pl.karol202.bolekgame.client.Client;
import pl.karol202.bolekgame.client.inputpacket.InputPacketFailure;
import pl.karol202.bolekgame.client.outputpacket.*;
import pl.karol202.bolekgame.game.acts.Acts;
import pl.karol202.bolekgame.game.gameplay.Act;
import pl.karol202.bolekgame.game.gameplay.Position;
import pl.karol202.bolekgame.game.gameplay.Role;
import pl.karol202.bolekgame.game.gameplay.WinCause;
import pl.karol202.bolekgame.game.main.ActionManager;
import pl.karol202.bolekgame.game.main.VotingResult;
import pl.karol202.bolekgame.game.main.actions.*;
import pl.karol202.bolekgame.game.main.actions.ActionChoosePlayerOrActsChecking.Choose;
import pl.karol202.bolekgame.game.players.Player;
import pl.karol202.bolekgame.game.players.Players;
import pl.karol202.bolekgame.server.Users;
import pl.karol202.bolekgame.Logic;
import pl.karol202.bolekgame.server.TextChat;
import pl.karol202.bolekgame.voice.VoiceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLogic extends Logic<ActivityGame>
{
	private boolean spectating;
	private ActionManager actionManager;
	private Acts acts;
	private Players players;
	private TextChat textChat;
	private int serverCode;
	
	private boolean ignoreCollaboratorsRevealment;
	private boolean ignoreGameExit;
	private String primeMinisterCandidate;
	private ActionChoosePrimeMinister primeMinisterChooseAction;
	private ActionVoteOnPrimeMinister voteAction;
	private ActionChooseActs chooseActsAction;
	private boolean randomAct;
	private ActionCheckPlayer playerCheckAction;
	private boolean playerCheckNoDescription;
	private boolean actsCheckingExplicitly;
	private boolean choosingOfPresident;
	private ActionChoosePresident presidentChooseAction;
	private ActionLustrate lustrateAction;
	private String lustratedPlayer;
	private boolean gameEnd;
	
	GameLogic(Client client, boolean spectating, Users users, TextChat textChat, int serverCode, ImagesSet imagesSet)
	{
		super(client);
		this.spectating = spectating;
		
		this.actionManager = new ActionManager(imagesSet);
		
		this.players = new Players(users);
		this.players.addOnPlayersUpdateListener(new Players.OnPlayersUpdateListener() {
			@Override
			public void onPlayerAdd(Player player) { }
			
			@Override
			public void onPlayerRemove(int position, Player player)
			{
				onPlayerLeaved(player);
			}
			
			@Override
			public void onPlayerUpdate(int position) { }
		});
		
		this.acts = new Acts(players.getPlayersAmount());
		
		this.textChat = textChat;
		
		this.serverCode = serverCode;
	}
	
	void startVoiceCommunication(VoiceService voiceService)
	{
		voiceService.setUsers(players);
		voiceService.start();
	}
	
	@Override
	protected void setActivity(ActivityGame activity)
	{
		super.setActivity(activity);
		actionManager.setContext(activity);
	}
	
	@Override
	protected void suspendClient()
	{
		super.suspendClient();
		players.removeAllListeners();
	}
	
	private void choosePrimeMinister(Player primeMinister)
	{
		sendPacket(new OutputPacketSetPrimeMinister(primeMinister.getName()));
		primeMinisterChooseAction = null;
	}
	
	private void voteOnPrimeMinister(boolean vote)
	{
		sendPacket(new OutputPacketPrimeMinisterVote(vote));
	}
	
	private void dismissActByPresident(Act act)
	{
		sendPacket(new OutputPacketDismissActByPresident(act.name()));
	}
	
	private void dismissActByPrimeMinister(Act act)
	{
		sendPacket(new OutputPacketDismissActByPrimeMinister(act.name()));
	}
	
	private void requestVeto()
	{
		sendPacket(new OutputPacketVetoRequest());
	}
	
	private void respondOnVeto(boolean response)
	{
		sendPacket(new OutputPacketVetoResponse(response));
	}
	
	private void checkPlayer(Player player)
	{
		sendPacket(new OutputPacketCheckPlayerPresient(player.getName()));
	}
	
	private void choosePlayerOrActsChecking(Choose choose)
	{
		if(choose == Choose.PLAYER_CHECKING) playerCheckNoDescription = true;
		else if(choose == Choose.ACTS_CHECKING) actsCheckingExplicitly = true;
		int choice = choose == Choose.PLAYER_CHECKING ? 0 : 1;
		sendPacket(new OutputPacketChoosePlayerOrActsCheckingPresident(choice));
	}
	
	private void showCheckedActs(Choose choose, Act[] acts)
	{
		if(choose != Choose.ACTS_CHECKING) return;
		actionManager.addAction(new ActionActsCheckingResult(acts));
	}
	
	private void choosePresident(Player player)
	{
		sendPacket(new OutputPacketChoosePresident(player.getName()));
		presidentChooseAction = null;
	}
	
	private void lustrate(Player player)
	{
		sendPacket(new OutputPacketLustratePresident(player.getName()));
		lustrateAction = null;
	}
	
	void exit()
	{
		sendPacket(new OutputPacketExitGame());
		suspend();
	}
	
	void exitSpectatorMode()
	{
		sendPacket(new OutputPacketStopSpectating());
		suspend();
	}
	
	public void sendMessage(String message)
	{
		message = EmojiParser.removeAllEmojis(message);
		sendPacket(new OutputPacketMessage(message));
		textChat.addEntry(players.getLocalPlayerName(), message, true);
		executeOnActivity(ActivityGame::onTextChatUpdate);
	}
	
	public String getTextChatString()
	{
		return textChat.getTextChatString();
	}
	
	boolean isNotificationSet()
	{
		return textChat.isNotificationSet();
	}
	
	void setTextChatNotification(boolean enabled)
	{
		textChat.setNotification(enabled);
	}
	
	private void onPlayerLeaved(Player player)
	{
		if(!player.getName().equals(lustratedPlayer))
		{
			executeOnActivity(a -> a.onPlayerLeaved(player));
			actionManager.addAction(new ActionPlayerLeaved(actionManager, player.getName()));
		}
		lustratedPlayer = null;
	}
	
	private void exitActivity()
	{
		executeOnActivityInUIThread(ActivityGame::onGameExit);
	}
	
	@Override
	public void onDisconnect()
	{
		executeOnActivityInUIThread(ActivityGame::onDisconnect);
	}
	
	@Override
	public void onFailure(int problem)
	{
		if(problem == InputPacketFailure.PROBLEM_INVALID_USER) executeOnActivityInUIThread(ActivityGame::onInvalidUserError);
		else executeOnActivityInUIThread(ActivityGame::onError);
	}
	
	@Override
	public void onUsersUpdate(List<String> usernames, List<Boolean> readiness, List<String> addresses)
	{
		runInUIThread(() -> players.updateUsersList(usernames, readiness, addresses));
	}
	
	@Override
	public void onPlayersUpdated(List<String> updatedPlayers)
	{
		runInUIThread(() -> players.updatePlayersList(updatedPlayers));
	}
	
	@Override
	public void onMessage(String sender, String message, boolean newMessage)
	{
		textChat.addEntry(sender, message, newMessage);
		executeOnActivityInUIThread(ActivityGame::onTextChatUpdate);
	}
	
	@Override
	public void onRoleAssigned(Role role)
	{
		runInUIThread(() -> {
			players.setLocalPlayerRole(role);
			actionManager.addAction(new ActionRoleAssigned(actionManager, role));
			executeOnActivity(a -> a.onRoleAssigned(role));
		});
	}
	
	@Override
	public void onCollaboratorsRevealment(List<String> ministers, List<String> collaborators, String bolek)
	{
		runInUIThread(() -> {
			Map<String, Role> playerRoles = createRolesMap(ministers, collaborators, bolek);
			players.setPlayersRoles(playerRoles);
			if(!ignoreCollaboratorsRevealment)
				actionManager.addAction(new ActionCollaboratorsRevealment(actionManager, playerRoles));
			ignoreCollaboratorsRevealment = true;
		});
	}
	
	private Map<String, Role> createRolesMap(List<String> ministersNames, List<String> collaboratorsNames, String bolekName)
	{
		Map<String, Role> rolesMap = new HashMap<>();
		for(String minister : ministersNames) rolesMap.put(minister, Role.MINISTER);
		for(String collaborator : collaboratorsNames) rolesMap.put(collaborator, Role.COLLABORATOR);
		rolesMap.put(bolekName, Role.BOLEK);
		return rolesMap;
	}
	
	@Override
	public void onStackRefill(int totalActs)
	{
		runInUIThread(() -> {
			if(!acts.isIgnoringStackRefill()) actionManager.addAction(new ActionStackRefill(actionManager));
			acts.refillStack();
		});
	}
	
	@Override
	public void onPresidentAssignment(String president)
	{
		runInUIThread(() -> {
			String previousPresident = players.getPlayerAtPosition(Position.PRESIDENT);
			boolean again = president.equals(previousPresident);
			
			boolean amIPresident = president.equals(players.getLocalPlayerName());
			actionManager.addAction(new ActionPresidentAssigned(actionManager, president, amIPresident, again, choosingOfPresident));
			players.setPlayerPosition(president, Position.PRESIDENT);
			choosingOfPresident = false;
			
			actionManager.cancelAllActions();
		});
	}
	
	@Override
	public void onChoosePrimeMinisterRequest(boolean update, List<String> candidatesNames)
	{
		runInUIThread(() -> {
			List<Player> candidates = StreamSupport.stream(candidatesNames).map(players::findPlayer).collect(Collectors.toList());
			if(update && primeMinisterChooseAction != null)
				primeMinisterChooseAction.setCandidates(candidates);
			else
			{
				primeMinisterChooseAction = new ActionChoosePrimeMinister(actionManager, this::choosePrimeMinister, candidates);
				actionManager.addAction(primeMinisterChooseAction);
			}
		});
	}
	
	@Override
	public void onPrimeMinisterChoose(String primeMinister)
	{
		runInUIThread(() -> {
			primeMinisterCandidate = primeMinister;
			String president = players.getPlayerAtPosition(Position.PRESIDENT);
			boolean amIPresident = president.equals(players.getLocalPlayerName());
			boolean amIPrimeMinister = primeMinister.equals(players.getLocalPlayerName());
			actionManager.addAction(new ActionPrimeMinisterChosen(actionManager, president, primeMinister, amIPresident, amIPrimeMinister));
		});
	}
	
	@Override
	public void onVoteOnPrimeMinisterRequest()
	{
		runInUIThread(() -> {
			if(primeMinisterCandidate == null) return;
			voteAction = new ActionVoteOnPrimeMinister(actionManager, primeMinisterCandidate, this::voteOnPrimeMinister);
			actionManager.addAction(voteAction);
			primeMinisterCandidate = null;
		});
	}
	
	@Override
	public void onVotingResult(int upvotes, int totalVotes, boolean passed, List<String> upvoters)
	{
		runInUIThread(() -> {
			Map<Player, Boolean> voters = createVotersMap(upvoters);
			VotingResult votingResult = new VotingResult(passed, upvotes, totalVotes, voters);
			if(voteAction != null) voteAction.onVotingEnd(votingResult, true);
			else if(primeMinisterCandidate != null) //Spectating
			{
				voteAction = new ActionVoteOnPrimeMinister(actionManager, primeMinisterCandidate, null);
				voteAction.onVotingEnd(votingResult, false);
				actionManager.addAction(voteAction);
				primeMinisterCandidate = null;
			}
			voteAction = null;
		});
	}
	
	private Map<Player, Boolean> createVotersMap(List<String> upvoters)
	{
		return players.getPlayersStream().collect(Collectors.toMap(p -> p, p -> upvoters.contains(p.getName())));
	}
	
	@Override
	public void onPrimeMinisterAssigment(String primeMinister)
	{
		runInUIThread(() -> {
			players.setPlayerPosition(primeMinister, Position.PRIME_MINISTER);
			
			if(primeMinister.isEmpty()) return;
			boolean amIPrimeMinister = primeMinister.equals(players.getLocalPlayerName());
			actionManager.addAction(new ActionPrimeMinisterAssigned(actionManager, primeMinister, amIPrimeMinister));
		});
	}
	
	@Override
	public void onPollIndexChange(int pollIndex)
	{
		runInUIThread(() -> {
			if(pollIndex == 3) return;
			actionManager.addAction(new ActionPollIndexChange(pollIndex));
			acts.updatePollIndex(pollIndex);
			executeOnActivityInUIThread(ActivityGame::onPollIndexChange);
		});
	}
	
	@Override
	public void onRandomActPassed()
	{
		runInUIThread(() -> randomAct = true);
	}
	
	@Override
	public void onChooseActsPresidentRequest(Act[] acts)
	{
		runInUIThread(() -> actionManager.addAction(new ActionChooseActs(this::dismissActByPresident, null,
				false, acts)));
	}
	
	@Override
	public void onPresidentChoosingActs()
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT);
			actionManager.addAction(new ActionPresidentChoosingActs(actionManager, president));
		});
	}
	
	@Override
	public void onChooseActsPrimeMinisterRequest(Act[] acts)
	{
		runInUIThread(() -> actionManager.addAction(new ActionChooseActs(this::dismissActByPrimeMinister, this::requestVeto,
				false, acts)));
	}
	
	@Override
	public void onChooseActsOrVetoPrimeMinisterRequest(Act[] acts)
	{
		runInUIThread(() -> {
			chooseActsAction = new ActionChooseActs(this::dismissActByPrimeMinister, this::requestVeto, true, acts);
			actionManager.addAction(chooseActsAction);
		});
	}
	
	@Override
	public void onPrimeMinisterChoosingActs()
	{
		runInUIThread(() -> {
			String primeMinister = players.getPlayerAtPosition(Position.PRIME_MINISTER);
			actionManager.addAction(new ActionPrimeMinisterChoosingActs(actionManager, primeMinister));
		});
	}
	
	@Override
	public void onVetoRequest()
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT);
			String primeMinister = players.getPlayerAtPosition(Position.PRIME_MINISTER);
			boolean amIPresident = president.equals(players.getLocalPlayerName());
			boolean amIPrimeMinister = primeMinister.equals(players.getLocalPlayerName());
			if(amIPresident)
				actionManager.addAction(new ActionVetoRequestPresident(actionManager, primeMinister, this::respondOnVeto));
			else actionManager.addAction(new ActionVetoRequest(actionManager, primeMinister, amIPrimeMinister));
		});
	}
	
	@Override
	public void onVetoResponse(boolean accepted)
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT);
			String primeMinister = players.getPlayerAtPosition(Position.PRIME_MINISTER);
			boolean amIPresident = president.equals(players.getLocalPlayerName());
			boolean amIPrimeMinister = primeMinister.equals(players.getLocalPlayerName());
			if(amIPrimeMinister && chooseActsAction != null) chooseActsAction.setVetoResult(accepted);
			else if(!amIPresident) actionManager.addAction(new ActionVetoResponse(actionManager, president, accepted));
			chooseActsAction = null;
		});
	}
	
	@Override
	public void onActPass(int lustrationPassed, int antilustrationPassed)
	{
		runInUIThread(() -> {
			Act act = acts.updateActs(lustrationPassed, antilustrationPassed);
			if(act == null) return;
			if(randomAct) actionManager.addAction(new ActionRandomActPassed(act));
			else actionManager.addAction(new ActionActPassed(act));
			executeOnActivity(ActivityGame::onActsUpdate);
			randomAct = false;
		});
	}
	
	@Override
	public void onPresidentCheckingPlayer()
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT);
			actionManager.addAction(new ActionPresidentCheckingPlayer(actionManager, president));
		});
	}
	
	@Override
	public void onCheckPlayerPresidentRequest(boolean update, List<String> checkablePlayers)
	{
		runInUIThread(() -> {
			List<Player> candidates = StreamSupport.stream(checkablePlayers).map(players::findPlayer).collect(Collectors.toList());
			if(update && playerCheckAction != null)
				playerCheckAction.setPlayersToCheck(candidates);
			else
			{
				playerCheckAction = new ActionCheckPlayer(actionManager, this::checkPlayer, !playerCheckNoDescription, candidates);
				actionManager.addAction(playerCheckAction);
				playerCheckNoDescription = false;
			}
		});
	}
	
	@Override
	public void onPlayerCheckingResult(int resultInt)
	{
		runInUIThread(() -> {
			boolean result = resultInt == 0;
			if(playerCheckAction == null) return;
			playerCheckAction.onCheckingResult(result);
			playerCheckAction = null;
		});
	}
	
	@Override
	public void onPresidentCheckedPlayer(String checkedPlayer)
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT);
			boolean amICheckedPlayer = checkedPlayer.equals(players.getLocalPlayerName());
			actionManager.addAction(new ActionPresidentCheckedPlayer(actionManager, president, checkedPlayer, amICheckedPlayer));
		});
	}
	
	@Override
	public void onPresidentCheckingPlayerOrActs()
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT);
			actionManager.addAction(new ActionPresidentCheckingPlayerOrActs(actionManager, president));
		});
	}
	
	@Override
	public void onChoosePlayerOrActsCheckingPresidentRequest()
	{
		runInUIThread(() -> actionManager.addAction(new ActionChoosePlayerOrActsChecking(this::choosePlayerOrActsChecking, true)));
	}
	
	@Override
	public void onActsCheckingResult(Act[] acts)
	{
		runInUIThread(() -> {
			if(actsCheckingExplicitly) showCheckedActs(Choose.ACTS_CHECKING, acts);
			else actionManager.addAction(new ActionChoosePlayerOrActsChecking(c -> this.showCheckedActs(c, acts), false));
			actsCheckingExplicitly = false;
		});
	}
	
	@Override
	public void onPresidentCheckedActs()
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT);
			actionManager.addAction(new ActionPresidentCheckedActs(actionManager, president));
		});
	}
	
	@Override
	public void onPresidentChoosingPresident()
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT);
			actionManager.addAction(new ActionPresidentChoosingPresident(actionManager, president));
			choosingOfPresident = true;
		});
	}
	
	@Override
	public void onChoosePresidentRequest(boolean update, List<String> availablePlayers)
	{
		runInUIThread(() -> {
			List<Player> candidates = StreamSupport.stream(availablePlayers).map(players::findPlayer).collect(Collectors.toList());
			if(update && presidentChooseAction != null)
				presidentChooseAction.setCandidates(candidates);
			else
			{
				presidentChooseAction = new ActionChoosePresident(actionManager, this::choosePresident, candidates);
				actionManager.addAction(presidentChooseAction);
			}
		});
	}
	
	@Override
	public void onPresidentLustrating()
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT);
			actionManager.addAction(new ActionPresidentLustrating(actionManager, president));
		});
	}
	
	@Override
	public void onLustratePresidentRequest(boolean update, List<String> availablePlayers)
	{
		runInUIThread(() -> {
			List<Player> candidates = StreamSupport.stream(availablePlayers).map(players::findPlayer).collect(Collectors.toList());
			if(update && lustrateAction != null)
				lustrateAction.setAvailablePlayers(candidates);
			else
			{
				lustrateAction = new ActionLustrate(actionManager, this::lustrate, candidates);
				actionManager.addAction(lustrateAction);
			}
		});
	}
	
	@Override
	public void onYouAreLustrated()
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT);
			executeOnActivity(a -> a.onYouAreLustrated(president));
			ignoreGameExit = true;
		});
	}
	
	@Override
	public void onPresidentLustrated(String player, boolean wasBolek)
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT);
			boolean amIPresident = president.equals(players.getLocalPlayerName());
			actionManager.addAction(new ActionLustrationResult(actionManager, president, amIPresident, player, wasBolek));
			lustratedPlayer = player;
		});
	}
	
	@Override
	public void onWin(boolean ministers, WinCause cause)
	{
		runInUIThread(() -> {
			actionManager.addAction(new ActionWin(ministers, cause));
			gameEnd = true;
			ignoreCollaboratorsRevealment = false;
		});
	}
	
	@Override
	public void onGameExited()
	{
		suspendClient();
		runInUIThread(() -> {
			if(ignoreGameExit) return;
			if(!gameEnd) executeOnActivity(ActivityGame::onGameExit);
			else actionManager.addAction(new ActionEndGame(this::exitActivity));
		});
	}
	
	@Override
	public void onTooFewPlayers()
	{
		suspendClient();
		runInUIThread(() -> {
			executeOnActivity(ActivityGame::onTooFewPlayers);
			ignoreGameExit = true;
		});
	}
	
	public boolean isSpectating()
	{
		return spectating;
	}
	
	public ActionManager getActionManager()
	{
		return actionManager;
	}
	
	public Acts getActs()
	{
		return acts;
	}
	
	public Players getPlayers()
	{
		return players;
	}
	
	public int getServerCode()
	{
		return serverCode;
	}
	
	boolean willGameBeEndedAfterMyLeave()
	{
		return players.getPlayersAmount() == players.getMinUsers();
	}
}
