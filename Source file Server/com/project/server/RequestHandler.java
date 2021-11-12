package com.project.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.project.bean.FileBean;
import com.project.bean.UserBean;
import com.project.dbhelper.FileTableHelper;
import com.project.dbhelper.UserTableHelper;
import com.project.util.Constants;
import com.project.util.FileUtils;

public class RequestHandler implements Runnable {

	Socket connectionSocket = null;

	public RequestHandler(Socket s) {
		connectionSocket = s;
	}

	@Override
	public void run() {

		try{

			DataInputStream din  = new DataInputStream(connectionSocket.getInputStream() );
			DataOutputStream dout = new DataOutputStream(connectionSocket.getOutputStream());

			String i=din.readUTF();
			System.out.println("Data from Client : "+i);
			//Create channel to read data coming from Client..

			//public upload...
			if(i.equals("1s"))
			{				
				int userId = din.readInt();
				System.out.println("userId:"+userId);

				String fileName = din.readUTF();
				System.out.println("filename"+fileName);

				int fileSize =  din.readInt();
				System.out.println("filesize");
			
				byte [] data = new byte[fileSize];
				System.out.println("filesize "+fileSize);

				int dataToRead = fileSize;
				int gotData = 0;

				while(gotData != fileSize)
				{
					gotData += din.read(data, gotData, dataToRead-gotData);
				}
				System.out.println("gotData: "+gotData);

				//Write file content in tmp..
				File fileToUpload = new File(Constants.TMP_FOLDER_PATH + File.separator + fileName);
				
				if(fileToUpload.exists())
					fileToUpload.delete();
				
				fileToUpload.createNewFile();
				
				FileOutputStream fout  = new FileOutputStream(fileToUpload);
				fout.write(data);
				fout.flush();
				fout.close();
				
				//Now do chunking and upload them in database ...and remove duplicate chunks..
				FileUtils.saveFileInPart(fileToUpload, userId,0);
			}

			//Public Dir Listing...
			else if(i.equals("1d"))
			{
				int userId = din.readInt();
				System.out.println("inside 2 userID is ::"+userId);
				
				//Fetch  entries of  public file in DB..
				FileTableHelper helper = new FileTableHelper();
				ArrayList<FileBean> files = helper.fetchAllPublicFiles();
				
				dout.writeInt(files.size());
				dout.flush();

				for(FileBean file : files)
				{
					System.out.println("file:" + file);
					dout.writeUTF(file.getFileName());
					dout.flush();
				}
			}
			
			//Public file download
			else if(i.equals("1r"))
			{
				int userId = din.readInt();

				String filename = din.readUTF();
				System.out.println("filename"+filename);

				//Add entry of file in DB..
				FileTableHelper helper = new FileTableHelper();
				FileBean bean = helper.getFileIdFromFileName(filename);
				
				File dic = FileUtils.downloadFileFromPart(bean.getId());
				byte [] data = new byte[(int)dic.length()];

				FileInputStream fin = new FileInputStream(dic);
				fin.read(data);
				
				dout.writeInt(data.length);
				dout.flush();

				dout.write(data);
				dout.flush();

			}
			else if(i.equals("2s"))
			{		
				//private upload
				int userId = din.readInt();
				System.out.println("userId:"+userId);

				String fileName = din.readUTF();
				System.out.println("filename"+fileName);

				int fileSize =  din.readInt();
				System.out.println("filesize");
				
				byte [] data = new byte[fileSize];
				System.out.println("filesize "+fileSize);

				int dataToRead = fileSize;
				int gotData = 0;

				while(gotData != fileSize)
				{
					gotData += din.read(data, gotData, dataToRead-gotData);
				}
				System.out.println("gotData: "+gotData);
				
				//Do SEED Logic..
				//Get User SEED from DB
				UserTableHelper uhelper = new UserTableHelper();

				String seedData = uhelper.takeSeed(userId); //GET this from db..
				System.out.println("seedData"+seedData);
				//and call xor(seed_data, fileData);

				data = XOR.doXor(seedData.getBytes(), data);

				//Write file content in tmp..
				File fileToUpload = new File(Constants.TMP_FOLDER_PATH + File.separator + fileName);
				
				if(fileToUpload.exists())
					fileToUpload.delete();
				
				fileToUpload.createNewFile();
				
				FileOutputStream fout  = new FileOutputStream(fileToUpload);
				fout.write(data);
				fout.flush();
				fout.close();
				
				//Now do chunking and upload them in database ...and remove duplicate chunks..
				FileUtils.saveFileInPart(fileToUpload, userId,1);
			}
			else if(i.equals("2d"))
			{
				int userId = din.readInt();
				System.out.println("inside 2 userID is ::"+userId);
				
				//Fetch  entries of  public file in DB..
				FileTableHelper helper = new FileTableHelper();
				ArrayList<FileBean> files = helper.fetchAllPrivateFilesByUserId(userId);
				
				dout.writeInt(files.size());
				dout.flush();

				for(FileBean file : files)
				{
					System.out.println("file:" + file);
					dout.writeUTF(file.getFileName());
					dout.flush();
				}
			}
			else if(i.equals("2r"))
			{
				
				int userId = din.readInt();

				String filename = din.readUTF();
				System.out.println("filename"+filename);

				//Add entry of file in DB..
				FileTableHelper helper = new FileTableHelper();
				FileBean bean = helper.getFileIdFromFileName(filename);
				
				File dic = FileUtils.downloadFileFromPart(bean.getId());
				byte [] data = new byte[(int)dic.length()];

				FileInputStream fin = new FileInputStream(dic);
				fin.read(data);
				
				//Do SEED Logic..
				//Get User SEED from DB
				//String seedData = "1234567890"; //GET this from db..
				//and call xor(seed_data, fileData);
				UserTableHelper uhelper =new UserTableHelper();
				String seedData = uhelper.takeSeed(userId); //GET this from db..
				System.out.println("seedData:s"+seedData);
				
				data = XOR.doXor(seedData.getBytes(), data);

				dout.writeInt(data.length);
				dout.flush();

				dout.write(data);
				dout.flush();
			}
			else if(i.equals("7"))
			{
				System.out.println("inside if of 7");

				String FirstName =din.readUTF();
				String LastName = din.readUTF();
				String EmailId= din.readUTF();
				String PhoneNo = din.readUTF();
				String PinCode= din.readUTF();
				String UserName =din.readUTF();
				String PassWord= din.readUTF();
				
				long seed = System.currentTimeMillis();

				UserBean bean =new UserBean();
				bean.setFirst_name(FirstName);
				bean.setLast_name(LastName);
				bean.setPhone_no(PhoneNo);
				bean.setEmail(EmailId);
				bean.setUsername(UserName);
				bean.setPass_word(PassWord);
				bean.setSeed(seed);
				bean.setPincode(PinCode);
				
				System.out.println("before adding user");

				UserTableHelper userTableHelper =new UserTableHelper();
				userTableHelper.addUser(bean);
				System.out.println("user ADDED TO DB");
				int userId =bean.getUserid();
				System.out.println("userId"+userId);
				//return userId on successful registration
				dout.writeInt(userId);
				dout.flush();
			}
			else if(i.equals("8"))
			{
				String UserName =din.readUTF();
				String PassWord= din.readUTF();
				
				System.out.println("inside 8");
				
				UserBean bean =new UserBean();
				bean.setUsername(UserName);
				bean.setPass_word(PassWord);

				UserTableHelper userTableHelper = new UserTableHelper();
				userTableHelper.validateUser(bean);
				int userId = bean.getUserid();
				System.out.println("login userId::"+userId);
				//return userId on successful registration
				dout.writeInt(userId);
				dout.flush();
			}
			
			else if(i.equals("9"))
			{
				int userid = din.readInt();
				String pincode = din.readUTF();
				System.out.println("inside 9");
				UserTableHelper userTableHelper = new UserTableHelper();
				int result = userTableHelper.validatePin(pincode, userid);
				dout.writeInt(result);
				dout.flush();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		UserBean bean =new UserBean();
		bean.setUsername("abc");
		bean.setPass_word("abc");

		UserTableHelper userTableHelper = new UserTableHelper();
		userTableHelper.validateUser(bean);
		int userId = bean.getUserid();
		System.out.println("login userId::"+userId);
		
	}
}
