/**
 * 
 */
package org.kobotoolbox.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.LayerDrawable;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * A slider and display for showing off
 * @author Gary Hendrick
 */
public class NumericSlider extends LinearLayout implements
		SeekBar.OnSeekBarChangeListener {
	private static final int[] grad = { Color.GRAY, Color.DKGRAY };
	private TextView mValueView;
	private SeekBar mSeekBar;
	Drawable[] layers = new GradientDrawable[6];
	LayerDrawable progress;
	private int mMinimum, mMaximum;
	private boolean mDoSuppressFlingGesture = false;

	public NumericSlider(Context context) {
		super(context);
		setOrientation(LinearLayout.VERTICAL);
		mSeekBar = new SeekBar(getContext());
		mSeekBar.setOnSeekBarChangeListener(this);
		mSeekBar.setPadding(80, 0, 80, 0);

		mValueView = new TextView(getContext());
		mValueView.setGravity(Gravity.CENTER_HORIZONTAL);
		mValueView.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
		// only allows numbers and no periods
		mValueView.setKeyListener(new DigitsKeyListener(true, false));

		addView(mValueView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		addView(mSeekBar, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		layers[0] = new GradientDrawable(Orientation.LEFT_RIGHT, grad);
		((GradientDrawable) layers[0]).setShape(GradientDrawable.RECTANGLE);
		for (int i = 1; i < 6; i++) {
			layers[i] = new GradientDrawable(Orientation.LEFT_RIGHT, grad);
			((GradientDrawable) layers[i]).setShape(GradientDrawable.OVAL);
		}
		progress = new LayerDrawable(layers);
		progress.mutate();
		progress.setLayerInset(0, 0, 20, 0, 20);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.RelativeLayout#onLayout(boolean, int, int, int, int)
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			int w = r - l - 160;
			float dw = 10f;
			float dg = (float) (0.25 * w);
			int cursor = 0;
			for (int i = 1; i < 6; i++) {
				progress.setLayerInset(i, (int) (cursor - dw / 2), 0,
						(int) ((w - cursor) - (dw / 2)), 0);
				cursor += dg;
			}
			mSeekBar.setProgressDrawable(progress);
		}
	}

	public void setRange(int min, int max) {
		this.mMinimum = min;
		this.mMaximum = max;
	}

	public int getRange() {
		return mMaximum - mMinimum;
	}

	public int getValue() {
		return (int) (mMinimum + ((double) mSeekBar.getProgress() / 100)
				* (mMaximum - mMinimum));
	}

	public void setValue(int value) {
		if (mSeekBar != null)
			mSeekBar.setProgress((int) (100 * (((double)(value - mMinimum)) / (double)getRange())));
		if (mValueView != null)
			mValueView.setText(String.valueOf(value));
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (mValueView != null)
			mValueView.setText(String.valueOf(getValue()));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		mDoSuppressFlingGesture = true;
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mDoSuppressFlingGesture = false;
	}

	public boolean doSuppressFlingGesture() {
		return mDoSuppressFlingGesture;
	}

	public void setTextSize(int complexUnitDip, int mAnswerFontsize) {
		mValueView.setTextSize(complexUnitDip, mAnswerFontsize);
	}
}