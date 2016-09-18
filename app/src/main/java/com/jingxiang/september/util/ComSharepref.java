package com.jingxiang.september.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wu on 2016/9/13.
 * 常见的保存对象是
 */
public class ComSharepref {
   private final String NAME = "SEPTEMBER";
   public static ComSharepref comSharepref;
   public static SharedPreferences sharedPref;
   public static SharedPreferences.Editor editor;

   private ComSharepref(Context context){
      sharedPref = context.getSharedPreferences(NAME,Context.MODE_PRIVATE);
      editor = sharedPref.edit();
   }

   public static ComSharepref getInstance(Context context){
      if(comSharepref == null)
         comSharepref = new ComSharepref(context);
      return comSharepref;
   }

   public static void put(String key,String value){
      editor.putString(key,value);
      editor.commit();
   }

   public static void put(String key,int value){
      editor.putInt(key,value);
      editor.commit();
   }

   public static String get(String key,String defualtValue){
     return sharedPref.getString(key,defualtValue);
   }

   public static int get(String key,int defualtValue){
      return sharedPref.getInt(key, defualtValue);
   }
}
