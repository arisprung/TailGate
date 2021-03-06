package com.arisprung.tailgate.gcm;

import org.json.JSONException;
import org.json.JSONObject;

import com.arisprung.tailgate.MessageBean;

public class JsonUtil
{

	public static MessageBean parseJsonToMessageBean(JSONObject jobj)
	{

		MessageBean mBean = null;
		

		try
		{
			String strFaceId = jobj.getString("id");
			String strMessage = jobj.getString("message");
			String strName = jobj.getString("username");
			String strTeam = jobj.getString("team");
		
			

			mBean = new MessageBean(strFaceId, strMessage, strName,strTeam);
		}
		catch (JSONException e)
		{

			e.printStackTrace();
		}

		return mBean;

	}

}
