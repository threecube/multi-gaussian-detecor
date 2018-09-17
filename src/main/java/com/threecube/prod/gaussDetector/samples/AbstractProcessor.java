/*
 * @(#)SamplesProcessor.java $version 2015-2-10
 *
 * Copyright 2007 THREECUBE Corp. All rights Reserved.
 * THREECUBE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.threecube.prod.gaussDetector.samples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import com.threecube.prod.gaussDetector.gauss.model.PointModel;
import com.threecube.prod.gaussDetector.gauss.model.SampleDataModel;

/**
 * @author Mike
 */
public abstract class AbstractProcessor implements Runnable {

	protected static List<SampleDataModel> dataList;

	protected static List<PointModel> points;

	protected CountDownLatch latch;

	protected volatile static boolean isFinished;

	/** final variable*/
	protected final static int MAX_BUFFER_LENGTH = 10000;

	/**
	 * 
	 */
	@SuppressWarnings("static-access")
	public AbstractProcessor() {
		this.dataList = new ArrayList<SampleDataModel>();
		this.points = new ArrayList<PointModel>();
		this.latch = null;
		this.isFinished = false;
	}

	/**
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public abstract void run();

	/**
	 * @return
	 */
	public static List<PointModel> getPoints() {
		return points;
	}

}
