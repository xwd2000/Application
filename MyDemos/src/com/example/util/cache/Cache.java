package com.example.util.cache;

public interface Cache <IDType,T>{
	  public void put(IDType key, T entry);
	  public T get(IDType key);
		   
}
