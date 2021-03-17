package com.revature.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Reimbursement;
import com.revature.beans.Reimbursement.ReimbStatus;
import com.revature.beans.Reimbursement.ReimbType;
import com.revature.beans.User.UserRole;
import com.revature.beans.User;
import com.revature.exceptions.InvalidArgumentsReimbursementException;
import com.revature.exceptions.UnauthorizedException;
import com.revature.logger.ReimbSysLogger;
import com.revature.utils.ConnectionUtil;
import com.revature.utils.SessionCache;

public class ReimbursementDaoDB implements ReimbursmentDao {

	// TABLE ers_reimbursements
	/*
	 * reimb_id int PRIMARY KEY , reimb_amount numeric, reimb_submitted timestamp,
	 * reimb_resolved timestamp, reimb_description varchar(250), reimb_receipt
	 * bytea, ---- doing this as stretch goal setting later. reimb_author int,
	 * reimb_resolver int, reimb_status_id int, reimb_type_id int
	 * 
	 * CONSTRAINT ers_users_fk_auth FOREIGN KEY (reimb_author) REFERENCES
	 * ers_users(ers_users_id) ON DELETE CASCADE , CONSTRAINT ers_users_fk_reslvr
	 * FOREIGN KEY (reimb_resolver) REFERENCES ers_users(ers_users_id) ON DELETE
	 * CASCADE , CONSTRAINT ers_reimbursment_status_fk FOREIGN KEY (reimb_status_id)
	 * REFERENCES ers_reimbursement_status(reimb_status_id) ON DELETE CASCADE ,
	 * CONSTRAINT ers_reimbursment_type_fk FOREIGN KEY (reimb_type_id) REFERENCES
	 * ers_reimbursement_type(reimb_type_id) ON DELETE CASCADE
	 */
	@SuppressWarnings("null")
	@Override
	public Reimbursement addReimbursment(Reimbursement reimb) {
		Reimbursement r = reimb;

		// checking all reimbursement inputs that must be initialized before adding to
		// reimbursement db

		if (r != null && r.getSubmittedTs() != null && r.getStatusid() != null && r.getType() != null
				&& r.getAuthor() != null && r.getAmount() != null) {
			try {
				String sql = "INSERT INTO ers_reimbursements (reimb_amount, reimb_submitted, reimb_resolved,"
						+ " reimb_description, reimb_author, reimb_resolver, reimb_status_id, reimb_type_id) VALUES (?,?,?,?,?,?,?,?)";
				PreparedStatement ps = ConnectionUtil.getConnectionUtil().getConnection().prepareStatement(sql);

				ps.setDouble(1, r.getAmount());
				ps.setTimestamp(2, Timestamp.valueOf(r.getSubmittedTs()));
				if (r.getResolvedTs() != null) {
					ps.setTimestamp(3, Timestamp.valueOf(r.getResolvedTs()));
				} else {
					ps.setTimestamp(3, null);
				}
				if (r.getDescription() != null) {
					ps.setString(4, r.getDescription());
				} else {
					ps.setString(4, null);
				}
				// column 6 is the reciept holder bytea column. Haven't discovered how to
				// implement yet. Will skip column for now.
				ps.setInt(5, r.getAuthor().getId());
				if (r.getResolver() != null) {
					ps.setInt(6, r.getResolver().getId());
				} else {
					ps.setNull(6, java.sql.Types.INTEGER);
				}
				if (r.getStatusid() == ReimbStatus.DENIED) {
					ps.setInt(7, 0);
				} else if (r.getStatusid() == ReimbStatus.APPROVED) {
					ps.setInt(7, 1);
				} else {// IF PENDING
					ps.setInt(7, 2);
				}
				if (r.getType() == ReimbType.LODGING) {
					ps.setInt(8, 1);
				} else if (r.getType() == ReimbType.TRAVEL) {
					ps.setInt(8, 2);
				} else if (r.getType() == ReimbType.FOOD) {
					ps.setInt(8, 3);
				} else {// If OTHER
					ps.setInt(8, 4);
				}

				ps.execute();
				ps.close();
				ps.getConnection().close();

				r = getReimbursmentId(r);

				ReimbSysLogger.getReimbSysLogger().getLogger().info("Write success");
				return r;

			} catch (SQLException e) {
				ReimbSysLogger.getReimbSysLogger().getLogger().error("Write failed");
				ReimbSysLogger.getReimbSysLogger().getLogger().debug("Unable to write to db", e);
			}

		}
		RuntimeException rte = new InvalidArgumentsReimbursementException(
				"Invalid arguments for creating a new reimbursement");
		throw rte;
	}

