package com.example.yhack2013;

public class ToDoList {
	public ToDoList(int id_, int importance_, int duedate_, String title_, String description_) {
		id = id_;
		importance = importance_;
		duedate = duedate_;
		title = title_;
		description = description_;
	    priority = 0;
	}
	public int id;
	public int importance;
	public int duedate;
    public int priority;
	public String title;
	public String description;
}
