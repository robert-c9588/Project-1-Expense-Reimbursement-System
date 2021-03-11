package com.revature.beans;

import java.time.LocalDateTime;

public class Reimbursement {
	
	public static enum ReimbType {
		//Employees must select the type of reimbursement as: LODGING, TRAVEL, FOOD, or OTHER. 
		/*
		 * When speaking to SQL server will convert these to Integers
		 * 1 - LODGING
		 * 2 - TRAVEL
		 * 3 - FOOD
		 * 4 - OTHER
		 */
		
		LODGING, TRAVEL, FOOD, OTHER
	}
	
	public static enum ReimbStatus {
		//Reimbursement statuses are: PENDING, APPROVED, or DENIED
		/*
		 * When speaking to SQL server will convert these to Integers
		 * 0 - DENIED
		 * 1 - APPROVED
		 * 2 - PENDING
		 * 
		 */
		PENDING, APPROVED, DENIED

	}
	private Integer id;
	private Double amount;
	private LocalDateTime submittedTs;
	private LocalDateTime resolvedTs;
	private String description;
	private User author;
	private User resolver;
	private ReimbStatus statusid;
	private ReimbType type;

	public Reimbursement() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDateTime getSubmittedTs() {
		return submittedTs;
	}

	public void setSubmittedTs(LocalDateTime submittedTs) {
		this.submittedTs = submittedTs;
	}

	public LocalDateTime getResolvedTs() {
		return resolvedTs;
	}

	public void setResolvedTs(LocalDateTime resolvedTs) {
		this.resolvedTs = resolvedTs;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public User getResolver() {
		return resolver;
	}

	public void setResolver(User resolver) {
		this.resolver = resolver;
	}

	public ReimbStatus getStatusid() {
		return statusid;
	}

	public void setStatusid(ReimbStatus statusid) {
		this.statusid = statusid;
	}

	public ReimbType getType() {
		return type;
	}

	public void setType(ReimbType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((resolvedTs == null) ? 0 : resolvedTs.hashCode());
		result = prime * result + ((resolver == null) ? 0 : resolver.hashCode());
		result = prime * result + ((statusid == null) ? 0 : statusid.hashCode());
		result = prime * result + ((submittedTs == null) ? 0 : submittedTs.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reimbursement other = (Reimbursement) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (resolvedTs == null) {
			if (other.resolvedTs != null)
				return false;
		} else if (!resolvedTs.equals(other.resolvedTs))
			return false;
		if (resolver == null) {
			if (other.resolver != null)
				return false;
		} else if (!resolver.equals(other.resolver))
			return false;
		if (statusid != other.statusid)
			return false;
		if (submittedTs == null) {
			if (other.submittedTs != null)
				return false;
		} else if (!submittedTs.equals(other.submittedTs))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Reimbursement [id=" + id + ", amount=" + amount + ", submittedTs=" + submittedTs + ", resolvedTs="
				+ resolvedTs + ", description=" + description + ", author=" + author + ", resolver=" + resolver
				+ ", statusid=" + statusid + ", type=" + type + "]";
	}

	
	
}
