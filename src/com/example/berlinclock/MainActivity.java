
package com.example.berlinclock;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/*
 * Using Threads and AsyncTask to develop Berlin Clock
 * 
 * The Berlin Uhr (Clock) is a rather strange way to show the time. On the top of the clock 
 * there is a yellow lamp that blinks on/off every two seconds. The time is calculated by 
 * adding rectangular lamps.
 * The top two rows of lamps are red. These indicate the hours of a day. In the top row there 
 * are 4 red lamps. Every lamp represents 5 hours. In the lower row of red lamps every lamp 
 * represents 1 hour. So if two lamps of the first row and three of the second row are switched 
 * on that indicates 5+5+3=13h or 1 pm.
 * The two rows of lamps at the bottom count the minutes. The first of these rows has 11 
 * lamps, the second 4. In the first row every lamp represents 5 minutes. In this first row the 
 * 3rd, 6th and 9th lamp are red and indicate the first quarter, half and last quarter of an 
 * hour. The other lamps are yellow. In the last row with 4 lamps every lamp represents 1 
 * minute.
 * The lamps are switched on from left to right.
 * Examples:
 * 13:17:01		O
 * 				RROO
 * 				RRRO
 * 				RRROOOOOOOO
 * 				RROO
 */
public class MainActivity extends Activity {
    Timer timer;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        timer = new Timer();
        
        // Using Threads
        // mainProcessing();
        
        // Using AsyncTask
        new MyAsyncTask().execute();
    
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
    
    // Clock Methods
    //=================================================================================
    
    protected int[] getClock(final int hours, final int minutes, final int seconds) {
    	int[] lights = new int[5];
        lights[0] = (seconds / 2) % 2;
        lights[1] = hours / 5;
        lights[2] = hours % 5;
        lights[3] = minutes / 5;
        lights[4] = minutes % 5;
        return lights;
    }

    private void updateLights(final int[] lights) {
        findViewById(R.id.sec21).setBackgroundColor(getColor(lights[0], 1));
        for (int i = 0; i < 11; i++) {
            if (i < 4) {
                ((ViewGroup) findViewById(R.id.h5)).getChildAt(i).setBackgroundColor(getColor(lights[1], i + 1));
                ((ViewGroup) findViewById(R.id.h1)).getChildAt(i).setBackgroundColor(getColor(lights[2], i + 1));
                ((ViewGroup) findViewById(R.id.m1)).getChildAt(i).setBackgroundColor(getColor(lights[4], i + 1));
            }
            ((ViewGroup) findViewById(R.id.m5)).getChildAt(i).setBackgroundColor(getColor(lights[3], i + 1));
        }
    }

    private int getColor(final int data, final int level) {
        return data >= level ? Color.RED : Color.GRAY;
    }
    
    // Using Thread
    //=================================================================================
    
    private void mainProcessing() {
    	// Child thread
 		Thread thread = new Thread(myRunnable, "Background");
 		thread.start();
 	}

 	private Runnable myRunnable = new Runnable() {
 		public void run() {
 			timer.schedule(new TimerTask() {
 	            @Override
 	            public void run() {
 	            	backgroundThreadProcessing();
 	            }
 	        }, 0, 2000);
 			
 		}
 	};

 	// Heavy homework
 	private void backgroundThreadProcessing() {
 		handler.post(doSomethingOutTheThread);
 	}

 	// Execute in the UI Thread
 	private Runnable doSomethingOutTheThread = new Runnable() {
 		public void run() {
 			Calendar c = Calendar.getInstance();
            updateLights(getClock(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                    c.get(Calendar.SECOND)));
 		}
 	};

 	// Using AsyncTask
    //=================================================================================
 	
 	public class MyAsyncTask extends android.os.AsyncTask<Void, Void, Void>{
 		
 		@Override
 	    protected Void doInBackground(Void... params) {
 			timer.schedule(new TimerTask() {
 		            @Override
 		            public void run() {
 		            	publishProgress();
 		            }
 		        }, 0, 2000);
 	       return null;
 	    }
 	 
 	    @Override
 	    protected void onProgressUpdate(Void... values) {
 	    	Calendar c = Calendar.getInstance();
            updateLights(getClock(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                     c.get(Calendar.SECOND)));
 	    }
 	 
 	    @Override
 	    protected void onPreExecute() {
 	    	//
 	    }
 	 
 	    @Override
 	    protected void onPostExecute(Void result) {
 	       //
 	    }
 	 

 		
 	}

}
