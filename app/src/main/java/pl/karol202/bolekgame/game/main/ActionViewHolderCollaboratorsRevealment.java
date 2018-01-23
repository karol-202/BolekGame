package pl.karol202.bolekgame.game.main;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.utils.ItemDivider;

class ActionViewHolderCollaboratorsRevealment extends ActionViewHolder<ActionCollaboratorsRevealment>
{
	private Context context;
	private RecyclerView recyclerCollaboratorsRevealment;
	
	ActionViewHolderCollaboratorsRevealment(View view, Context context)
	{
		super(view);
		this.context = context;
		
		recyclerCollaboratorsRevealment = view.findViewById(R.id.recycler_collaborators_revealment);
		recyclerCollaboratorsRevealment.setLayoutManager(new LinearLayoutManager(context));
		
		recyclerCollaboratorsRevealment.addItemDecoration(new ItemDivider(context));
	}
	
	@Override
	void bind(ActionCollaboratorsRevealment action)
	{
		CollaboratorsRevealmentAdapter adapter = new CollaboratorsRevealmentAdapter(context, action.getPlayerRoles());
		recyclerCollaboratorsRevealment.setAdapter(adapter);
	}
}
