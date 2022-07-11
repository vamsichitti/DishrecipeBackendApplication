package com.recipes.exception;

public class UserAlreadyRegisteredException extends Exception{

	public  UserAlreadyRegisteredException(String msg){
		super(msg);
	}
}
