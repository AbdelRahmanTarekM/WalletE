package com.abdelrahman.wallete;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by AbdelRahman on 8/28/2016.
 */
public class AppWalletE extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration realmConfiguration=new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public static void save(Context context, int filterOption){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("filter",filterOption);
        editor.apply();
    }

    public static int load(Context context){
        SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("filter",Filter.NONE);
    }
}
