package com.arisprung.tailgate;

import static com.arisprung.tailgate.gcm.CommonUtilities.SENDER_ID;
import static com.arisprung.tailgate.gcm.CommonUtilities.SERVER_URL;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.arisprung.tailgate.adapter.NavDrawerListAdapter;
import com.arisprung.tailgate.facebook.FacebookUser;
import com.arisprung.tailgate.fragments.HomeFragment;
import com.arisprung.tailgate.fragments.LeagueListFragment;
import com.arisprung.tailgate.fragments.MapUserFragment;
import com.arisprung.tailgate.fragments.MessageListFragment;
import com.arisprung.tailgate.fragments.SendMessageFragment;
import com.arisprung.tailgate.gcm.ServerUtilities;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphLocation;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends FragmentActivity
{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private TailGateSharedPreferences mTailgateSharedPreferences = null;
	private String TAG = MainActivity.class.toString();
	AsyncTask<Void, Void, Void> mRegisterTask;
	// nav drawer title
	private CharSequence mDrawerTitle;

	private LoginButton authButton;
	private ProfilePictureView profilePic;
	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private boolean isResumed = false;
	public static String mLeagueSelected;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	private UiLifecycleHelper uiHelper;
	private static final int SPLASH = 0;
	private static final int MESSAGELIST = 1;
	private static final int FRAGMENT_COUNT = MESSAGELIST + 1;
	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

	private FacebookUser mUser = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tailgate_main_activity);
		if (mTailgateSharedPreferences == null)
			mTailgateSharedPreferences = TailGateSharedPreferences.getInstance(getApplicationContext());
		mTitle = mDrawerTitle = getTitle();

		checkNotNull(SERVER_URL, "SERVER_URL");
		checkNotNull(SENDER_ID, "SENDER_ID");
		// ! Do unregister for debug; must remove on production
		// GCMRegistrar.unregister(this); // DEBUG
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		// try
		// {
		// PackageInfo info = getPackageManager().getPackageInfo("com.arisprung.tailgate", PackageManager.GET_SIGNATURES);
		// for (Signature signature : info.signatures)
		// {
		// MessageDigest md = MessageDigest.getInstance("SHA");
		// md.update(signature.toByteArray());
		// Log.d("Your Tag", Base64.encodeToString(md.digest(), Base64.DEFAULT));
		// }
		// }
		// catch (NameNotFoundException e)
		// {
		//
		// }
		// catch (NoSuchAlgorithmException e)
		// {
		//
		// }

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		LayoutInflater mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View footerView = mInflater.inflate(R.layout.drawer_item_face_login, null);
		LoginButton loginBut = (LoginButton) footerView.findViewById(R.id.auth_button);
//		loginBut.setOnClickListener(new OnClickListener() {
//			
//			;
//
//			@Override
//			public void onClick(View v)
//			{
//				Log.i(TAG, " FaceBook login button was pressed");
//				
//			}
//		});
		mDrawerList.addFooterView(footerView);
		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(2, -1)));
