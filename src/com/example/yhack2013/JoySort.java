package com.example.yhack2013;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;


public class JoySort {
    
    public ArrayList<ToDoList> sort(ArrayList<ToDoList> ltToDos){
        
        
        ArrayList<ToDoList> PL = ltToDos;
        
        Collections.sort(PL, new DateCompare());
        for ( int i = 0; i < PL.size(); i++) {
        	if (i == 0)
        		PL.get(i).priority = 1;
        	else if (i < 3)
        		PL.get(i).priority = 2;
        	else if (i < 6)
        		PL.get(i).priority = 3;
        	else if (i < 10)
        		PL.get(i).priority = 4;
        	else
        		PL.get(i).priority = 5;
        	
        }
                
        
        return PL;
    
        }
    
}

class DateCompare implements Comparator<ToDoList> {
    public int compare(ToDoList pi1, ToDoList pi2){
        int iDayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    	int daysLeft1 = pi1.duedate - iDayOfYear;
    	int daysLeft2 = pi2.duedate - iDayOfYear;
    	int criteria1, criteria2;

        if (daysLeft1 == 0)
        	criteria1 = 4;
        else if (daysLeft1 < 3)
        	criteria1 = 3;
        else if (daysLeft1 < 7)
        	criteria1 = 2;
        else if (daysLeft1 < 31)
        	criteria1 = 1;
        else
        	criteria1 = 0;

        if (daysLeft2 == 0)
        	criteria2 = 4;
        else if (daysLeft2 < 3)
        	criteria2 = 3;
        else if (daysLeft2 < 7)
        	criteria2 = 2;
        else if (daysLeft2 < 31)
        	criteria2 = 1;
        else
        	criteria2 = 0;
    	
        return (pi1.importance + criteria1) - (pi2.importance + criteria2);
    }
}
