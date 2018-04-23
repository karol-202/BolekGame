package pl.karol202.bolekgame.game.acts;

import android.content.Context;
import android.graphics.*;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import pl.karol202.bolekgame.R;

public class ActsOverlayView extends View
{
	private View rootView;
	private View[] lustrationSlots;
	private View[] antilustrationSlots;
	
	private Bitmap actLustration;
	private Bitmap actAntilustration;
	
	private int lustrationActs;
	private int antilustrationActs;
	
	public ActsOverlayView(Context context)
	{
		this(context, null);
	}
	
	public ActsOverlayView(Context context, @Nullable AttributeSet attrs)
	{
		this(context, attrs, 0);
	}
	
	public ActsOverlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
	{
		super(context, attrs, defStyleAttr);
		lustrationSlots = new View[5];
		antilustrationSlots = new View[6];
		
		actLustration = BitmapFactory.decodeResource(context.getResources(), R.drawable.act_lustration);
		actAntilustration = BitmapFactory.decodeResource(context.getResources(), R.drawable.act_antilustration);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		for(int i = 0; i < lustrationActs; i++) drawInSlot(canvas, getSlotRect(lustrationSlots[i]), actLustration);
		for(int i = 0; i < antilustrationActs; i++) drawInSlot(canvas, getSlotRect(antilustrationSlots[i]), actAntilustration);
	}
	
	private Rect getSlotRect(View slot)
	{
		Point point = new Point();
		View current = slot;
		while(current != rootView)
		{
			point.offset(current.getLeft(), current.getTop());
			current = (View) current.getParent();
		}
		return new Rect(point.x, point.y, point.x + slot.getWidth(), point.y + slot.getHeight());
	}
	
	private void drawInSlot(Canvas canvas, Rect slot, Bitmap bitmap)
	{
		Point bitmapSize = new Point(bitmap.getWidth(), bitmap.getHeight());
		Rect bitmapRect = fitBitmapInSlot(slot, bitmapSize);
		canvas.drawBitmap(bitmap, null, bitmapRect, null);
	}
	
	private Rect fitBitmapInSlot(Rect slot, Point bitmapSize)
	{
		Point slotSize = new Point(slot.width(), slot.height());
		Point newBitmapSize = getSizeOfBitmapFitInSlot(slotSize, bitmapSize);
		int xDelta = (slotSize.x - newBitmapSize.x) / 2;
		int yDelta = (slotSize.y - newBitmapSize.y) / 2;
		return new Rect(slot.left + xDelta, slot.top + yDelta, slot.right - xDelta, slot.bottom - yDelta);
	}
	
	private Point getSizeOfBitmapFitInSlot(Point slot, Point bitmap)
	{
		float slotRatio = (float) slot.x / slot.y;
		float drawableRatio = (float) bitmap.x / bitmap.y;
		if(slotRatio > drawableRatio)
		{
			int width = (int) (slot.y * drawableRatio);
			return new Point(width, slot.y);
		}
		else
		{
			int height = (int) (slot.x / drawableRatio);
			return new Point(slot.x, height);
		}
	}
	
	void setRootView(View view)
	{
		rootView = view;
	}
	
	void setLustrationActSlotView(int index, View slot)
	{
		if(index < 0 || index >= lustrationSlots.length) return;
		lustrationSlots[index] = slot;
	}
	
	void setAntiustrationActSlotView(int index, View slot)
	{
		if(index < 0 || index >= antilustrationSlots.length) return;
		antilustrationSlots[index] = slot;
	}
	
	public void setLustrationActs(int lustrationActs)
	{
		this.lustrationActs = lustrationActs;
		invalidate();
	}
	
	public void setAntilustrationActs(int antilustrationActs)
	{
		this.antilustrationActs = antilustrationActs;
		invalidate();
	}
}
