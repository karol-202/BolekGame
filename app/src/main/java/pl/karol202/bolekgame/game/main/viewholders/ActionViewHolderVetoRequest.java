package pl.karol202.bolekgame.game.main.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.main.actions.ActionVetoRequestPresident;

public class ActionViewHolderVetoRequest extends ActionViewHolder<ActionVetoRequestPresident>
{
	private TextView textVetoRequest;
	private Button buttonAccept;
	private Button buttonReject;
	private TextView textDecision;
	
	private Context context;
	
	public ActionViewHolderVetoRequest(View view, Context context)
	{
		super(view);
		this.context = context;
		
		textVetoRequest = view.findViewById(R.id.text_action_veto_request);
		buttonAccept = view.findViewById(R.id.button_veto_accept);
		buttonReject = view.findViewById(R.id.button_veto_reject);
		textDecision = view.findViewById(R.id.text_action_veto_request_decision);
	}
	
	@Override
	public void bind(ActionVetoRequestPresident action)
	{
		textVetoRequest.setText(context.getString(R.string.action_veto_request, action.getPrimeMinisterName()));
		
		buttonAccept.setEnabled(!action.isResponsed());
		buttonAccept.setOnClickListener(v -> action.makeADecision(true));
		
		buttonReject.setEnabled(!action.isResponsed());
		buttonReject.setOnClickListener(v -> action.makeADecision(false));
		
		textDecision.setVisibility(action.isResponsed() ? View.VISIBLE : View.GONE);
		textDecision.setText(action.getResponse() ? R.string.text_veto_accepted : R.string.text_veto_rejected);
	}
}
