package pl.karol202.bolekgame.game.main.actions;

import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ContextSupplier;

public class ActionVetoRequest extends SimpleAction
{
	private String text;
	
	public ActionVetoRequest(ContextSupplier contextSupplier, String primeMinister, boolean amIPrimeMinister)
	{
		if(amIPrimeMinister) text = contextSupplier.getString(R.string.action_veto_request_you);
		else text = contextSupplier.getString(R.string.action_veto_request, primeMinister);
	}
	
	@Override
	public String getText()
	{
		return text;
	}
}
