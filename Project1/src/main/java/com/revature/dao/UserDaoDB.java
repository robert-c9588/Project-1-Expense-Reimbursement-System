package com.revature.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.User;
import com.revature.beans.User.UserRole;
import com.revature.driver.ReimbursementSystemDriver;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.InvalidUserSettingsException;
import com.revature.exceptions.UsernameAlreadyExistsException;
import com.revature.utils.ConnectionUtil;

public class UserDaoDB implements UserDao {

	// TABLE NAME: ers_users
	/*
	 * ers_users_id int PRIMARY KEY, ers_username varchar(50) UNIQUE , ers_password
	 * varchar(50), user_first_name varchar(100), user_last_name varchar(100),
	 * user_email varchar(150) UNIQUE , user_role_id int
	 * 
	 * CONSTRAINT user_roles_fk FOREIGN KEY (user_role_id) REFERENCES
	 * ers_user_roles(ers_user_role_id) ON DELETE CASCADE
	 */
	@Override
	public User addUser(User user) {

		// Need to throw exception if id/username/password/roleId is null
		User u = user;

		if (u != null && u.getId() != null && !u.getUsername().equals(null) && !u.getPassword().equals(null)
				&& u.getRoleId() != null && !u.getEmail().equals(null)) {
			try {
				String sql = "INSERT INTO ers_users VALUES(?,?,?,?,?,?,?)";

				PreparedStatement ps = ConnectionUtil.getConnectionUtil().getConnection().prepareStatement(sql);
				ps.setInt(1, u.getId());
				ps.setString(2, u.getUsername());
				ps.setString(3, u.getPassword());
				ps.setString(4, u.getFname());
				ps.setString(5, u.getLname());
				ps.setString(6, u.getEmail());

				if (u.getRoleId().equals(UserRole.EMPLOYEE)) {
					ps.setInt(7, 1);
				} else {
					ps.setInt(7, 1);
				}
				ps.execute();
				ps.close();
				ps.getConnection().close();
				ReimbursementSystemDriver.logger.info("Write success");
				return u;

			} catch (SQLException e) {
				ReimbursementSystemDriver.logger.error("Write failed");
				ReimbursementSystemDriver.logger.debug("Unable write to db", e);
			}
		} else {
			RuntimeException rte = new InvalidUserSettingsException("Settings of user are invalid. New user not added");
			throw rte;
		}

		RuntimeException rte = new UsernameAlreadyExistsException(
				"Username: '" + user.getUsername() + "' already exists");
		throw rte;
	}

