/*
 * @(#)SampleDataModel.java $version 2015-2-12
 *
 * Copyright 2007 THREECUBE Corp. All rights Reserved.
 * THREECUBE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.threecube.prod.gaussDetector.gauss.model;

/**
 * @author Mike
 */
public class SampleDataModel {

	/** property : the time of request, microsecond*/
	private long timestamp;

	/** service name*/
	private String service;

	private String ip;

	private long hits;

	private long topUrlHits;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

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

	public long getHits() {
		return hits;
	}

	public void setHits(long hits) {
		this.hits = hits;
	}

	public long getTopUrlHits() {
		return topUrlHits;
	}

	public void setTopUrlHits(long topUrlHits) {
		this.topUrlHits = topUrlHits;
	}

}
