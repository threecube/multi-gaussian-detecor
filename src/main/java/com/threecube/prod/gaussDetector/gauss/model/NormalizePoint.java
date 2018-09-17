/*
 * @(#)NormalizePointModel.java $version 2015-1-30
 *
 * Copyright 2007 THREECUBE Corp. All rights Reserved.
 * THREECUBE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.threecube.prod.gaussDetector.gauss.model;

import java.math.BigDecimal;

/**
 * normalized point model
 * 
 * @author Mike
 */
public class NormalizePoint {

	private String service;

	private long gmtCreate;

	private String ip;

	private BigDecimal dTotalHits;

	private BigDecimal dTotalUrlHits;

	public NormalizePoint() {
		this.dTotalHits = new BigDecimal("0");
		this.dTotalUrlHits = new BigDecimal("0");
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public long getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(long gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public BigDecimal getdTotalHits() {
		return dTotalHits;
	}

	public void setdTotalHits(BigDecimal dTotalHits) {
		this.dTotalHits = dTotalHits;
	}

	public BigDecimal getdTotalUrlHits() {
		return dTotalUrlHits;
	}

	public void setdTotalUrlHits(BigDecimal dTotalUrlHits) {
		this.dTotalUrlHits = dTotalUrlHits;
	}

}
