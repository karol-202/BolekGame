package pl.karol202.bolekgame.game.main.viewholders;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionChoosePresident;
import pl.karol202.bolekgame.view.ItemDivider;

public class ActionViewHolderChoosePresident extends ActionViewHolder<ActionChoosePresident>
{
	private RecyclerView recyclerCandidates;
	
	private PresidentCandidatesAdapter adapter;
	
	public ActionViewHolderChoosePresident(View view, Context context)
	{
		super(view);
		adapter = new PresidentCandidatesAdapter(context);
		
		recyclerCandidates = view.findViewById(R.id.recycler_president_candidates);
		recyclerCandidates.setLayoutManager(new LinearLayoutManager(context));
		recyclerCandidates.setAdapter(adapter);
		recyclerCandidates.addItemDecoration(new ItemDivider(context));
	}
	
	@Override
	public void bind(ActionChoosePresident action)
	{
		adapter.setAction(action);
	}
}
