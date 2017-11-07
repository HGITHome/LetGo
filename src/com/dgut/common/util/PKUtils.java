package com.dgut.common.util;

import java.util.Calendar;
import java.util.Random;



public class PKUtils {
	static Random rand=new Random(); 

	public static int NextInt(final int min, final int max)
	{

		int tmp = Math.abs(rand.nextInt());
		return tmp % (max - min + 1) + min;
	}

	public static String getUniqueID(){
		Calendar c=Calendar.getInstance();
		int year=c.get(Calendar.YEAR);
		String month=String.format("%2d", c.get(Calendar.MONTH)+1).replace(" ", "0");
		String day=String.format("%2d", c.get(Calendar.DAY_OF_MONTH)).replace(" ", "0");
		String minute=String.format("%2d", c.get(Calendar.MINUTE)).replace(" ", "0");
		
		return year+month+day+minute+PKUtils.NextInt(1000,9999);
	}

	public static void main(String[] args){
		System.out.println(getUniqueID());
	}

}