	@Override
	public User getUser(Integer userId) {
		User u = null;
		if (userId != null) {
			try {
				// Selecting only one record
				String sql = "SELECT * FROM ers_users WHERE ers_users_id=?";
				PreparedStatement ps = ConnectionUtil.getConnectionUtil().getConnection().prepareStatement(sql);
				ps.setInt(1, userId);
				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					u = new User();
					u.setId(rs.getInt(1));
					u.setUsername(rs.getString(2));
					u.setPassword(rs.getString(3));
					u.setFname(rs.getString(4));
					u.setLname(rs.getString(5));
					u.setEmail(rs.getString(6));
					// check the number for type
					if (rs.getInt(7) == 1) {
						u.setRoleId(UserRole.EMPLOYEE);
					} else {
						u.setRoleId(UserRole.FINANCE_MANAGER);
					}
				}
				rs.close();
				ps.close();
				ps.getConnection().close();

				if (u != null && u.getId().equals(userId)) {
					ReimbursementSystemDriver.logger.info("User successfully read from db");
					return u;
				} else {
					RuntimeException rte = new InvalidCredentialsException("User not found");
					throw rte;
				}
			} catch (SQLException e) {
				ReimbursementSystemDriver.logger.error("Read failed");
				ReimbursementSystemDriver.logger.debug("Unable read from db", e);
			}
		}
		RuntimeException rte = new InvalidCredentialsException("User not found");
		throw rte;
	}

	@Override
	public User getUser(String username, String pass) {

		User u = null;
		if (username != null && pass != null) {
			try {
				// Selecting only one record
				String sql = "SELECT * FROM ers_users WHERE ers_username=? AND ers_password=?";
				PreparedStatement ps = ConnectionUtil.getConnectionUtil().getConnection().prepareStatement(sql);
				ps.setString(1, username);
				ps.setString(2, pass);
				ResultSet rs = ps.executeQuery();

				while (rs.next()) {
					u = new User();
					u.setId(rs.getInt(1));
					u.setUsername(rs.getString(2));
					u.setPassword(rs.getString(3));
					u.setFname(rs.getString(4));
					u.setLname(rs.getString(5));
					u.setEmail(rs.getString(6));
					// check the number for type
					if (rs.getInt(7) == 1) {
						u.setRoleId(UserRole.EMPLOYEE);
					} else {
						u.setRoleId(UserRole.FINANCE_MANAGER);
					}
				}

				rs.close();
				ps.close();
				ps.getConnection().close();

				if (u.getUsername() != null && u.getUsername().equals(username) && u.getPassword() != null
						&& u.getPassword().equals(pass)) {
					ReimbursementSystemDriver.logger.info("User successfully read from db");
					return u;
				} else {
					RuntimeException rte = new InvalidCredentialsException("User not found");
					throw rte;
				}
			} catch (SQLException e) {
				ReimbursementSystemDriver.logger.error("Read failed");
				ReimbursementSystemDriver.logger.debug("Unable read from db", e);
			}
		}

		RuntimeException rte = new InvalidCredentialsException("Unable to authenticate user: '" + username + "'");
		throw rte;
	}

	@Override
	public List<User> getAllUsers() {
		List<User> ulist = new ArrayList<User>();
		try {
			String sql = "SELECT * FROM ers_users";
			Statement s = ConnectionUtil.getConnectionUtil().getConnection().createStatement();
			ResultSet rs = s.executeQuery(sql);

			while (rs.next()) {
				User u = new User();
				u.setId(rs.getInt(1));
				u.setUsername(rs.getString(2));
				u.setPassword(rs.getString(3));
				u.setFname(rs.getString(4));
				u.setLname(rs.getString(5));
				u.setEmail(rs.getString(6));
				// check the number for type
				if (rs.getInt(7) == 1) {
					u.setRoleId(UserRole.EMPLOYEE);
				} else {
					u.setRoleId(UserRole.FINANCE_MANAGER);
				}
				ulist.add(u);
			}

			rs.close();
			s.close();
			s.getConnection().close();

			ReimbursementSystemDriver.logger.info("User successfully read from db");
			return ulist;
		} catch (SQLException e) {
			ReimbursementSystemDriver.logger.error("Read failed");
			ReimbursementSystemDriver.logger.debug("Unable read from db", e);
		}
		return ulist;
	}

	/*
	 * ers_users_id int PRIMARY KEY, ers_username varchar(50) UNIQUE , ers_password
	 * varchar(50), user_first_name varchar(100), user_last_name varchar(100),
	 * user_email varchar(150) UNIQUE , user_role_id int,
	 */
	@Override
	public User updateUser(User user) {

		User u = getUser(user.getId());
		if (u != null && u.getId() != null && !u.getUsername().equals(null) && !u.getPassword().equals(null)
				&& u.getRoleId() != null) {

			String sql = "UPDATE ers_users SET ers_users_id=?,ers_username=?,ers_password=?,user_first_name=?,user_last_name=?,user_email=?,user_role_id=?";
			try {
				PreparedStatement ps = ConnectionUtil.getConnectionUtil().getConnection().prepareStatement(sql);

				ps.setInt(1, user.getId());
				ps.setString(2, user.getUsername());
				ps.setString(3, user.getPassword());
				ps.setString(4, user.getFname());
				ps.setString(5, user.getLname());
				ps.setString(6, user.getEmail());

				if (user.getRoleId().equals(UserRole.EMPLOYEE)) {
					ps.setInt(7, 1);
				} else {
					ps.setInt(7, 1);
				}
				ps.execute();
				ps.close();
				ps.getConnection().close();
				u = user;

				ReimbursementSystemDriver.logger.info("Write success");
				return u;

			} catch (SQLException e) {
				ReimbursementSystemDriver.logger.error("Write failed");
				ReimbursementSystemDriver.logger.debug("Unable write to db", e);
			}
		}
		RuntimeException rte = new InvalidCredentialsException("User does not exist. Cannot update");
		throw rte;
	}

	@Override
	public boolean removeUser(User user) {
		User u = getUser(user.getId());
		if (u != null) {
			try {
				String sql = "DELETE FROM ers_users WHERE  ers_users_id=?";

				PreparedStatement ps = ConnectionUtil.getConnectionUtil().getConnection().prepareStatement(sql);
				ps.setInt(1, u.getId());
				ps.execute();
				ps.close();
				ps.getConnection().close();

				ReimbursementSystemDriver.logger.info("Delete success");
				return true;

			} catch (SQLException e) {
				ReimbursementSystemDriver.logger.error("Write failed");
				ReimbursementSystemDriver.logger.debug("Unable write to db", e);
			}
		}
		return false;
	}

}
