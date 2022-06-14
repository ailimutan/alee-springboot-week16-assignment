package com.promineotech.jeep.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import static org.assertj.core.api.Assertions.assertThat;

import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Order;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {
    "classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
    "classpath:flyway/migrations/V1.1__Jeep_Data.sql"}, 
    config = @SqlConfig(encoding = "utf-8"))

public class CreateOrderTest {

  @Autowired
  private TestRestTemplate restTemplate;
  
  @LocalServerPort
  private int serverPort;

@Test
void testCreateOrderReturnsSuccess201() {
    String body = createOrderBody();
    String uri = String.format("http://localhost:%d/orders", serverPort);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    HttpEntity<String> bodyEntity = new HttpEntity<>(body, headers);
    
    ResponseEntity<Order> response = restTemplate.exchange(uri,
            HttpMethod.POST, bodyEntity, Order.class);
    
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    
    
    Order order = response.getBody();
    
    assertThat(order.getCustomer().getCustomerId()).isEqualTo("SCOLA_ARISTAIOS");
    assertThat(order.getModel().getModelId()).isEqualTo(JeepModel.GRAND_CHEROKEE);
    assertThat(order.getModel().getTrimLevel()).isEqualTo("Limited");
    assertThat(order.getModel().getNumDoors()).isEqualTo(4);
    assertThat(order.getColor().getColorId()).isEqualTo("EXT_DIAMOND_BLACK");
    assertThat(order.getEngine().getEngineId()).isEqualTo("2_0_HYBRID");
    assertThat(order.getTire().getTireId()).isEqualTo("35_NITTO");
    assertThat(order.getOptions()).hasSize(5);

}

public String createOrderBody() {
  
  //@formatter: off
  return "{\r\n"
          + "  \"customer\":\"SCOLA_ARISTAIOS\",\r\n"
          + "  \"model\":\"GRAND_CHEROKEE\",\r\n"
          + "  \"trim\":\"Limited\",\r\n"
          + "  \"doors\":4,\r\n"
          + "  \"color\":\"EXT_DIAMOND_BLACK\",\r\n"
          + "  \"engine\":\"2_0_HYBRID\",\r\n"
          + "  \"tire\":\"35_NITTO\",\r\n"
          + "  \"options\":[\r\n"
          + "    \"DOOR_SMITTY_FRONT\",\r\n"
          + "    \"EXT_MOPAR_STEP_BLACK\",\r\n"
          + "    \"EXT_DUAL_UPPER_PREMIUM\",\r\n"
          + "    \"EXT_WARN_BUMPER_FRONT\",\r\n"
          + "    \"EXT_WARN_BUMPER_REAR\",\r\n"
          + "    \"DOOR_SMITTY_FRONT\"\r\n"
          + "  ]\r\n"
          + "}";
  //@formatter: on
}
  
}
