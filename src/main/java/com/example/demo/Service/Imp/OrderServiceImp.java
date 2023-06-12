package com.example.demo.Service.Imp;

import java.util.Date;
import java.util.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.Exception.ResourceNotFoundException;
import com.example.demo.Model.Cart;
import com.example.demo.Model.CartItem;
import com.example.demo.Model.Order;
import com.example.demo.Model.OrderItem;
import com.example.demo.Model.Product;
import com.example.demo.Model.Role;
import com.example.demo.Model.User;
import com.example.demo.Repository.CartRepository;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.OrderService;
import com.example.demo.payload.OrderDto;
import com.example.demo.payload.OrderRequest;
import com.example.demo.payload.RoleDto;
import com.example.demo.payload.UserDto;

//import com.ecom.Exception.ResourceNotFoundException;
//import com.ecom.Model.Cart;
//import com.ecom.Model.CartItem;
//import com.ecom.Model.Order;
//import com.ecom.Model.OrderItem;
//import com.ecom.Model.User;
//import com.ecom.Repository.CartRepository;
//import com.ecom.Repository.OrderRepository;
//import com.ecom.Repository.UserRepository;
//import com.ecom.Service.OrderService;
//import com.ecom.payload.ItemRequest;
//import com.ecom.payload.OrderDto;
//import com.ecom.payload.OrderRequest;
@Service
public class OrderServiceImp implements OrderService{
	 @Autowired
	private UserRepository userRepository;
	 @Autowired
	private CartRepository cartRepository;
	 @Autowired
	private OrderRepository orderRepository;

	 @Autowired
	private ProductRepository productRepository;
	 @Autowired
		ModelMapper mapper;
	

	@Override
	public OrderDto create(OrderRequest request,String Username) {
		System.out.println("This is implementation"); 
	   User findByEmail = this.userRepository.findByEmail(Username).orElseThrow(() -> new ResourceNotFoundException("User Not Found")); 
	   System.out.println("This is implementation done ");
	  	int cartId=request.getCartID();
	   Cart cart = this.cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart id not found"));
	    Set<CartItem> items= cart.getIteam();
	    
	    Order order=new Order();
	    
	    AtomicReference<Double>totalOrderPrize= new AtomicReference<>(0.0);
	    Set<OrderItem> orderItems = items.stream().map((cartItem) ->{

	    	  System.out.println("Hi there Order  this is product quantity");
	    	OrderItem orderItem=new OrderItem();
	    	  orderItem.setProduct(cartItem.getProduct());
//	    	  if(productQuntity() == product.getProductQuantity() ) {
//			    	 product.setStock(false);
//			    	 product.setProductQuantity(0);
//			  }
	    	  Product product = this.productRepository.findById(cartItem.getProduct().getProductId()).orElseThrow(()->new ResourceNotFoundException("Product not Found"));
	    	  if(cartItem.getProduct().getProductQuantity() == cartItem.getQuantity()) {
	    		  product.setProductQuantity(0);
	    		  product.setStock(false);
	    		  
	    	  }
	    	  else if(cartItem.getProduct().getProductQuantity() < cartItem.getQuantity()){
	    		  System.out.println("Error is thrown Order Quantity is not available");
	    		  throw new ResourceNotFoundException("Order Quantity is not available");
	    	  }
	    	  else {
	    		  product.setProductQuantity(cartItem.getProduct().getProductQuantity()-cartItem.getQuantity());
	    		  if(cartItem.getProduct().getProductQuantity()==0) 
		    		  product.setStock(false);
	    	  }
//	    	  System.out.println(cartItem.getProduct().getProductQuantity()+" this is total product quantity");
//	    	  System.out.println(cartItem.getQuantity()+" this is cart quantity");
//	    	  System.out.println(cartItem.getProduct()+" this is product");
	    	  
	    	  
	    	  
	    	  orderItem.setQuantity(cartItem.getQuantity());
	    	  orderItem.setTotalProductPrize(cartItem.getTotalproductprize());
	    	  orderItem.setOrder(order);
	    	 totalOrderPrize.set(totalOrderPrize.get()+orderItem.getTotalProductPrize());
	    	 
	    	 
	    	   int productId = orderItem.getProduct().getProductId();
	    	   
	    	return orderItem;
	    }).collect(Collectors.toSet());
	    
	   
	          order.setItem(orderItems);
	        String  address = request.getAddress();
	          order.setBillingAddress(address);
	          order.setPaymentStatus("NOT PAID");
	          order.setOrderCreated(new Date());
	          order.setOrderDelivered(null);
	          order.setOrderStatus("Created");
	          order.setUser(findByEmail);
	          order.setOrderAmout(totalOrderPrize.get());
	         
	         if(order.getOrderAmout()>0) {
	        	 Order savedorder = this.orderRepository.save(order); 	
		          
			       cart.getIteam().clear();
			       this.cartRepository.save(cart);
			
			
			
			return this.mapper.map(savedorder,OrderDto.class) ;
	        	 
	         }else {
	        	 throw new ResourceNotFoundException("Plz Add Cart First and place Order");
	         }
	}

