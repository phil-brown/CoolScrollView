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

import java.util.Random;

import self.philbrown.droidQuery.$;
import self.philbrown.droidQuery.AnimationOptions;
import self.philbrown.droidQuery.Function;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Activity for showing a ScrollView similar to that in the <a href="http://drippler.com/">Drippler</a> app for Android.
 * <br>
 * @author Phil Brown
 * @since 9:35:02 PM Mar 13, 2014
 *
 */
public class MainActivity extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final $ image = $.with(this, R.id.image);
		image.animate("{alpha:1.0}", new AnimationOptions().complete(new Function() {
			
			@Override
			public void invoke($ d, Object... args) {
				d.parent().selectByType(TextView.class).animate("{alpha:1.0}", 
						new AnimationOptions().duration(new Random().nextInt(2000)));
			}
		}));
	}

}
