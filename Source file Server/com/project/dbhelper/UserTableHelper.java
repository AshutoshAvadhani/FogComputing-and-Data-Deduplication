package com.project.dbhelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.project.bean.UserBean;
import com.project.db.DataBaseConnection;


public class UserTableHelper {

	Connection conn;
	PreparedStatement statement; //PreparedStatement object for sending SQL statements to the database
	DataBaseConnection db=new DataBaseConnection();

	public int addUser(UserBean user_Bean)
	{

		conn=db.connect();
		try
		{
			statement=conn.prepareStatement("insert into user_table(fname ,lname ,email ,phoneNo ,username ,password ,seed, pincode) values(?,?,?,?,?,?,?,?)");


			statement.setString(1,user_Bean.getFirst_name());
			statement.setString(2,user_Bean.getLast_name());
			statement.setString(3, user_Bean.getEmail());
			statement.setString(4,user_Bean.getPhone_no());
			statement.setString(5, user_Bean.getUsername());
			statement.setString(6,user_Bean.getPass_word());
			statement.setLong(7,user_Bean.getSeed());
			statement.setString(8,user_Bean.getPincode());
			statement.executeUpdate();

			//max id
			statement=conn.prepareStatement("select max(user_id) FROM user_table");

			ResultSet resultSet=statement.executeQuery();
			System.out.println("resultset");
			if(resultSet.next())
			{
				int userId=resultSet.getInt(1);
				System.out.println("userId"+userId);
				user_Bean.setUserid(userId);
				return userId;
			}


		}


		catch(Exception e)
		{
			e.printStackTrace();
		}


		return 0;

	}
	//........................................
	public UserBean validateUser(UserBean userbean)
	{
		conn=db.connect();

		try {
			statement=conn.prepareStatement("select * from user_table where username=? and password=?;");

			statement.setString(1, userbean.getUsername());
			statement.setString(2, userbean.getPass_word());

			ResultSet rs=statement.executeQuery();

			rs.beforeFirst();

			if(rs.next())
			{
				userbean.setUserid(rs.getInt("user_id"));
				userbean.setFirst_name(rs.getString("fname"));
				userbean.setLast_name(rs.getString("lname"));
				userbean.setEmail(rs.getString("email"));
				userbean.setPhone_no(rs.getString("phoneNo"));
				userbean.setUsername(rs.getString("username"));
				userbean.setPass_word(rs.getString("password"));

				return userbean;
			}

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}
	//.....take currentTime from db 
	public String takeSeed(int userId)
	{
		conn=db.connect();
		try
		{
			statement=conn.prepareStatement("select seed from user_table where user_Id=?");
			statement.setInt(1,userId);
			ResultSet set = statement.executeQuery();
			set.next();
			String seed = set.getLong(1) + "";
			return seed;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	public int validatePin(String pincode, int userid) {
	
		conn=db.connect();
		try
		{
			statement=conn.prepareStatement("select pincode from user_table where user_id=?");
			statement.setInt(1, userid);
			ResultSet set = statement.executeQuery();
			set.next();
			String pin = set.getString(1);
			
			if(pin.equals(pincode + ""))
			{
				return 1;
			}
			else
				return 0;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return 0;
	}
}




