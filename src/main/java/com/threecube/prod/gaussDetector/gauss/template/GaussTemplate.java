/*
 * @(#)GaussTemplate.java $version 2015-1-30
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
import com.threecube.prod.gaussDetector.gauss.util.AttributesUtils;

/**
 * The template of learning and detecting by Multivariate Gaussian algorithm
 * 
 * @author Mike
 */
public class GaussTemplate {

	/**
	 * learning by  Multivariate Gaussian algorithm
	 * 
	 * @param points
	 * @param means
	 * @param callback
	 * @return
	 */
	public GaussLearnResult learn(List<PointModel> points, GaussLearnCallback callback) {

		GaussLearnResult result = new GaussLearnResult();
		NormalizePoint means = new NormalizePoint();
		BigDecimal[][] covariances = new BigDecimal[AttributesUtils.PROP_NUM][AttributesUtils.PROP_NUM];
		BigDecimal consParam = null;
		BigDecimal determinant = null;

		try {

			//step1: calculate means of properties,
			callback.calcMeans(points, means);

			//step2: calculate convariance of training data
			callback.calcCovariance(points, means, covariances);
			System.out.println("End to calculate the covariances.");

			//step3: calculate the determinant of convariance
			determinant = callback.calcDeterminant(covariances);

			if (determinant.compareTo(new BigDecimal(0)) < 1) {
				/*if all properties are completely correlation, the determinant will be 0; 
				 * in this case, new properties should be choosed.
				 */
				throw new Exception("ERROR, the determinant of covatiance is zero, please choose new properties.");
			}

			//step3: calculate the inverse of convariance
			callback.inverseCovariance(covariances, determinant);
			System.out.println("End to calculate the inverse covariances.");

			//step4: calculate parameter 
			consParam = callback.calcConsParam(determinant);

			//generate learning result
			result.setConsParam(consParam);
			result.setDeterminant(determinant);
			result.setInverseVariance(covariances);
			result.setMeans(means);
			result.setSuccess(true);

			//step5: calculate probability
			callback.calcProbility(points, result);
			System.out.println("End to find the maximum and minimum probability value.");

		} catch (Exception e) {
			result.setSuccess(false);
			result.setResultMsg(e.toString());
			result.setConsParam(null);
			result.setDeterminant(null);
			result.setInverseVariance(null);
			result.setMaxProbility(null);
			result.setMeans(null);
			result.setMinProbility(null);
			return result;
		}

		return result;
	}

}
