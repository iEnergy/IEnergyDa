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
package it.proximacentauri.ienergy.da.dao;

import it.proximacentauri.ienergy.da.domain.CategoryType;
import it.proximacentauri.ienergy.da.domain.FunctionType;
import it.proximacentauri.ienergy.da.domain.Indicator;
import it.proximacentauri.ienergy.da.domain.Vote;

import java.util.Date;
import java.util.List;

public interface ConfortDao {

	/**
	 * Create a new {@link Indicator}
	 * 
	 * @param name
	 *            the name
	 * @param desc
	 *            the description
	 * @param function
	 *            the function
	 * @param type
	 *            the category type
	 * @return the new created indicator
	 */
	Indicator createIndicator(String name, String desc, FunctionType function, CategoryType type);

	/**
	 * List all indicator
	 * 
	 * @return the list of indicator
	 */
	List<Indicator> listIndicator();

	/**
	 * Find a new indicator
	 * 
	 * @param name
	 *            the indicator name
	 * @return the finded indicator
	 */
	Indicator findIndicator(String name);

	/**
	 * Delete a indicator
	 * 
	 * @param indicator
	 *            the indicator to delete
	 */
	void deleteIndicator(Indicator indicator);

	/**
	 * Save the update to indicator
	 * 
	 * @param indicator
	 *            the indicator to save
	 * @return the saved instance
	 */
	Indicator saveIndicator(Indicator indicator);

	/**
	 * Append a new vote to indicator
	 * 
	 * @param indicator
	 *            the indicator of vote
	 * @param vote
	 *            the vote to append
	 * @return the appended vote
	 */
	Vote appendVote(Indicator indicator, Vote vote);

	/**
	 * List votes from date to to date
	 * 
	 * @param indicator
	 *            the source indicator
	 * @param start
	 *            the start date
	 * @param end
	 *            the end date
	 * @return the list of votes
	 */
	List<Vote> listVote(Indicator indicator, Date start, Date end);

	/**
	 * Compute the result of comfort votation
	 * 
	 * @param indicator
	 *            the reference indicator
	 * @param start
	 *            the start date
	 * @param end
	 *            the end date
	 * @return the result of votation
	 */
	Double computeResult(Indicator indicator, Date start, Date end);

	/**
	 * Delete a vote
	 * 
	 * @param vote
	 */
	void deleteVote(Vote vote);

	/**
	 * List votes from date to to date
	 * 
	 * @param indicator
	 *            the source indicator
	 * @param start
	 *            the start date
	 * @param end
	 *            the end date
	 * @return the list of votes
	 */
	int countVote(Indicator indicator, Date start, Date end);
}
