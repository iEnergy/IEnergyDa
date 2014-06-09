/*******************************************************************************
 * Copyright (c) 2014 Proxima Centauri SRL <info@proxima-centauri.it>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Proxima Centauri SRL <info@proxima-centauri.it> - initial API and implementation
 ******************************************************************************/
package it.proximacentauri.ienergy.da.model;

public final class ErrorBean {

	public static final ErrorResponse MEASURE_NOT_FOUND = new ErrorResponse(1, Message.MEASURE_NOT_FOUND);
	public static final ErrorResponse API_NOT_FOUND = new ErrorResponse(2, Message.API_NOT_FOUND);
	public static final ErrorResponse API_NOT_IMPLEMENTED = new ErrorResponse(3, Message.API_NOT_IMPLEMENTED);
	public static final ErrorResponse SQL_EXCEPTION = new ErrorResponse(4, Message.SQL_EXECEPTION);
	public static final ErrorResponse ELEMENT_NOT_FOUND = new ErrorResponse(5, Message.ELEMENT_NOT_FOUND);
	public static final ErrorResponse INVALID_VOTE = new ErrorResponse(6, Message.INVALID_VOTE);

	static private final class Message {
		private static final String MEASURE_NOT_FOUND = "Measure not found or invalid query parameters";
		private static final String API_NOT_FOUND = "Api not found";
		private static final String API_NOT_IMPLEMENTED = "Api not implemented";
		private static final String SQL_EXECEPTION = "Unable to perform query";
		private static final String ELEMENT_NOT_FOUND = "Element not found";
		private static final String INVALID_VOTE = "Invalid vote";

	}
}
