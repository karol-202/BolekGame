package pl.karol202.bolekgame.game.main;

import java.util.ArrayList;
import java.util.List;

public class ActionManager
{
	interface OnActionsUpdateListener
	{
		void onActionAdd();
		
		void onActionUpdate(int position);
	}
	
	private List<Action> actions;
	private List<OnActionsUpdateListener> actionsUpdateListeners;
	
	public ActionManager()
	{
		actions = new ArrayList<>();
		actionsUpdateListeners = new ArrayList<>();
	}
	
	public void addAction(Action action)
	{
		actions.add(action);
		for(OnActionsUpdateListener listener : actionsUpdateListeners) listener.onActionAdd();
	}
	
	public void updateAction(Action action)
	{
		int position = actions.indexOf(action);
		for(OnActionsUpdateListener listener : actionsUpdateListeners) listener.onActionUpdate(position);
	}
	
	boolean containsAction(int position)
	{
		return actions.size() > position;
	}
	
	Action getAction(int position)
	{
		return actions.get(position);
	}
	
	int getActionsAmount()
	{
		return actions.size();
	}
	
	void addOnActionsUpdateListener(OnActionsUpdateListener actionsUpdateListener)
	{
		actionsUpdateListeners.add(actionsUpdateListener);
	}
	
	void removeOnActionsUpdateListener(OnActionsUpdateListener actionsUpdateListener)
	{
		actionsUpdateListeners.remove(actionsUpdateListener);
	}
}
