package com.example.yhack2013;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Environment;

public class DataManager {

    public static boolean deleteItemFromFile(int id) {
    	try{
    		File dir = Environment.getExternalStorageDirectory();
			File file = new File(dir, "todo.json");
			if (file.exists()) {
				// read file in as json string
				FileInputStream stream = new FileInputStream(file);
				String jString = null;
				try {
					 FileChannel fc = stream.getChannel();
					 MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
					 /* Instead of using default, pass in a decoder. */
					 jString = Charset.defaultCharset().decode(bb).toString();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				stream.close();
				JSONArray top = new JSONArray(jString);
				
				// make copy of json array with the item deleted
				JSONArray newTop = new JSONArray();
				int j = 0;
				for (int i = 0; i < top.length(); i++) {
					if (top.getJSONObject(i).getInt("Id") != id) {
						newTop.put(j, top.getJSONObject(i));
						j++;
					}
				}
				
				// write the new JSONArray to file
				try {
					String sOutput = newTop.toString();
					FileOutputStream fos;

					fos = new FileOutputStream(file);
					fos.write(sOutput.getBytes());
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return true;
			}
			else {
				return false;
			}
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
    }
}
