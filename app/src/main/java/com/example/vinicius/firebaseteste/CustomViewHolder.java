package com.example.vinicius.firebaseteste;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by vinicius on 03/03/17.
 */

public class CustomViewHolder extends RecyclerView.ViewHolder
{
	protected TextView messageTextView;

	public CustomViewHolder(View view)
	{
		super(view);

		this.messageTextView = (TextView) view.findViewById(R.id.messageTextView);
	}
}