	@Override
	public Reimbursement getReimbursement(Integer reimbId) {
		Reimbursement r = null;
		if (reimbId != null) {
			try {
				String sql = "SELECT * FROM ers_reimbursements WHERE reimb_id=?";
				PreparedStatement ps = ConnectionUtil.getConnectionUtil().getConnection().prepareStatement(sql);

				ps.setInt(1, reimbId);
				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					r = new Reimbursement();
					r.setId(rs.getInt(1));
					r.setAmount(rs.getDouble(2));
					r.setSubmittedTs(rs.getTimestamp(3).toLocalDateTime());

					if (rs.getTimestamp(4) != null) {
						r.setResolvedTs(rs.getTimestamp(4).toLocalDateTime());
					}
					if (rs.getString(5) != null) {
						r.setDescription(rs.getString(5));
					}
					// receipt set not implemented.
					UserDao udao = new UserDaoDB();
					r.setAuthor(udao.getUser(rs.getInt(7)));

					Integer resolverid = rs.getInt(8);
					if (!rs.wasNull()) {
						r.setResolver(udao.getUser(rs.getInt(8)));
					}
					if (rs.getInt(9) == 0) {
						r.setStatusid(ReimbStatus.DENIED);
					} else if (rs.getInt(9) == 1) {
						r.setStatusid(ReimbStatus.APPROVED);
					} else {// If pending
						r.setStatusid(ReimbStatus.PENDING);
					}
					if (rs.getInt(10) == 1) {
						r.setType(ReimbType.LODGING);
					} else if (rs.getInt(10) == 2) {
						r.setType(ReimbType.TRAVEL);
					} else if (rs.getInt(10) == 3) {
						r.setType(ReimbType.FOOD);
					} else {// if other
						r.setType(ReimbType.OTHER);
					}
				}
				rs.close();
				ps.close();
				ps.getConnection().close();
				ReimbSysLogger.getReimbSysLogger().getLogger().info("Read success");
				return r;
			} catch (SQLException e) {
				ReimbSysLogger.getReimbSysLogger().getLogger().error("Read failed");
				ReimbSysLogger.getReimbSysLogger().getLogger().debug("Unable to read from db", e);
			}
		}
		RuntimeException rte = new InvalidArgumentsReimbursementException("Invalid id number");
		throw rte;
	}

	@Override
	public List<Reimbursement> getAllReimbursements() {
		List<Reimbursement> rlist = new ArrayList<Reimbursement>();
		try {
			String sql = "SELECT * FROM ers_reimbursements";
			Statement s = ConnectionUtil.getConnectionUtil().getConnection().createStatement();
			ResultSet rs = s.executeQuery(sql);

			while (rs.next()) {
				Reimbursement r = new Reimbursement();
				r = new Reimbursement();
				r.setId(rs.getInt(1));
				r.setAmount(rs.getDouble(2));
				r.setSubmittedTs(rs.getTimestamp(3).toLocalDateTime());
				r.setResolvedTs(rs.getTimestamp(4).toLocalDateTime());
				r.setDescription(rs.getString(5));
				// receipt set not implemented.
				UserDao udao = new UserDaoDB();
				r.setAuthor(udao.getUser(rs.getInt(7)));
				r.setResolver(udao.getUser(rs.getInt(8)));
				if (rs.getInt(9) == 0) {
					r.setStatusid(ReimbStatus.DENIED);
				} else if (rs.getInt(9) == 1) {
					r.setStatusid(ReimbStatus.APPROVED);
				} else {// If pending
					r.setStatusid(ReimbStatus.PENDING);
				}
				if (rs.getInt(10) == 1) {
					r.setType(ReimbType.LODGING);
				} else if (rs.getInt(10) == 2) {
					r.setType(ReimbType.TRAVEL);
				} else if (rs.getInt(10) == 3) {
					r.setType(ReimbType.FOOD);
				} else {// if other
					r.setType(ReimbType.OTHER);
				}
				rlist.add(r);
			}
			rs.close();
			s.close();
			s.getConnection().close();

			ReimbSysLogger.getReimbSysLogger().getLogger().info("Read success");
			return rlist;
		} catch (SQLException e) {
			ReimbSysLogger.getReimbSysLogger().getLogger().error("Read failed");
			ReimbSysLogger.getReimbSysLogger().getLogger().debug("Unable to read from db", e);
		}
		return rlist;
	}

	@Override
	public List<Reimbursement> getReimbursementsByEmployee(User user) {
		List<Reimbursement> rlist = new ArrayList<Reimbursement>();

		try {
			String sql = "SELECT * FROM ers_reimbursements WHERE reimb_author=?";
			PreparedStatement ps = ConnectionUtil.getConnectionUtil().getConnection().prepareStatement(sql);
			ps.setInt(1, user.getId());

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Reimbursement r = new Reimbursement();
				r = new Reimbursement();
				r.setId(rs.getInt(1));
				r.setAmount(rs.getDouble(2));
				r.setSubmittedTs(rs.getTimestamp(3).toLocalDateTime());

				if (rs.getTimestamp(4) != null) {
					r.setResolvedTs(rs.getTimestamp(4).toLocalDateTime());
				} else {
					r.setResolvedTs(null);
				}
				r.setDescription(rs.getString(5));
				// receipt set not implemented.
				UserDao udao = new UserDaoDB();
				r.setAuthor(udao.getUser(rs.getInt(7)));
				r.setResolver(udao.getUser(rs.getInt(8)));
				if (rs.getInt(9) == 0) {
					r.setStatusid(ReimbStatus.DENIED);
				} else if (rs.getInt(9) == 1) {
					r.setStatusid(ReimbStatus.APPROVED);
				} else {// If pending
					r.setStatusid(ReimbStatus.PENDING);
				}
				if (rs.getInt(10) == 1) {
					r.setType(ReimbType.LODGING);
				} else if (rs.getInt(10) == 2) {
					r.setType(ReimbType.TRAVEL);
				} else if (rs.getInt(10) == 3) {
					r.setType(ReimbType.FOOD);
				} else {// if other
					r.setType(ReimbType.OTHER);
				}
				rlist.add(r);
			}
			rs.close();
			ps.close();
			ps.getConnection().close();

			ReimbSysLogger.getReimbSysLogger().getLogger().info("Read success");
			return rlist;
		} catch (SQLException e) {
			ReimbSysLogger.getReimbSysLogger().getLogger().error("Read failed");
			ReimbSysLogger.getReimbSysLogger().getLogger().debug("Unable to read from db", e);
		}

		return rlist;
	}

	@SuppressWarnings("null")
	@Override
	public List<Reimbursement> getReimbursementsToApprove() {
		User currentManager = SessionCache.getCurrentUser().get();
		List<Reimbursement> rlist = new ArrayList<Reimbursement>();

		if (!(currentManager == null && currentManager.getRoleId().equals(UserRole.FINANCE_MANAGER))) {
			try {
				String sql = "SELECT * FROM ers_reimbursements WHERE NOT reimb_author=?";
				PreparedStatement ps = ConnectionUtil.getConnectionUtil().getConnection().prepareStatement(sql);
				ps.setInt(1, currentManager.getId());

				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					Reimbursement r = new Reimbursement();
					r = new Reimbursement();
					r.setId(rs.getInt(1));
					r.setAmount(rs.getDouble(2));
					r.setSubmittedTs(rs.getTimestamp(3).toLocalDateTime());
					r.setResolvedTs(rs.getTimestamp(4).toLocalDateTime());
					r.setDescription(rs.getString(5));
					// receipt set not implemented.
					UserDao udao = new UserDaoDB();
					r.setAuthor(udao.getUser(rs.getInt(7)));
					r.setResolver(udao.getUser(rs.getInt(8)));
					if (rs.getInt(9) == 0) {
						r.setStatusid(ReimbStatus.DENIED);
					} else if (rs.getInt(9) == 1) {
						r.setStatusid(ReimbStatus.APPROVED);
					} else {// If pending
						r.setStatusid(ReimbStatus.PENDING);
					}
					if (rs.getInt(10) == 1) {
						r.setType(ReimbType.LODGING);
					} else if (rs.getInt(10) == 2) {
						r.setType(ReimbType.TRAVEL);
					} else if (rs.getInt(10) == 3) {
						r.setType(ReimbType.FOOD);
					} else {// if other
						r.setType(ReimbType.OTHER);
					}
					rlist.add(r);
				}
				rs.close();
				ps.close();
				ps.getConnection().close();

				ReimbSysLogger.getReimbSysLogger().getLogger().info("Read success");
				return rlist;
			} catch (SQLException e) {
				ReimbSysLogger.getReimbSysLogger().getLogger().error("Read failed");
				ReimbSysLogger.getReimbSysLogger().getLogger().debug("Unable to read from db", e);
			}
		}
		ReimbSysLogger.getReimbSysLogger().getLogger().warn("User tried to acces approve function when not a manager");
		RuntimeException rte = new UnauthorizedException("You are not allowed to access this function");
		throw rte;
	}

	@Override
	public Reimbursement updateReimbursement(Reimbursement reimb) {
		Reimbursement r = reimb;
		if (r != null && r.getSubmittedTs() != null && r.getStatusid() != null && r.getType() != null
				&& r.getAuthor() != null && r.getAmount() != null) {
			try {
				// 9 parameters, keeping the id the same.
				String sql = "UPDATE ers_reimbursements SET reimb_amount=?, reimb_submitted=?, reimb_resolved=?, reimb_description=?, reimb_author=?,"
						+ " reimb_resolver=?, reimb_status_id=?, reimb_type_id=? WHERE reimb_id=?";

				PreparedStatement ps = ConnectionUtil.getConnectionUtil().getConnection().prepareStatement(sql);
				ps.setInt(9, r.getId());
				ps.setDouble(1, r.getAmount());
				ps.setTimestamp(2, Timestamp.valueOf(r.getSubmittedTs()));
				if (r.getResolvedTs() != null) {
					ps.setTimestamp(3, Timestamp.valueOf(r.getResolvedTs()));
				} else {
					ps.setTimestamp(3, null);
				}
				if (r.getDescription() != null) {
					ps.setString(4, r.getDescription());
				} else {
					ps.setNull(4, java.sql.Types.VARCHAR);
				}
				// column 6 is the reciept holder bytea column. Haven't discovered how to
				// implement yet. Will skip column for now.
				ps.setInt(5, r.getAuthor().getId());
				if (r.getResolver() != null) {
					ps.setInt(6, r.getResolver().getId());
				} else {
					ps.setNull(6, java.sql.Types.INTEGER);
				}
				if (r.getStatusid() == ReimbStatus.DENIED) {
					ps.setInt(7, 0);
				} else if (r.getStatusid() == ReimbStatus.APPROVED) {
					ps.setInt(7, 1);
				} else {// IF PENDING
					ps.setInt(7, 2);
				}
				if (r.getType() == ReimbType.LODGING) {
					ps.setInt(8, 1);
				} else if (r.getType() == ReimbType.TRAVEL) {
					ps.setInt(8, 2);
				} else if (r.getType() == ReimbType.FOOD) {
					ps.setInt(8, 3);
				} else {// If OTHER
					ps.setInt(8, 4);
				}

				ps.execute();
				ps.close();
				ps.getConnection().close();
				ReimbSysLogger.getReimbSysLogger().getLogger().info("Update success");
				return r;
			} catch (SQLException e) {
				ReimbSysLogger.getReimbSysLogger().getLogger().error("Write failed");
				ReimbSysLogger.getReimbSysLogger().getLogger().debug("Unable to write to db", e);
			}
		}

		RuntimeException rte = new InvalidArgumentsReimbursementException(
				"Invalid arguments for updating a reimbursement were given");
		throw rte;
	}

	@Override
	public boolean removeReimbursement(Reimbursement reimb) {
		Reimbursement r = reimb;
		if (r != null && r.getSubmittedTs() != null && r.getStatusid() != null && r.getType() != null
				&& r.getAuthor() != null && r.getAmount() != null) {
			try {
				String sql = "DELETE FROM ers_reimbursements WHERE reimb_id=?";
				PreparedStatement ps = ConnectionUtil.getConnectionUtil().getConnection().prepareStatement(sql);

				ps.setInt(1, r.getId());
				ps.close();
				ps.getConnection();
				ReimbSysLogger.getReimbSysLogger().getLogger().info("Delete success");
			} catch (SQLException e) {
				ReimbSysLogger.getReimbSysLogger().getLogger().error("Delete failed");
				ReimbSysLogger.getReimbSysLogger().getLogger().debug("Unable to delete from db", e);
			}

		}
		return false;
	}

	@Override
	public Reimbursement getReimbursmentId(Reimbursement reimb) {
		Reimbursement r = reimb;
		try {
			String sql = "SELECT reimb_id FROM ers_reimbursements WHERE reimb_amount=? AND reimb_author=? AND reimb_submitted=? AND reimb_status_id=? AND reimb_type_id=?";

			PreparedStatement ps = ConnectionUtil.getConnectionUtil().getConnection().prepareStatement(sql);
			ps.setDouble(1, r.getAmount());
			ps.setInt(2, r.getAuthor().getId());
			ps.setTimestamp(3, Timestamp.valueOf(r.getSubmittedTs().withNano(0)));
			if (r.getStatusid() == ReimbStatus.DENIED) {
				ps.setInt(4, 0);
			} else if (r.getStatusid() == ReimbStatus.APPROVED) {
				ps.setInt(4, 1);
			} else {// IF PENDING
				ps.setInt(4, 2);
			}
			if (r.getType() == ReimbType.LODGING) {
				ps.setInt(5, 1);
			} else if (r.getType() == ReimbType.TRAVEL) {
				ps.setInt(5, 2);
			} else if (r.getType() == ReimbType.FOOD) {
				ps.setInt(5, 3);
			} else {// If OTHER
				ps.setInt(5, 4);
			}

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				r.setId(rs.getInt(1));
			}
			ReimbSysLogger.getReimbSysLogger().getLogger().info("Read success");
			return r;
		} catch (SQLException e) {
			ReimbSysLogger.getReimbSysLogger().getLogger().error("Read failed");
			ReimbSysLogger.getReimbSysLogger().getLogger().debug("Unable to read from db", e);
		}

		return r;
	}
}
