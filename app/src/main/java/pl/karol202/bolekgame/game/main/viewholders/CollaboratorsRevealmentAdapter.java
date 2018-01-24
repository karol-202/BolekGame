package pl.karol202.bolekgame.game.main.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.gameplay.Role;
import pl.karol202.bolekgame.game.players.Player;

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
		
		void bind(Player player, Role role)
		{
			textPlayerName.setText(player.getName());
			textPlayerRole.setText(role.getName());
		}
	}
	
	private Context context;
	private Map<Player, Role> playerRoles;
	
	CollaboratorsRevealmentAdapter(Context context, Map<Player, Role> playerRoles)
	{
		this.context = context;
		this.playerRoles = playerRoles;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.item_collaborators_revealment, parent, false);
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		Player[] playersArray = new Player[playerRoles.size()];
		Player player = playerRoles.keySet().toArray(playersArray)[position];
		Role role = playerRoles.get(player);
		holder.bind(player, role);
	}
	
	@Override
	public int getItemCount()
	{
		return playerRoles.size();
	}
}
