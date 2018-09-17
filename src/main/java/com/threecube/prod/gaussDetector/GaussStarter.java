package com.threecube.prod.gaussDetector;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import com.threecube.prod.gaussDetector.gauss.model.NormalizePoint;
import com.threecube.prod.gaussDetector.gauss.model.PointModel;
import com.threecube.prod.gaussDetector.gauss.processor.GaussDetector;
import com.threecube.prod.gaussDetector.gauss.processor.GaussLearner;
import com.threecube.prod.gaussDetector.gauss.result.GaussDetectResult;
import com.threecube.prod.gaussDetector.gauss.result.GaussLearnResult;
import com.threecube.prod.gaussDetector.gauss.util.AttributesUtils;
import com.threecube.prod.gaussDetector.samples.AbstractProcessor;
import com.threecube.prod.gaussDetector.samples.DataConsumer;
import com.threecube.prod.gaussDetector.samples.DataProducer;

/**
 * Hello world!
 * 
 * @author Mike
 */
public class GaussStarter {

	private static GaussLearner gaussLearner = new GaussLearner();
	private static GaussDetector gaussDetector = new GaussDetector();

	private final static String outFile = "data/result.data";

	@SuppressWarnings({"deprecation", "static-access"})
	public static void main(String[] args) {

		//1. parse training data from files
		CountDownLatch latch = new CountDownLatch(2);
		AbstractProcessor producer = new DataProducer(latch);
		AbstractProcessor consumer = new DataConsumer(latch);

		Thread a = new Thread(producer);
		a.start();
		Thread b = new Thread(consumer);
		b.start();

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			a.stop();
			b.stop();
		}

		//2. learning using parsed training data by gaussian algorithm
		GaussLearnResult result = gaussLearner.learn(producer.getPoints());

		//get a sample to test
		PointModel point = new PointModel();
		point.setTotalHits(10);
		point.setTotalUrlHits(10);

		//3. detecting using gaussian learning result
		GaussDetectResult detectResult = gaussDetector.detect(point, result);

		recordRersult(result, detectResult);

		System.out.println("finished , please check file[" + outFile + "].");

	}

	private static void recordRersult(GaussLearnResult learnResult, GaussDetectResult detectResult) {

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(outFile);
			fileWriter.write("################ Gaussian Learning Result #################\r\n\r\n");

			fileWriter.write("Result.isSuccess: " + learnResult.isSuccess() + "\r\n\r\n");

			if (learnResult.isSuccess()) {

				// write parameter
				fileWriter.write("Suffix parameter of the furmula p(x): " + learnResult.getConsParam().doubleValue() + "\r\n\r\n");

				// write means
				NormalizePoint point = learnResult.getMeans();
				fileWriter.write("The mean of training data for all attibutes :");
				for (int i = 0; i < AttributesUtils.PROP_NUM; i++) {
					fileWriter.write(AttributesUtils.getAttrValue(point, i) + ", ");
				}
				fileWriter.write("\r\n\r\n");

				// write inverse covariance
				fileWriter.write("The inverse covariance of training data: ");
				for (int i = 0; i < AttributesUtils.PROP_NUM; i++) {
					for (int j = 0; j < AttributesUtils.PROP_NUM; j++) {
						fileWriter.write(learnResult.getInverseVariance()[i][j].doubleValue() + ", ");
					}
					fileWriter.write("\r\n\r\n");
				}

				// write determinant
				fileWriter.write("The determinant of training data: " + learnResult.getDeterminant().doubleValue() + "\r\n\r\n");

				// write probability
				fileWriter.write("The maximum probability of training data: " + learnResult.getMaxProbility().doubleValue() + "\r\n\r\n");
				fileWriter.write("The minimum probability of training data: " + learnResult.getMinProbility().doubleValue() + "\r\n\r\n\r\n");

				fileWriter.write("################ Gaussian Detecting Result #################\r\n\r\n");
				fileWriter.write("Result.isSuccess: " + detectResult.isSuccess() + "\r\n\r\n");
				if (detectResult.isSuccess()) {
					fileWriter.write("probability of the given sample: " + detectResult.getProbability().doubleValue() + "\r\n\r\n");
					fileWriter.write("if the given sample is normal: " + detectResult.isNormal() + "\r\n\r\n");
				}
			}

		} catch (IOException e) {

		} finally {
			try {
				fileWriter.close();
			} catch (IOException e) {
			}
		}
	}
}
