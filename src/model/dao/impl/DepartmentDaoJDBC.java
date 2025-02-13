package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class DepartmentDaoJDBC implements DepartmentDao{
	
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = this.conn.prepareStatement(
					"INSERT INTO department " +
					"(Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				rs = st.getGeneratedKeys();
				while (rs.next()) {
					int key = rs.getInt(1);
					System.out.printf("New register with Id: %d", key);
				}
			}
			
		} 
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void update(Department obj) {
		this.findById(obj.getId());
		PreparedStatement st = null;
		try {
			st = this.conn.prepareStatement(
					"UPDATE department " +
							"SET Name = ? " +
					"WHERE Id = ?");
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
			
			st.executeUpdate();
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		this.findById(id);
		SellerDao seller = DaoFactory.createSellerDao();
		Department department = new Department();
		department.setId(id);
		List<Seller> sellersList = seller.findByDepartment(department);
		if (sellersList.size() > 0) {
			throw new IllegalArgumentException("This Id is linked to another register");
		}
		else {
			PreparedStatement st = null;
			try {
				st = this.conn.prepareStatement(
						"DELETE FROM department " +
						"WHERE Id = ?");
				
				st.setInt(1, id);
				Integer rowsAffected = st.executeUpdate();
				if (rowsAffected > 0) {
					System.out.printf("Rows affected: %d", rowsAffected);
				}
			} 
			catch (SQLException e){
				throw new DbException(e.getMessage());
			}
			finally {
				DB.closeStatement(st);
			}
		}
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		Department dep = null;
		ResultSet rs = null;
		try {
			st = this.conn.prepareStatement(
					"SELECT * FROM department "
					+ "WHERE Id = ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			while (rs.next()) {
				dep = new Department(rs.getInt("Id"), rs.getString("Name"));
			}
			return dep;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
			if (dep == null) {
				throw new DbException("No register with Id = " + id + " found");
			}
		}
	}

	@Override
	public List<Department> findAll() {
		Statement st = null;
		ResultSet rs = null;
		List<Department> departments = new ArrayList<>();
		try {
			st = this.conn.createStatement();
			rs = st.executeQuery("SELECT * FROM department");
			while (rs.next()) {
				departments.add(new Department(rs.getInt("Id"), rs.getString("Name")));
			}
			return departments;
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	
}
