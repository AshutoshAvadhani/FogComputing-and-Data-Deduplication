package com.project.server;

public class XOR 
{
	public static byte [] doXor (byte [] seedData, byte [] fileData)
	{
		byte [] destData = new byte[fileData.length];
		
		System.arraycopy(fileData, 0, destData, 0, destData.length);
		
		for(int i=0; i<seedData.length; i++)
		{
			destData[i] = (byte) (seedData[i] ^ fileData[i]);
		}
		return destData;
	}
	
	public static void main(String []args)
	{
		String seed = "ABCDE";
		String data = "123456789";
		
		byte [] output = XOR.doXor(seed.getBytes(), data.getBytes());
		
		System.out.println("output:" + new String(output));	
		
		output = XOR.doXor(seed.getBytes(), output );
		System.out.println("output:" + new String(output));
		
	}
}

