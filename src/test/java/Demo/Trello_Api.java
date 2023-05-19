package Demo;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class Trello_Api {

    //going to test all the end points of Trello
	//want to create base UrL bcz common to end points
	public static String baseURL = "https://api.trello.com";
   //making it public will make it usable to all other classes also
	public static String key = "1609a064e951b2190b65d10619c1a02f";
	public static String token = "ATTAa261450aafa07de33ed9121388a5969ddecc601c7b8de20a5600a30fab9086fa248BC2C7";
    public static String board_id;
	
	@Test(priority = 0)
	public void CreateBoard() {
		//Rest-Assured we work with below aspect
		//given: request-contains,body,headers-authorization-content-type,parameter,query parameter
		//when: resource /board(this will have only resource)
		//then: response- assertion - string format --JSONpath()
		
		System.out.println("Board CREATE Started");
		
		//this is for my BaseUrl
		RestAssured.baseURI=baseURL;
		
		//Pre-Condition
		
		Response response = given().queryParam("name", "Masai")
		.queryParam("key",key)
		.queryParam("token", token)
		.header("Content-Type","application/json")
		
		.when()
		.post("/1/boards")
		
		.then()
		.assertThat().statusCode(200).contentType(ContentType.JSON)
		.extract().response();
		
		//get the response in string format 
		String jsonresponse = response.asString();
		System.out.println(jsonresponse);
		
		//to read in Json format
		JsonPath js = new JsonPath(jsonresponse);
		System.out.println(js); //not able to print as in object json format
	
		//to fetch specific object
		//as we have to store id
		board_id = js.get("id");
		System.out.println(board_id);
		
		System.out.println("Board CREATE successfully");
	}

	//Try Get , Update("desc and name") and Delete
	 @Test(priority = 1)
	    public void UpdateBoard() {
		 
		 System.out.println("Board UPDATE Started");
		 
	        RestAssured.baseURI = baseURL;

	        Response response = given().queryParam("name", "Updated Masai Board")
	        		.queryParam("desc", "I am Learning this Update of Trello board by Rest Assured")
	                .queryParam("key", key)
	                .queryParam("token", token)
	                .header("Content-Type", "application/json")
	                .when()
	                .put("/1/boards/" + board_id)
	                .then()
	                .assertThat().statusCode(200).contentType(ContentType.JSON)
	                .extract().response();

	        String jsonresponse = response.asString();
	        System.out.println(jsonresponse);

	        JsonPath js = new JsonPath(jsonresponse);
	        System.out.println(js);
	        
	        System.out.println("Board UPDATE successfully");
	    }

	    @Test(priority = 2)
	    public void GetBoard() {
	    	
	    	System.out.println("Board GET Started");
	    	
	        RestAssured.baseURI = baseURL;

	        Response response = given().queryParam("key", key)
	                .queryParam("token", token)
	                .header("Content-Type", "application/json")
	                .when()
	                .get("/1/boards/" + board_id)
	                .then()
	                .assertThat().statusCode(200).contentType(ContentType.JSON)
	                .extract().response();

	        String jsonresponse = response.asString();
	        System.out.println(jsonresponse);

	        JsonPath js = new JsonPath(jsonresponse);
	        System.out.println(js);
	        
	        System.out.println("Board GET successfully");
	    }
	    
	    @Test(priority = 3)
	    public void DeleteBoard() {
	    	
	    	System.out.println("Board DELETE Started");
	    	
	        RestAssured.baseURI = baseURL;

	        Response response = given().queryParam("key", key)
	                .queryParam("token", token)
	                .header("Content-Type", "application/json")
	                .when()
	                .delete("/1/boards/" + board_id)
	                .then()
	                .assertThat().statusCode(200).contentType(ContentType.JSON)
	                .extract().response();

	        String jsonresponse = response.asString();
	        System.out.println(jsonresponse);

	        JsonPath js = new JsonPath(jsonresponse);
	        System.out.println(js);
	        
	        System.out.println("Board DELETE successfully");
	    }
	   
   
}
