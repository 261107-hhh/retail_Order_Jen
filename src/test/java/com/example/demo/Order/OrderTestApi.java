package com.example.demo.Order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.MethodOrderer;
//import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.Controller.OrderController;
import com.example.demo.Controller.PaymentController;
import com.example.demo.payload.ApiResponse;
import com.example.demo.payload.PaymentSuccessResponse;
import com.example.demo.payload.paymentOrderResponse;
import com.razorpay.RazorpayException;

import org.json.*;
import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;



@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class OrderTestApi {

	@Autowired
	PaymentController paymentController;

	@Autowired
	OrderController orderController;
	
	static int OrderId;
	@Test
	@Order(1)
	public void testCreateOrder()throws JSONException, RazorpayException {
//		Login user
		 String jsonBody="{ \"username\": \"himanshu.nainwal@stl.tech\", \"password\":\"123\"}";
	     String tokenn =  given()
	             .header("Content-type", "application/json").contentType(ContentType.JSON).accept(ContentType.JSON)
	             .body(jsonBody)
	             .when()
	             .post("http://localhost:9001/auth/login")
	             .then().assertThat().statusCode(equalTo(200)).extract().response().asString();
	     
//	     System.out.println("Hi there Token"+ tokenn);
	     
	     JSONObject json;
	     json = new JSONObject(tokenn);
	     
	             
//	      System.out.println(json.getString("token"));

//	     Add product to cart.
		  String addToCart = "{ \"productId\": \"6\", \"quantity\": \"1\"}";
		  String daata =  given()
				  .header("Authorization", "Bearer " + json.getString("token")).contentType(ContentType.JSON).accept(ContentType.JSON)
		             .body(addToCart)
		             .when()
		             .post("http://localhost:9002/cart/")
		             .then().assertThat().statusCode(equalTo(200)).extract().response().asString();
//		  System.out.println(daata);
		  JSONObject jsonData;
		     jsonData = new JSONObject(daata);
		     JSONArray j1 = new JSONArray(jsonData.get("iteam").toString());
		     JSONObject j2 = new JSONObject(j1.get(0).toString());
		     JSONObject j3 = new JSONObject(j2.get("product").toString());
//		     System.out.println("Datat :"+ jsonData.get("product"));
//		     System.out.println("Full Data"+jsonData);
//		     System.out.println("Iteam"+jsonData.get("iteam").toString());
//		     System.out.println("Iteam array"+j1);
//		     System.out.println("Iteam array"+j1.get(0));
//		     System.out.println("Product oBject"+j2);
//		     System.out.println("Product "+j2.getString("product"));
//		     System.out.println("Product object new "+j3);
//		     System.out.println("Product object new id"+j3.get("productId"));
		     
//		     Assure the product is added to cart/
		  assertEquals("6", j3.get("productId").toString());
		  
		  
//		  String orderDetail = "{ \"productId\": \"6\", \"quantity\": \"1\"}";
		
		  String orderDetail = "{\"address\": \"asdasdasdaasdasdasda\", \"cartID\": \"1\"}";
		  
//		  String orderDetail = "{\"cartId\": \"1\",\"iteam\":[{\"cartIteamId\":\"235\",\"quantity\":\"1\",\"totalproductprize\":\"121999.0\",\"product\":{\"productId\":\"6\",\"productName\":\"Iphone 12 pro\",\"productDesc\":\"<p>Iphone 12Pro 256GB</p>\",\"productPrize\":\"121999.0\",\"stock\":true,\"productQuantity\":\"12\",\"live\":true,\"imageName\":\"0adf68ba-fd53-4b13-935e-fcc7364b23ca.jpg\",\"category\":{\"categoryId\":\"2\",\"title\":\"Electronics\"}}}]}";
		
		  String order =  given()
				  .header("Authorization", "Bearer " + json.getString("token")).contentType(ContentType.JSON).accept(ContentType.JSON)
		             .body(orderDetail)
		             .when()
		             .post("http://localhost:9004/order/")
		             .then().assertThat().statusCode(equalTo(201)).extract().response().asString();
		    
		  JSONObject j4 = new JSONObject(order);
		  OrderId = Integer.parseInt(j4.getString("orderId").toString());
		  System.out.println("orferID :"+OrderId);
		  System.out.println(j4);
		  System.out.println(j4.get("orderStatus"));
		  assertThat("Created").isEqualTo(j4.get("orderStatus"));
		 
		  
//		  System.out.println(" do payment ");
//		  String payment =  given()
//				  .header("Authorization", "Bearer " + json.getString("token")).contentType(ContentType.JSON).accept(ContentType.JSON)
//		             .body(orderDetail)
//		             .when()
//		             .post("http://localhost:9004/payment/success")
//		             .then().assertThat().statusCode(equalTo(400)).extract().response().asString();
//		  System.out.println(payment);
		 
		  
		  PaymentSuccessResponse  psuccess=new PaymentSuccessResponse();
		  
		  
		  
		  double prize = (double) j4.get("orderAmout");
		  System.out.println(j4.get("orderAmout").toString());
		  try {
			  paymentOrderResponse res = paymentController.createOrder((int)prize/100);
			  assertThat(res.getMessage()).isEqualTo("CREATED");
//		  String OrderId = res.getOrderId();
			  System.out.println("Ordedr id :"+res.getOrderId());
			  PaymentSuccessResponse ress = paymentController.capturePayment("pay_LnvAdnnxOtNxgB",res.getOrderId(),
					  "fb9b3ad54d11f877c0aef5448ee04d50940b5d20429363efd444a02811115a71", OrderId);
			  System.out.println("Pay status"+ress.getPaymentStatus());
			  
//		  Verify that order is done and payment also
			  assertThat("PAID").isEqualTo(ress.getPaymentStatus());
			  
		  }catch (Exception e) {
			// TODO: handle exception
			  System.out.println("Exception called"+e);
		}
  }
  

	@Test
	@Order(2)
	void testDelete() {
		ResponseEntity<ApiResponse> res = orderController.delete(OrderId);
		assertEquals(HttpStatus.OK, res.getStatusCode());
//		assertEquals("Order Deleted", res.getStatusCode());
		System.out.println(res.getBody().getMessage());
		
//		 String product =  given()
//             .header("Content-type", "application/json").contentType(ContentType.JSON).accept(ContentType.JSON)
//             .delete("http://localhost:9004/order/"+OrderId)
//             .then().assertThat().statusCode(equalTo(200)).extract().response().asString();
//		 System.out.println(product);
	}
  
  
  
  
  
  

}