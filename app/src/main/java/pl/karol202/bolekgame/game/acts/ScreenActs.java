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
	private ImageView[] panelsActLustration;
	private ImageView[] panelsActAntilustration;
	
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
		
		panelsActLustration = new ImageView[5];
		panelsActLustration[0] = view.findViewById(R.id.panel_act_slot_lustration_1);
		panelsActLustration[1] = view.findViewById(R.id.panel_act_slot_lustration_2);
		panelsActLustration[2] = view.findViewById(R.id.panel_act_slot_lustration_3);
		panelsActLustration[3] = view.findViewById(R.id.panel_act_slot_lustration_4);
		panelsActLustration[4] = view.findViewById(R.id.panel_act_slot_lustration_5);
		
		panelsActAntilustration = new ImageView[6];
		panelsActAntilustration[0] = view.findViewById(R.id.panel_act_slot_antilustration_1);
		panelsActAntilustration[0].setOnClickListener(v -> showHint(panelsActAntilustration[0], R.string.hint_antilustration_1));
		
		panelsActAntilustration[1] = view.findViewById(R.id.panel_act_slot_antilustration_2);
		panelsActAntilustration[1].setOnClickListener(v -> showHint(panelsActAntilustration[1], R.string.hint_antilustration_2));
		
		panelsActAntilustration[2] = view.findViewById(R.id.panel_act_slot_antilustration_3);
		panelsActAntilustration[2].setOnClickListener(v -> showHint(panelsActAntilustration[2], R.string.hint_antilustration_3));
		
		panelsActAntilustration[3] = view.findViewById(R.id.panel_act_slot_antilustration_4);
		panelsActAntilustration[3].setOnClickListener(v -> showHint(panelsActAntilustration[3], R.string.hint_antilustration_4));
		
		panelsActAntilustration[4] = view.findViewById(R.id.panel_act_slot_antilustration_5);
		panelsActAntilustration[4].setOnClickListener(v -> showHint(panelsActAntilustration[4], R.string.hint_antilustration_5));
		
		panelsActAntilustration[5] = view.findViewById(R.id.panel_act_slot_antilustration_6);
		panelsActAntilustration[5].setOnClickListener(v -> showHint(panelsActAntilustration[5], R.string.hint_antilustration_6));
		
		imageActAntilustration1Icon = view.findViewById(R.id.image_act_slot_antilustration_1_icon);
		textActAntilustration1 = view.findViewById(R.id.text_act_slot_antilustration_1);
		
		imageActAntilustration2Icon = view.findViewById(R.id.image_act_slot_antilustration_2_icon);
		textActAntilustration2 = view.findViewById(R.id.text_act_slot_antilustration_2);
		
		overlayView = view.findViewById(R.id.acts_overlay_view);
		overlayView.setRootView(mainLayout);
		for(int i = 0; i < 5; i++) overlayView.setLustrationActSlotView(i, panelsActLustration[i]);
		for(int i = 0; i < 6; i++) overlayView.setAntiustrationActSlotView(i, panelsActAntilustration[i]);
		
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
		updateAccesibilityContent();
	}
	
	private void updateHints()
	{
		if(!acts.isPlayerCheckingAvailable())
		{
			panelsActAntilustration[0].setOnClickListener(null);
			imageActAntilustration1Icon.setVisibility(View.GONE);
			textActAntilustration1.setVisibility(View.GONE);
		}
		if(!acts.isPlayerOrActsCheckingAvailable())
		{
			panelsActAntilustration[1].setOnClickListener(null);
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
		updateAccesibilityContent();
	}
	
	private void updateAccesibilityContent()
	{
		for(int i = 0; i < 6; i++) updateAccesibilityContentForSlot(panelsActAntilustration[i], i + 1, acts.getAntilustrationActs() > i);
	}
	
	private void updateAccesibilityContentForSlot(ImageView panel, int slot, boolean passed)
	{
		String passedText = getString(passed ? R.string.text_act_passed : R.string.text_act_not_passed);
		panel.setContentDescription(getString(R.string.acc_act_antilustration_slot, slot, passedText));
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
	
	public void closeAllHints()
	{
		if(currentTooltip != null) currentTooltip.close();
	}
}
