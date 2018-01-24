package pl.karol202.bolekgame.game.main.actions;

import android.content.Context;
import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.gameplay.Role;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderCollaboratorsRevealment;
import pl.karol202.bolekgame.game.players.Player;

import java.util.Map;

public class ActionCollaboratorsRevealment implements Action
{
	private Context context;
	private Map<Player, Role> playerRoles;
	
	public ActionCollaboratorsRevealment(Context context, Map<Player, Role> playerRoles)
	{
		this.context = context;
		this.playerRoles = playerRoles;
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_collaborators_revealment;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderCollaboratorsRevealment.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return v -> new ActionViewHolderCollaboratorsRevealment(v, context);
	}
	
	public Map<Player, Role> getPlayerRoles()
	{
		return playerRoles;
	}
}
