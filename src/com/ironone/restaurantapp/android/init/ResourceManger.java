package com.ironone.restaurantapp.android.init;

import java.io.File;

import android.os.Environment;
import android.util.Log;

public class ResourceManger extends Resources{	
	
	String TAG = "com.ironone.restaurantapp.android.init/ResourceManger";
	
	public void CreateResourceDirectoy(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
        	APP_DIR_FILE = new File(Environment.getExternalStorageDirectory() + "/"+getAppDirName());
	    	IMG_DIR_FILE = new File(Environment.getExternalStorageDirectory() + "/"+getAppDirName()+"/"+getImgDirName());
	    	createExternalDir(APP_DIR_FILE);
	    	createExternalDir(IMG_DIR_FILE);
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
        	Log.d(TAG+"ER101", "SD casrd is not writable");
            
        } else {
        	Log.d(TAG+"ER102", "SD card not found");
        }
	}
	
	
	
	private void createExternalDir(File file){
        boolean hasDir = false; 
        if (!file.exists()) { 
            hasDir = file.mkdir(); 
        } 
        if (!hasDir) { 
        	Log.d(TAG,"Directory already exists"); 
        } 
        else { 
        	Log.d(TAG,"Directory created at path "+file.getPath());
        }
    }

}
