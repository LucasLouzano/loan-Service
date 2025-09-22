package com.project.loan.api;

import com.project.loan.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerClient {

    @Autowired
    private RestTemplate restTemplate;

    public CustomerDTO buscarClientePorId(Long customerId) {
        try {
            String url = "http://localhost:8080/customer/" + customerId;
            ResponseEntity<CustomerDTO> response = restTemplate.getForEntity(url, CustomerDTO.class);
            return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (RestClientException e) {
            throw new RuntimeException("Erro ao consultar serviço de cliente", e);
        }
    }
}