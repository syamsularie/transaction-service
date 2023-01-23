package com.kezbek.transaction.external;

import com.kezbek.transaction.entity.StatusTransaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class TopUpEmoney {

    public Boolean topup(String phone, BigDecimal cashback){
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl
                = "https://jsonplaceholder.typicode.com/todos/1";
        ResponseEntity<Object> response
                = restTemplate.getForEntity(resourceUrl , Object.class);
        if (response.getStatusCode().equals(HttpStatus.OK)){
            return true;
        }
        return false;
    }
}
