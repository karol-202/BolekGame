package pl.karol202.bolekgame.game.main.actions;

import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ContextSupplier;

public class ActionPrimeMinisterChoosingActs extends SimpleAction
{
	private String text;
	
	public ActionPrimeMinisterChoosingActs(ContextSupplier contextSupplier, String primeMinister)
	{
		text = contextSupplier.getString(R.string.action_prime_minister_choosing_acts, primeMinister);
	}
	
	@Override
	public String getText()
	{
		return text;
	}
}
