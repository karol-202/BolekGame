package pl.karol202.bolekgame.game;

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
import pl.karol202.bolekgame.game.players.Player;
import pl.karol202.bolekgame.game.players.Players;
import pl.karol202.bolekgame.server.Users;
import pl.karol202.bolekgame.utils.Logic;
import pl.karol202.bolekgame.utils.TextChat;
import pl.karol202.bolekgame.voice.VoiceService;

import java.util.List;
import java.util.Map;

public class GameLogic extends Logic<ActivityGame>
{
	private ActionManager actionManager;
	private Acts acts;
	private Players players;
	private TextChat textChat;
	private VoiceService voiceService;
	
	private Role role;
	private boolean ignoreGameExit;
	private Player primeMinisterCandidate;
	private ActionVoteOnPrimeMinister votingAction;
	private ActionChooseActs chooseActsAction;
	private boolean randomAct;
	private ActionCheckPlayer playerCheckAction;
	private boolean playerCheckNoDescription;
	private boolean choosingOfPresident;
	private String lustratedPlayerName;
	private boolean gameEnd;
	
	GameLogic(Client client, Users users, TextChat textChat)
	{
		super(client);
		
		this.actionManager = new ActionManager();
		
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
	}
	
	void startVoiceCommunication(VoiceService voiceService)
	{
		this.voiceService = voiceService;
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
	}
	
	private void voteOnPrimeMinister(boolean vote)
	{
		sendPacket(new OutputPacketPrimeMinisterVote(vote));
		votingAction.onVote(vote);
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
	
	private void choosePlayerOrActsChecking(ActionChoosePlayerOrActsChecking.Choose choose)
	{
		if(choose == ActionChoosePlayerOrActsChecking.Choose.PLAYER_CHECKING) playerCheckNoDescription = true;
		int choice = choose == ActionChoosePlayerOrActsChecking.Choose.PLAYER_CHECKING ? 0 : 1;
		sendPacket(new OutputPacketChoosePlayerOrActsCheckingPresident(choice));
	}
	
	private void choosePresident(Player player)
	{
		sendPacket(new OutputPacketChoosePresident(player.getName()));
	}
	
	private void lustrate(Player player)
	{
		sendPacket(new OutputPacketLustratePresident(player.getName()));
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
		if(!player.getName().equals(lustratedPlayerName))
		{
			activity.onPlayerLeaved(player);
			actionManager.addAction(new ActionPlayerLeaved(actionManager, player.getName()));
		}
		lustratedPlayerName = null;
	}
	
	@Override
	public void onDisconnect()
	{
		runInUIThread(activity::onDisconnect);
	}
	
	@Override
	public void onFailure(int problem)
	{
		if(problem == InputPacketFailure.PROBLEM_INVALID_USER) runInUIThread(activity::onInvalidUserError);
		else runInUIThread(activity::onError);
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
	public void onMessage(String sender, String message)
	{
		textChat.addEntry(sender, message);
		runInUIThread(() -> activity.onTextChatUpdate());
	}
	
	@Override
	public void onRoleAssigned(Role role)
	{
		runInUIThread(() -> {
			this.role = role;
			actionManager.addAction(new ActionRoleAssigned(actionManager, role));
			activity.onRoleAssigned(role);
		});
	}
	
	@Override
	public void onCollaboratorsRevealment(List<String> collaborators, String bolek)
	{
		runInUIThread(() -> {
			Map<Player, Role> playerRoles = createRolesMap(collaborators, bolek);
			actionManager.addAction(new ActionCollaboratorsRevealment(actionManager, playerRoles));
		});
	}
	
	private Map<Player, Role> createRolesMap(List<String> collaboratorsNames, String bolekName)
	{
		return players.getPlayersStream().collect(Collectors.toMap(p -> p, p -> {
			if(bolekName.equals(p.getName())) return Role.BOLEK;
			else if(collaboratorsNames.contains(p.getName())) return Role.COLLABORATOR;
			else return Role.MINISTER;
		}));
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
			Player previousPresident = players.getPlayerAtPosition(Position.PRESIDENT);
			boolean again = previousPresident != null && president.equals(previousPresident.getName());
			
			boolean amIPresident = president.equals(players.getLocalPlayerName());
			actionManager.addAction(new ActionPresidentAssigned(actionManager, president, amIPresident, again, choosingOfPresident));
			players.setPlayerPositionAndResetRest(president, Position.PRESIDENT);
			choosingOfPresident = false;
			
			actionManager.cancelAllActions();
		});
	}
	
