package com.ischoolbar.programmer.entity;

public enum VoteType {
	SINGLE_ONE_TIME("��ѡֻ��Ͷһ��"),//��ѡֻ��Ͷһ��
	SINGLE_ONE_DAY("��ѡһ��ֻ��Ͷһ��"),//��ѡһ��ֻ��Ͷһ��
	MUILTE_ONE_TIME("��ѡֻ��Ͷһ��"),//��ѡֻ��Ͷһ��
	MUILTE_ONE_DAY("��ѡһ��ֻ��Ͷһ��");//��ѡһ��ֻ��Ͷһ��
	private String tips;
	private VoteType(String tips){
		this.tips = tips;
	}
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	
}
