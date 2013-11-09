package com.example.yhack2013;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;


public class JoySort {
    
    public ArrayList<ToDoList> sort(ArrayList<ToDoList> ltToDos){
        
        
        ArrayList<ToDoList> PL = ltToDos;
        
        
        int criteria;
        int largestItemFlag = 0;
        
        
        for ( int i = 0; i < ltToDos.size(); i++) {
        	
            ToDoList PI = ltToDos.get(i);
            
            ToDoList item = ltToDos.get(i);
            
            int iDayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
            int DaysLeft = item.duedate - iDayOfYear;
            
            if (DaysLeft == 0){ 
                criteria = 1 + item.importance;
            } else if (DaysLeft < 3){
                criteria = 2 + item.importance;
            } else if (DaysLeft < 7){
                criteria = 3 + item.importance;
            } else if (DaysLeft < 31){
                criteria = 4 + item.importance;
            } else {
                criteria = 5 + item.importance;
            }
            
            if ((criteria == 2) && (largestItemFlag == 0)){
                PI.priority = 1;
                largestItemFlag = 1;
            } else if (criteria < 4){
                PI.priority = 2;
            } else if (criteria < 6){
                PI.priority = 3;
            } else if (criteria < 8){
                PI.priority = 4;
            } else {
                PI.priority = 5;
            }
        }
                
        Collections.sort(PL, new DateCompare());
        
        return PL;
    
        }
    
}

class DateCompare implements Comparator<ToDoList> {
    public int compare(ToDoList pi1, ToDoList pi2){
        return pi1.priority - pi2.priority;
    }
}
