package testcase;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;

public class TestGET {
	
	// Reusable response check
	ResponseSpecification checkStatusCodeAndContentType = 
		    new ResponseSpecBuilder().
		        expectStatusCode(200).
		        expectContentType(ContentType.JSON).log(LogDetail.PARAMS).
		        build();
	
	@Before
	public void before() {
		RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
	}
	
	public void testExtractResponse(int id) {
		Response response =
			given()
				.contentType(ContentType.JSON)
			.when()
				.get("/todos/"+id)
			.then()
			.spec(checkStatusCodeAndContentType)
				.extract().response();

        assertEquals(200, response.statusCode());
        assertEquals(1, response.getBody().jsonPath().getInt("userId"));
	}
	
	@Test
	public void testExtractPath() {
		String company =
			given()
				.contentType(ContentType.JSON)
			.when()
				.get("/users/1")
			.then()
				.extract().path("company.name");

        
        assertEquals("Romaguera-Crona", company);
	}
	
	@Test
	public void testExtractTypedJsonPath() {
		JsonPath json =
			given()
				.contentType(ContentType.JSON)
			.when()
				.get("/users/2")
			.then()
				.extract().jsonPath();

        
        assertEquals(2, json.getInt("id"));
	}
	
	@Test
	public void testAssertInline() {
		given()
			.contentType(ContentType.JSON)
		.when()
			.get("/todos/1")
		.then()
			.assertThat()
				.contentType(ContentType.JSON)
			.and()
				.statusCode(is(200))
			.and()
				.body("userId", is(1));
	}
	
	@Test
	public void testBodyContainsString() {
		given()
			.contentType(ContentType.JSON)
		.when()
			.get("/todos/1/comments")
		.then()
			.statusCode(is(200))
			.body(containsString("laudantium"));
	}
	
	@Test
	public void testAssertPath() {
		given()
			.contentType(ContentType.JSON)
		.when()
			.get("/users/1")
		.then()
			.statusCode(is(200))
			.body("address.city", is("Gwenborough"))
			.log().all();	
	}
	
	@Test
	public void testQueryParam() {
		given()
			.contentType(ContentType.JSON)
			.queryParam("postId","1")
		.when()
			.get("/comments")
		.then()
			.statusCode(is(200))
			.log().body();
	}
	
	@Test
	public void testExtractHeaders() {
	   Headers headers = get("/photos/10").then().extract().headers();
	   
	   assertEquals("cloudflare", headers.get("Server").getValue());
	}
	
	@Test
	public void testResponseTime() {
	  Long time = get("/albums/1").timeIn(TimeUnit.MILLISECONDS);
	  assertThat(time, is(lessThan(5000L)));
	}
	
	@Test
	public void testCountItems() {
	  when()
	  	.get("/comments?postId=1")
	  .then()
	  	.body("findAll.size()", is(5));
	  
	}
	
	@Test
	public void testCountFilteredItems() {
	  when()
	  	.get("/comments?postId=1")
	  .then()
	  	.body("findAll { it.id < 4 }", hasSize(3));
	  
	}
	
	@Test
	public void testNameFilteredItems() {
	  when()
	  	.get("/comments?postId=1")
	  .then()
	  	.body("findAll { it.id < 3 }.name", 
	  			hasItems("quo vero reiciendis velit similique earum",
	  					"id labore ex et quam laborum"));
	  
	}
	
	@Test
	public void testSumIdsFilteredItems() {
	  when()
	  	.get("/comments?postId=1")
	  .then()
	  	.assertThat()
	  	.body("findAll { it.id <= 4 }.id.sum()", is(1+2+3+4));
	  
	}
	
	@Test
	public void testGetItemByIndex() {
		given()
			.queryParam("postId","1")
		.expect()
			.statusCode(200)
			.body("[0].id", is(1))
		.when()
	  		.get("comments");	  		
	  
	}
	
	@Test
	public void testPathParameter() {
	  when()
	  	.get("/posts/{postId}", 1)
	  .then()
	  	.body("id", is(1));
	  
	}
	
	
	@Test
	public void testNamedPathParameter() {
	 given().
        pathParam("postId", "1")
	  .when()
	  	.get("/posts/{postId}")
	  .then()
	  	.body("id", is(1));
	  
	}
	
}
