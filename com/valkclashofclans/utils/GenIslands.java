package com.valkclashofclans.utils;

public class GenIslands {
	public int x = 0, z = 0;
	
	private int dir_diff[][] = {
		  //x  y  
		  {0, +1}, // north
		  {-1, 0}, // west
		  {0, -1}, // south
		  {+1, 0}, // east
		};
	
	public GenIslands (int n) {
		int d = 1; //East
		int step = 0;
		
		int currx = 0, currz = 0;
		
		while (n > 0) {
	    for (int i = 0; i < 2 && n > 0; i++) {
	      
	      for (int j = 0; j < step && n > 0; j++) {
	        
	        x = currx;
	        z = currz;
	        
	        currx += dir_diff[d][0];
	        currz += dir_diff[d][1];
	        
	        n -= 1;
	      }
	      
	      d = (d+1)%4;
	      
	    }
	    
	    step++;
	  }
	}
}
