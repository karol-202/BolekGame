package pl.karol202.bolekgame.game.main.actions;

import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ContextSupplier;

public class ActionPrimeMinisterChosen extends SimpleAction
{
	private String text;
	
	public ActionPrimeMinisterChosen(ContextSupplier contextSupplier, String president, String primeMinister, boolean amIPresident, boolean amIPrimeMinister)
	{
		if(amIPresident && !amIPrimeMinister) text = contextSupplier.getString(R.string.action_prime_minister_chosen_by_you, primeMinister);
		else if(!amIPresident && amIPrimeMinister) text = contextSupplier.getString(R.string.action_prime_minister_chosen_you, president);
		else text = contextSupplier.getString(R.string.action_prime_minister_chosen, primeMinister, president);
	}
	
	@Override
	public String getText()
	{
		return text;
	}
}
