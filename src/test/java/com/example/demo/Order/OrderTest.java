//package com.example.demo.Order;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.when;
//
//import java.security.Principal;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.Set;
//
//import org.aspectj.lang.annotation.Before;
//import org.junit.internal.requests.OrderingRequest;
//import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
////import org.junit.Before;
////import org.junit.Test;
////import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.example.demo.Controller.OrderController;
//import com.example.demo.Model.Cart;
//import com.example.demo.Model.CartItem;
//import com.example.demo.Model.Category;
//import com.example.demo.Model.Product;
//import com.example.demo.Model.User;
//import com.example.demo.Repository.CartRepository;
//import com.example.demo.Service.OrderService;
//import com.example.demo.payload.ItemRequest;
//import com.example.demo.payload.OrderDto;
//import com.example.demo.payload.OrderRequest;
//
//
////@RunWith(SpringRunner.class)
////@AutoConfigureTestDatabase(replace=Replace.NONE)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@SpringBootTest
//public class OrderTest {
//	
//	@Autowired
//	private OrderService orderService;
//	
//	@Autowired
//	private OrderController orderController;
//	
//	@Test
//	@Order(1)
//	public void testCreateOrder() {
//		
//		
//		
//		OrderRequest orderRequest = new OrderRequest();
//		orderRequest.setAddress("Hld");
//		orderRequest.setCartID(1);
////		System.out.println(principal.getName());
////		orderController.createOrder(orderRequest, principal);
//		OrderDto order =  orderService.create(orderRequest, "himanshu.nainwal@stl.tech");
//		System.out.println(order.getOrderId());
//		System.out.println(order.getUser());
//	}
//	
//}