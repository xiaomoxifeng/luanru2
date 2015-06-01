/**
 * Copyright (c) www.longdw.com
 */
package com.haoge.luanru.music.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class AlwaysMarqueeTextView extends TextView {

	public AlwaysMarqueeTextView(Context context) {
		super(context);
	}

	public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AlwaysMarqueeTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	//设置focused为true，这样TextView就可以始终获取到焦点
	@Override
	public boolean isFocused() {
		return true;
	}
	

}
