package pl.karol202.bolekgame.game.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.Screen;

public class ScreenMain extends Screen
{
	private class ActionsListener implements ActionManager.OnActionsUpdateListener
	{
		@Override
		public void onActionAdd()
		{
			actionAdapter.onActionAdd();
			new Handler().postDelayed(() -> recyclerActions.smoothScrollToPosition(actionAdapter.getItemCount() - 1), 20);
		}
		
		@Override
		public void onActionUpdate(int position)
		{
			actionAdapter.onActionUpdate(position);
		}
	}
	
	private RecyclerView recyclerActions;
	private View viewSpectatorModeTop;
	private View viewSpectatorModeBottom;
	private TextView textSpectatorMode;
	
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
		
		viewSpectatorModeTop = view.findViewById(R.id.view_spectator_mode_top);
		viewSpectatorModeBottom = view.findViewById(R.id.view_spectator_mode_bottom);
		textSpectatorMode = view.findViewById(R.id.text_spectator_mode);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle bundle)
	{
		super.onActivityCreated(bundle);
		if(actionsUpdateListener != null) return;
		actionsUpdateListener = new ActionsListener();
		
		actionManager = gameLogic.getActionManager();
		actionManager.addOnActionsUpdateListener(actionsUpdateListener);
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		actionAdapter.setActionManager(actionManager);
		
		setSpectatorMode(gameLogic.isSpectating());
	}
	
	private void setSpectatorMode(boolean enabled)
	{
		viewSpectatorModeTop.setVisibility(enabled ? View.VISIBLE : View.GONE);
		viewSpectatorModeBottom.setVisibility(enabled ? View.VISIBLE : View.GONE);
		textSpectatorMode.setVisibility(enabled ? View.VISIBLE : View.GONE);
	}
	
	@Override
	public void onDetach()
	{
		super.onDetach();
		if(actionManager == null) return;
		actionManager.removeOnActionsUpdateListener(actionsUpdateListener);
		actionAdapter.setContext(null);
	}
}
