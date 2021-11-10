package com.example.jsonparsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.jsonparsing.classes.Address;
import com.example.jsonparsing.classes.Customer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JsonparsingApplication implements CommandLineRunner {

	// Source https://attacomsian.com/blog/processing-json-spring-boot

    public static void main(String... args) {
        SpringApplication.run(JsonparsingApplication.class, args);
    }

    @Override
    public void run(String... args) throws IOException {

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

		// [JsonString to Object]
		// String
		String jsonString = "{\n  \"id\": 123,\n  \"name\": \"Jovan Lee\",\n  \"email\": \"jovan@example.com\",\n  \"phone\": \"+49 176 14890478\",\n  \"age\": 32,\n  \"projects\": [\n    \"Remote Job Board\",\n    \"Data Migration\"\n  ],\n  \"address\": {\n    \"street\": \"Yorckstr. 75\",\n    \"city\": \"Berlin\",\n    \"zipcode\": 10965,\n    \"country\": \"Germany\"\n  },\n  \"paymentMethods\": [\n    \"PayPal\",\n    \"Stripe\"\n  ],\n  \"profileInfo\": {\n    \"gender\": \"Male\",\n    \"married\": \"Yes\",\n    \"source\": \"LinkedIn\"\n  }\n}";

        //read json file and convert to customer object
        Customer customer = objectMapper.readValue(jsonString, Customer.class);

        //print customer details
        System.out.println(customer);


		// [Object to JsonString on JsonFile]
		//create a customer object
		Customer customerObj = new Customer();
		customerObj.setId(567L);
		customerObj.setName("Maria Kovosi");
		customerObj.setEmail("maria@example.com");
		customerObj.setPhone("+12 785 4895 321");
		customerObj.setAge(29);
		customerObj.setProjects(new String[]{"Path Finder App", "Push Notifications"});

		Address address = new Address();
		address.setStreet("Karchstr.");
		address.setCity("Hanover");
		address.setZipcode(66525);
		address.setCountry("Germany");
		customerObj.setAddress(address);

		List<String> paymentMethods = new ArrayList<>();
		paymentMethods.add("PayPal");
		paymentMethods.add("SOFORT");
		customerObj.setPaymentMethods(paymentMethods);

		Map<String, Object> info = new HashMap<>();
		info.put("gender", "female");
		info.put("married", "No");
		info.put("income", "120,000 EURO");
		info.put("source", "Google Search");
		customerObj.setProfileInfo(info);

		//configure objectMapper for pretty input
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

		//write customerObj object to customer2.json file
		objectMapper.writeValue(new File("customer2.json"), customerObj);
		

		// [JsonFile to Map]
		//convert json file to map
		Map<?, ?> map = objectMapper.readValue(new FileInputStream("customer.json"), Map.class);

		//iterate over map entries and print to console
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "=" + entry.getValue());
		}		


		// [JsonFile to Tree]
		//read customer.json file into tree model
		JsonNode rootNode = objectMapper.readTree(new File("customer.json"));

		//read name and phone nodes
		System.out.println("Customer Name: " + rootNode.path("name").asText());
		System.out.println("Customer Phone: " + rootNode.path("phone").asText());
		System.out.println("Customer Age: " + rootNode.path("age").asInt());
		System.out.println("Customer City: " + rootNode.path("address").path("city").asText());
		System.out.println("Customer Project: " + rootNode.path("projects").get(0).asText());
    }
}