	@Override
	public List<OrderDto> getAllOrder(String p) {
		
	    User findByEmail = this.userRepository.findByEmail(p).orElseThrow(()->new ResourceNotFoundException("User Not Found"));
		List<Order> findAll = this.orderRepository.findByUser(findByEmail).orElseThrow(()->new ResourceNotFoundException("Order Not Found"));
		
		List<OrderDto> collect = findAll.stream().map((each) -> toDto(each)).collect(Collectors.toList());
		return collect;
	}

	@Override
	public OrderDto getOrder(int OrderId) {
		 Order order = this.orderRepository.findById(OrderId).orElseThrow(()->new ResourceNotFoundException("Order not Found"));
		 return toDto(order);
//		 return this.mapper.map(order,OrderDto.class);
	}

	@Override
	public void deleteOrder(int orderId) {
		Order findById = this.orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order not Found"));
//		System.out.println(findById.getItem().toString()+" this is item"); 
//		System.out.println(findById.getItem().iterator().toString()+" this is delete item"); 
//		System.out.println(findById.getItem()+" this is 0 delete item"); 
//		System.out.println(findById.getItem()); 
//		System.out.println(findById.getItem()); 
		this.orderRepository.delete(findById);
		
	}

	@Override
	public OrderDto updateOrder(int orderId, OrderDto orderDto) {
		
		Order findOrder = this.orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("order not Found"));
		
		findOrder.setOrderDelivered(orderDto.getOrderDelivered());
		findOrder.setPaymentStatus(orderDto.getPaymentStatus().toUpperCase());
		findOrder.setOrderStatus(orderDto.getOrderStatus());
		  Order save = this.orderRepository.save(findOrder);
		
		return this.mapper.map(save,OrderDto.class);
	}

	@Override
	public List<OrderDto> listAllOrder() {
		List<Order> listallorder = this.orderRepository.findAll();
		
		 List<OrderDto> collect2 = listallorder.stream().map((each)-> toDto(each)).collect(Collectors.toList());
		
		return collect2;
	}

	public OrderDto toDto(Order ordr) {
		OrderDto oDto = new OrderDto();
		oDto.setBillingAddress(ordr.getBillingAddress());
		oDto.setItem(ordr.getItem());
		oDto.setOrderAmout(ordr.getOrderAmout());
		oDto.setOrderCreated(ordr.getOrderCreated());
		oDto.setOrderDelivered(ordr.getOrderDelivered());
		oDto.setOrderId(ordr.getOrderId());
		oDto.setOrderStatus(ordr.getOrderStatus());
		oDto.setPaymentStatus(ordr.getPaymentStatus());
		UserDto udot = new UserDto();
		udot.setAbout(ordr.getUser().getAbout());
		udot.setActive(ordr.getUser().isActive());
		udot.setAddress(ordr.getUser().getAddress());
		udot.setDate(ordr.getUser().getDate());
		udot.setEmail(ordr.getUser().getEmail());
		udot.setGender(ordr.getUser().getGender());
		udot.setName(ordr.getUser().getName());
		udot.setPassword(ordr.getUser().getPassword());
		udot.setPhone(ordr.getUser().getPhone());
		Set<Role> rr = ordr.getUser().getRoles();
		Set<RoleDto> rd = rr.stream().map((e) -> mapper.map(e, RoleDto.class)).collect(Collectors.toSet());
//		RoleDto.class).collect(Collectors.toList());;
//		RoleDto rdto = new RoleDto();
//		rdto.setId(ordr.getUser().getRoles());
		udot.setRoles(rd);
		udot.setUserId(ordr.getUser().getUserId());
		oDto.setUser(udot);
		return oDto;
	}
	


}
