/*
 * @(#)GaussDetectResult.java $version 2015-1-30
 *
 * Copyright 2007 THREECUBE Corp. All rights Reserved.
 * THREECUBE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.threecube.prod.gaussDetector.gauss.result;

import java.math.BigDecimal;

/**
 * detect result by gauss
 * 
 * @author Mike
 */
public class GaussDetectResult extends BaseResult {

	/** if normal, true: normal, false: abnormal*/
	private boolean isNormal;

	/** the probability for the point's existing */
	private BigDecimal probability;

	public GaussDetectResult() {
		super();
		this.isNormal = false;
		this.probability = new BigDecimal("0");
	}

	public boolean isNormal() {
		return isNormal;
	}

	public void setNormal(boolean isNormal) {
		this.isNormal = isNormal;
	}

	public BigDecimal getProbability() {
		return probability;
	}

	public void setProbability(BigDecimal probability) {
		this.probability = probability;
	}

}
