/*
 * @(#)GaussLearnResult.java $version 2015-1-30
 *
 * Copyright 2007 THREECUBE Corp. All rights Reserved.
 * THREECUBE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.threecube.prod.gaussDetector.gauss.result;

import java.math.BigDecimal;

import com.threecube.prod.gaussDetector.gauss.model.NormalizePoint;

/**
 * the result of gaussial algorithm
 * this result will be used to calculate probability that point is normal 
 * calcaultion method is :
 * 
 * p(point) = consParam * exp((point-means)T)*inverseVariance*(point-means)
 * 
 * @author Mike
 */
public class GaussLearnResult extends BaseResult {

	/** mean of each property in Multivariate Gaussian*/
	private NormalizePoint means;

	/** the inverse value of covariance*/
	private BigDecimal[][] inverseVariance;

	/** the determinant of covariance*/
	private BigDecimal determinant;

	/** constant parameter*/
	private BigDecimal consParam;

	/** the maximum value of probility for normal data*/
	private BigDecimal maxProbility;

	/** the minimum value of probility for normal data*/
	private BigDecimal minProbility;

	public NormalizePoint getMeans() {
		return means;
	}

	public void setMeans(NormalizePoint means) {
		this.means = means;
	}

	public BigDecimal[][] getInverseVariance() {
		return inverseVariance;
	}

	public void setInverseVariance(BigDecimal[][] inverseVariance) {
		this.inverseVariance = inverseVariance;
	}

	public BigDecimal getDeterminant() {
		return determinant;
	}

	public void setDeterminant(BigDecimal determinant) {
		this.determinant = determinant;
	}

	public BigDecimal getConsParam() {
		return consParam;
	}

	public void setConsParam(BigDecimal consParam) {
		this.consParam = consParam;
	}

	public BigDecimal getMaxProbility() {
		return maxProbility;
	}

	public void setMaxProbility(BigDecimal maxProbility) {
		this.maxProbility = maxProbility;
	}

	public BigDecimal getMinProbility() {
		return minProbility;
	}

	public void setMinProbility(BigDecimal minProbility) {
		this.minProbility = minProbility;
	}

}
