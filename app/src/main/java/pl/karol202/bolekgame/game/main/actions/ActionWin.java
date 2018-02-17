package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.gameplay.WinCause;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderWin;

public class ActionWin implements Action
{
	private boolean ministersWin;
	private WinCause winCause;
	
	public ActionWin(boolean ministersWin, WinCause winCause)
	{
		this.ministersWin = ministersWin;
		this.winCause = winCause;
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_win;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderWin.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return ActionViewHolderWin::new;
	}
	
	public boolean isMinistersWin()
	{
		return ministersWin;
	}
	
	public WinCause getWinCause()
	{
		return winCause;
	}
}
