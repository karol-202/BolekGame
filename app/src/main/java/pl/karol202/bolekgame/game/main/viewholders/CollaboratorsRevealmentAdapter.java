package pl.karol202.bolekgame.game.main.viewholders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.gameplay.Role;

import java.util.Map;

public class CollaboratorsRevealmentAdapter extends RecyclerView.Adapter<CollaboratorsRevealmentAdapter.ViewHolder>
{
	class ViewHolder extends RecyclerView.ViewHolder
	{
		private TextView textPlayerName;
		private TextView textPlayerRole;
		
		ViewHolder(View view)
		{
			super(view);
			textPlayerName = view.findViewById(R.id.text_collaborators_revealment_player_name);
			textPlayerRole = view.findViewById(R.id.text_collaborators_revealment_player_role);
		}
		
		void bind(String player, Role role)
		{
			textPlayerName.setText(player);
			textPlayerRole.setText(role.getName());
		}
	}
	
	private Context context;
	private Map<String, Role> playerRoles;
	
	CollaboratorsRevealmentAdapter(Context context, Map<String, Role> playerRoles)
	{
		this.context = context;
		this.playerRoles = playerRoles;
	}
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.item_collaborators_revealment, parent, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position)
	{
		String[] playersArray = new String[playerRoles.size()];
		String player = playerRoles.keySet().toArray(playersArray)[position];
		Role role = playerRoles.get(player);
		holder.bind(player, role);
	}
	
	@Override
	public int getItemCount()
	{
		return playerRoles.size();
	}
}
