/*
 * @(#)PointModel.java $version 2015-1-27
 *
 * Copyright 2007 THREECUBE Corp. All rights Reserved.
 * THREECUBE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.threecube.prod.gaussDetector.gauss.model;

/**
 * property model which is used by Multivariate Gaussian Algorithm
 * 
 * @author Mike
 */
public class PointModel {

	private String service;

	private long gmtCreate;

	private String ip;

	private long totalHits;

	private long totalUrlHits;

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public long getTotalHits() {
		return totalHits;
	}

	public void setTotalHits(long totalHits) {
		this.totalHits = totalHits;
	}

	public long getTotalUrlHits() {
		return totalUrlHits;
	}

	public void setTotalUrlHits(long totalUrlHits) {
		this.totalUrlHits = totalUrlHits;
	}

	public long getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(long gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

}
