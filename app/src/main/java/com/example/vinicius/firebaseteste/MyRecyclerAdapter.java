package com.example.vinicius.firebaseteste;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by vinicius on 03/03/17.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<CustomViewHolder>
{
	private List<String> messages;
	private Context mContext;

	public MyRecyclerAdapter(List<String> messages, Context mContext)
	{
		this.messages = messages;
		this.mContext = mContext;
	}

	@Override
	public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);

		CustomViewHolder viewHolder = new CustomViewHolder(view);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(CustomViewHolder customViewHolder, int position)
	{
		String message = messages.get(position);

		customViewHolder.messageTextView.setText(message);
	}

	@Override
	public int getItemCount()
	{
		return (null != messages ? messages.size() : 0);
	}

	@Override
	public void onViewRecycled(CustomViewHolder holder)
	{
		super.onViewRecycled(holder);
	}
}
