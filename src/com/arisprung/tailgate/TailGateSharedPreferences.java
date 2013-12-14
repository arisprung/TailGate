package com.arisprung.tailgate;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class TailGateSharedPreferences {
    
    private static final String SHARED_PREFS = "tailgate_prefs";
    private SharedPreferences mSharedPreferencesFile;
    private static TailGateSharedPreferences mHomeSharedPreferences = null;
    public static final String FACEBOOK_ID = "face_id";
    public static final String FACEBOOK_NAME = "face_name";
    public static final String FACEBOOK_EMAIL = "face_email";
    public static final String FACEBOOK_LOCATION = "face_location";
    public static final String FACEBOOK_FIRST_NAME = "face_first_name";
    public static final String FACEBOOK_LAST_NAME = "face_lastt_name";
    public static final String SELECTED_TEAM = "selected_team";
    public static final String SELECTED_LEAGUE = "selected_league";

    private TailGateSharedPreferences(Context context){
    	mSharedPreferencesFile = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
    }
    
    
    public static synchronized TailGateSharedPreferences getInstance(Context context){
    	
    	if(mHomeSharedPreferences == null){
    		mHomeSharedPreferences = new TailGateSharedPreferences(context);
    	}
    	
    	return mHomeSharedPreferences;
    }
    
	public void putBooleanSharedPreferences(String key, boolean keyValue){
	    
	    if(mSharedPreferencesFile != null){
	    	Editor editor = mSharedPreferencesFile.edit();
        	editor.putBoolean(key, keyValue);
        	editor.commit();
	    }
	}
    
	public void putStringSharedPreferences(String key, String keyValue){
	    
	    if(mSharedPreferencesFile != null){
        	    Editor editor = mSharedPreferencesFile.edit();
        	    editor.putString(key, keyValue);
        	    editor.commit();
	    }
	}
	
	public void putIntSharedPreferences(String key, int keyValue){
	    
	    if(mSharedPreferencesFile != null){
        	    Editor editor = mSharedPreferencesFile.edit();
        	    editor.putInt(key, keyValue);
        	    editor.commit();
	    }
	}
	
	
	public String getStringSharedPreferences(String key, String defValue){
	    
	    if(mSharedPreferencesFile != null)
	    	return mSharedPreferencesFile.getString(key, defValue);
	    
	    return defValue;
	}
	
	
	public boolean getBooleanSharedPreferences(String key, boolean defValue){
	    
	    if(mSharedPreferencesFile != null)
	    	return mSharedPreferencesFile.getBoolean(key, defValue);
	    
	    return defValue;
	}
	
	public int getIntSharedPreferences(String key, int defValue){
	    
	    if(mSharedPreferencesFile != null)
	    	return mSharedPreferencesFile.getInt(key, defValue);
	    
	    return defValue;
	    
	}
	
	
	public boolean contains(String key){
		
	    if(mSharedPreferencesFile != null)
	    	return mSharedPreferencesFile.contains(key);
	    
	    return false;
		
	}
	
	

	
	
	public void removeKey(String keyToRemove){
		
		if(mSharedPreferencesFile != null){
			
    	    Editor editor = mSharedPreferencesFile.edit();
    	    editor.remove(keyToRemove);
    	    editor.commit();
		}		
	}
}
