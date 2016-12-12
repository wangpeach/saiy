package com.wly.service;

import org.apache.ibatis.session.RowBounds;

import com.wly.utils.Utils;

public class BaseService {
	
	protected RowBounds rowBounds;
	
	protected int curInx = 1, pageSize = 20; 
	
	public BaseService() {
		// TODO Auto-generated constructor stub
		this.setRowBounds(curInx);
	}
	
	public <T> T setRowBounds(Object curInx) {
		return this.setRowBounds(curInx, pageSize);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T setRowBounds(Object curInx, int pageSize) {
		if(Utils.isNotNullOrEmpty(curInx)) {
			this.curInx = Integer.parseInt(curInx.toString());
		}
		this.pageSize = pageSize;
		rowBounds = new RowBounds((this.curInx - 1) * pageSize, pageSize);
		return (T) this;
	}
	
}
