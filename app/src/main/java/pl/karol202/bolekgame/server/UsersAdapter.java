package pl.karol202.bolekgame.server;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import pl.karol202.bolekgame.R;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>
{
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
			user.setReady(true);
			update(true);
		}
		
		void bind(User user, boolean enoughUsers)
		{
			if(this.user == user) update(enoughUsers);
			else bindNewUser(user, enoughUsers);
		}
		
		private void bindNewUser(User user, boolean enoughUsers)
		{
			this.user = user;
			view.setBackgroundColor(user.isReady() ? getReadyUserColor() : Color.TRANSPARENT);
			textUserName.setText(user.getName());
			buttonUserReady.setVisibility(isLocalUser() && enoughUsers && !user.isReady() ? View.VISIBLE : View.GONE);
		}
		
		private void update(boolean enoughUsers)
		{
			TransitionManager.beginDelayedTransition(view);
			view.setBackgroundColor(user.isReady() ? getReadyUserColor() : Color.TRANSPARENT);
			buttonUserReady.setVisibility(isLocalUser() && enoughUsers && !user.isReady() ? View.VISIBLE: View.GONE);
		}
		
		private int getReadyUserColor()
		{
			return ResourcesCompat.getColor(context.getResources(), R.color.ready_user_background, null);
		}
		
		private boolean isLocalUser()
		{
			return user == users.getLocalUser();
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
			textReadyUsersAmount.setText(context.getString(R.string.text_ready_users_amount, users.getReadyUsersAmount()));
		}
	}
	
	private static final int VIEW_USER = 0;
	private static final int VIEW_SUMMARY = 1;
	
	private Context context;
	private Users users;
	
	public UsersAdapter(Context context, Users users)
	{
		this.context = context;
		this.users = users;
	}
	
	@SuppressLint("InflateParams")
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		if(viewType == VIEW_USER)
			return new UserViewHolder(inflater.inflate(R.layout.item_user, null));
		else if(viewType == VIEW_SUMMARY)
			return new UsersSummaryViewHolder(inflater.inflate(R.layout.item_users_summary, null));
		else return null;
	}
	
	@Override
	public void onBindViewHolder(ViewHolder holder, int position)
	{
		if(position == 0) ((UsersSummaryViewHolder) holder).bind();
		else ((UserViewHolder) holder).bind(users.getUser(position - 1), users.areThereEnoughUsers());
	}
	
	@Override
	public int getItemCount()
	{
		return users.getUsersAmount();
	}
	
	@Override
	public int getItemViewType(int position)
	{
		return position == 0 ? VIEW_SUMMARY : VIEW_USER;
	}
}
