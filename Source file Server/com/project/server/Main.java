package com.project.server;

import java.io.File;

import com.project.util.FileUtils;

public class Main {

	public static void main(String[] args) 
	{
		FileUtils.saveFileInPart(new File("D:\\resume.arff"), 1, 0);
	}
}
