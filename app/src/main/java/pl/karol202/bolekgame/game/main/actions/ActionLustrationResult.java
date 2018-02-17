package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ContextSupplier;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderLustrationResult;

public class ActionLustrationResult implements Action
{
	private ContextSupplier contextSupplier;
	
	private String president;
	private boolean amIPresident;
	private String lustratedPlayer;
	private boolean wasBolek;
	
	public ActionLustrationResult(ContextSupplier contextSupplier, String president, boolean amIPresident, String lustratedPlayer, boolean wasBolek)
	{
		this.contextSupplier = contextSupplier;
		
		this.president = president;
		this.amIPresident = amIPresident;
		this.lustratedPlayer = lustratedPlayer;
		this.wasBolek = wasBolek;
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_lustration_result;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderLustrationResult.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return v -> new ActionViewHolderLustrationResult(v, contextSupplier.getContext());
	}
	
	public String getPresidentName()
	{
		return president;
	}
	
	public boolean amIPresident()
	{
		return amIPresident;
	}
	
	public String getLustratedPlayerName()
	{
		return lustratedPlayer;
	}
	
	public boolean wasPlayerBolek()
	{
		return wasBolek;
	}
}
