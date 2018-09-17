/*
 * @(#)GaussCallBack.java $version 2015-1-30
 *
 * Copyright 2007 THREECUBE Corp. All rights Reserved.
 * THREECUBE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.threecube.prod.gaussDetector.gauss.template;

import java.math.BigDecimal;
import java.util.List;

import com.threecube.prod.gaussDetector.gauss.model.NormalizePoint;
import com.threecube.prod.gaussDetector.gauss.model.PointModel;
import com.threecube.prod.gaussDetector.gauss.result.GaussLearnResult;

/**
 * @author Mike
 */
public interface GaussLearnCallback {

	/**
	 * calculate mean for each property by training data
	 * 
	 * @param points, input, training data
	 * @param means, output, means of properties
	 * @throws Exception 
	 */
	public void calcMeans(final List<PointModel> points, NormalizePoint means) throws Exception;

	/**
	 * calculate convariance base on means and training data.
	 * <p>for example: point{a,b,c},means{A,B,C}, the convariance is </p>
	 * <li>firstly , get vector = {log(a)-A, log(b)-B, log(c)-C}</li>
	 * <li>then, covariances[0] = {vector[0]*vector[0], vector[0]*vector[1], vector[2]*vector[2]}</li>
	 * <li>covariances[i] = ...</li>
	 * 
	 * @param points, input, training data
	 * @param means, input , mean of each property
	 * @param covariances, output
	 */
	public void calcCovariance(final List<PointModel> points, final NormalizePoint means, BigDecimal[][] covariances) throws Exception;

	/**
	 * calculate the inverse of convariance
	 * inverse = Adjoint matrix(covariances) / determinant
	 * 
	 * @param covariances
	 * @param determinant, the determinant of covariances
	 * @throws Exception 
	 */
	public void inverseCovariance(final BigDecimal[][] covariances, BigDecimal determinant) throws Exception;

	/**
	 * calculate parameter which will be used to calculate probability, the parameter is the suffix of function of p(x)
	 * <p>value = 1 / (MATH.pow(2PI,n/2) * MATH.sqrt(determinant))</p>
	 * 
	 * @param covariances
	 * @param determinant, the determinant of covariances
	 * @return
	 */
	public BigDecimal calcConsParam(BigDecimal determinant);

	/**
	 * calculate the determinant of convariance : |convariance|
	 * <p>for example, covariances[3][3], the Determinant is :</p>
	 * <li>a00*A00 - a01*A01 + a02*A02, A0i is the cofactor of a0i in covariances</li>
	 * @param covariances
	 * @return
	 * @throws Exception 
	 */
	public BigDecimal calcDeterminant(BigDecimal[][] covariances) throws Exception;

	/**
	 * calculate the maximum and minimum values of propbility for all training data
	 * 
	 * @param points
	 * @param l
	 * @throws Exception
	 */
	public void calcProbility(List<PointModel> points, GaussLearnResult learnParam) throws Exception;

}
