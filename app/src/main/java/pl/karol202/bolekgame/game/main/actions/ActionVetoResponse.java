package pl.karol202.bolekgame.game.main.actions;

import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.ContextSupplier;

public class ActionVetoResponse extends SimpleAction
{
	private String text;
	
	public ActionVetoResponse(ContextSupplier contextSupplier, String president, boolean accepted)
	{
		int messageId = accepted ? R.string.action_veto_response_accepted : R.string.action_veto_response_rejected;
		text = contextSupplier.getString(messageId, president);
	}
	
	@Override
	public String getText()
	{
		return text;
	}
}
