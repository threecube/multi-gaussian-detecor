/*
 * @(#)MathUtil.java $version 2015-2-3
 *
 * Copyright 2007 THREECUBE Corp. All rights Reserved.
 * THREECUBE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.threecube.prod.gaussDetector.gauss.util;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Math util, it is used for improving the accuracy of operations on number whose type is double
 * 
 * @author Mike
 */
public class MathUtils {

	/** accuracy for dividing of bigDecimal, the number is bigger, the result is more accurate*/
	private final static int SCALE = 50;

	/**
	 * add function: param1 + param2
	 * 
	 * @param param1
	 * @param param2
	 * @return
	 */
	public static BigDecimal add(BigDecimal param1, BigDecimal param2) {
		if (param1 == null) {
			param1 = new BigDecimal(0);
		}
		return param1.add(param2, MathContext.UNLIMITED);
	}

	/**
	 * subtract function: param1 - param2
	 * 
	 * @param param1
	 * @param param2
	 * @return
	 */
	public static BigDecimal sub(BigDecimal param1, BigDecimal param2) {

		if (param1 == null) {
			param1 = new BigDecimal(0);
		}
		return param1.subtract(param2, MathContext.UNLIMITED);
	}

	/**
	 * multiple function: param1*param2
	 * 
	 * @param param1
	 * @param param2
	 * @return
	 */
	public static BigDecimal mult(BigDecimal param1, BigDecimal param2) {
		if (param1 == null) {
			param1 = new BigDecimal(1);
		}

		return param1.multiply(param2, MathContext.UNLIMITED);
	}

	/**
	 * divide function: param1/param2
	 * 
	 * @param param1
	 * @param param2
	 * @return
	 */
	public static BigDecimal div(BigDecimal param1, BigDecimal param2) {

		return param1.divide(param2, SCALE, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * double to bigdecimal
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal doub2BigD(double value) {
		return new BigDecimal(Double.toString(value));
	}
}
