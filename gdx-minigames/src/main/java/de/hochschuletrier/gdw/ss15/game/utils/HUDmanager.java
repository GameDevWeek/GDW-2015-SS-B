package de.hochschuletrier.gdw.ss15.game.utils;


import java.awt.Font;

import de.hochschuletrier.gdw.ss15.game.data.Team;

public class HUDmanager {

	private Team team;
	private Font font1;
	private Font font2;
	private int pointsred;
	private int pointsblue;
	private long time;
	
	
	
    public HUDmanager(Team team){ //Konstruktor 
    	this.team = team;
    	this.pointsred=0;
    	this.pointsblue=0;
    	this.time =System.currentTimeMillis();
    	
    }
    
    public synchronized void incrementPointsRed()
    {
    	this.pointsred++;   
    }
    
    public synchronized void incrementPointsBlue()
    {
    	this.pointsblue++;
    }
    
    public synchronized int getPointsRed()
    {
    	return this.pointsred;
    }
    
    public synchronized int getPointsBlue()
    {
    	return this.pointsblue;
    }
    
}
