package pl.karol202.bolekgame.game.acts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.github.florent37.viewtooltip.ViewTooltip;
import pl.karol202.bolekgame.R;
import pl.karol202.bolekgame.game.Screen;

public class ScreenActs extends Screen
{
	private ViewGroup mainLayout;
	private TextView textPollIndex;
	private ImageView panelActLustration1;
	private ImageView panelActLustration2;
	private ImageView panelActLustration3;
	private ImageView panelActLustration4;
	private ImageView panelActLustration5;
	private ImageView panelActAntilustration1;
	private ImageView panelActAntilustration2;
	private ImageView panelActAntilustration3;
	private ImageView panelActAntilustration4;
	private ImageView panelActAntilustration5;
	private ImageView panelActAntilustration6;
	
	private ImageView imageActAntilustration1Icon;
	private TextView textActAntilustration1;
	private ImageView imageActAntilustration2Icon;
	private TextView textActAntilustration2;
	
	private ActsOverlayView overlayView;
	private ViewTooltip currentTooltip;
	
	private Acts acts;
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.screen_acts, container, false);
		
		mainLayout = view.findViewById(R.id.main_layout);
		
		textPollIndex = view.findViewById(R.id.text_poll_index);
		
		panelActLustration1 = view.findViewById(R.id.panel_act_slot_lustration_1);
		panelActLustration2 = view.findViewById(R.id.panel_act_slot_lustration_2);
		panelActLustration3 = view.findViewById(R.id.panel_act_slot_lustration_3);
		panelActLustration4 = view.findViewById(R.id.panel_act_slot_lustration_4);
		panelActLustration5 = view.findViewById(R.id.panel_act_slot_lustration_5);
		
		panelActAntilustration1 = view.findViewById(R.id.panel_act_slot_antilustration_1);
		panelActAntilustration1.setOnClickListener(v -> showHint(panelActAntilustration1, R.string.hint_antilustration_1));
		
		panelActAntilustration2 = view.findViewById(R.id.panel_act_slot_antilustration_2);
		panelActAntilustration2.setOnClickListener(v -> showHint(panelActAntilustration2, R.string.hint_antilustration_2));
		
		panelActAntilustration3 = view.findViewById(R.id.panel_act_slot_antilustration_3);
		panelActAntilustration3.setOnClickListener(v -> showHint(panelActAntilustration3, R.string.hint_antilustration_3));
		
		panelActAntilustration4 = view.findViewById(R.id.panel_act_slot_antilustration_4);
		panelActAntilustration4.setOnClickListener(v -> showHint(panelActAntilustration4, R.string.hint_antilustration_4));
		
		panelActAntilustration5 = view.findViewById(R.id.panel_act_slot_antilustration_5);
		panelActAntilustration5.setOnClickListener(v -> showHint(panelActAntilustration5, R.string.hint_antilustration_5));
		
		panelActAntilustration6 = view.findViewById(R.id.panel_act_slot_antilustration_6);
		panelActAntilustration6.setOnClickListener(v -> showHint(panelActAntilustration6, R.string.hint_antilustration_6));
		
		imageActAntilustration1Icon = view.findViewById(R.id.image_act_slot_antilustration_1_icon);
		textActAntilustration1 = view.findViewById(R.id.text_act_slot_antilustration_1);
		
		imageActAntilustration2Icon = view.findViewById(R.id.image_act_slot_antilustration_2_icon);
		textActAntilustration2 = view.findViewById(R.id.text_act_slot_antilustration_2);
		
		overlayView = view.findViewById(R.id.acts_overlay_view);
		overlayView.setRootView(mainLayout);
		overlayView.setLustrationActSlotView(0, panelActLustration1);
		overlayView.setLustrationActSlotView(1, panelActLustration2);
		overlayView.setLustrationActSlotView(2, panelActLustration3);
		overlayView.setLustrationActSlotView(3, panelActLustration4);
		overlayView.setLustrationActSlotView(4, panelActLustration5);
		overlayView.setAntiustrationActSlotView(0, panelActAntilustration1);
		overlayView.setAntiustrationActSlotView(1, panelActAntilustration2);
		overlayView.setAntiustrationActSlotView(2, panelActAntilustration3);
		overlayView.setAntiustrationActSlotView(3, panelActAntilustration4);
		overlayView.setAntiustrationActSlotView(4, panelActAntilustration5);
		overlayView.setAntiustrationActSlotView(5, panelActAntilustration6);
		
		return view;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		acts = gameLogic.getActs();
		updateHints();
		onPollIndexUpdate();
		onActsUpdate();
	}
	
	private void updateHints()
	{
		if(!acts.isPlayerCheckingAvailable())
		{
			panelActAntilustration1.setOnClickListener(null);
			imageActAntilustration1Icon.setVisibility(View.GONE);
			textActAntilustration1.setVisibility(View.GONE);
		}
		if(!acts.isPlayerOrActsCheckingAvailable())
		{
			panelActAntilustration2.setOnClickListener(null);
			imageActAntilustration2Icon.setVisibility(View.GONE);
			textActAntilustration2.setVisibility(View.GONE);
		}
	}
	
	public void onPollIndexUpdate()
	{
		textPollIndex.setText(String.valueOf(acts.getPollIndex()));
	}
	
	public void onActsUpdate()
	{
		overlayView.setLustrationActs(acts.getLustrationActs());
		overlayView.setAntilustrationActs(acts.getAntilustrationActs());
	}
	
	private void showHint(View view, int textId)
	{
		int color = ResourcesCompat.getColor(getResources(), R.color.colorAccent, null);
		int textColor = ResourcesCompat.getColor(getResources(), R.color.text_primary_white, null);
		String text = getString(textId);
		
		if(currentTooltip != null) currentTooltip.close();
		currentTooltip = ViewTooltip.on(view)
									.autoHide(true, 5000)
									.clickToHide(true)
									.position(ViewTooltip.Position.TOP)
									.color(color)
									.textColor(textColor)
									.withShadow(false)
									.text(text);
		currentTooltip.show();
	}
}
