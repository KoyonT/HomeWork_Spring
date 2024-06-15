package com.example.newMock.Controller;

import com.example.newMock.Model.RequestDTO;
import com.example.newMock.Model.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Random;


@RestController
public class MainController {
    private Logger log = LoggerFactory.getLogger(MainController.class);

    ObjectMapper mapper = new ObjectMapper();

    @PostMapping(
        value = "/info/postBalances",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Object postBalances(@RequestBody RequestDTO requestDTO) {
        try {
            String clientId = requestDTO.getClientId();
            char firstDigit = clientId.charAt(0);
            BigDecimal maxlimit;
            String RqUID = requestDTO.getRqUID();

            ResponseDTO responseDTO = new ResponseDTO();

            if(firstDigit == '8') {
                maxlimit = new BigDecimal(2000);
                responseDTO.setCurrency("US");
            } else if (firstDigit == '9') {
                maxlimit = new BigDecimal(1000);
                responseDTO.setCurrency("EU");
            } else {
                maxlimit = new BigDecimal(10000);
                responseDTO.setCurrency("RUB");
            }

            responseDTO.setRqUID(RqUID);
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(requestDTO.getAccount());

            Random r = new Random();
            int x = r.nextInt(0, maxlimit.intValue()) + 1;
            responseDTO.setBalance(new BigDecimal(x));
            responseDTO.setMaxLimit(maxlimit);

            log.error("******* RequestDTO *******" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.error("******* ResponseDTO *******" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));

            return responseDTO;

        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
