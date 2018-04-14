package pl.karol202.bolekgame.game.main.actions;

import android.graphics.Bitmap;
import android.view.View;
import java8.util.function.Function;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.ImagesSet;
import pl.karol202.bolekgame.game.gameplay.Role;
import pl.karol202.bolekgame.game.main.ContextSupplier;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolder;
import pl.karol202.bolekgame.game.main.viewholders.ActionViewHolderRoleAssigned;

public class ActionRoleAssigned implements Action
{
	private ContextSupplier contextSupplier;
	
	private Role role;
	
	public ActionRoleAssigned(ContextSupplier contextSupplier, Role role)
	{
		this.contextSupplier = contextSupplier;
		
		this.role = role;
	}
	
	@Override
	public int getViewHolderLayout()
	{
		return R.layout.item_action_role_assigned;
	}
	
	@Override
	public Class<? extends ActionViewHolder> getViewHolderClass()
	{
		return ActionViewHolderRoleAssigned.class;
	}
	
	@Override
	public Function<View, ? extends ActionViewHolder> getViewHolderCreator()
	{
		return v -> new ActionViewHolderRoleAssigned(v, contextSupplier.getContext());
	}
	
	public Role getRole()
	{
		return role;
	}
	
	public Bitmap getRoleImage()
	{
		ImagesSet imagesSet = contextSupplier.getImagesSet();
		return imagesSet.getRoleImage(role);
	}
}
