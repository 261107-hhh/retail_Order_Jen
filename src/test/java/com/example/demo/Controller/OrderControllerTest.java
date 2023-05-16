package com.example.demo.Controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.Controller.OrderController;
import com.example.demo.Controller.PaymentController;
import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.Model.Cart;
import com.example.demo.Model.CartItem;
import com.example.demo.Model.Order;
import com.example.demo.Model.OrderItem;
import com.example.demo.Model.User;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.OrderService;
import com.example.demo.payload.ApiResponse;
import com.example.demo.payload.OrderDto;
import com.example.demo.payload.OrderRequest;
import com.example.demo.payload.PaymentSuccessResponse;
import com.example.demo.payload.paymentOrderResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.razorpay.RazorpayException;

import org.json.*;
import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;



@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class OrderControllerTest {
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderController orderController;
	
	
	
	String mail = "himanshu.nainwal@stl.tech";
	int cartId = 1;
	static int orderId;
	
	@JsonManagedReference
	Set<CartItem> items;
	
//	@Test
//	@org.junit.jupiter.api.Order(1)
//	public void testCreateOrder() {
//		  User findByEmail = this.userRepository.findByEmail(mail).orElseThrow(() -> new ResourceNotFoundException("User Not Found")); 
//		OrderRequest request = new OrderRequest();
//		request.setCartID(cartId);
//		request.setAddress("Haldwani");
//		Cart cart = this.cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart id not found"));
//		   
//		Order order = new Order();
//		
//		items= cart.getIteam();
//		
//		AtomicReference<Double>totalOrderPrize= new AtomicReference<>(0.0);
//		Set<OrderItem> orderItems = items.stream().map((cartItem) ->{
//			OrderItem orderItem=new OrderItem();
//		    	  orderItem.setProduct(cartItem.getProduct());
//		    	  orderItem.setQuantity(cartItem.getQuantity());
//		    	  orderItem.setTotalProductPrize(cartItem.getTotalproductprize());
//		    	  orderItem.setOrder(order);
//		    	  totalOrderPrize.set(totalOrderPrize.get()+orderItem.getTotalProductPrize());
//		    	 
//		    	   int productId = orderItem.getProduct().getProductId();
//		    	   
//		    	return orderItem;
//		}).collect(Collectors.toSet());
//		order.setItem(orderItems);
//        String  address = request.getAddress();
//          order.setBillingAddress(address);
//          order.setPaymentStatus("PAID");
//          order.setOrderCreated(new Date());
//          order.setOrderDelivered(null);
//          order.setOrderStatus("Created");
//          order.setUser(findByEmail);
//          order.setOrderAmout(totalOrderPrize.get());
//          Order savedorder = this.orderRepository.save(order); 	
//          orderId = savedorder.getOrderId();
//          Cart crt = cartRepository.save(cart);
////          System.out.println(crt.getCartId() +": lalalala :"+ crt.getIteam().toString());
//          assertEquals(cartId, crt.getCartId());
//
//		
//	}

	
	@Test
	@org.junit.jupiter.api.Order(1)
	public void testCreateOrder() {
			
		OrderRequest request = new OrderRequest();
		request.setCartID(cartId);
		request.setAddress("Haldwani");
		try {
			OrderDto order = orderService.create(request, mail);			
			orderId = order.getOrderId();
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("No element present in cart :"+ e);
		}
	}
	
	@Test
	@org.junit.jupiter.api.Order(2)
	public void	testGetOrderByUser() {
		try {
			List<OrderDto> orders = orderService.getAllOrder(mail);
			System.out.println(orders);
			assertEquals(orderId,orders.get(orders.size()-1).getOrderId());
			assertEquals( orders.get(orders.size()-1).getUser().getEmail(), mail);			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("No order by user: "+mail);
		}
//		System.out.println(orders.getStatusCodeValue());

	}
	
	@Test
	@org.junit.jupiter.api.Order(3)
	public void	testGetAllOrderOrderByUser() {
		try {
		    ResponseEntity<List<OrderDto>> res = orderController.getAllOrder();
		    int size = res.getBody().size();
		    assertEquals(HttpStatus.OK, res.getStatusCode());
		    assertEquals(orderId, res.getBody().get(size-1).getOrderId());

		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("NO order:");
		}

	}
	
	@Test
	@org.junit.jupiter.api.Order(4)
	public void	testGetByOrderId() {
		try {
			ResponseEntity<OrderDto> res = orderController.getByOrderId(orderId);
			assertEquals(HttpStatus.OK, res.getStatusCode());
			assertEquals(res.getBody().getOrderId(), orderId);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("NO order with id:"+ orderId);
		}
	}
	
	
	@Test
	@org.junit.jupiter.api.Order(6)
	public void testDeleteOrder() {
		try {
			ResponseEntity<ApiResponse> res = orderController.delete(orderId);
			assertEquals(HttpStatus.OK, res.getStatusCode());
			System.out.println(res.getBody().getMessage());
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("No product is ordered: "+e);
		}
	}
}
