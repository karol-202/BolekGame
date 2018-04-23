package pl.karol202.bolekgame.game.main.viewholders;

import android.content.Context;
import android.support.transition.Scene;
import android.support.transition.TransitionManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionCheckPlayer;
import pl.karol202.bolekgame.view.ItemDivider;

public class ActionViewHolderCheckPlayer extends ActionViewHolder<ActionCheckPlayer>
{
	private abstract class State
	{
		View sceneView;
		Scene scene;
		
		State(LayoutInflater inflater)
		{
			sceneView = inflater.inflate(getSceneLayout(), scenesRoot, false);
			scene = new Scene(scenesRoot, sceneView);
		}
		
		abstract void bind(ActionCheckPlayer action);
		
		abstract int getSceneLayout();
		
		Scene getScene()
		{
			return scene;
		}
	}
	
	private class StateChoosing extends State
	{
		private TextView textAction;
		private RecyclerView recyclerPlayers;
		
		private PlayersToCheckAdapter adapter;
		
		StateChoosing(LayoutInflater layoutInflater)
		{
			super(layoutInflater);
			adapter = new PlayersToCheckAdapter(context);
			
			textAction = sceneView.findViewById(R.id.text_check_player_choosing);
			
			recyclerPlayers = sceneView.findViewById(R.id.recycler_players_to_check);
			recyclerPlayers.setLayoutManager(new LinearLayoutManager(context));
			recyclerPlayers.setAdapter(adapter);
			recyclerPlayers.addItemDecoration(new ItemDivider(context));
		}
		
		@Override
		void bind(ActionCheckPlayer action)
		{
			textAction.setText(action.hasDescription() ? R.string.action_check_player : R.string.action_check_player_no_description);
			adapter.setAction(action);
		}
		
		@Override
		int getSceneLayout()
		{
			return R.layout.scene_action_check_player_choosing;
		}
	}
	
	private class StateResult extends State
	{
		private TextView textAction;
		private ImageView imageResult;
		private TextView textResult;
		
		StateResult(LayoutInflater inflater)
		{
			super(inflater);
			textAction = sceneView.findViewById(R.id.text_check_player_result);
			imageResult = sceneView.findViewById(R.id.image_check_player_result);
			textResult = sceneView.findViewById(R.id.text_check_player_result_result);
		}
		
		@Override
		void bind(ActionCheckPlayer action)
		{
			textAction.setText(context.getString(R.string.action_check_player_result, action.getCheckedPlayerName()));
			imageResult.setImageResource(action.getResult() ? R.drawable.ic_thumb_positive : R.drawable.ic_thumb_negative);
			textResult.setText(context.getString(action.getResult() ? R.string.text_checking_result_positive : R.string.text_checking_result_negative, action.getCheckedPlayerName()));
		}
		
		@Override
		int getSceneLayout()
		{
			return R.layout.scene_action_check_player_result;
		}
	}
	
	private ViewGroup scenesRoot;
	
	private Context context;
	private State stateChoosing;
	private State stateResult;
	
	private ActionCheckPlayer action;
	private State currentState;
	
	public ActionViewHolderCheckPlayer(View view, Context context)
	{
		super(view);
		scenesRoot = view.findViewById(R.id.scenes_layout);
		
		LayoutInflater inflater = LayoutInflater.from(context);
		this.context = context;
		stateChoosing = new StateChoosing(inflater);
		stateResult = new StateResult(inflater);
	}
	
	@Override
	public void bind(ActionCheckPlayer action)
	{
		if(this.action == action) update();
		else bindNewAction(action);
	}
	
	private void update()
	{
		currentState = action.hasResult() ? stateResult : stateChoosing;
		Scene currentScene = currentState.getScene();
		currentState.bind(action);
		TransitionManager.go(currentScene);
	}
	
	private void bindNewAction(ActionCheckPlayer action)
	{
		this.action = action;
		
		currentState = action.hasResult() ? stateResult : stateChoosing;
		Scene currentScene = currentState.getScene();
		currentState.bind(action);
		currentScene.enter();
	}
}
