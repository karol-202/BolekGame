package pl.karol202.bolekgame.game.main;

import android.content.Context;
import pl.karol202.bolekgame.game.ImagesSet;
import pl.karol202.bolekgame.game.main.actions.Action;
import pl.karol202.bolekgame.game.main.actions.CancellableAction;
import pl.karol202.bolekgame.game.main.actions.UpdatingAction;

import java.util.ArrayList;
import java.util.List;

public class ActionManager implements ContextSupplier
{
	public interface ActionUpdateCallback
	{
		void updateAction();
	}
	
	interface OnActionsUpdateListener
	{
		void onActionAdd();
		
		void onActionUpdate(int position);
	}
	
	private Context context;
	private ImagesSet imagesSet;
	
	private List<Action> actions;
	private List<OnActionsUpdateListener> actionsUpdateListeners;
	
	public ActionManager(ImagesSet imagesSet)
	{
		this.imagesSet = imagesSet;
		
		actions = new ArrayList<>();
		actionsUpdateListeners = new ArrayList<>();
	}
	
	public void addAction(Action action)
	{
		if(action instanceof UpdatingAction) ((UpdatingAction) action).setUpdateCallback(() -> updateAction(action));
		actions.add(action);
		for(OnActionsUpdateListener listener : actionsUpdateListeners) listener.onActionAdd();
	}
	
	private void updateAction(Action action)
	{
		int position = actions.indexOf(action);
		for(OnActionsUpdateListener listener : actionsUpdateListeners) listener.onActionUpdate(position);
	}
	
	public void cancelAllActions()
	{
		for(Action action : actions) if(action instanceof CancellableAction) ((CancellableAction) action).cancel();
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
	
	@Override
	public Context getContext()
	{
		return context;
	}
	
	public void setContext(Context context)
	{
		this.context = context;
	}
	
	@Override
	public String getString(int resId, Object... formatArgs)
	{
		return context.getString(resId, formatArgs);
	}
	
	@Override
	public ImagesSet getImagesSet()
	{
		return imagesSet;
	}
}
