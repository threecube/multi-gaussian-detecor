/*
 * @(#)AtributesUtils.java $version 2015-2-2
 *
 * Copyright 2007 THREECUBE Corp. All rights Reserved.
 * THREECUBE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.threecube.prod.gaussDetector.gauss.util;

import java.math.BigDecimal;

import com.threecube.prod.gaussDetector.gauss.model.NormalizePoint;
import com.threecube.prod.gaussDetector.gauss.model.PointModel;

/**
 * tool for processing atributes of object, such as PointModel
 * 
 * @author Mike
 */
public class AttributesUtils {

	/** the number of properties used*/
	public static final int PROP_NUM = 2;

	/**
	 * get value of point's attribute by the given index
	 * 
	 * @param point
	 * @param index
	 * @return
	 */
	public static long getAttrValue(PointModel point, int index) {

		long value = -1l;

		switch (index) {
			case 0:
				value = point.getTotalHits();
				break;
			case 1:
				value = point.getTotalUrlHits();
				break;
			//			case 2:
			//				value = point.getMaxIPNum();
			//				break;
			//			case 3:
			//				value = point.getMaxReqNum();
			//				break;
			default:
				value = -1l;
		}

		return value;
	}

	/**
	 * get value of point's attribute by the given index
	 * 
	 * @param point
	 * @param index
	 * @return
	 */
	public static BigDecimal getAttrValue(NormalizePoint point, int index) {

		BigDecimal value = null;

		switch (index) {
			case 0:
				value = point.getdTotalHits();
				break;
			case 1:
				value = point.getdTotalUrlHits();
				break;
			//			case 2:
			//				value = point.getdMaxIPNum();
			//				break;
			//			case 3:
			//				value = point.getdMaxReqNum();
			//				break;
			default:
				value = new BigDecimal(-1);
		}

		return value;
	}
}
