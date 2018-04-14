package pl.karol202.bolekgame.game.main.viewholders;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.gameplay.Role;
import pl.karol202.bolekgame.game.main.actions.ActionRoleAssigned;

public class ActionViewHolderRoleAssigned extends ActionViewHolder<ActionRoleAssigned>
{
	private TextView textRoleAssigned;
	private ImageView imageRole;
	
	private Context context;
	
	public ActionViewHolderRoleAssigned(View view, Context context)
	{
		super(view);
		this.context = context;
		
		textRoleAssigned = view.findViewById(R.id.text_action_role_assigned);
		imageRole = view.findViewById(R.id.image_action_role_assigned);
	}
	
	@Override
	public void bind(ActionRoleAssigned action)
	{
		Role role = action.getRole();
		String roleInstr = context.getString(role.getNameInstr());
		textRoleAssigned.setText(context.getString(R.string.action_role_assigned, roleInstr));
		
		Bitmap roleBitmap = action.getRoleImage();
		imageRole.setImageBitmap(roleBitmap);
		if(roleBitmap == null) imageRole.setVisibility(View.GONE);
	}
}
