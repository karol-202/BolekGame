package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.gameplay.Act;
import pl.karol202.bolekgame.game.main.ActionManager;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderChooseActs;

public class ActionChooseActs implements UpdatingAction
{
	public interface OnActDismissListener
	{
		void onActDismiss(Act act);
	}
	
	public interface OnVetoRequestListener
	{
		void onVetoRequest();
	}
	
	private OnActDismissListener actListener;
	private OnVetoRequestListener vetoListener;
	private ActionManager.ActionUpdateCallback updateCallback;
	
	private boolean vetoApplicable;
	private Act[] acts;
	private boolean[] actSelection;
	private boolean chosen;
	private boolean vetoRequested;
	private boolean vetoResponsed;
	private boolean vetoAccepted;
	
	public ActionChooseActs(OnActDismissListener actListener, OnVetoRequestListener vetoListener, boolean vetoApplicable, Act[] acts)
	{
		this.actListener = actListener;
		this.vetoListener = vetoListener;
		
		if(acts.length != 2 && acts.length != 3) return;
		this.vetoApplicable = vetoApplicable;
		this.acts = acts;
		this.actSelection = new boolean[acts.length];
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_choose_acts;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderChooseActs.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return ActionViewHolderChooseActs::new;
	}
	
	public void dismissAct(Act act)
	{
		if(!canActBeDismissed()) return;
		chosen = true;
		if(actListener != null) actListener.onActDismiss(act);
		if(updateCallback != null) updateCallback.updateAction();
	}
	
	public void requestVeto()
	{
		if(!vetoApplicable || vetoRequested) return;
		vetoRequested = true;
		for(int i = 0; i < actSelection.length; i++) actSelection[i] = false;
		if(vetoListener != null) vetoListener.onVetoRequest();
		if(updateCallback != null) updateCallback.updateAction();
	}
	
	public boolean isVetoApplicable()
	{
		return vetoApplicable;
	}
	
	public boolean isPresidentAction()
	{
		return acts.length == 3;
	}
	
	public Act getAct(int index)
	{
		return acts[index];
	}
	
	public int getSelectedActsAmount()
	{
		int amount = 0;
		for(boolean b : actSelection) if(b) amount++;
		return amount;
	}
	
	public boolean isActSelected(int index)
	{
		return actSelection[index];
	}
	
	public void setActSelection(int index, boolean selection)
	{
		actSelection[index] = selection;
	}
	
	public boolean isChosen()
	{
		return chosen;
	}
	
	public boolean canActBeDismissed()
	{
		return !(chosen || (vetoRequested && !vetoResponsed) || (vetoResponsed && vetoAccepted));
	}
	
	public boolean isVetoRequested()
	{
		return vetoRequested;
	}
	
	public boolean isVetoResponsed()
	{
		return vetoResponsed;
	}
	
	public boolean isVetoAccepted()
	{
		return vetoAccepted;
	}
	
	public void setVetoResult(boolean accepted)
	{
		if(!vetoRequested) return;
		vetoResponsed = true;
		vetoAccepted = accepted;
		if(updateCallback != null) updateCallback.updateAction();
	}
	
	@Override
	public void setUpdateCallback(ActionManager.ActionUpdateCallback callback)
	{
		updateCallback = callback;
	}
}
