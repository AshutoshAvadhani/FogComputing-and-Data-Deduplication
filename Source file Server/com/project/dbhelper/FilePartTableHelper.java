package com.project.dbhelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.project.bean.FilePartBean;
import com.project.db.DataBaseConnection;


public class FilePartTableHelper
{
	public static final String FILE_PART_TABLE_NAME = "filepart";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_FILE_ID = "fileId";
	public static final String COLUMN_MD5 = "md5";
	public static final String COLUMN_FILE_PART_DATA = "filePartData";
	
	Connection conn;
	PreparedStatement statement; //PreparedStatement object for sending SQL statements to the database
	DataBaseConnection db=new DataBaseConnection();

	public FilePartTableHelper() {
		super();
	}

	public ArrayList<FilePartBean> fetchAllFilePartByType(String fileType){
		conn=db.connect();
		ArrayList<FilePartBean> filePartBeanList = new ArrayList<FilePartBean>();
		String query = "select * from filePart where id in(select filePartId from filedetail where fileId in (select id from file where fileType = '"+fileType+"'))";
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet!=null){

				resultSet.beforeFirst();
				while(resultSet.next()){
					filePartBeanList.add(fetchFilePartFromResultSet(resultSet));
				}
				resultSet.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return filePartBeanList;
	}
	
	public int fetchFilePartByMD5(String md5){
		conn=db.connect();
		String query = "select id from filepart where md5='"+md5+"'";
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet!=null){

				resultSet.beforeFirst();
				while(resultSet.next()){
					return resultSet.getInt(1);
				}
				resultSet.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	private FilePartBean fetchFilePartFromResultSet(ResultSet resultSet) throws SQLException{ 
		FilePartBean filePartBean=new FilePartBean();
		filePartBean.setId(resultSet.getInt(FilePartTableHelper.COLUMN_ID));
		filePartBean.setFilePartData(resultSet.getBytes(FilePartTableHelper.COLUMN_FILE_PART_DATA));
		filePartBean.setMd5(resultSet.getString(FilePartTableHelper.COLUMN_MD5));
		return filePartBean;
	}

	public int getActualSizeConsumed()
	{
		conn=db.connect();
		int count = 0;
		String query = "select COUNT(id) from fog_homecloud_db.filepart";
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			rs.beforeFirst();
	        if (rs.next()){
	            count=rs.getInt(1);
	        }
	        rs.close();
	      
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	
	
	public ArrayList<FilePartBean> fetchAllFilePartByFileId(int fileId){
		conn=db.connect();
		ArrayList<FilePartBean> filePartBeanList = new ArrayList<FilePartBean>();
		String query = "select fp.*, fd.is_local from filedetail fd,filepart fp where fd.filePartId = fp.id and fd.fileId = "+fileId;
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet!=null){

				resultSet.beforeFirst();
				while(resultSet.next()){
					filePartBeanList.add(fetchFilePartFromResultSet(resultSet, fileId));
				}
				resultSet.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return filePartBeanList;
	}



	private FilePartBean fetchFilePartFromResultSet(ResultSet resultSet, int fileId) throws SQLException{ 
		FilePartBean filePartBean=new FilePartBean();
		filePartBean.setId(resultSet.getInt(FilePartTableHelper.COLUMN_ID));
		filePartBean.setMd5(resultSet.getString(FilePartTableHelper.COLUMN_MD5));
		int isLocal = resultSet.getInt(FileDetailTableHelper.COLUMN_IS_LOCAL);
		
		if(isLocal == 1)
		{
			filePartBean.setFilePartData(resultSet.getBytes(FilePartTableHelper.COLUMN_FILE_PART_DATA));
		}
		
		return filePartBean;
	}

	public int insert(FilePartBean fileBean)
	{
		conn=db.connect();
		int filePartId = 0;
		String query = "insert into filepart(filePartData,md5) values (?,?)";
		try {
			java.sql.PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setBytes(1, fileBean.getFilePartData());
			statement.setString(2,  fileBean.getMd5());

			statement.executeUpdate();
			ResultSet tableKeys = statement.getGeneratedKeys();
			tableKeys.next();
			filePartId = tableKeys.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return filePartId;
	}

	public ResultSet fetchLastInsertedRow(){
		String query = "SELECT * FROM userTable order by userId desc limit 1";
		ResultSet resultSet = null;
		try {
			Statement statement = conn.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public byte[] fetchFilePartByPartID(int filePartID) {
		conn=db.connect();
		String query = "select "+FilePartTableHelper.COLUMN_FILE_PART_DATA+" from filePart where id=" + filePartID;
		System.out.println("query:" + query);
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet!=null){
				resultSet.beforeFirst();
				if(resultSet.next()){
					return resultSet.getBytes(COLUMN_FILE_PART_DATA);
				}
				resultSet.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
