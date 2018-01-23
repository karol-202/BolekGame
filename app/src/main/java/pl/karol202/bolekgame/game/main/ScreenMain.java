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
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		actionManager = gameLogic.getActionManager();
		actionManager.addOnActionsUpdateListener(actionAdapter);
		actionAdapter.setActionManager(actionManager);
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if(actionManager != null) actionManager.removeOnActionsUpdateListener(actionAdapter);
	}
}
