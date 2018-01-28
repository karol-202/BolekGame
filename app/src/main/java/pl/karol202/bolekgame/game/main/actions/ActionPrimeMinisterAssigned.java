package pl.karol202.bolekgame.game.main.actions;

import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ContextSupplier;

public class ActionPrimeMinisterAssigned extends SimpleAction
{
	private String text;
	
	public ActionPrimeMinisterAssigned(ContextSupplier contextSupplier, String primeMinister, boolean amIPrimeMinister)
	{
		if(amIPrimeMinister) text = contextSupplier.getString(R.string.action_prime_minister_assigned_you);
		else if(!primeMinister.isEmpty()) text = contextSupplier.getString(R.string.action_prime_minister_assigned, primeMinister);
		else text = contextSupplier.getString(R.string.action_prime_minister_assigned_empty);
	}
	
	@Override
	public String getText()
	{
		return text;
	}
}
