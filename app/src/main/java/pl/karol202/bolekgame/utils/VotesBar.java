package pl.karol202.bolekgame.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import pl.karol202.bolekgame.R;

public class VotesBar extends ConstraintLayout
{
	private int borderSize;
	private int borderColor;
	private int votesYesColor;
	private int votesNoColor;
	private int allVotes;
	private int upvotes;
	private String votesText;
	private int votesTextAppearance;
	private int votesTextColor;
	private float votesTextSize;
	
	private ConstraintLayout votesBarRoot;
	private View voteBarYes;
	private View voteBarNo;
	private TextView text;
	private TextView textValue;
	
	private ConstraintSet constraintSet;
	private float upvotesFract;
	
	public VotesBar(Context context)
	{
		this(context, null);
	}
	
	public VotesBar(Context context, @Nullable AttributeSet attrs)
	{
		this(context, attrs, R.attr.votesBarStyle);
	}
	
	public VotesBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		inflate(getContext(), R.layout.view_votes_bar, this);
		constraintSet = new ConstraintSet();
		
		votesBarRoot = findViewById(R.id.votes_bar_root);
		voteBarYes = findViewById(R.id.votes_bar_yes);
		voteBarNo = findViewById(R.id.votes_bar_no);
		text = findViewById(R.id.votes_bar_text);
		textValue = findViewById(R.id.votes_bar_text_value);
		
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VotesBar, defStyleAttr, 0);
		setBorderSize(array.getDimensionPixelSize(R.styleable.VotesBar_borderSize, -1));
		setBorderColor(array.getColor(R.styleable.VotesBar_borderColor, -1));
		setVotesYesColor(array.getColor(R.styleable.VotesBar_votesYesColor, -1));
		setVotesNoColor(array.getColor(R.styleable.VotesBar_votesNoColor, -1));
		setAllVotes(array.getInteger(R.styleable.VotesBar_allVotes, 0));
		setUpvotes(array.getInteger(R.styleable.VotesBar_upvotes, 0));
		setText(array.getString(R.styleable.VotesBar_votesText));
		setTextAppearance(array.getResourceId(R.styleable.VotesBar_votesTextAppearance, R.style.TextAppearance_AppCompat_Medium));
		setTextColor(array.getColor(R.styleable.VotesBar_votesTextColor, -1));
		setTextSize(array.getDimension(R.styleable.VotesBar_votesTextSize, -1));
		array.recycle();
	}
	
	private void update()
	{
		upvotesFract = allVotes != 0 ? upvotes / (float) allVotes : 0;
		
		constraintSet.clone(votesBarRoot);
		constraintSet.setGuidelinePercent(R.id.votes_bar_guideline, upvotesFract);
		constraintSet.applyTo(votesBarRoot);
	}
	
	private void updateValueText()
	{
		textValue.setText(getContext().getString(R.string.votes_value, upvotes, allVotes));
	}
	
	public int getBorderSize()
	{
		return borderSize;
	}
	
	public void setBorderSize(int borderSize)
	{
		if(borderSize == -1) return;
		this.borderSize = borderSize;
		votesBarRoot.setPadding(borderSize, borderSize, borderSize, borderSize);
	}
	
	public int getBorderColor()
	{
		return borderColor;
	}
	
	public void setBorderColor(int borderColor)
	{
		if(borderColor == -1) return;
		this.borderColor = borderColor;
		votesBarRoot.setBackgroundColor(borderColor);
	}
	
	public int getVotesYesColor()
	{
		return votesYesColor;
	}
	
	public void setVotesYesColor(int votesYesColor)
	{
		if(votesYesColor == -1) return;
		this.votesYesColor = votesYesColor;
		voteBarYes.setBackgroundColor(votesYesColor);
	}
	
	public int getVotesNoColor()
	{
		return votesNoColor;
	}
	
	public void setVotesNoColor(int votesNoColor)
	{
		if(votesNoColor == -1) return;
		this.votesNoColor = votesNoColor;
		voteBarNo.setBackgroundColor(votesNoColor);
	}
	
	public int getAllVotes()
	{
		return allVotes;
	}
	
	public void setAllVotes(int allVotes)
	{
		this.allVotes = allVotes;
		update();
		updateValueText();
	}
	
	public int getUpvotes()
	{
		return upvotes;
	}
	
	public void setUpvotes(int upvotes)
	{
		this.upvotes = upvotes;
		update();
		updateValueText();
	}
	
	public String getText()
	{
		return votesText;
	}
	
	public void setText(String votesText)
	{
		this.votesText = votesText;
		text.setText(votesText);
	}
	
	public int getTextAppearance()
	{
		return votesTextAppearance;
	}
	
	public void setTextAppearance(int votesTextAppearance)
	{
		this.votesTextAppearance = votesTextAppearance;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setTextAppearanceMarshmallow();
		else setTextAppearancePreMarshmallow();
	}
	
	@TargetApi(Build.VERSION_CODES.M)
	private void setTextAppearanceMarshmallow()
	{
		text.setTextAppearance(votesTextAppearance);
		textValue.setTextAppearance(votesTextAppearance);
	}
	
	private void setTextAppearancePreMarshmallow()
	{
		text.setTextAppearance(getContext(), votesTextAppearance);
		textValue.setTextAppearance(getContext(), votesTextAppearance);
	}
	
	public int getTextColor()
	{
		return votesTextColor;
	}
	
	public void setTextColor(int votesTextColor)
	{
		if(votesTextColor == -1) return;
		this.votesTextColor = votesTextColor;
		text.setTextColor(votesTextColor);
		textValue.setTextColor(votesTextColor);
	}
	
	public float getTextSize()
	{
		return votesTextSize;
	}
	
	public void setTextSize(float votesTextSize)
	{
		if(votesTextSize == -1) return;
		this.votesTextSize = votesTextSize;
		text.setTextSize(votesTextSize);
		textValue.setTextSize(votesTextSize);
	}
}
