package com.project.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import com.project.bean.FileBean;
import com.project.bean.FileDetailBean;
import com.project.bean.FilePartBean;
import com.project.dbhelper.FileTableHelper;
import com.project.dbhelper.FileDetailTableHelper;
import com.project.dbhelper.FilePartTableHelper;

public class FileUtils {

	public static void saveFileInPart(File file, int userId, int isPrivate) {
		try {
			FileBean fileBean = new FileBean();
			fileBean.setFileName(file.getName());
			fileBean.setFileType(getFileType(file.getName().substring(file.getName().lastIndexOf(".")+1)));
			fileBean.setUserId(userId);
			fileBean.setIsPrivate(isPrivate);
			
			FileTableHelper fileTableHelper = new FileTableHelper();
			int fileId = fileTableHelper.insert(fileBean);
			
			System.out.println("inserted fileid :" + fileId);
			long filesSize = file.length();
			FileInputStream fin = new FileInputStream(file);
			byte[] filePart = new byte[100];
			int length;
			while((length = fin.read(filePart, 0 , (int)Math.min(filePart.length,filesSize)))!=0) 
			{
				System.out.println("in");
				String md5 = getMD5(filePart);
				int filePartId;
				if((filePartId = getMatchedLocalFilePart(fileBean.getFileType(), md5)) != 0) 
				{
					System.out.println("inside if");
					FileDetailBean fileDetailBean = new FileDetailBean();
					fileDetailBean.setFilePartId(filePartId);
					fileDetailBean.setFileId(fileId);
					fileDetailBean.setIsLocal(1);
					FileDetailTableHelper fileDetailTableHelper = new FileDetailTableHelper();
					fileDetailTableHelper.insert(fileDetailBean);
				} 
				else
				{
					System.out.println("else");
					FilePartBean filePartBean = new FilePartBean();
					filePartBean.setFilePartData(filePart);
					filePartBean.setMd5(getMD5(filePart));
					FilePartTableHelper filePartTableHelper = new FilePartTableHelper();
					filePartId = filePartTableHelper.insert(filePartBean);
					
					FileDetailBean fileDetailBean = new FileDetailBean();
					fileDetailBean.setFilePartId(filePartId);
					fileDetailBean.setFileId(fileId);
					fileDetailBean.setIsLocal(1);
					FileDetailTableHelper fileDetailTableHelper = new FileDetailTableHelper();
					fileDetailTableHelper.insert(fileDetailBean);
				}
				filesSize-=length;
				System.out.println(filesSize);
			}
			fin.close();
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	public static File downloadFileFromPart(int fileId) 
	{
		File file = null;
		try {
			FilePartTableHelper filePartTableHelper = new FilePartTableHelper();
			ArrayList<FilePartBean> filePartBeanList = filePartTableHelper.fetchAllFilePartByFileId(fileId);
			
			File dir = new File(Constants.DEDUP_FOLDER_PATH);
			dir.mkdirs();
			
			FileTableHelper fileTableHelper = new FileTableHelper();
			FileBean fileBean = fileTableHelper.fetchFileByFileId(fileId);
			String filePath = dir.getAbsolutePath() + File.separator + fileBean.getFileName();
			file = new File(filePath);
			
			if(file.exists())
				file.delete();
			file.createNewFile();

			FileOutputStream fos = new  FileOutputStream(file);
			for(FilePartBean filePartBean : filePartBeanList) 
			{
				fos.write(filePartBean.getFilePartData(), 0, filePartBean.getFilePartData().length);
			}
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	public static int getMatchedLocalFilePart(String fileType, String md5) 
	{
		FilePartTableHelper filePartTableHelper = new FilePartTableHelper();
		return filePartTableHelper.fetchFilePartByMD5(md5);
	}
	
	public static String getMD5(byte[] bytes){ 
		String digest = null; 
		try { 
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			byte[] hash = md.digest(bytes); 
			//converting byte array to Hexadecimal String 
			StringBuilder sb = new StringBuilder(2*hash.length); 
			for(byte b : hash) { 
				sb.append(String.format("%02x", b&0xff)); 
			} 
			digest = sb.toString(); 
		} catch (NoSuchAlgorithmException ex) { 
			ex.printStackTrace();
		} 
		return digest; 
	}
	
	public static String getFileType(String fileExt) {
		System.out.println(fileExt);
		String fileType = "";
		if("png".equalsIgnoreCase(fileExt) || "jpg".equalsIgnoreCase(fileExt) || "jpeg".equalsIgnoreCase(fileExt)) 
		{
			fileType = Constants.FILE_TYPE_IMAGE;
		} 
		else if("flv".equalsIgnoreCase(fileExt) || "mp4".equalsIgnoreCase(fileExt)) 
		{
			fileType = Constants.FILE_TYPE_VIDEO;
		} 
		else if("mp3".equalsIgnoreCase(fileExt)) 
		{
			fileType = Constants.FILE_TYPE_AUDIO;
		} 
		else 
		{
			fileType = Constants.FILE_TYPE_TEXT;
		}
		return fileType;
	}
	
}
