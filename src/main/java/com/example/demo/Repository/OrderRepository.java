package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.Model.Order;
import com.example.demo.Model.User;

//import com.ecom.Model.Order;
//import com.ecom.Model.User;

public interface OrderRepository extends JpaRepository<Order,Integer>{
	
  Optional<List<Order>>findByUser(User findByEmail);
  
  
  
  

}