	@Override
	public void onChoosePrimeMinisterRequest(boolean update, List<String> candidatesNames)
	{
		runInUIThread(() -> {
			List<Player> candidates = StreamSupport.stream(candidatesNames).map(players::findPlayer).collect(Collectors.toList());
			if(update && actionManager.getLastAction() instanceof ActionChoosePrimeMinister)
				((ActionChoosePrimeMinister) actionManager.getLastAction()).setCandidates(candidates);
			else actionManager.addAction(new ActionChoosePrimeMinister(actionManager, this::choosePrimeMinister, candidates));
		});
	}
	
	@Override
	public void onPrimeMinisterChoose(String primeMinister)
	{
		runInUIThread(() -> {
			primeMinisterCandidate = players.findPlayer(primeMinister);
			String president = players.getPlayerAtPosition(Position.PRESIDENT).getName();
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
			votingAction = new ActionVoteOnPrimeMinister(actionManager, primeMinisterCandidate, this::voteOnPrimeMinister);
			actionManager.addAction(votingAction);
			primeMinisterCandidate = null;
		});
	}
	
	@Override
	public void onVotingResult(int upvotes, int totalVotes, boolean passed, List<String> upvoters)
	{
		runInUIThread(() -> {
			if(votingAction == null) return;
			Map<Player, Boolean> voters = createVotersMap(upvoters);
			VotingResult votingResult = new VotingResult(passed, upvotes, totalVotes, voters);
			votingAction.onVotingEnd(votingResult);
			votingAction = null;
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
			players.setPlayerPositionAndResetRest(primeMinister, Position.PRIME_MINISTER);
			
			if(actionManager.getLastAction() instanceof ActionPresidentAssigned) return;
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
			activity.onPollIndexChange();
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
			String president = players.getPlayerAtPosition(Position.PRESIDENT).getName();
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
			String primeMinister = players.getPlayerAtPosition(Position.PRIME_MINISTER).getName();
			actionManager.addAction(new ActionPrimeMinisterChoosingActs(actionManager, primeMinister));
		});
	}
	
