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
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{
	
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		
		
	}

	@Override
	public void update(Department obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Department findBydId(Integer id) {
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
