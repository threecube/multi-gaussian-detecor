/*
 * @(#)BaseResult.java $version 2015-1-30
 *
 * Copyright 2007 THREECUBE Corp. All rights Reserved.
 * THREECUBE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.threecube.prod.gaussDetector.gauss.result;

/**
 * base result
 * 
 * @author Mike
 */
public class BaseResult {

	/** if success*/
	private boolean isSuccess;

	/** result message*/
	private String resultMsg;

	public BaseResult() {
		this.isSuccess = false;
		this.resultMsg = "";
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

}
