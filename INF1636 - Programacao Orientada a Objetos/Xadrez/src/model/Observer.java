package model;

import controller.Event;

public interface Observer 
{
	void update(Event event);
}