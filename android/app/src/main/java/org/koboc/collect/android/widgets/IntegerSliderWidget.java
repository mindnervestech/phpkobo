package org.koboc.collect.android.widgets;

import org.javarosa.core.model.data.IAnswerData;
import org.javarosa.core.model.data.IntegerData;
import org.javarosa.form.api.FormEntryPrompt;
import org.kobotoolbox.view.NumericSlider;

import android.content.Context;
import android.util.TypedValue;
import android.view.MotionEvent;

/* A Widget that provides a custom slider control to select Integer values
 * over a continuous number line
 * @author Gary Hendrick
 */
public class IntegerSliderWidget extends QuestionWidget {

	boolean mReadOnly = false;
	private NumericSlider mAnswer;

	public IntegerSliderWidget(Context context, FormEntryPrompt prompt,
			int min, int max) {
		super(context, prompt);
		mAnswer = new NumericSlider(context);
		mAnswer.setId(QuestionWidget.newUniqueId());
		mReadOnly = prompt.isReadOnly();
		mAnswer.setRange(min, max);
		if (mReadOnly) {
			mAnswer.setBackgroundDrawable(null);
			mAnswer.setFocusable(false);
			mAnswer.setClickable(false);
		}

		mAnswer.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mAnswerFontsize);
		Integer i = getIntegerAnswerValue();

        if (i != null) {
            mAnswer.setValue(i);
        }
		addView(mAnswer);
	}

	private Integer getIntegerAnswerValue() {
		IAnswerData dataHolder = mPrompt.getAnswerValue();
		Integer d = null;
		if (dataHolder != null) {
			Object dataValue = dataHolder.getValue();
			if (dataValue != null) {
				if (dataValue instanceof Double) {
					d = Integer.valueOf(((Double) dataValue).intValue());
				} else {
					d = (Integer) dataValue;
				}
			}
		}
		return d;
	}
	
	@Override
	public IAnswerData getAnswer() {
		clearFocus();
		Integer i = mAnswer.getValue();
		if (i == null) {
			return null;
		} else {
			return new IntegerData(i);
		}
	}

	@Override
	public void clearAnswer() {
		mAnswer.setValue(mAnswer.getValue());
	}

	@Override
	public void setFocus(Context context) {
		// Put focus on text input field
		mAnswer.requestFocus();
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		mAnswer.setOnLongClickListener(l);
	}

	/* (non-Javadoc)
	 * @see org.koboc.collect.android.widgets.QuestionWidget#suppressFlingGesture(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 */
	@Override
	public boolean suppressFlingGesture(MotionEvent e1, MotionEvent e2,
			float velocityX, float velocityY) {
		return mAnswer.doSuppressFlingGesture();
	}

	@Override
	public void cancelLongPress() {
		super.cancelLongPress();
		if (mAnswer != null) {
			mAnswer.cancelLongPress();
		}
	}
}
