/*
 * @(#)GaussLearnProcessor.java $version 2015-2-2
 *
 * Copyright 2007 THREECUBE Corp. All rights Reserved.
 * THREECUBE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.threecube.prod.gaussDetector.gauss.processor;

import java.math.BigDecimal;

import com.threecube.prod.gaussDetector.gauss.model.NormalizePoint;
import com.threecube.prod.gaussDetector.gauss.model.PointModel;
import com.threecube.prod.gaussDetector.gauss.util.AttributesUtils;
import com.threecube.prod.gaussDetector.gauss.util.MathUtils;

/**
 * processor of gauss learner
 * 
 * @author Mike
 */
public class GaussLearnProcessor {

	/**
	 * calculate the convariance for point by using means, calculation steps:
	 * <p>for example: point:[a,b,c], means:[A,B,C], then covariances are:</p>
	 * <li>covariances[0] = {(log(a)-A)*(log(a)-A), (log(a)-A)*(long(b)-B), (log(a)-A)*(log(c)-C),...}</li>
	 * <li>covariances[1] = {(log(b)-B)*(log(a)-A), (log(b)-B)*(long(b)-B), (log(b)-B)*(log(c)-C),...}</li>
	 * <li>covariances[i] = {...}</li>
	 * @param point
	 * @param means
	 * @param covariances
	 * @throws Exception 
	 */
	public void calcConvForPoint(PointModel point, NormalizePoint means, BigDecimal[][] covariances) throws Exception {

		if (point == null || means == null || covariances == null) {
			throw new Exception("ERROR : input of calcConvForPoint in GaussLearnProcessor is null.");
		}

		//calculate for covariances[i]
		for (int i = 0; i < AttributesUtils.PROP_NUM; i++) {

			double curValue = AttributesUtils.getAttrValue(point, i);
			BigDecimal currentAttr = MathUtils.sub(MathUtils.doub2BigD(curValue), AttributesUtils.getAttrValue(means, i));

			//step2
			for (int j = 0; j < AttributesUtils.PROP_NUM; j++) {

				double value = AttributesUtils.getAttrValue(point, j);
				BigDecimal attr = MathUtils.sub(MathUtils.doub2BigD(value), AttributesUtils.getAttrValue(means, j));

				BigDecimal multi = MathUtils.mult(currentAttr, attr);
				covariances[i][j] = MathUtils.add(covariances[i][j], multi);
			}
		}
	}

	/**
	 * calculate the cofactor of covariances for the element covariances[rowIndex][colmIndex]
	 * <p>for example, covariances[3][3], rowIndex=1, colmIndex=1</p>
	 * <p>then cofactor is : cofactor{covariances[0][0], covariances[0][2],covariances[2][0],covariances[2][2]}</p>
	 * 
	 * @param covariances
	 * @param rowIndex
	 * @param colmIndex
	 * @param cofactor
	 * @throws Exception 
	 */
	public BigDecimal[][] calcCofactor(BigDecimal[][] covariances, int rowIndex, int colmIndex) throws Exception {

		if (covariances == null || covariances.length <= 0 || rowIndex == -1 || colmIndex == -1) {
			throw new Exception("ERROR : input for calcCofactor in GaussLearnProcessor is null.");
		}

		BigDecimal[][] cofactor = new BigDecimal[covariances.length - 1][covariances[0].length - 1];

		for (int i = 0; i < covariances.length; i++) {

			// for elements whose row's index is smaller than rowIndex
			if (i < rowIndex) {
				for (int j = 0; j < covariances[i].length; j++) {

					if (j < colmIndex) {
						// for elements whose column's index is smaller than colmIndex
						cofactor[i][j] = covariances[i][j];
					} else if (j > colmIndex) {
						// for elements whose column's index is bigger than colmIndex
						cofactor[i][j - 1] = covariances[i][j];
					}
				}
			} else if (i > rowIndex) {
				// for elements whose row's index is bigger than rowIndex
				for (int j = 0; j < covariances[i].length; j++) {
					if (j < colmIndex) {
						// for elements whose column's index is smaller than colmIndex
						cofactor[i - 1][j] = covariances[i][j];
					} else if (j > colmIndex) {
						// for elements whose column's index is bigger than colmIndex
						cofactor[i - 1][j - 1] = covariances[i][j];
					}
				}

			}
		}

		return cofactor;
	}

	/**
	 * calculate the determinant of covariances : |covariances|
	 * 
	 * @param covariances
	 * @return
	 * @throws Exception 
	 */
	public BigDecimal calcDeterminant(BigDecimal[][] covariances) throws Exception {

		if (covariances == null || covariances.length <= 0) {
			throw new Exception("ERROR : input of determinant in GaussLearnProcessor is null.");
		}

		if (covariances.length == 1) {
			return covariances[0][0];
		}

		if (covariances.length == 2) {
			BigDecimal multi1 = MathUtils.mult(covariances[0][0], covariances[1][1]);
			BigDecimal multi2 = MathUtils.mult(covariances[0][1], covariances[1][0]);
			return MathUtils.sub(multi1, multi2);
		}

		BigDecimal totalValue = MathUtils.doub2BigD(0d);

		//based on the first row elements, calculate the determinant of covariances
		for (int i = 0; i < covariances.length; i++) {

			if (i % 2 == 0) {
				//loop to calculate the determinant of cofactor of covariances for element covariances[0][i] 
				BigDecimal multi = MathUtils.mult(covariances[0][i], calcDeterminant(calcCofactor(covariances, 0, i)));
				totalValue = MathUtils.add(totalValue, multi);
			} else {
				BigDecimal multi = MathUtils.mult(covariances[0][i], calcDeterminant(calcCofactor(covariances, 0, i)));
				totalValue = MathUtils.sub(totalValue, multi);
			}
		}

		return totalValue;
	}
}
