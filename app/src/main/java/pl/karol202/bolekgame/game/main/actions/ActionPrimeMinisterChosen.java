package pl.karol202.bolekgame.game.main.actions;

import android.content.Context;
import pl.karol202.bolekgame.R;

public class ActionPrimeMinisterChosen extends SimpleAction
{
	private String text;
	
	public ActionPrimeMinisterChosen(Context context, String president, String primeMinister, boolean amIPresident, boolean amIPrimeMinister)
	{
		if(amIPresident && !amIPrimeMinister) text = context.getString(R.string.action_prime_minister_chosen_by_you, primeMinister);
		else if(!amIPresident && amIPrimeMinister) text = context.getString(R.string.action_prime_minister_chosen_you, president);
		else text = context.getString(R.string.action_prime_minister_chosen, primeMinister, president);
	}
	
	@Override
	public String getText()
	{
		return text;
	}
}
