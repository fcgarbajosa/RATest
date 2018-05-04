package service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author Fidel C. Garbajosa
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc

public class ResultsFromToControllerTest {
	
	private static final Logger log = LoggerFactory.getLogger(ResultsFromToControllerTest.class);
	TestRestTemplate testRestTemplate  = new TestRestTemplate();
	
	@LocalServerPort
	private int port;
	
    @Test
    public void testCase() throws Exception {
    	
    	Boolean ok = false;
        Map<String, String> params = new HashMap<>();
        
        String targetUrl= "http://localhost:" + port + "/interconnections?departure={departure}&arrival={arrival}&departureDateTime={departureDateTime}&arrivalDateTime={arrivalDateTime}";
        params.put("departure", "DUB");
        params.put("arrival","WRO");
        params.put("departureDateTime","2018-06-04T18:00");
        params.put("arrivalDateTime","2018-06-06T22:00");
        String response = testRestTemplate.getForObject(targetUrl, String.class, params);
        
        String jsonOKCase1 = "[{\"stops\":0,\"legs\":[{\"departureAirport\":\"DUB\",\"arrivalAirport\":\"WRO\",\"departureDateTime\":\"2018-06-06T17:50\",\"arrivalDateTime\":\"2018-06-06T21:25\"}]},{\"stops\":1,\"legs\":[{\"departureAirport\":\"DUB\",\"arrivalAirport\":\"BLQ\",\"departureDateTime\":\"2018-06-05T19:25\",\"arrivalDateTime\":\"2018-06-05T23:10\"},{\"departureAirport\":\"BLQ\",\"arrivalAirport\":\"WRO\",\"departureDateTime\":\"2018-06-06T12:15\",\"arrivalDateTime\":\"2018-06-06T14:00\"},{\"departureAirport\":\"DUB\",\"arrivalAirport\":\"GRO\",\"departureDateTime\":\"2018-06-05T07:10\",\"arrivalDateTime\":\"2018-06-05T10:40\"},{\"departureAirport\":\"GRO\",\"arrivalAirport\":\"WRO\",\"departureDateTime\":\"2018-06-06T18:25\",\"arrivalDateTime\":\"2018-06-06T21:00\"}]}]";
        String jsonOKCase2 = "[{\"stops\":0,\"legs\":[{\"departureAirport\":\"DUB\",\"arrivalAirport\":\"WRO\",\"departureDateTime\":\"2018-06-06T17:50\",\"arrivalDateTime\":\"2018-06-06T21:25\"}]},{\"stops\":1,\"legs\":[{\"departureAirport\":\"DUB\",\"arrivalAirport\":\"GRO\",\"departureDateTime\":\"2018-06-05T07:10\",\"arrivalDateTime\":\"2018-06-05T10:40\"},{\"departureAirport\":\"GRO\",\"arrivalAirport\":\"WRO\",\"departureDateTime\":\"2018-06-06T18:25\",\"arrivalDateTime\":\"2018-06-06T21:00\"},{\"departureAirport\":\"DUB\",\"arrivalAirport\":\"BLQ\",\"departureDateTime\":\"2018-06-05T19:25\",\"arrivalDateTime\":\"2018-06-05T23:10\"},{\"departureAirport\":\"BLQ\",\"arrivalAirport\":\"WRO\",\"departureDateTime\":\"2018-06-06T12:15\",\"arrivalDateTime\":\"2018-06-06T14:00\"}]}]";
  
        if (response.equals(jsonOKCase1)) {
           log.info("Json Case 1");
           ok = true;		
        }
        
        if (response.equals(jsonOKCase2)){
            log.info("Json Case 2");
            ok = true;		
        }
        
        assertTrue(ok);
    }
}
