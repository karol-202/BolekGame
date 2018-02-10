package pl.karol202.bolekgame.game.main.actions;

import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ActionManager.ActionUpdateCallback;
import pl.karol202.bolekgame.game.main.ContextSupplier;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderVetoRequest;

public class ActionVetoRequestPresident implements UpdatingAction
{
	public interface OnVetoResponseListener
	{
		void onVetoResponse(boolean response);
	}
	
	private ContextSupplier contextSupplier;
	private OnVetoResponseListener listener;
	private ActionUpdateCallback updateCallback;
	
	private String primeMinister;
	private boolean responsed;
	private boolean response;
	
	public ActionVetoRequestPresident(ContextSupplier contextSupplier, String primeMinister, OnVetoResponseListener listener)
	{
		this.contextSupplier = contextSupplier;
		this.primeMinister = primeMinister;
		this.listener = listener;
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_veto_request;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderVetoRequest.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return v -> new ActionViewHolderVetoRequest(v, contextSupplier.getContext());
	}
	
	public void makeADecision(boolean decision)
	{
		if(responsed) return;
		responsed = true;
		response = decision;
		if(listener != null) listener.onVetoResponse(response);
		if(updateCallback != null) updateCallback.updateAction();
	}
	
	public String getPrimeMinisterName()
	{
		return primeMinister;
	}
	
	public boolean isResponsed()
	{
		return responsed;
	}
	
	public boolean getResponse()
	{
		return response;
	}
	
	@Override
	public void setUpdateCallback(ActionUpdateCallback callback)
	{
		updateCallback = callback;
	}
}
