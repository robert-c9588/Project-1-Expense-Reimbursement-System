package com.revature.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.beans.User;
import com.revature.beans.User.UserRole;
import com.revature.exceptions.InvalidCredentialsException;
import com.revature.exceptions.InvalidUserSettingsException;
import com.revature.exceptions.UsernameAlreadyExistsException;
import com.revature.logger.ReimbSysLogger;
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

		if (!checkUser(user)) {
			if (u != null && !u.getUsername().equals(null) && !u.getPassword().equals(null) && u.getRoleId() != null
					&& !u.getEmail().equals(null)) {
				try {
					String sql = "INSERT INTO ers_users (ers_username,ers_password,user_first_name,user_last_name,user_email,user_role_id) VALUES(?,?,?,?,?,?)";

					PreparedStatement ps = ConnectionUtil.getConnectionUtil().getConnection().prepareStatement(sql);

					ps.setString(1, u.getUsername());
					ps.setString(2, u.getPassword());
					ps.setString(3, u.getFname());
					ps.setString(4, u.getLname());
					ps.setString(5, u.getEmail());

					if (u.getRoleId().equals(UserRole.EMPLOYEE)) {
						ps.setInt(6, 1);
					} else {
						ps.setInt(6, 1);
					}
					ps.execute();
					ps.close();
					ps.getConnection().close();
					ReimbSysLogger.getReimbSysLogger().getLogger().info("Write success");
					u = getUserId(u);
					return u;

				} catch (SQLException e) {
					ReimbSysLogger.getReimbSysLogger().getLogger().error("Write failed");
					ReimbSysLogger.getReimbSysLogger().getLogger().debug("Unable write to db", e);
				}
			} else {
				RuntimeException rte = new InvalidUserSettingsException(
						"Settings of user are invalid. New user not added");
				throw rte;
			}
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
					ReimbSysLogger.getReimbSysLogger().getLogger().info("User successfully read from db");
					return u;
				} else {
					RuntimeException rte = new InvalidCredentialsException("User not found");
					throw rte;
				}
			} catch (SQLException e) {
				ReimbSysLogger.getReimbSysLogger().getLogger().error("Read failed");
				ReimbSysLogger.getReimbSysLogger().getLogger().debug("Unable read from db", e);
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
					ReimbSysLogger.getReimbSysLogger().getLogger().info("User successfully read from db");
					return u;
				} else {
					RuntimeException rte = new InvalidCredentialsException("User not found");
					throw rte;
				}
			} catch (SQLException e) {
				ReimbSysLogger.getReimbSysLogger().getLogger().error("Read failed");
				ReimbSysLogger.getReimbSysLogger().getLogger().debug("Unable read from db", e);
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

			ReimbSysLogger.getReimbSysLogger().getLogger().info("User successfully read from db");
			return ulist;
		} catch (SQLException e) {
			ReimbSysLogger.getReimbSysLogger().getLogger().error("Read failed");
			ReimbSysLogger.getReimbSysLogger().getLogger().debug("Unable read from db", e);
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

			String sql = "UPDATE ers_users SET ers_username=?,ers_password=?,user_first_name=?,user_last_name=?,user_email=?,user_role_id=? WHERE ers_users_id=?";
			try {
				PreparedStatement ps = ConnectionUtil.getConnectionUtil().getConnection().prepareStatement(sql);

				ps.setInt(7, user.getId());
				ps.setString(1, user.getUsername());
				ps.setString(2, user.getPassword());
				ps.setString(3, user.getFname());
				ps.setString(4, user.getLname());
				ps.setString(5, user.getEmail());

				if (user.getRoleId().equals(UserRole.EMPLOYEE)) {
					ps.setInt(6, 1);
				} else {
					ps.setInt(6, 1);
				}
				ps.execute();
				ps.close();
				ps.getConnection().close();
				u = user;

				ReimbSysLogger.getReimbSysLogger().getLogger().info("Write success");
				return u;

			} catch (SQLException e) {
				ReimbSysLogger.getReimbSysLogger().getLogger().error("Write failed");
				ReimbSysLogger.getReimbSysLogger().getLogger().debug("Unable write to db", e);
			}
		}
		RuntimeException rte = new InvalidCredentialsException("User does not exist. Cannot update");
		throw rte;
	}

	@Override
	public boolean removeUser(User user) {
		User u = user;
		if (u != null) {
			try {
				String sql = "DELETE FROM ers_users WHERE  ers_users_id=?";

				PreparedStatement ps = ConnectionUtil.getConnectionUtil().getConnection().prepareStatement(sql);
				ps.setInt(1, u.getId());
				ps.execute();
				ps.close();
				ps.getConnection().close();

				ReimbSysLogger.getReimbSysLogger().getLogger().info("Delete success");
				return true;

			} catch (SQLException e) {
				ReimbSysLogger.getReimbSysLogger().getLogger().error("Write failed");
				ReimbSysLogger.getReimbSysLogger().getLogger().debug("Unable write to db", e);
			}
		}
		return false;
	}

	@Override
	public boolean checkUser(User user) {
		User u = user;
		try {
			String sql = "SELECT count(1) FROM ers_users eu WHERE ers_username=?";
			PreparedStatement ps = ConnectionUtil.getConnectionUtil().getConnection().prepareStatement(sql);

			ps.setString(1, u.getUsername());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getInt(1) == 1) {
					return true;
				}
			}
		} catch (SQLException e) {

		}
		return false;
	}

	@Override
	public User getUserId(User user) {
		User u = getUser(user.getUsername(), user.getPassword());
		return u;

	}

}
