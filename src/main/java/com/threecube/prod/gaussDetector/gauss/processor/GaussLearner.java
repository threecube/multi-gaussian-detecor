/*
 * @(#)GaussLearnProcessor.java $version 2015-1-30
 *
 * Copyright 2007 THREECUBE Corp. All rights Reserved.
 * THREECUBE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.threecube.prod.gaussDetector.gauss.processor;

import java.math.BigDecimal;
import java.util.List;

import com.threecube.prod.gaussDetector.gauss.model.NormalizePoint;
import com.threecube.prod.gaussDetector.gauss.model.PointModel;
import com.threecube.prod.gaussDetector.gauss.result.GaussDetectResult;
import com.threecube.prod.gaussDetector.gauss.result.GaussLearnResult;
import com.threecube.prod.gaussDetector.gauss.template.GaussLearnCallback;
import com.threecube.prod.gaussDetector.gauss.template.GaussTemplate;
import com.threecube.prod.gaussDetector.gauss.util.AttributesUtils;
import com.threecube.prod.gaussDetector.gauss.util.MathUtils;

/**
 * learning processor by Multivariate Gaussian Algorithm
 * 
 * @author Mike
 */
public class GaussLearner {

	private GaussTemplate gaussTemplate = new GaussTemplate();

	private GaussLearnProcessor gaussLearnProcessor = new GaussLearnProcessor();

	private GaussDetector gaussDetector = new GaussDetector();

	/**
	 * @param points
	 * @return
	 */
	public GaussLearnResult learn(List<PointModel> points) {

		return gaussTemplate.learn(points, new GaussLearnCallback() {

			/**
			 * @param points
			 * @param means
			 * @throws Exception
			 */
			@Override
			public void calcMeans(List<PointModel> points, NormalizePoint means) throws Exception {

				if (points == null || points.isEmpty() || means == null) {
					throw new Exception("calcMeans error in GaussLearner, input is null");
				}

				long dataNum = 0l;
				double totalUrlHits = 0l;
				double totalHits = 0l;
				for (PointModel point : points) {
					dataNum++;
					//using log is changing training data into Gauss distribution 
					//ipNum += point.getIpNum();
					totalUrlHits += point.getTotalUrlHits();
					totalHits += point.getTotalHits();
				}

				BigDecimal bDataNum = MathUtils.doub2BigD(dataNum);

				//lg(property)/num -> bigdecimal
				//means.setdIpNum(MathUtils.div(MathUtils.doub2BigD(ipNum), bDataNum));
				means.setdTotalHits(MathUtils.div(MathUtils.doub2BigD(totalHits), bDataNum));
				means.setdTotalUrlHits(MathUtils.div(MathUtils.doub2BigD(totalUrlHits), bDataNum));
			}

			/**
			 * @param points
			 * @param means
			 * @param covariances
			 * @throws Exception 
			 */
			public void calcCovariance(List<PointModel> points, NormalizePoint means, BigDecimal[][] covariances) throws Exception {

				long totalDataNum = 0l;
				BigDecimal[][] totalConv = new BigDecimal[AttributesUtils.PROP_NUM][AttributesUtils.PROP_NUM];

				//step1: calculate total value of covariances
				for (PointModel point : points) {
					gaussLearnProcessor.calcConvForPoint(point, means, totalConv);
					totalDataNum++;
				}

				//step2: calculate covariances
				BigDecimal dataNum = MathUtils.doub2BigD(totalDataNum);
				for (int i = 0; i < AttributesUtils.PROP_NUM; i++) {
					for (int j = 0; j < AttributesUtils.PROP_NUM; j++) {
						covariances[i][j] = MathUtils.div(totalConv[i][j], dataNum);
						System.out.println("convariance of " + i + "," + j + ": " + covariances[i][j].doubleValue());
					}
				}
			}

			/**
			 * @param covariances
			 * @throws Exception 
			 */
			@Override
			public void inverseCovariance(final BigDecimal[][] covariances, BigDecimal determinant) throws Exception {
				if (covariances == null || covariances.length <= 0 || determinant == null) {
					throw new Exception("ERROR: input of inverseCovariance in GaussLearner is null.");
				}

				if (covariances.length == 1) {
					covariances[0][0] = MathUtils.div(new BigDecimal(1), determinant);
					return;
				}

				BigDecimal[][] cofactor = new BigDecimal[covariances.length][covariances[0].length];

				//step1 : calculate the cofactor for each elements in covariances
				for (int i = 0; i < covariances.length; i++) {

					for (int j = 0; j < covariances[0].length; j++) {
						//step 1.1: calculate the cofactor for the element covariances[i][j]
						BigDecimal[][] subCofactor = gaussLearnProcessor.calcCofactor(covariances, i, j);

						//step 1.2: calculate the determinant of subCofactor
						BigDecimal subDeterm = gaussLearnProcessor.calcDeterminant(subCofactor);

						//step 1.3: calculate the element of cofactor
						cofactor[j][i] = subDeterm.multiply(MathUtils.doub2BigD(Math.pow(-1, i + j)));
					}
				}

				//step2: calculate the inverse of covariances by cofactor/determinant
				for (int i = 0; i < covariances.length; i++) {
					for (int j = 0; j < covariances[0].length; j++) {
						covariances[i][j] = MathUtils.div(cofactor[i][j], determinant);
					}
				}
			}

			/**
			 * @param covariances
			 * @return
			 */
			@Override
			public BigDecimal calcConsParam(BigDecimal determinant) {

				BigDecimal param = MathUtils.doub2BigD(2 * Math.PI).pow(AttributesUtils.PROP_NUM);

				param = MathUtils.mult(param, determinant);

				double value = Math.pow(param.doubleValue(), 0.5);

				return MathUtils.div(MathUtils.doub2BigD(1), MathUtils.doub2BigD(value));
			}

			/**
			 * @param covariances
			 * @return
			 * @throws Exception 
			 */
			@Override
			public BigDecimal calcDeterminant(BigDecimal[][] covariances) throws Exception {
				return gaussLearnProcessor.calcDeterminant(covariances);
			}

			/**
			 * @param points
			 * @param consParam
			 * @param covariances
			 * @param determinant
			 * @param maxProbability
			 * @param minProbability
			 * @throws Exception 
			 * @see GaussLearnCallback#calcProbility(java.util.List, java.math.BigDecimal, java.math.BigDecimal[][], java.math.BigDecimal, java.math.BigDecimal, java.math.BigDecimal)
			 */
			@Override
			public void calcProbility(List<PointModel> points, GaussLearnResult learnParam) throws Exception {
				if (points == null || points.size() <= 0 || learnParam == null) {
					throw new Exception("ERROR: input of calcProbility in GaussLeaner is null.");
				}

				BigDecimal maxProbability = new BigDecimal(0);
				BigDecimal minProbability = new BigDecimal(1);
				for (PointModel point : points) {
					GaussDetectResult result = null;

					result = gaussDetector.detect(point, learnParam);

					if (result == null) {
						throw new Exception("ERROR: result of detector in GaussLeaner is null.");
					}
					if (maxProbability.compareTo(result.getProbability()) < 0) {
						maxProbability = result.getProbability();
					}
					if (minProbability.compareTo(result.getProbability()) > 0) {
						minProbability = result.getProbability();
					}
				}

				learnParam.setMaxProbility(maxProbability);
				learnParam.setMinProbility(minProbability);
			}

		});
	}
}
