package com.tradedemo.application;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TradeServiceApplicationTests {


	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;


	private String validRequest = "date,product_id,currency,price\n" +
			"20160101,1,EUR,10.0" ;

	private String invalidDateRequest = "date,product_id,currency,price\n" +
			"20161313,1,EUR,10.0" ;

	private String invalidDateRequest2 = "date,product_id,currency,price\n" +
			"20160101,1,EUR,10.0\n" +
			"20161313,1,EUR,10.0" ;

	private static HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type","text/csv");
		headers.set("Accept","text/csv");
		return headers;
	}

	@Test
	public void testSuccessMessage() throws Exception {

		HttpEntity<String> httpEntity = new HttpEntity<>(validRequest, getHttpHeaders());
		assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/api/v1/enrich",httpEntity,String.class)).contains("20160101,Treasury Bills Domestic,EUR,10.0");
	}

	@Test
	public void testInvalidDateMessage() throws Exception {
		HttpEntity<String> httpEntity = new HttpEntity<>(invalidDateRequest, getHttpHeaders());
		assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/api/v1/enrich",httpEntity,String.class)).contains("");
	}

	@Test
	public void testInvalidDateMessage2() throws Exception {
		HttpEntity<String> httpEntity = new HttpEntity<>(invalidDateRequest2, getHttpHeaders());
		assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/api/v1/enrich",httpEntity,String.class)).contains("20160101,Treasury Bills Domestic,EUR,10.0").doesNotContain("20161313,Treasury Bills Domestic,EUR,10.0");
	}

	@Test
	public void testInvalidHeaders(){
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type","text/xml");
		headers.set("Accept","text/xml");
		HttpEntity<String> httpEntity = new HttpEntity<>(validRequest, headers);
		this.restTemplate.postForObject("http://localhost:" + port + "/api/v1/enrich",httpEntity , String.class);


	}

}
