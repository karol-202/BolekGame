package pl.karol202.bolekgame.server;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import pl.karol202.bolekgame.R;

class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> implements Users.OnUsersUpdateListener
{
	interface ServerStatusSupplier
	{
		boolean isGameInProgress();
	}
	
	interface OnUserReadyListener
	{
		void onUserReady();
	}
	
	abstract class ViewHolder extends RecyclerView.ViewHolder
	{
		ViewHolder(View view)
		{
			super(view);
		}
	}
	
	class UserViewHolder extends ViewHolder
	{
		private ConstraintLayout view;
		private TextView textUserName;
		private Button buttonUserReady;
		
		private User user;
		
		UserViewHolder(View view)
		{
			super(view);
			this.view = (ConstraintLayout) view;
			textUserName = view.findViewById(R.id.text_user_name);
			buttonUserReady = view.findViewById(R.id.button_user_ready);
			buttonUserReady.setOnClickListener(v -> onReadyButtonClick());
		}
		
		private void onReadyButtonClick()
		{
			if(!(user instanceof LocalUser)) return;
			userReadyListener.onUserReady();
			onUsersUpdate();
		}
		
		void bind(User user)
		{
			if(this.user == user) update();
			else bindNewUser(user);
		}
		
		private void bindNewUser(User user)
		{
			this.user = user;
			view.setBackgroundResource(user.isReady() ? R.drawable.background_item_checked : R.drawable.background_item);
			textUserName.setText(user.getName());
			buttonUserReady.setVisibility(canBeMadeReady() ? View.VISIBLE : View.GONE);
		}
		
		private void update()
		{
			TransitionManager.beginDelayedTransition(view);
			view.setBackgroundResource(user.isReady() ? R.drawable.background_item_checked : R.drawable.background_item);
			buttonUserReady.setVisibility(canBeMadeReady() ? View.VISIBLE: View.GONE);
		}
		
		private boolean canBeMadeReady()
		{
			return user instanceof LocalUser && !serverStatusSupplier.isGameInProgress() && users.areThereEnoughUsers() &&
				   !user.isReady();
		}
	}
	
	class UsersSummaryViewHolder extends ViewHolder
	{
		private TextView textUsersAmount;
		private TextView textReadyUsersAmount;
		
		UsersSummaryViewHolder(View view)
		{
			super(view);
			textUsersAmount = view.findViewById(R.id.text_users_amount);
			textReadyUsersAmount = view.findViewById(R.id.text_ready_users_amount);
		}
		
		void bind()
		{
			textUsersAmount.setText(context.getString(R.string.text_users_amount, users.getUsersAmount()));
			if(serverStatusSupplier.isGameInProgress()) textReadyUsersAmount.setText(R.string.text_game_in_progress);
			else if(users.areThereEnoughUsers()) textReadyUsersAmount.setText(context.getString(R.string.text_ready_users_amount, users.getReadyUsersAmount()));
			else textReadyUsersAmount.setText(R.string.text_too_few_users);
		}
	}
	
	private static final int VIEW_USER = 0;
	private static final int VIEW_SUMMARY = 1;
	
	private Context context;
	private Users users;
	private ServerStatusSupplier serverStatusSupplier;
	private OnUserReadyListener userReadyListener;
	
	UsersAdapter(Context context, Users users, ServerStatusSupplier serverStatusSupplier, OnUserReadyListener userReadyListener)
	{
		this.context = context;
		this.users = users;
		this.serverStatusSupplier = serverStatusSupplier;
		this.userReadyListener = userReadyListener;
		users.addOnUsersUpdateListener(this);
	}
	
	void onDestroy()
	{
		users.removeOnUsersUpdateListener(this);
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		if(viewType == VIEW_USER)
			return new UserViewHolder(inflater.inflate(R.layout.item_user, parent, false));
		else if(viewType == VIEW_SUMMARY)
			return new UsersSummaryViewHolder(inflater.inflate(R.layout.item_users_summary, parent, false));
		else return null;
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		if(position == 0) ((UsersSummaryViewHolder) holder).bind();
		else ((UserViewHolder) holder).bind(users.getUser(position - 1));
	}
	
	@Override
	public int getItemCount()
	{
		return users.getUsersAmount() + 1;
	}
	
	@Override
	public int getItemViewType(int position)
	{
		return position == 0 ? VIEW_SUMMARY : VIEW_USER;
	}
	
	@Override
	public void onUserAdd(User user)
	{
		notifyItemInserted(users.getUsersAmount());
		for(int i = 0; i < users.getUsersAmount(); i++) notifyItemChanged(i);
	}
	
	@Override
	public void onUserRemove(User user, int position)
	{
		notifyItemRemoved(position);
		for(int i = 0; i < users.getUsersAmount() + 1; i++) notifyItemChanged(i);
	}
	
	@Override
	public void onUsersUpdate()
	{
		for(int i = 0; i < getItemCount(); i++) notifyItemChanged(i);
	}
}
