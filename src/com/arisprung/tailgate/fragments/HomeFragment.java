package com.arisprung.tailgate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arisprung.tailgate.R;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

public class HomeFragment extends Fragment
{

	public HomeFragment()
	{
	}
//
//	private boolean isResumed = false;
//	private UiLifecycleHelper uiHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		View rootView = inflater.inflate(R.layout.facebook_splash, container, false);
//		LoginButton authButton = (LoginButton) rootView.findViewById(R.id.auth_button);
//		authButton.setFragment(this);
		return rootView;
	}

//	@Override
//	public void onCreate(Bundle savedInstanceState)
//	{
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
////		uiHelper = new UiLifecycleHelper(getActivity(), callback);
////		uiHelper.onCreate(savedInstanceState);
//
//	}
//
//	@Override
//	public void onResume()
//	{
//		super.onResume();
//		Session session = Session.getActiveSession();
//		if (session != null && (session.isOpened() || session.isClosed()))
//		{
//			onSessionStateChange(session, session.getState(), null);
//		}
//		isResumed = true;
//		uiHelper.onResume();
//	}
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data)
//	{
//		super.onActivityResult(requestCode, resultCode, data);
//		uiHelper.onActivityResult(requestCode, resultCode, data);
//	}
//
//	@Override
//	public void onPause()
//	{
//		super.onPause();
//		uiHelper.onPause();
//		isResumed = false;
//	}
//
//	@Override
//	public void onDestroy()
//	{
//		super.onDestroy();
//		uiHelper.onDestroy();
//	}
//
//	@Override
//	public void onSaveInstanceState(Bundle outState)
//	{
//		super.onSaveInstanceState(outState);
//		uiHelper.onSaveInstanceState(outState);
//	}
//
//	private Session.StatusCallback callback = new Session.StatusCallback() {
//		@Override
//		public void call(Session session, SessionState state, Exception exception)
//		{
//			onSessionStateChange(session, state, exception);
//		}
//	};
//
//	private void onSessionStateChange(Session session, SessionState state, Exception exception)
//	{
//		// Only make changes if the activity is visible
//		if (isResumed)
//		{
//			FragmentManager manager = getFragmentManager();
//			// Get the number of entries in the back stack
//			int backStackSize = manager.getBackStackEntryCount();
//			// Clear the back stack
//			for (int i = 0; i < backStackSize; i++)
//			{
//				manager.popBackStack();
//			}
//			if (state.isOpened())
//			{
//				// If the session state is open:
//				// Show the authenticated fragment
//				FragmentManager fm = getFragmentManager();
//				FragmentTransaction transaction = fm.beginTransaction();
//				transaction.show(new MessagesListFragment());
//				transaction.commit();
//				
//				
//				FragmentManager fragmentManager2 = getFragmentManager();
//				FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
//				MessagesListFragment fragment2 = new MessagesListFragment();
//				fragmentTransaction2.hide(HomeFragment.this);
//				fragmentTransaction2.add(android.R.id.content, fragment2);
//				fragmentTransaction2.commit();
//			}
//			else if (state.isClosed())
//			{
//				// If the session state is closed:
//				// Show the login fragment
//				FragmentManager fm = getFragmentManager();
//				FragmentTransaction transaction = fm.beginTransaction();
//				transaction.show(new HomeFragment());
//				transaction.commit();
//			}
//		}
//	}
//
//	private void showFragment(int fragmentIndex, boolean addToBackStack)
//	{
//		FragmentManager fm = getFragmentManager();
//		FragmentTransaction transaction = fm.beginTransaction();
//		for (int i = 0; i < fragments.length; i++)
//		{
//			if (i == fragmentIndex)
//			{
//				transaction.show(fragments[i]);
//			}
//			else
//			{
//				transaction.hide(fragments[i]);
//			}
//		}
//		if (addToBackStack)
//		{
//			transaction.addToBackStack(null);
//		}
//		transaction.commit();
//	}
}
