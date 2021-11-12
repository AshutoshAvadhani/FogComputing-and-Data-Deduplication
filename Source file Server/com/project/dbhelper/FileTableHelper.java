package com.project.dbhelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.project.bean.FileBean;
import com.project.db.DataBaseConnection;


public class FileTableHelper  {
	public static final String FILE_TABLE_NAME = "file";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_FILE_NAME = "fileName";
	public static final String COLUMN_FILE_TYPE = "fileType";
	
	Connection conn;
	PreparedStatement statement; //PreparedStatement object for sending SQL statements to the database
	DataBaseConnection db=new DataBaseConnection();

	public FileTableHelper() {
		super();
	}

	public ArrayList<FileBean> fetchAll(){
		conn=db.connect();
		ArrayList<FileBean> userBeanList = new ArrayList<FileBean>();
		String query = "select * from "+FILE_TABLE_NAME;
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet!=null){

				resultSet.beforeFirst();
				while(resultSet.next()){
					userBeanList.add(fetchFileFromResultSet(resultSet));
				}
				resultSet.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		return userBeanList;
	}
	
	public FileBean fetchFileByFileId(int fileId){

		conn=db.connect();
		FileBean fileBean = null;
		String query = "select * from " + FILE_TABLE_NAME + " where id = "+ fileId;
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet!=null){

				resultSet.beforeFirst();
				while(resultSet.next()){
					fileBean = fetchFileFromResultSet(resultSet);
				}
				resultSet.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return fileBean;
	}
	
	public ArrayList<FileBean> fetchAllFilesByUserId(int userId){
		conn=db.connect();
		ArrayList<FileBean> fileBeanList = new ArrayList<FileBean>();
		String query = "select * from " + FILE_TABLE_NAME + " where userId = "+userId;
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet!=null){

				resultSet.beforeFirst();
				while(resultSet.next()){
					fileBeanList.add(fetchFileFromResultSet(resultSet));
				}
				resultSet.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return fileBeanList;
	}
	
	public ArrayList<FileBean> fetchAllPublicFiles(){
		conn=db.connect();
		ArrayList<FileBean> fileBeanList = new ArrayList<FileBean>();
		String query = "select * from " + FILE_TABLE_NAME + " where isPrivate=0" ;
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet!=null){

				resultSet.beforeFirst();
				while(resultSet.next()){
					fileBeanList.add(fetchFileFromResultSet(resultSet));
				}
				resultSet.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return fileBeanList;
	}
	
	public ArrayList<FileBean> fetchAllPrivateFilesByUserId(int userId)
	{
		conn=db.connect();
		ArrayList<FileBean> fileBeanList = new ArrayList<FileBean>();
		String query = "select * from " + FILE_TABLE_NAME + " where isPrivate=1 and userId="+userId;
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet!=null){

				resultSet.beforeFirst();
				while(resultSet.next()){
					fileBeanList.add(fetchFileFromResultSet(resultSet));
				}
				resultSet.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return fileBeanList;
	}

	private FileBean fetchFileFromResultSet(ResultSet resultSet) throws SQLException{ 
		FileBean userBean=new FileBean();
		userBean.setId(resultSet.getInt(FileTableHelper.COLUMN_ID));
		userBean.setFileName(resultSet.getString(FileTableHelper.COLUMN_FILE_NAME));
		userBean.setFileType(resultSet.getString(FileTableHelper.COLUMN_FILE_TYPE));
		return userBean;
	}

	public int insert(FileBean fileBean)
	{
		conn=db.connect();
		int fileId = 0;
		String query = "insert into file(fileName,fileType,userId,isPrivate) values('"+fileBean.getFileName()+"','"+fileBean.getFileType()+"','"+fileBean.getUserId()+"',"+fileBean.getIsPrivate()+")";
		try {
			Statement statement = conn.createStatement();
			statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = statement.getGeneratedKeys();
	        if (rs.next()){
	            fileId=rs.getInt(1);
	        }
	        rs.close();
	      
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileId;
	}

	
	public int getTotalFileUploaded()
	{
		conn=db.connect();
		int count = 0;
		String query = "select COUNT(*) from fog_homecloud_db.file";
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
	
	
	public FileBean getFileIdFromFileName(String filename)
	{
		conn=db.connect();
		FileBean fileBean = null;
		String query = "select * from " + FILE_TABLE_NAME + " where fileName='"+ filename + "'";
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if(resultSet!=null){

				resultSet.beforeFirst();
				while(resultSet.next()){
					fileBean = fetchFileFromResultSet(resultSet);
				}
				resultSet.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return fileBean;
	}
	
}
