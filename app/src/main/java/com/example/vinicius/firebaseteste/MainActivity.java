package com.example.vinicius.firebaseteste;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, EditText.OnFocusChangeListener
{
	private FirebaseAuth mAuth;
	private FirebaseAuth.AuthStateListener mAuthListener;
	private Button registerButton;
	private Button logoutButton;
	private Button loginButton;
	private Button realTimeDatabaseButton;
	private TextInputLayout emailWrapper;
	private TextInputLayout passwordWrapper;
	private TextInputLayout emailWrapperSignIn;
	private TextInputLayout passwordWrapperSignIn;
	private EditText email;
	private EditText password;
	private EditText emailSignIn;
	private EditText passwordSignIn;
	private RelativeLayout loggedLayout;
	private LinearLayout loginLayout;
	private TextView loggedEmail;
	private ProgressBar progressBarRegister;
	private ProgressBar progressBarSignIn;
	public final static String TAG = "MainActivity";
	private boolean emailIsOk = false;
	private boolean passwordIsOk = false;
	private boolean emailSignInIsOk = false;
	private boolean passwordSignInIsOk = false;

	private ServiceConnection loginService = new ServiceConnection()
	{

		@Override
		public void onServiceConnected(ComponentName componentName, IBinder iBinder)
		{

		}

		@Override
		public void onServiceDisconnected(ComponentName componentName)
		{

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		emailWrapper = (TextInputLayout) findViewById(R.id.emailWrapper);
		passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
		emailWrapperSignIn = (TextInputLayout) findViewById(R.id.emailWrapperSignIn);
		passwordWrapperSignIn = (TextInputLayout) findViewById(R.id.passwordWrapperSignIn);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		emailSignIn = (EditText) findViewById(R.id.emailSignIn);
		passwordSignIn = (EditText) findViewById(R.id.passwordSignIn);
		registerButton = (Button) findViewById(R.id.registerButton);
		logoutButton = (Button) findViewById(R.id.logoutButton);
		loginButton = (Button) findViewById(R.id.loginButton);
		realTimeDatabaseButton = (Button) findViewById(R.id.realTimeDatabaseButton);
		loggedLayout = (RelativeLayout) findViewById(R.id.loggedLayout);
		loginLayout = (LinearLayout) findViewById(R.id.loginLayout);
		loggedEmail = (TextView) findViewById(R.id.loggedEmail);
		progressBarRegister = (ProgressBar) findViewById(R.id.progressBarRegister);
		progressBarSignIn = (ProgressBar) findViewById(R.id.progressBarSignin);


		email.setOnFocusChangeListener(this);
		password.setOnFocusChangeListener(this);
		email.addTextChangedListener(this);
		password.addTextChangedListener(this);
		emailSignIn.setOnFocusChangeListener(this);
		passwordSignIn.setOnFocusChangeListener(this);
		emailSignIn.addTextChangedListener(this);
		passwordSignIn.addTextChangedListener(this);
		registerButton.setOnClickListener(this);
		logoutButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);
		realTimeDatabaseButton.setOnClickListener(this);

		mAuth = FirebaseAuth.getInstance();

		mAuthListener = new FirebaseAuth.AuthStateListener()
		{
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
			{
				FirebaseUser user = firebaseAuth.getCurrentUser();

				if(user != null)
				{
					loginLayout.setVisibility(View.INVISIBLE);
					loggedLayout.setVisibility(View.VISIBLE);
					loggedEmail.setText(user.getEmail());
				}
				else
				{
					loggedLayout.setVisibility(View.GONE);
					loginLayout.setVisibility(View.VISIBLE);
					loggedEmail.setText("");
				}
			}
		};
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		mAuth.addAuthStateListener(mAuthListener);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		if(mAuthListener != null)
		{
			mAuth.removeAuthStateListener(mAuthListener);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
	{

	}

	@Override
	public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
	{

	}

	@Override
	public void afterTextChanged(Editable editable)
	{
		if(email.getText().hashCode() == editable.hashCode())
		{
			if(!isValidEmail(editable.toString()))
			{
				emailIsOk = false;
				showTextInputLayoutMessageError(emailWrapper, "Not a valid email");

				if(!emailIsOk || !passwordIsOk)
				{
					registerButton.setEnabled(false);
				}
				else
				{
					registerButton.setEnabled(true);
				}
			}
			else
			{
				emailIsOk = true;
				showTextInputLayoutMessageError(emailWrapper, "");

				if(emailIsOk && passwordIsOk)
				{
					registerButton.setEnabled(true);
				}
				else
				{
					registerButton.setEnabled(false);
				}
			}
		}

		if(emailSignIn.getText().hashCode() == editable.hashCode())
		{
			if(!isValidEmail(editable.toString()))
			{
				emailSignInIsOk = false;
				showTextInputLayoutMessageError(emailWrapperSignIn, "Not a valid email");

				if(!emailSignInIsOk || !passwordSignInIsOk)
				{
					loginButton.setEnabled(false);
				}
				else
				{
					loginButton.setEnabled(true);
				}
			}
			else
			{
				emailSignInIsOk = true;
				showTextInputLayoutMessageError(emailWrapperSignIn, "");

				if(emailSignInIsOk && passwordSignInIsOk)
				{
					loginButton.setEnabled(true);
				}
				else
				{
					loginButton.setEnabled(false);
				}
			}
		}

		if(password.getText().hashCode() == editable.hashCode())
		{
			if(!isValidPassword(editable.toString()))
			{
				passwordIsOk = false;
				showTextInputLayoutMessageError(passwordWrapper, "Password needs to be more than 5 characters");

				if(!emailIsOk || !passwordIsOk)
				{
					registerButton.setEnabled(false);
				}
				else
				{
					registerButton.setEnabled(true);
				}
			}
			else
			{
				passwordIsOk = true;
				showTextInputLayoutMessageError(passwordWrapper, "");

				if(emailIsOk && passwordIsOk)
				{
					registerButton.setEnabled(true);
				}
				else
				{
					registerButton.setEnabled(false);
				}
			}
		}

		if(passwordSignIn.getText().hashCode() == editable.hashCode())
		{
			if(!isValidPassword(editable.toString()))
			{
				passwordSignInIsOk = false;
				showTextInputLayoutMessageError(passwordWrapperSignIn, "Password needs to be more than 5 characters");

				if(!emailSignInIsOk || !passwordSignInIsOk)
				{
					loginButton.setEnabled(false);
				}
				else
				{
					loginButton.setEnabled(true);
				}
			}
			else
			{
				passwordSignInIsOk = true;
				showTextInputLayoutMessageError(passwordWrapperSignIn, "");

				if(emailSignInIsOk && passwordSignInIsOk)
				{
					loginButton.setEnabled(true);
				}
				else
				{
					loginButton.setEnabled(false);
				}
			}
		}
	}

	@Override
	public void onClick(View view)
	{
		switch(view.getId())
		{
			case R.id.registerButton:
				registerButton.setVisibility(View.INVISIBLE);
				registerButton.setEnabled(false);
				progressBarRegister.setVisibility(View.VISIBLE);
				createUser(email.getText().toString(), password.getText().toString());
				break;

			case R.id.logoutButton:
				signOut();
				break;

			case R.id.loginButton:
				loginButton.setVisibility(View.INVISIBLE);
				loginButton.setEnabled(false);
				progressBarSignIn.setVisibility(View.VISIBLE);
				login(emailSignIn.getText().toString(), passwordSignIn.getText().toString());
				break;

			case R.id.realTimeDatabaseButton:
				Intent i = new Intent(this, DatabaseActivity.class);
				startActivity(i);

			default:
				Log.d(TAG, "Nothing happens");
				break;
		}
	}

	private void createUser(String email, String password)
	{
		mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
		{
			@Override
			public void onComplete(@NonNull Task<AuthResult> task)
			{
				registerButton.setEnabled(true);
				progressBarRegister.setVisibility(View.INVISIBLE);
				registerButton.setVisibility(View.VISIBLE);

				if(task.isSuccessful())
				{
					Toast.makeText(MainActivity.this, R.string.user_created, Toast.LENGTH_SHORT).show();
				}
			}
		}).addOnFailureListener(new OnFailureListener()
		{
			@Override
			public void onFailure(@NonNull Exception e)
			{
				registerButton.setEnabled(true);
				progressBarRegister.setVisibility(View.INVISIBLE);
				registerButton.setVisibility(View.VISIBLE);

				Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void login(String email, String password)
	{
		mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
		{
			@Override
			public void onComplete(@NonNull Task<AuthResult> task)
			{
				loginButton.setEnabled(true);
				progressBarSignIn.setVisibility(View.INVISIBLE);
				loginButton.setVisibility(View.VISIBLE);
			}
		}).addOnFailureListener(this, new OnFailureListener()
		{
			@Override
			public void onFailure(@NonNull Exception e)
			{
				loginButton.setEnabled(true);
				progressBarSignIn.setVisibility(View.INVISIBLE);
				loginButton.setVisibility(View.VISIBLE);

				Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void signOut()
	{
		mAuth.signOut();
	}

	@Override
	public void onFocusChange(View view, boolean b)
	{
		if(view.getId() == R.id.email)
		{
			if(b)
			{
				if(!isValidEmail(email.getText().toString()))
				{
					showTextInputLayoutMessageError(emailWrapper, "Not a valid email");
				}
				else
				{
					showTextInputLayoutMessageError(emailWrapper, "");
				}
			}
			else
			{
				showTextInputLayoutMessageError(emailWrapper, "");
			}
		}

		if(view.getId() == R.id.password)
		{
			if(b)
			{
				if(!isValidPassword(password.getText().toString()))
				{
					showTextInputLayoutMessageError(passwordWrapper, "Password needs to be more than 5 characters");
				}
				else
				{
					showTextInputLayoutMessageError(passwordWrapper, "");
				}
			}
			else
			{
				showTextInputLayoutMessageError(passwordWrapper, "");
			}
		}

		if(view.getId() == R.id.emailSignIn)
		{
			if(b)
			{
				if(!isValidEmail(emailSignIn.getText().toString()))
				{
					showTextInputLayoutMessageError(emailWrapperSignIn, "Not a valid email");
				}
				else
				{
					showTextInputLayoutMessageError(emailWrapperSignIn, "");
				}
			}
			else
			{
				showTextInputLayoutMessageError(emailWrapperSignIn, "");
			}
		}

		if(view.getId() == R.id.passwordSignIn)
		{
			if(b)
			{
				if(!isValidPassword(passwordSignIn.getText().toString()))
				{
					showTextInputLayoutMessageError(passwordWrapperSignIn, "Password needs to be more than 5 characters");
				}
				else
				{
					showTextInputLayoutMessageError(passwordWrapperSignIn, "");
				}
			}
			else
			{
				showTextInputLayoutMessageError(passwordWrapperSignIn, "");
			}
		}
	}

	public boolean isValidEmail(String email)
	{
		if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean isValidPassword(String password)
	{
		if(password.length() < 6)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public void showTextInputLayoutMessageError(TextInputLayout textInputLayout, String message)
	{
		if(message.equals(""))
		{
			textInputLayout.setErrorEnabled(false);
		}
		else
		{
			textInputLayout.setErrorEnabled(true);
			textInputLayout.setError(message);
		}
	}
}