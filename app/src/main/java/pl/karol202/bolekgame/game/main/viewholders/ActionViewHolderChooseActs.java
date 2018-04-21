package pl.karol202.bolekgame.game.main.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.gameplay.Act;
import pl.karol202.bolekgame.game.main.actions.ActionChooseActs;
import pl.karol202.bolekgame.utils.CheckableLayout;

import java.util.ArrayList;
import java.util.List;

public class ActionViewHolderChooseActs extends ActionViewHolder<ActionChooseActs>
{
	private TextView textAction;
	
	private CheckableLayout layoutAct1;
	private ImageView imageAct1;
	private TextView textAct1;
	
	private CheckableLayout layoutAct2;
	private ImageView imageAct2;
	private TextView textAct2;
	
	private CheckableLayout layoutAct3;
	private ImageView imageAct3;
	private TextView textAct3;
	
	private Button buttonDone;
	private Button buttonVeto;
	private TextView textVeto;
	
	private ActionChooseActs action;
	
	public ActionViewHolderChooseActs(View view)
	{
		super(view);
		textAction = view.findViewById(R.id.text_action_choose_acts);
		
		layoutAct1 = view.findViewById(R.id.panel_current_act_1);
		imageAct1 = view.findViewById(R.id.image_current_act_1);
		textAct1 = view.findViewById(R.id.text_current_act_1);
		
		layoutAct2 = view.findViewById(R.id.panel_current_act_2);
		imageAct2 = view.findViewById(R.id.image_current_act_2);
		textAct2 = view.findViewById(R.id.text_current_act_2);
		
		layoutAct3 = view.findViewById(R.id.panel_current_act_3);
		imageAct3 = view.findViewById(R.id.image_current_act_3);
		textAct3 = view.findViewById(R.id.text_current_act_3);
		
		buttonDone = view.findViewById(R.id.button_choose_acts_done);
		buttonDone.setOnClickListener(v -> onDoneButtonClick());
		
		buttonVeto = view.findViewById(R.id.button_choose_acts_veto);
		buttonVeto.setOnClickListener(v -> onVetoButtonClick());
		
		textVeto = view.findViewById(R.id.text_choose_acts_veto);
	}
	
	@Override
	public void bind(ActionChooseActs action)
	{
		this.action = action;
		
		textAction.setText(action.isPresidentAction() ? R.string.action_choose_acts_president : R.string.action_choose_acts_prime_minister);
		
		layoutAct1.setEnabled(action.canActBeDismissed());
		layoutAct1.setChecked(action.isActSelected(0));
		layoutAct1.setOnCheckedChangeListener(c -> this.onActCheckedChange(0, c));
		imageAct1.setImageResource(action.getAct(0).getIcon());
		textAct1.setText(action.getAct(0).getName());
		
		layoutAct2.setEnabled(action.canActBeDismissed());
		layoutAct2.setChecked(action.isActSelected(1));
		layoutAct2.setOnCheckedChangeListener(c -> this.onActCheckedChange(1, c));
		imageAct2.setImageResource(action.getAct(1).getIcon());
		textAct2.setText(action.getAct(1).getName());
		
		layoutAct3.setVisibility(action.isPresidentAction() ? View.VISIBLE : View.GONE);
		if(action.isPresidentAction())
		{
			layoutAct3.setEnabled(action.canActBeDismissed());
			layoutAct3.setChecked(action.isActSelected(2));
			layoutAct3.setOnCheckedChangeListener(c -> this.onActCheckedChange(2, c));
			imageAct3.setImageResource(action.getAct(2).getIcon());
			textAct3.setText(action.getAct(2).getName());
		}
		else layoutAct3.setOnCheckedChangeListener(null);
		
		buttonDone.setEnabled(action.canActBeDismissed() && action.getSelectedActsAmount() == getValidSelectedActsAmount());
		if(action.isVetoApplicable())
		{
			buttonVeto.setVisibility(action.isVetoRequested() ? View.INVISIBLE : View.VISIBLE);
			buttonVeto.setEnabled(!action.isChosen() && !action.isCancelled());
			textVeto.setVisibility(action.isVetoRequested() ? View.VISIBLE : View.GONE);
			if(!action.isVetoResponsed()) textVeto.setText(R.string.text_waiting_for_veto_response);
			else textVeto.setText(action.isVetoAccepted() ? R.string.text_veto_accepted : R.string.text_veto_rejected);
		}
		else
		{
			buttonVeto.setVisibility(View.GONE);
			textVeto.setVisibility(View.GONE);
		}
	}
	
	private int getValidSelectedActsAmount()
	{
		return action.isPresidentAction() ? 2 : 1;
	}
	
	private void onActCheckedChange(int index, boolean checked)
	{
		action.setActSelection(index, checked);
		buttonDone.setEnabled(action.canActBeDismissed() && action.getSelectedActsAmount() == getValidSelectedActsAmount());
	}
	
	private void onDoneButtonClick()
	{
		List<Act> actsToDismiss = new ArrayList<>();
		if(!layoutAct1.isChecked()) actsToDismiss.add(action.getAct(0));
		if(!layoutAct2.isChecked()) actsToDismiss.add(action.getAct(1));
		if(!layoutAct3.isChecked() && action.isPresidentAction()) actsToDismiss.add(action.getAct(2));
		if(actsToDismiss.size() == 1) action.dismissAct(actsToDismiss.get(0));
	}
	
	private void onVetoButtonClick()
	{
		action.requestVeto();
	}
}
