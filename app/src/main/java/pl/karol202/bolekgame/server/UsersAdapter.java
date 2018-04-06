package pl.karol202.bolekgame.server;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.settings.Settings;

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
		private ImageButton buttonUserSettings;
		private UserSettingsWindow settingsWindow;
		
		private User user;
		
		UserViewHolder(View view)
		{
			super(view);
			this.view = (ConstraintLayout) view;
			
			textUserName = view.findViewById(R.id.text_user_name);
			
			buttonUserReady = view.findViewById(R.id.button_user_ready);
			buttonUserReady.setOnClickListener(v -> onReadyButtonClick());
			
			buttonUserSettings = view.findViewById(R.id.button_user_settings);
			buttonUserSettings.setOnClickListener(v -> showSettingsWindow(settingsWindow, buttonUserSettings));
			
			settingsWindow = new UserSettingsWindow(view);
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
			if(user instanceof RemoteUser) settingsWindow.setUser((RemoteUser) user);
			
			textUserName.setText(user.getName());
			updateViews();
		}
		
		private void update()
		{
			TransitionManager.beginDelayedTransition(view);
			updateViews();
		}
		
		private void updateViews()
		{
			view.setBackgroundResource(user.isReady() ? R.drawable.background_item_checked : R.drawable.background_item);
			buttonUserReady.setVisibility(canBeMadeReady() ? View.VISIBLE: View.GONE);
			buttonUserSettings.setVisibility(Settings.isVoiceChatEnabled(context) && user instanceof RemoteUser ? View.VISIBLE : View.GONE);
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
	private UserSettingsWindow currentSettingsWindow;
	
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
	
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		if(viewType == VIEW_SUMMARY)
			return new UsersSummaryViewHolder(inflater.inflate(R.layout.item_users_summary, parent, false));
		else return new UserViewHolder(inflater.inflate(R.layout.item_user, parent, false));
	}
	
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position)
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
	
	private void showSettingsWindow(UserSettingsWindow settingsWindow, View anchor)
	{
		if(currentSettingsWindow != null && currentSettingsWindow.isShowing()) currentSettingsWindow.dismiss();
		currentSettingsWindow = settingsWindow;
		settingsWindow.show(anchor);
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
		if(currentSettingsWindow != null && currentSettingsWindow.getUser() == user) currentSettingsWindow.dismiss();
	}
	
	@Override
	public void onUsersUpdate()
	{
		for(int i = 0; i < getItemCount(); i++) notifyItemChanged(i);
	}
}
