package com.project.httpserver;

import com.project.dbhelper.FileDetailTableHelper;
import com.project.dbhelper.FilePartTableHelper;
import com.project.dbhelper.FileTableHelper;

public class StatsCalculator {

	
	public static int GetTotalfileCount()
	{
		FileTableHelper fileHelper=new FileTableHelper();
		int count=fileHelper.getTotalFileUploaded();
		System.out.println("StatsCalculator getTotalfileCount:"+count);
		return count;
	}
	
	
	
	public static int TotalSizeNeeded()
	{
		FileDetailTableHelper helper=new FileDetailTableHelper();
		int count=helper.getTotalSizeNeeded();
		return count*100;
	}
	
	public static int acutalSizeCounsumed()
	{
		FilePartTableHelper helper=new FilePartTableHelper();
		int count=helper.getActualSizeConsumed();
		return count*100;
	}
	
	
}
