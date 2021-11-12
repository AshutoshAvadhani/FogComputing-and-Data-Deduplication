package com.project.bean;

public class FilePartBean {

	private int id;
	private byte[] filePartData;
	private String md5;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public byte[] getFilePartData() {
		return filePartData;
	}
	public void setFilePartData(byte[] filePartData) {
		this.filePartData = filePartData;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
}