	@Override
	public void onVetoRequest()
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT).getName();
			String primeMinister = players.getPlayerAtPosition(Position.PRIME_MINISTER).getName();
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
			String president = players.getPlayerAtPosition(Position.PRESIDENT).getName();
			String primeMinister = players.getPlayerAtPosition(Position.PRIME_MINISTER).getName();
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
			activity.onActsUpdate();
			randomAct = false;
		});
	}
	
	@Override
	public void onPresidentCheckingPlayer()
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT).getName();
			actionManager.addAction(new ActionPresidentCheckingPlayer(actionManager, president));
		});
	}
	
	@Override
	public void onCheckPlayerPresidentRequest(boolean update, List<String> checkablePlayers)
	{
		runInUIThread(() -> {
			List<Player> candidates = StreamSupport.stream(checkablePlayers).map(players::findPlayer).collect(Collectors.toList());
			if(update && actionManager.getLastAction() instanceof ActionCheckPlayer)
				((ActionCheckPlayer) actionManager.getLastAction()).setPlayersToCheck(candidates);
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
			String president = players.getPlayerAtPosition(Position.PRESIDENT).getName();
			boolean amICheckedPlayer = checkedPlayer.equals(players.getLocalPlayerName());
			actionManager.addAction(new ActionPresidentCheckedPlayer(actionManager, president, checkedPlayer, amICheckedPlayer));
		});
	}
	
	@Override
	public void onPresidentCheckingPlayerOrActs()
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT).getName();
			actionManager.addAction(new ActionPresidentCheckingPlayerOrActs(actionManager, president));
		});
	}
	
	@Override
	public void onChoosePlayerOrActsCheckingPresidentRequest()
	{
		runInUIThread(() -> actionManager.addAction(new ActionChoosePlayerOrActsChecking(this::choosePlayerOrActsChecking)));
	}
	
	@Override
	public void onActsCheckingResult(Act[] acts)
	{
		runInUIThread(() -> actionManager.addAction(new ActionActsCheckingResult(acts)));
	}
	
	@Override
	public void onPresidentCheckedActs()
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT).getName();
			actionManager.addAction(new ActionPresidentCheckedActs(actionManager, president));
		});
	}
	
	@Override
	public void onPresidentChoosingPresident()
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT).getName();
			actionManager.addAction(new ActionPresidentChoosingPresident(actionManager, president));
			choosingOfPresident = true;
		});
	}
	
	@Override
	public void onChoosePresidentRequest(boolean update, List<String> availablePlayers)
	{
		runInUIThread(() -> {
			List<Player> candidates = StreamSupport.stream(availablePlayers).map(players::findPlayer).collect(Collectors.toList());
			if(update && actionManager.getLastAction() instanceof ActionChoosePresident)
				((ActionChoosePresident) actionManager.getLastAction()).setCandidates(candidates);
			else actionManager.addAction(new ActionChoosePresident(actionManager, this::choosePresident, candidates));
		});
	}
	
	@Override
	public void onPresidentLustrating()
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT).getName();
			actionManager.addAction(new ActionPresidentLustrating(actionManager, president));
		});
	}
	
	@Override
	public void onLustratePresidentRequest(boolean update, List<String> availablePlayers)
	{
		runInUIThread(() -> {
			List<Player> candidates = StreamSupport.stream(availablePlayers).map(players::findPlayer).collect(Collectors.toList());
			if(update && actionManager.getLastAction() instanceof ActionLustrate)
				((ActionLustrate) actionManager.getLastAction()).setAvailablePlayers(candidates);
			else actionManager.addAction(new ActionLustrate(actionManager, this::lustrate, candidates));
		});
	}
	
	@Override
	public void onYouAreLustrated()
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT).getName();
			activity.onYouAreLustrated(president);
			ignoreGameExit = true;
		});
	}
	
	@Override
	public void onPresidentLustrated(String player, boolean wasBolek)
	{
		runInUIThread(() -> {
			String president = players.getPlayerAtPosition(Position.PRESIDENT).getName();
			boolean amIPresident = president.equals(players.getLocalPlayerName());
			actionManager.addAction(new ActionLustrationResult(actionManager, president, amIPresident, player, wasBolek));
			lustratedPlayerName = player;
		});
	}
	
	@Override
	public void onWin(WinCause cause)
	{
		runInUIThread(() -> onGameEnd(true, cause));
	}
	
	@Override
	public void onLoss(WinCause cause)
	{
		runInUIThread(() -> onGameEnd(false, cause));
	}
	
	private void onGameEnd(boolean win, WinCause cause)
	{
		boolean ministersWin = win && role == Role.MINISTER ||
							  !win && role != Role.MINISTER;
		actionManager.addAction(new ActionWin(ministersWin, cause));
		gameEnd = true;
	}
	
	@Override
	public void onGameExited()
	{
		runInUIThread(() -> {
			if(ignoreGameExit) return;
			if(!gameEnd) activity.onGameExit();
			else actionManager.addAction(new ActionEndGame(activity::onGameExit));
		});
		suspendClient();
	}
	
	@Override
	public void onTooFewPlayers()
	{
		runInUIThread(() -> {
			activity.onTooFewPlayers();
			ignoreGameExit = true;
		});
		suspendClient();
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
	
	boolean willGameBeEndedAfterMyLeave()
	{
		return players.getPlayersAmount() == Users.MIN_USERS;
	}
}
