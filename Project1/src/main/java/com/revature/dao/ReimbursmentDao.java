package com.revature.dao;

import java.util.List;

import com.revature.beans.Reimbursement;
import com.revature.beans.User;

/**
 * The data access object interface for CRUD operations on Reimbursments.
 * This allows us to define a contract which can be implemented in different ways
 * using different methods of data storage and retrieval.
 */

public interface ReimbursmentDao {
	
	/**
	 * Inserts a new reimbursement into the persistence layer
	 * @param reimb the reimbursement to insert
	 * @return the newly added reimbursement object
	 */
	public Reimbursement addReimbursment(Reimbursement reimb);
	
	/**
	 * Retrieves a reimbursement by id
	 * @param reimbId the id to search by
	 * @return the reimbursement object
	 */
	public Reimbursement getReimbursement(Integer reimbId);
	
	
	/**
	 * Retrieves all reimbursements in the persistence layer
	 * @return a list of all reimbursements
	 */
	public List<Reimbursement> getAllReimbursements();
	
	/**
	 * Retrieves reimbursements belonging to a specific user 
	 * in the persistence layer
	 * @return a list of reimbursements belonging to user
	 */
	public List<Reimbursement> getReimbursementsByEmployee(User user);
	
	
	/**
	 * Returns a list of reimbursements that the Manager can approve
	 * Does not include reimbursment requests that the manager has requested
	 * @return a list of reimbursements from all users yet to be approved,
	 * 			except 
	 */
	public List<Reimbursement> getReimbursementsToApprove();
	
	
	
	/**
	 * Updates a specific reimbursement
	 * @param reimb the reimb to update
	 * @return the newly updated reimbursement object
	 * 
	 */
	public Reimbursement updateReimbursement(Reimbursement reimb);
	
	
	/**
	 * Deletes a reimbursement from the persistence layer
	 * @param reimb the reimbursement to remove
	 * @return true if successful; false if not
	 */
	public boolean removeReimbursement(Reimbursement reimb);
	

}
