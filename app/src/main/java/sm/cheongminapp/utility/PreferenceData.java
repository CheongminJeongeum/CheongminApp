package sm.cheongminapp.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tjswo on 2015-11-04.
 */
public class PreferenceData {
    private final String PREF_NAME = "sm.cheongminapp";
    static Context mContext;

    public PreferenceData(Context c) {
        mContext = c;
    }

    public void put(String key, String value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void put(String key, boolean value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void put(String key, int value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void put(String key, long value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public String getValue(String key, String dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        try {
            return pref.getString(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public boolean getValue(String key, boolean dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        try {
            return pref.getBoolean(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public int getValue(String key, int dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        try {
            return pref.getInt(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public Long getValue(String key, long dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME,
                Activity.MODE_PRIVATE);
        try {
            return pref.getLong(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

}
