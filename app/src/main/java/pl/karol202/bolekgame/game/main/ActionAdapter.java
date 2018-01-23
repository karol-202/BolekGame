package pl.karol202.bolekgame.game.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java8.util.function.Function;

import java.util.ArrayList;
import java.util.List;

public class ActionAdapter extends RecyclerView.Adapter<ActionViewHolder> implements ActionManager.OnActionsUpdateListener
{
	private Context context;
	private ActionManager actionManager;
	private List<Integer> layoutIdList;
	private List<Class<? extends ActionViewHolder>> viewHolderClassList;
	private List<Function<View, ? extends ActionViewHolder>> viewHolderCreatorsList;
	
	ActionAdapter(Context context)
	{
		this.context = context;
		
		layoutIdList = new ArrayList<>();
		viewHolderClassList = new ArrayList<>();
		viewHolderCreatorsList = new ArrayList<>();
	}
	
	@Override
	public ActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		int layout = layoutIdList.get(viewType);
		View view = createView(layout, parent);
		return viewHolderCreatorsList.get(viewType).apply(view);
	}
	
	private View createView(int layout, ViewGroup parent)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		return inflater.inflate(layout, parent, false);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onBindViewHolder(ActionViewHolder holder, int position)
	{
		if(actionManager == null || !actionManager.containsAction(position)) return;
		Action action = actionManager.getAction(position);
		holder.bind(action);
	}
	
	@Override
	public int getItemCount()
	{
		return actionManager != null ? actionManager.getActionsAmount() : 0;
	}
	
	@Override
	public int getItemViewType(int position)
	{
		if(actionManager == null || !actionManager.containsAction(position)) return 0;
		Action action = actionManager.getAction(position);
		Class<? extends ActionViewHolder> viewHolderClass = action.getViewHolderClass();
		if(viewHolderClassList.contains(viewHolderClass)) return viewHolderClassList.indexOf(viewHolderClass);
		layoutIdList.add(action.getViewHolderLayout());
		viewHolderClassList.add(viewHolderClass);
		viewHolderCreatorsList.add(action.getViewHolderCreator());
		return viewHolderClassList.indexOf(viewHolderClass);
	}
	
	@Override
	public void onActionAdd()
	{
		notifyItemInserted(actionManager.getActionsAmount() - 1);
	}
	
	@Override
	public void onActionUpdate(int position)
	{
		notifyItemChanged(position);
	}
	
	void setActionManager(ActionManager actionManager)
	{
		this.actionManager = actionManager;
		notifyDataSetChanged();
	}
}