;
		// Communities, Will add a counter here
		// navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
		// // Pages
		// navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// // What's hot, We will add a counter here
		// navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));

		// Recycle the typed array
		navMenuIcons.recycle();

		FragmentManager fm = getSupportFragmentManager();
		fragments[SPLASH] = new HomeFragment();
		fragments[MESSAGELIST] = new MessageListFragment();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view)
			{
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView)
			{
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};

		// Inflate your custom layout
		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.action_bar_layout, null);

		// Set up your ActionBar
		final ActionBar actionBar = getActionBar();
		// actionBar.setDisplayShowHomeEnabled(false);
		// actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionBarLayout);
		authButton = (LoginButton) findViewById(R.id.auth_button);
		profilePic = (ProfilePictureView) findViewById(R.id.selection_profile_pic);
		profilePic.setVisibility(View.INVISIBLE);
		authButton.setReadPermissions(Arrays.asList("email", "user_location"));
		// authButton.callOnClick();
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null)
		{
			// on first time display view for first nav item
			displayView(0);
		}

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception)
		{
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(Session session, SessionState state, Exception exception)
	{
		// Only make changes if the activity is visible
		if (isResumed)
		{
			FragmentManager manager = getSupportFragmentManager();
			// Get the number of entries in the back stack
			int backStackSize = manager.getBackStackEntryCount();
			// Clear the back stack
			for (int i = 0; i < backStackSize; i++)
			{
				manager.popBackStack();
			}
			if (state.isOpened())
			{
				// If the session state is open:
				// Show the authenticated fragment
				showFragment(MESSAGELIST, false);
				logInFacebookView();
			}
			else if (state.isClosed())
			{

				logOutFacebookView();
				// If the session state is closed:
				// Show the login fragment
				showFragment(SPLASH, false);
			}
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		Session session = Session.getActiveSession();

		if (session != null && session.isOpened())
		{
			// Get the user's data
			logInFacebookView();
			makeMeRequest(session);
			
		}
		else
		{
			logOutFacebookView();

		}
		uiHelper.onResume();
		isResumed = true;
	}

	private void registerForGCM()
	{

		// registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals(""))
		{
			// Automatically registers application on startup.
			GCMRegistrar.register(getApplicationContext(), SENDER_ID);
		}
		else
		{
			// Device is already registered on GCM, check server.
			if (GCMRegistrar.isRegisteredOnServer(this))
			{
				// Skips registration.
				// mDisplay.append("Already Registered" + "\n");
				Log.i("MainActivity", "Already Registered");
			}
			else
			{
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params)
					{
						boolean registered = ServerUtilities.register(context, regId);
						// At this point all attempts to register with the app
						// server failed, so we need to unregister the device
						// from GCM - the app will try to register again when
						// it is restarted. Note that GCM will send an
						// unregistered callback upon completion, but
						// GCMIntentService.onUnregistered() will ignore it.
						if (!registered)
						{
							GCMRegistrar.unregister(context);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result)
					{
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}

	private void checkNotNull(Object reference, String name)
	{
		if (reference == null)
		{
			throw new NullPointerException("Error" + name);
		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private void showFragment(int fragmentIndex, boolean addToBackStack)
	{
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++)
		{
			if (i == fragmentIndex)
			{
				transaction.show(fragments[i]);
			}
			else
			{
				transaction.hide(fragments[i]);
			}
		}
		if (addToBackStack)
		{
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId())
		{
			case R.id.action_settings:
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position)
	{
		// update the main content by replacing fragments
		Fragment fragment = null;

		switch (position)
		{

			case 0:
				 fragment = new MessageListFragment();
				
				break;
			case 1:
				fragment = new LeagueListFragment();
				break;
			case 2:
				 fragment = new SendMessageFragment();
				break;
			case 3:

				 fragment = new MapUserFragment();
				break;
			case 4:
				// fragment = new WhatsHotFragment();
				break;

			default:
				break;
		}

		if (fragment != null)
		{
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		else
		{
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title)
	{
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void makeMeRequest(final Session session)
	{
		// Make an API call to get user data and define a
		// new callback to handle the response.
		Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
			@Override
			public void onCompleted(GraphUser user, Response response)
			{

				// If the response is successful
				if (session == Session.getActiveSession())
				{
					if (user != null)
					{
						// Set the id for the ProfilePictureView
						// view that in turn displays the profile picture.
						profilePic.setProfileId(user.getId());
						;
						// Set the Textview's text to the user's name.
						// userNameView.setText(user.getName());
					
						mTailgateSharedPreferences.putStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_ID, user.getId());
						mTailgateSharedPreferences.putStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_EMAIL,
								(String) user.getProperty("email"));

						mTailgateSharedPreferences.putStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_NAME, user.getUsername());
						mTailgateSharedPreferences.putStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_FIRST_NAME, user.getFirstName());
						mTailgateSharedPreferences.putStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_LAST_NAME, user.getLastName());
				

						// String name = user.getUsername();
						// String strId = user.getId();
						// String strEmail = (String) user.getProperty("email");
						// String strBirthday = user.getBirthday();
						GraphLocation location = user.getLocation();
						String strCity = "";
						if(location != null)
						{
							JSONObject json = location.getInnerJSONObject();
							
							try
							{
								strCity = json.getString("name");
								Log.i("MainActivity", "location :" + strCity);
							}
							catch (JSONException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						
						// FacebookUser faceuser = new FacebookUser(name, strId, strEmail, strCity);
						// mUser = faceuser;
						mTailgateSharedPreferences.putStringSharedPreferences(TailGateSharedPreferences.FACEBOOK_LOCATION, strCity);
						registerForGCM();
					}
				}
				if (response.getError() != null)
				{
					Log.e("MainActivity", "Error -- " + response.getError());
				}

			}
		});
		request.executeAsync();

	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent)
		{
			// String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// mDisplay.append(newMessage + "\n");
		}
	};

	private void logInFacebookView()
	{
		profilePic.setVisibility(View.VISIBLE);
		authButton.setVisibility(View.GONE);
	}

	private void logOutFacebookView()
	{
		profilePic.setVisibility(View.GONE);
		authButton.setVisibility(View.VISIBLE);
	}

}
