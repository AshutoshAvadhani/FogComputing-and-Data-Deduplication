package com.project.dbhelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.project.bean.FileDetailBean;
import com.project.db.DataBaseConnection;


public class FileDetailTableHelper {
	public static final String FILE_DETAIL_TABLE_NAME = "filedetail";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_FILE_PART_ID = "filePartId";
	public static final String COLUMN_FILE_ID = "fileId";
	public static final String COLUMN_IS_LOCAL = "is_local";
	
	Connection conn;
	PreparedStatement statement; //PreparedStatement object for sending SQL statements to the database
	DataBaseConnection db=new DataBaseConnection();

	public FileDetailTableHelper() {
		super();
	}

	public ArrayList<FileDetailBean> fetchAll(){
		conn=db.connect();
		ArrayList<FileDetailBean> userBeanList = new ArrayList<FileDetailBean>();
		String query = "select * from "+FILE_DETAIL_TABLE_NAME;
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
		}
		return userBeanList;
	}

	private FileDetailBean fetchFileFromResultSet(ResultSet resultSet) throws SQLException{ 
		FileDetailBean userBean=new FileDetailBean();
		userBean.setId(resultSet.getInt(FileDetailTableHelper.COLUMN_ID));
		userBean.setFilePartId(resultSet.getInt(FileDetailTableHelper.COLUMN_FILE_PART_ID));
		return userBean;
	}
	
	public int insert(FileDetailBean fileBean)
	{
		
		System.out.println("in insert File Details");
		System.out.println("islocal"+fileBean.getIsLocal());
		conn=db.connect();
		String query = "insert into filedetail("+COLUMN_FILE_ID+","+COLUMN_FILE_PART_ID+","+COLUMN_IS_LOCAL+") values('"+fileBean.getFileId()+"','"+fileBean.getFilePartId()+"','"+fileBean.getIsLocal()+"')";
		try {
			Statement statement = conn.createStatement();
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
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

	public ResultSet getUserByUserId(int userId){
		String query = "SELECT * FROM userTable where userId = " + userId;
		ResultSet resultSet = null;
		try {
			Statement statement = conn.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public ResultSet getUserByUsernameAndPassword(String username, String password){
		String query = "SELECT * FROM userTable where user_name = '" + username + "' and password = '"+password+"'";
		ResultSet resultSet = null;
		try {
			Statement statement = conn.createStatement();
			resultSet = statement.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public int getTotalSizeNeeded()
	{
		conn=db.connect();
		int count = 0;
		String query = "select COUNT(fileId) from fog_homecloud_db.filedetail";
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
	
	
	
}
