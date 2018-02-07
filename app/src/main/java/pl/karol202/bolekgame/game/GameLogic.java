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

import java.util.List;
import java.util.Map;

public class GameLogic extends Logic<ActivityGame>
{
	private ActionManager actionManager;
	private Acts acts;
	private Players players;
	private TextChat textChat;
	
	private boolean ignoreGameExit;
	private Player primeMinisterCandidate;
	private ActionVoteOnPrimeMinister votingAction;
	private boolean randomAct;
	
	GameLogic(Client client, TextChat textChat, String localPlayerName)
	{
		super(client);
		
		actionManager = new ActionManager();
		
		acts = new Acts();
		
		players = new Players(localPlayerName);
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
	}
	
	@Override
	protected void setActivity(ActivityGame activity)
	{
		super.setActivity(activity);
		actionManager.setContext(activity);
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
		actionManager.addAction(new ActionPlayerLeaved(actionManager, player.getName()));
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
		if(problem == InputPacketFailure.PROBLEM_INVALID_USER) runInUIThread(activity::onInvalidUserError);
		else runInUIThread(activity::onError);
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
		return StreamSupport.stream(players.getPlayers()).collect(Collectors.toMap(p -> p, p -> {
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
			boolean amIPresident = president.equals(players.getLocalPlayerName());
			actionManager.addAction(new ActionPresidentAssigned(actionManager, president, amIPresident));
			players.setPlayerPositionAndResetRest(president, Position.PRESIDENT);
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
			Map<Player, Boolean> voters = createVotersMap(upvoters);
			VotingResult votingResult = new VotingResult(passed, upvotes, totalVotes, voters);
			votingAction.onVotingEnd(votingResult);
			votingAction = null;
		});
	}
	
	private Map<Player, Boolean> createVotersMap(List<String> upvoters)
	{
		return StreamSupport.stream(players.getPlayers()).collect(Collectors.toMap(p -> p, p -> upvoters.contains(p.getName())));
	}
	
	@Override
	public void onPrimeMinisterAssigment(String primeMinister)
	{
		runInUIThread(() -> {
			boolean amIPrimeMinister = primeMinister.equals(players.getLocalPlayerName());
			actionManager.addAction(new ActionPrimeMinisterAssigned(actionManager, primeMinister, amIPrimeMinister));
			players.setPlayerPositionAndResetRest(primeMinister, Position.PRIME_MINISTER);
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
		runInUIThread(() -> actionManager.addAction(new ActionChooseActs(this::dismissActByPrimeMinister, this::requestVeto,
				true, acts)));
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
	
	}
	
	@Override
	public void onVetoResponse(boolean accepted)
	{
	
	}
	
	@Override
	public void onActPass(int lustrationPassed, int antilustrationPassed)
	{
		runInUIThread(() -> {
			Act act = acts.updateActs(lustrationPassed, antilustrationPassed);
			if(randomAct) actionManager.addAction(new ActionRandomActPassed(act));
			activity.onActsUpdate();
		});
	}
	
	@Override
	public void onPresidentCheckingPlayer()
	{
	
	}
	
	@Override
	public void onCheckPlayerPresidentRequest(boolean update, List<String> chechablePlayers)
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
