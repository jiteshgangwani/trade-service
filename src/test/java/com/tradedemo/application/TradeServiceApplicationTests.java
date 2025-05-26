package com.tradedemo.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TradeServiceApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private static final String VALID_CSV = "date,product_id,currency,price\n20160101,1,EUR,10.0";
	private static final String INVALID_DATE_CSV = "date,product_id,currency,price\n20161313,1,EUR,10.0";
	private static final String MIXED_DATES_CSV = "date,product_id,currency,price\n20160101,1,EUR,10.0\n20161313,1,EUR,10.0";

	private static final String VALID_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<trades>" +
			"<trade>" +
			"<date>20160101</date>" +
			"<product_id>1</product_id>" +
			"<currency>EUR</currency>" +
			"<price>10.0</price>" +
			"</trade>" +
			"</trades>";

	private static final String INVALID_DATE_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<trades>" +
			"<trade>" +
			"<date>20161313</date>" +
			"<product_id>1</product_id>" +
			"<currency>EUR</currency>" +
			"<price>10.0</price>" +
			"</trade>" +
			"</trades>";

	private static final String MIXED_DATES_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<trades>" +
			"<trade>" +
			"<date>20160101</date>" +
			"<product_id>1</product_id>" +
			"<currency>EUR</currency>" +
			"<price>10.0</price>" +
			"</trade>" +
			"<trade>" +
			"<date>20161313</date>" +
			"<product_id>1</product_id>" +
			"<currency>EUR</currency>" +
			"<price>10.0</price>" +
			"</trade>" +
			"</trades>";

	private static final String EXPECTED_VALID_RESPONSE = "date,product_name,currency,price\n20160101,Treasury Bills Domestic,EUR,10.0\n";
	private static final String EXPECTED_INVALID_DATE_RESPONSE = "date,product_name,currency,price\n";
	private static final String EXPECTED_MIXED_DATES_RESPONSE = "date,product_name,currency,price\n20160101,Treasury Bills Domestic,EUR,10.0\n";

	@Test
	void testValidCsvInput() {
		HttpEntity<String> request = new HttpEntity<>(VALID_CSV, createHeaders(MediaType.TEXT_PLAIN_VALUE));
		ResponseEntity<String> response = restTemplate.postForEntity(createUrl(), request, String.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertEquals(EXPECTED_VALID_RESPONSE, response.getBody());
	}

	@Test
	void testInvalidDateCsvInput() {
		HttpEntity<String> request = new HttpEntity<>(INVALID_DATE_CSV, createHeaders(MediaType.TEXT_PLAIN_VALUE));
		ResponseEntity<String> response = restTemplate.postForEntity(createUrl(), request, String.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertEquals(EXPECTED_INVALID_DATE_RESPONSE, response.getBody());
	}

	@Test
	void testMixedDatesCsvInput() {
		HttpEntity<String> request = new HttpEntity<>(MIXED_DATES_CSV, createHeaders(MediaType.TEXT_PLAIN_VALUE));
		ResponseEntity<String> response = restTemplate.postForEntity(createUrl(), request, String.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertEquals(EXPECTED_MIXED_DATES_RESPONSE, response.getBody());
	}

	@Test
	void testValidXmlInput() {
		HttpEntity<String> request = new HttpEntity<>(VALID_XML, createHeaders(MediaType.APPLICATION_XML_VALUE));
		ResponseEntity<String> response = restTemplate.postForEntity(createUrl(), request, String.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertEquals(EXPECTED_VALID_RESPONSE, response.getBody());
	}

	@Test
	void testInvalidDateXmlInput() {
		HttpEntity<String> request = new HttpEntity<>(INVALID_DATE_XML, createHeaders(MediaType.APPLICATION_XML_VALUE));
		ResponseEntity<String> response = restTemplate.postForEntity(createUrl(), request, String.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertEquals(EXPECTED_INVALID_DATE_RESPONSE, response.getBody());
	}

	@Test
	void testMixedDatesXmlInput() {
		HttpEntity<String> request = new HttpEntity<>(MIXED_DATES_XML, createHeaders(MediaType.APPLICATION_XML_VALUE));
		ResponseEntity<String> response = restTemplate.postForEntity(createUrl(), request, String.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertEquals(EXPECTED_MIXED_DATES_RESPONSE, response.getBody());
	}

	@Test
	void testUnsupportedContentType() {
		HttpEntity<String> request = new HttpEntity<>(VALID_CSV, createHeaders(MediaType.APPLICATION_JSON_VALUE));
		ResponseEntity<String> response = restTemplate.postForEntity(createUrl(), request, String.class);
		
		assertEquals(415, response.getStatusCode().value());
	}

	private HttpHeaders createHeaders(String contentType) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(contentType));
		return headers;
	}

	private String createUrl() {
		return "http://localhost:" + port + "/api/v1/enrich";
	}
}
