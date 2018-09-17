/*
 * @(#)Producer.java $version 2015-2-10
 *
 * Copyright 2007 THREECUBE Corp. All rights Reserved.
 * THREECUBE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.threecube.prod.gaussDetector.samples;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.threecube.prod.gaussDetector.gauss.model.SampleDataModel;

/**
 * training data producer which read trainig data from file
 * 
 * @author Mike
 */
public class DataProducer extends AbstractProcessor {

	private final static String FILE_PATH = "data/pc_visitor.data";

	private static Gson gson = new Gson();

	public DataProducer(CountDownLatch latch) {
		this.latch = latch;
	}

	/**
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@SuppressWarnings("static-access")
	@Override
	public void run() {

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(FILE_PATH));

			String line = null;
			synchronized (this.dataList) {
				while ((line = reader.readLine()) != null) {
					while (dataList.size() > MAX_BUFFER_LENGTH) {
						this.dataList.wait();
					}
					dataList.add(dataList.size(), gson.fromJson(line, SampleDataModel.class));
				}
			}

		} catch (JsonSyntaxException e) {
			System.out.println("ERROR-1, " + e);
		} catch (IOException e) {
			System.out.println("ERROR-2, " + e);
		} catch (InterruptedException e) {
			System.out.println("ERROR-3, " + e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.out.println("ERROR when closing bufferedReader, " + e);
				}
			}
			this.isFinished = true;
			this.latch.countDown();
		}

	}

}
