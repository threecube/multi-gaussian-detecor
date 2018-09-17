/*
 * @(#)DataConsumer.java $version 2015-2-10
 *
 * Copyright 2007 THREECUBE Corp. All rights Reserved.
 * THREECUBE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.threecube.prod.gaussDetector.samples;

import java.util.concurrent.CountDownLatch;

import com.threecube.prod.gaussDetector.gauss.model.PointModel;
import com.threecube.prod.gaussDetector.gauss.model.SampleDataModel;

/**
 * training data consumer for doing some statistic to get properties of algorithm
 * 
 * @author Mike
 */
public class DataConsumer extends AbstractProcessor {

	private static int currentReadNum = 0;

	public DataConsumer(CountDownLatch latch) {
		this.latch = latch;
	}

	/**
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@SuppressWarnings("static-access")
	@Override
	public void run() {

		while (true) {
			while (currentReadNum < dataList.size() && dataList.size() > 0) {
				process(dataList.get(currentReadNum));
				currentReadNum++;
			}

			if (dataList.size() > MAX_BUFFER_LENGTH && currentReadNum >= dataList.size()) {
				currentReadNum = 0;
				synchronized (this.dataList) {
					dataList.clear();
					this.dataList.notify();
				}
			}

			if (this.isFinished) {
				System.out.println("finished to process data:" + this.isFinished);
				break;
			}

		}

		this.latch.countDown();
	}

	/**
	 * @param sampleDataModel
	 */
	private boolean process(SampleDataModel sampleDataModel) {

		if (sampleDataModel == null) {
			return false;
		}
		//		for (PointModel point : this.points) {
		//			if (point.getGmtCreate() == sampleDataModel.getTimestamp() && sampleDataModel.getService().equals(point.getService())) {
		//				point.setIpNum(point.getIpNum() + 1);
		//				point.setTotalHits(point.getTotalHits() + sampleDataModel.getHitsRatio());
		//				point.setTotalUrlHits(point.getTotalUrlHits() + sampleDataModel.getUrlHash());
		//				return true;
		//			}
		//		}

		PointModel point = new PointModel();
		point.setService(sampleDataModel.getService());
		point.setIp(sampleDataModel.getIp());
		point.setGmtCreate(sampleDataModel.getTimestamp());
		point.setTotalHits(sampleDataModel.getHits());
		point.setTotalUrlHits(sampleDataModel.getTopUrlHits());
		points.add(point);
		return true;
	}
}
