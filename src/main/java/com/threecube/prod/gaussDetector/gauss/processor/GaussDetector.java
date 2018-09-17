/*
 * @(#)GaussDetector.java $version 2015-1-30
 *
 * Copyright 2007 THREECUBE Corp. All rights Reserved.
 * THREECUBE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.threecube.prod.gaussDetector.gauss.processor;

import java.math.BigDecimal;

import com.threecube.prod.gaussDetector.gauss.model.NormalizePoint;
import com.threecube.prod.gaussDetector.gauss.model.PointModel;
import com.threecube.prod.gaussDetector.gauss.result.GaussDetectResult;
import com.threecube.prod.gaussDetector.gauss.result.GaussLearnResult;
import com.threecube.prod.gaussDetector.gauss.util.AttributesUtils;
import com.threecube.prod.gaussDetector.gauss.util.MathUtils;

/**
 * detect if point is abnormal
 * 
 * @author Mike
 */
public class GaussDetector {

	/**
	 * detect by calculating probability of normal type for point, if probability < threshold,
	 * then point is abnormal
	 * 
	 * @param point, object to be detected
	 * @param gauss, object will be detected base on gauss, which is the learning result 
	 * @return
	 */
	public GaussDetectResult detect(PointModel point, GaussLearnResult gauss) {

		GaussDetectResult result = new GaussDetectResult();
		if (!gauss.isSuccess()) {
			result.setResultMsg("Gauss learning error, it can't to detect");
			return result;
		}

		//1. calculate the converter of (point-means)
		BigDecimal probability = null;

		try {
			BigDecimal[] vector = calcNumerator1(point, gauss.getMeans());

			//2. calculate the value by (converter*inverseVariance)
			BigDecimal[] param = calcNumerator2(vector, gauss.getInverseVariance());

			//3. calculate the value by param*vector
			BigDecimal value = calcNumerator3(vector, param);

			//4. calculate the probability of point's distribution
			double subValue = -value.doubleValue() / 2;
			BigDecimal bigDecimal = BigDecimal.valueOf(Math.pow(Math.E, subValue));
			probability = MathUtils.mult(gauss.getConsParam(), bigDecimal);

		} catch (Exception e) {
			result.setResultMsg(e.toString());
			return result;
		}

		result.setSuccess(true);

		if (gauss.getMinProbility() != null) {
			result.setNormal(probability.compareTo(gauss.getMinProbility()) < 0 ? false : true);
		} else {
			result.setNormal(true);
		}
		result.setProbability(probability);

		return result;
	}

	/**
	 * 
	 * @param point
	 * @param means
	 * @return
	 * @throws Exception 
	 */
	private BigDecimal[] calcNumerator1(PointModel point, NormalizePoint means) throws Exception {

		if (point == null || means == null) {
			throw new Exception("ERROR: input of calcNumerator1 in GaussDetector is null");
		}

		BigDecimal[] result = new BigDecimal[AttributesUtils.PROP_NUM];

		for (int i = 0; i < AttributesUtils.PROP_NUM; i++) {

			long attrValue = AttributesUtils.getAttrValue(point, i);
			//get the lg of attrValue, then format into Bigdecimal
			BigDecimal bAttrValue = BigDecimal.valueOf(attrValue);

			BigDecimal meanValue = AttributesUtils.getAttrValue(means, i);
			result[i] = MathUtils.sub(bAttrValue, meanValue);
		}

		return result;
	}

	/**
	 * 
	 * 
	 * @param point
	 * @param means
	 * @param inverseVariance
	 * @return
	 * @throws Exception 
	 */
	private BigDecimal[] calcNumerator2(BigDecimal[] vector, BigDecimal[][] inverseVariance) throws Exception {
		if (vector == null || vector.length <= 0 || inverseVariance == null || inverseVariance.length <= 0) {
			throw new Exception("ERROR: input of calcNumerator2 in GaussDetector is null");
		}

		BigDecimal[] result = new BigDecimal[AttributesUtils.PROP_NUM];

		//loop for columns of inverseVariance
		for (int j = 0; j < AttributesUtils.PROP_NUM; j++) {
			BigDecimal value = new BigDecimal(0);

			// loop for each element in vector
			for (int i = 0; i < AttributesUtils.PROP_NUM; i++) {
				BigDecimal temp = MathUtils.mult(vector[i], inverseVariance[i][j]);
				value = MathUtils.add(value, temp);
			}
			result[j] = value;
		}
		return result;
	}

	/**
	 * @param vector
	 * @param param
	 * @return
	 */
	private BigDecimal calcNumerator3(BigDecimal[] vector, BigDecimal[] param) {

		BigDecimal result = new BigDecimal(0);
		for (int i = 0; i < AttributesUtils.PROP_NUM; i++) {
			BigDecimal temp = MathUtils.mult(vector[i], param[i]);
			result = MathUtils.add(result, temp);
		}

		return result;
	}
}
