/*
 * Copyright 2014 Phil Brown
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package self.philbrown.cooltask;

import self.philbrown.droidQuery.$;
import self.philbrown.droidQuery.AnimationOptions;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * This custom ScrollView contains a LinearLayout with an transparent view as the first view. Scrolling the view causes this
 * transparent view to resize, creating an effect like the <a href="http://drippler.com/">Drippler</a> app for Android.
 * <br>
 * @author Phil Brown
 * @since 10:59:00 PM Mar 13, 2014
 *
 */
public class CoolScrollView extends ScrollView
{
	private static final String TAG = "CoolScrollView";
	
	private boolean hasFlung;
	private final float imageMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getContext().getResources().getDisplayMetrics());
	private Point size = new Point();
	
	public CoolScrollView(Context context) {
		super(context);
		initialize(context, null);
	}

	public CoolScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context, attrs);
	}

	public CoolScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context, attrs);
	}
	
	protected void initialize(Context context, AttributeSet attrs)
	{
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		display.getSize(size);
		
		//remove the fading edge effect
		setOverScrollMode(OVER_SCROLL_NEVER);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP)
		{
			if (getScrollY() == 0)
			{
				reset();
			}
		}
				
		return super.onTouchEvent(ev);
	}
	
	@Override
	public void fling(int velocityY)
	{
		super.fling(velocityY);
		hasFlung = true;
	}
	
	@Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (hasFlung) {
            if (Math.abs(y - oldY) < 2 || y >= getMeasuredHeight() || y == 0) {
            	//end of scroll
            	reset();
                hasFlung = false;
            }
        }
    }
	
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent)
	{
		//deltaY contains the overscroll value that can be used for the animation.
		
		//Log.i(TAG, "scrollRangeY = " + scrollRangeY);
		
		if (isTouchEvent)
		{
			if (scrollY <= 0)
			{
				$ d = $.with(getContext(), R.id.transparent_region);
				int factor = (int) Math.abs(deltaY);
				float divisor = (size.y/d.height());
				if (divisor == 0)
					divisor = 0.1f;
				factor = (int) (factor/divisor);
				//TODO slow down - only allow up to almost the size of the image.
				d.height(d.height() + factor);
				
				$ img = $.with(getContext(), R.id.image);
				img.width(img.width()+factor).height(img.height()+factor);
				
				//adding effect to textviews
				$.with(getContext()).selectByType(TextView.class).animate("{alpha:"+ (1f/(float)divisor)+"}", new AnimationOptions().debug(true));
			}
			else
			{
				reset();
			}
		}
		
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
		
	}
	
	/**
	 * Resets to the original layout
	 */
	private void reset()
	{
		$.with(getContext(), R.id.transparent_region).animate("{height:200dp}", new AnimationOptions().easing($.Easing.DECELERATE));
		$.with(getContext()).selectByType(TextView.class).animate("{alpha:1.0}", new AnimationOptions().debug(true));
		$.with(getContext(), R.id.image).animate("{width:"+size.x+", height:"+size.y+"}", new AnimationOptions().debug(true).easing($.Easing.DECELERATE));
	}

}
