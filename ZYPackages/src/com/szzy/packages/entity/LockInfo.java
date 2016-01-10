package com.szzy.packages.entity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.szzy.packages.tool.Tools;

/**
 * ��������Ϣ
 * @author mac
 *
 */
public class LockInfo {

	private String systemid ;
	private String ename ;
	private int boxNum = 0;
	private List<BoxInfo> listBox ;
	public String getSystemid() {
		return systemid;
	}
	public void setSystemid(String systemid) {
		this.systemid = systemid;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		//����������������ʮ�����Ƶ�����ת������gbk��ʽ���ַ���
		try {
			ename = new String(Tools.HexString2Bytes(ename), "GBK") ;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.ename = ename;
	}
	public int getBoxNum() {
		return boxNum;
	}
	public void setBoxNum(int boxNum) {
		this.boxNum = boxNum;
	}
	public List<BoxInfo> getListBox() {
		return listBox;
	}
	public void setListBox(List<BoxInfo> listBox) {
		//���������չʾ��, 1 2 3Ϊ������Ͷ�����ţ� 4 5 ���������չʾ��
		List<BoxInfo> listBoxTemp = new ArrayList<BoxInfo>();
		if(listBox != null && !listBox.isEmpty()){
			for(int i = 0; i < listBox.size(); i++){
				if(listBox.get(i).getBtype().equals("1") || listBox.get(i).getBtype().equals("2")  || listBox.get(i).getBtype().equals("3") ){
					listBoxTemp.add(listBox.get(i)) ;
				}
			}
		}
		this.listBox = listBoxTemp;
	}
	
	
}
