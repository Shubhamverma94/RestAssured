package Demo;

import static io.restassured.RestAssured.given;

import java.util.Random;

import org.junit.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class SimpleBooks {

	public static String baseURL = "https://simple-books-api.glitch.me";
	// Generate random name and email
		public static   String[] names = {"John", "Mary", "Adam", "Emma", "Oliver", "Ava", "Sophia"};
		public static   String[] domains = {"example.com", "test.com", "example.org", "test.org"};
		public static  Random rand = new Random();
		public static  String name = names[rand.nextInt(names.length)];
		public static  String Updatename = names[rand.nextInt(names.length)];
		public static  String CusName = names[rand.nextInt(names.length)];
		public static   String email = name.toLowerCase() + "@" + domains[rand.nextInt(domains.length)];
		public static String token;
		public static String orderId;
	
	@Test(priority = 0)
	public void CreateBook() {
		
		String RequestBody = "{\r\n"
				+ "   \"clientName\": \""+name+"\",\r\n"
				+ "   \"clientEmail\": \""+email+"\"\r\n"
				+ "}";
		
		//this is for my BaseUrl
		RestAssured.baseURI=baseURL;
		          
		//Pre-Condition
		Response response = given()
		
		.header("Content-Type","application/json")
		.body(RequestBody)
		.when()
		.post("/api-clients/")
		
		.then()
		.assertThat().statusCode(201).contentType(ContentType.JSON)
		.extract().response();
		
		//get the response in string format 
		String jsonresponse = response.asString();
		System.out.println(jsonresponse);
		
		//to read in Json format
		JsonPath js = new JsonPath(jsonresponse);
//		System.out.println(js); //not able to print as in object json format
	
		//to fetch specific object
		//as we have to store token
		token = js.get("accessToken");
		System.out.println(token);
		
		System.out.println("Book CREATE successfully");
	}


	@Test(priority = 1)
	public void orderBook() {
	    String orderBody = "{\r\n"
	            + "   \"bookId\": \"01\",\r\n"
	            + "   \"customerName\": \""+CusName+"\"\r\n"
	            + "}";

	    // Set the base URL
	    RestAssured.baseURI = baseURL;

	    // Send POST request to order a book
	    Response response = given()
	            .header("Content-Type", "application/json")
	            .header("Authorization", "Bearer " + token)
	            .body(orderBody)
	            .when()
	            .post("/orders")
	            .then()
	            .assertThat()
	            .statusCode(201)
	            .contentType(ContentType.JSON)
	            .extract()
	            .response();

	    // Get the response body as a string
	    String jsonResponse = response.asString();
	    System.out.println(jsonResponse);

	    // Parse the response body as JSON
	    JsonPath js = new JsonPath(jsonResponse);

	    // Fetch the order ID
	    orderId = js.get("orderId");
	    System.out.println(orderId);

	    System.out.println("Customer name:- "+CusName);
	    System.out.println("Book ordered successfully");
	}

	@Test(priority = 2)
	public void updateBookName() {
		
		String jsonBody = "{\r\n"
				+ "  \"customerName\": \""+Updatename+"\"\r\n"
				+ "}" ;
		
		RestAssured.baseURI = baseURL ;
		
	    Response response = 	given()
		 .header("Content-Type" , "application/json" )
		 .header("Authorization", token)
		 .body(jsonBody)
		.when()
		 .patch("/orders/" + orderId)
		
		.then()
		 .assertThat()
		  .statusCode(204)
		.extract().response() ;
	
	String result = response.asPrettyString() ;
	System.out.println(result);
	System.out.println("updated Cus Name:- "+Updatename);
	System.out.println("Book Update Name Successfully");

	}	
	
	@Test(priority = 3)
	public void getBookDetails() {
		
		
		RestAssured.baseURI = baseURL ;
		
	    Response response = 	given()
		 .header("Content-Type" , "application/json" )
		 .header("Authorization", token)
		 
		.when()
		 .get("/orders/" + orderId)
		
		.then()
		 .assertThat()
		  .statusCode(200)
		  .contentType(ContentType.JSON) 
		.extract().response() ;
	
	String result = response.asPrettyString() ;
	System.out.println(result);
	
	JsonPath js = new JsonPath(result);
	String cname = js.get("customerName") ;
	Assert.assertEquals( cname,Updatename);
	System.out.println(cname);
	System.out.println("Book Get Successfully");
	}
	
	@Test(priority = 4)
	public void deleteBook() {
		
		
		RestAssured.baseURI = baseURL ;
		
	    Response response = 	given()
		 .header("Content-Type" , "application/json" )
		 .header("Authorization", token)
		 
		.when()
		 .delete("/orders/" + orderId)
		
		.then()
		 .assertThat()
		  .statusCode(204)
		.extract().response() ;
	
	String result = response.asPrettyString() ;
	System.out.println(result);
	System.out.println("Book Deleted Successfully");
	}

}
