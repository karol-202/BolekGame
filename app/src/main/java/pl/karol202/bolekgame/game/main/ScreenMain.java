package pl.karol202.bolekgame.game.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.Screen;

public class ScreenMain extends Screen
{
	private RecyclerView recyclerActions;
	
	private ActionAdapter actionAdapter;
	private ActionManager.OnActionsUpdateListener actionsUpdateListener;
	
	private ActionManager actionManager;
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.screen_main, container, false);
		
		actionAdapter = new ActionAdapter(getActivity());
		
		recyclerActions = view.findViewById(R.id.recycler_actions);
		recyclerActions.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerActions.setAdapter(actionAdapter);
		
		return view;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		actionsUpdateListener = new ActionManager.OnActionsUpdateListener() {
			@Override
			public void onActionAdd()
			{
				actionAdapter.onActionAdd();
				recyclerActions.smoothScrollToPosition(actionAdapter.getItemCount() - 1);
			}
			
			@Override
			public void onActionUpdate(int position)
			{
				actionAdapter.onActionUpdate(position);
			}
		};
		
		actionManager = gameLogic.getActionManager();
		actionManager.addOnActionsUpdateListener(actionsUpdateListener);
		
		actionAdapter.setActionManager(actionManager);
	}
	
	@Override
	public void onDetach()
	{
		super.onDetach();
		actionManager.removeOnActionsUpdateListener(actionsUpdateListener);
		actionAdapter.setContext(null);
	}
}
