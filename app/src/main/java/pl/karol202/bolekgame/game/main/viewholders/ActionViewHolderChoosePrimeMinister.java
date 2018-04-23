package pl.karol202.bolekgame.game.main.viewholders;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionChoosePrimeMinister;
import pl.karol202.bolekgame.view.ItemDivider;

public class ActionViewHolderChoosePrimeMinister extends ActionViewHolder<ActionChoosePrimeMinister>
{
	private RecyclerView recyclerPrimeMinisterCandidates;
	
	private PrimeMinisterCandidatesAdapter adapter;
	
	public ActionViewHolderChoosePrimeMinister(View view, Context context)
	{
		super(view);
		adapter = new PrimeMinisterCandidatesAdapter(context);
		
		recyclerPrimeMinisterCandidates = view.findViewById(R.id.recycler_prime_minister_candidates);
		recyclerPrimeMinisterCandidates.setLayoutManager(new LinearLayoutManager(context));
		recyclerPrimeMinisterCandidates.setAdapter(adapter);
		recyclerPrimeMinisterCandidates.addItemDecoration(new ItemDivider(context));
	}
	
	@Override
	public void bind(ActionChoosePrimeMinister action)
	{
		adapter.setAction(action);
	}
}
