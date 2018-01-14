package pl.karol202.bolekgame.server;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import pl.karol202.bolekgame.R;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> implements Users.OnUsersUpdateListener
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
			if(!(user instanceof LocalUser)) return;
			((LocalUser) user).changeReadiness(true);
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
			setBackground();
			textUserName.setText(user.getName());
			buttonUserReady.setVisibility(isLocalUser() && users.areThereEnoughUsers() && !user.isReady() ? View.VISIBLE : View.GONE);
		}
		
		private void update()
		{
			TransitionManager.beginDelayedTransition(view);
			setBackground();
			buttonUserReady.setVisibility(isLocalUser() && users.areThereEnoughUsers() && !user.isReady() ? View.VISIBLE: View.GONE);
		}
		
		private void setBackground()
		{
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) setBackgroundLollipop();
			else setBackgroundPreLollipop();
		}
		
		@TargetApi(Build.VERSION_CODES.LOLLIPOP)
		private void setBackgroundLollipop()
		{
			if(user.isReady())
				view.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.background_item_user_ready));
			else view.setBackgroundTintList(null);
		}
		
		private void setBackgroundPreLollipop()
		{
			view.setBackgroundResource(user.isReady() ? R.drawable.background_item_user_ready : getSelectableItemBackgroundId());
		}
		
		private int getSelectableItemBackgroundId()
		{
			TypedArray array = context.obtainStyledAttributes(new int[] { R.attr.selectableItemBackground });
			int resource = array.getResourceId(0, 0);
			array.recycle();
			return resource;
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
			if(users.areThereEnoughUsers()) textReadyUsersAmount.setText(context.getString(R.string.text_ready_users_amount, users.getReadyUsersAmount()));
			else textReadyUsersAmount.setText(R.string.text_too_few_users);
		}
	}
	
	private static final int VIEW_USER = 0;
	private static final int VIEW_SUMMARY = 1;
	
	private Context context;
	private Users users;
	
	UsersAdapter(Context context, Users users)
	{
		this.context = context;
		this.users = users;
		users.setOnUsersUpdateListener(this);
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
	public void onUserAdd()
	{
		notifyItemInserted(users.getUsersAmount());
		for(int i = 0; i < users.getUsersAmount(); i++) notifyItemChanged(i);
	}
	
	@Override
	public void onUserRemove(int position)
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
