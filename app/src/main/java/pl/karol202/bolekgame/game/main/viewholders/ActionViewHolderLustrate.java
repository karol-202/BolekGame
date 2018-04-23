package pl.karol202.bolekgame.game.main.viewholders;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionLustrate;
import pl.karol202.bolekgame.view.ItemDivider;

public class ActionViewHolderLustrate extends ActionViewHolder<ActionLustrate>
{
	private RecyclerView recyclerCandidates;
	
	private LustrationCandidatesAdapter adapter;
	
	public ActionViewHolderLustrate(View view, Context context)
	{
		super(view);
		adapter = new LustrationCandidatesAdapter(context);
		
		recyclerCandidates = view.findViewById(R.id.recycler_lustration_candidates);
		recyclerCandidates.setLayoutManager(new LinearLayoutManager(context));
		recyclerCandidates.setAdapter(adapter);
		recyclerCandidates.addItemDecoration(new ItemDivider(context));
	}
	
	@Override
	public void bind(ActionLustrate action)
	{
		adapter.setAction(action);
	}
}
