package com.example.vinicius.firebaseteste;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseActivity extends AppCompatActivity implements View.OnClickListener
{
	private RecyclerView recyclerView;
	private EditText editText;
	private Button sendButton;
	private MyRecyclerAdapter adapter;
	private List<String> messagesList = new ArrayList<String>();
	private DatabaseReference databaseReference;
	private DatabaseReference messagesCloudEndPoint;
	private ChildEventListener messagesCloudEndPointChildEventListener;
	private final static String TAG = "DatabaseActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);

		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		editText = (EditText) findViewById(R.id.editText);
		sendButton = (Button) findViewById(R.id.sendButton);

		sendButton.setOnClickListener(this);

		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		adapter = new MyRecyclerAdapter(messagesList, getApplicationContext());
		recyclerView.setAdapter(adapter);

		databaseReference = FirebaseDatabase.getInstance().getReference();
		messagesCloudEndPoint = databaseReference.child("messages");


		messagesCloudEndPointChildEventListener = new ChildEventListener()
		{
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s)
			{
				editText.setText("");
				String message = dataSnapshot.getValue(String.class);
				messagesList.add(message);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s)
			{

			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot)
			{

			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s)
			{

			}

			@Override
			public void onCancelled(DatabaseError databaseError)
			{

			}
		};
	}

	@Override
	protected void onStart()
	{
		super.onStart();

		messagesCloudEndPoint.addChildEventListener(messagesCloudEndPointChildEventListener);
	}

	@Override
	protected void onStop()
	{
		super.onStop();

		if(messagesCloudEndPointChildEventListener != null)
		{
			messagesCloudEndPoint.removeEventListener(messagesCloudEndPointChildEventListener);
		}
	}

	@Override
	public void onClick(View view)
	{
		switch(view.getId())
		{
			case R.id.sendButton:
				saveMessage();
				break;

			default:
				Log.d(TAG, "Nothing happens");
				break;
		}
	}

	private void saveMessage()
	{
		String message = editText.getText().toString();

		String key = messagesCloudEndPoint.push().getKey();

		messagesCloudEndPoint.child(key).setValue(message).addOnFailureListener(new OnFailureListener()
		{
			@Override
			public void onFailure(@NonNull Exception e)
			{
				Toast.makeText(DatabaseActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT);
			}
		});
	}
}
