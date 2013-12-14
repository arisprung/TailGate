//package com.arisprung.tailgate.facebook;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.util.Log;
//import android.view.View;
//
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.model.GraphLocation;
//import com.facebook.model.GraphUser;
//
//public class FacebookUtil
//{
//
//	private  FacebookUser mUser;
//
//	private  void makeMeRequest(final Session session)
//	{
//		// Make an API call to get user data and define a
//		// new callback to handle the response.
//		Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
//			@Override
//			public void onCompleted(GraphUser user, Response response)
//			{
//
//				// If the response is successful
//				if (session == Session.getActiveSession())
//				{
//					if (user != null)
//					{
//						// Set the id for the ProfilePictureView
//						// view that in turn displays the profile picture.
//						// profilePic.setProfileId(user.getId());
//						// profilePic.setVisibility(View.VISIBLE);
//						// authButton.setVisibility(View.GONE);
//						// Set the Textview's text to the user's name.
//						// userNameView.setText(user.getName());
//						String name = user.getUsername();
//						String strId = user.getId();
//						String strEmail = (String) user.getProperty("email");
//					//	String strBirthday = user.getBirthday();
//						GraphLocation location = user.getLocation();
//
//						JSONObject json = location.getInnerJSONObject();
//						String strCity = "";
//						try
//						{
//							strCity = json.getString("name");
//							Log.i("MainActivity", "location :" + strCity);
//						}
//						catch (JSONException e)
//						{
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						mUser = new FacebookUser(name, strId, strEmail, strCity);
//						
//
//					}
//				}
//				if (response.getError() != null)
//				{
//					Log.e("MainActivity", "Error -- " + response.getError());
//				}
//
//			}
//		});
//		request.executeAsync();
//
//	}
//
//	public  FacebookUser getmUser()
//	{
//		return mUser;
//	}
//
//	public static void setmUser(FacebookUser mUser)
//	{
//		FacebookUtil.mUser = mUser;
//	}
//	
//	
//	
//
//}
