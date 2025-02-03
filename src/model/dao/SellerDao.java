package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	
void insert(Seller obj);
	
	void update(Seller obj);
	
	void deleterById(Integer id);
	
	Seller findBydId(Integer id);
	
	List<Seller> findByDepartment(Department department);
	
	List<Seller> findAll();
}
