package com.example.demo.Service;

import java.util.List;

//import com.ecom.Model.Order;
//import com.example.demo.payload.ItemRequest;
import com.example.demo.payload.OrderDto;
import com.example.demo.payload.OrderRequest;

public interface OrderService {
 public OrderDto create(OrderRequest request,String username);
 //Get OrderBy User
 public List<OrderDto> getAllOrder(String p);
 //Get All Order
 public List<OrderDto>listAllOrder();
 public OrderDto getOrder(int OrderId);
 public void deleteOrder(int orderId);
 public OrderDto updateOrder(int orderId,OrderDto orderDto);

 

}
