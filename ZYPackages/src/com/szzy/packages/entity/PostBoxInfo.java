package com.szzy.packages.entity;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.szzy.packages.tool.Tools;

public class PostBoxInfo {

	String lockName ;//投递柜名
	List<Box> listBox ;//箱子
	int boxNum ;//箱子数量
	public String getLockName() {
		return lockName;
	}
	public void setLockName(String lockName) {
		String name = null; 
		try {
			name = new String(Tools.HexString2Bytes(lockName), "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.lockName = name;
	}
	public List<Box> getListBox() {
		return listBox;
	}
	public void setListBox(List<Box> listBox) {
		this.listBox = listBox;
	}
	public int getBoxNum() {
		return boxNum;
	}
	public void setBoxNum(int boxNum) {
		this.boxNum = boxNum;
	}
	
}
