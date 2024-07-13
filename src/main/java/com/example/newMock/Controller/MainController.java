package com.example.newMock.Controller;

import com.example.newMock.Model.RequestDTO;
import com.example.newMock.Model.ResponceDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    public Object postBalances(@RequestBody RequestDTO requestDTO){
        try {
            String clientID = requestDTO.getClientId();
            char firstDigit = clientID.charAt(0);
            BigDecimal maxlimit;
            BigDecimal balance;
            String currency;
            String rqUID = requestDTO.getRqUID();

            if(firstDigit == '8') {
                maxlimit = new BigDecimal(2000.00);
                currency = "US";
            }
            else if (firstDigit == '9'){
                maxlimit = new BigDecimal(1000.00);
                currency = "EU";
            }
            else
            {
                maxlimit = new BigDecimal(10000.00);
                currency = "RU";
            }

            balance = getRandomBigDecimalInRange(new BigDecimal(0.00), maxlimit);

            ResponceDTO responceDTO = new ResponceDTO();

            responceDTO.setRqUID(rqUID);
            responceDTO.setClientId(clientID);
            responceDTO.setAccount(requestDTO.getAccount());
            responceDTO.setCurrency(currency);
            responceDTO.setBalance(balance);
            responceDTO.setMaxLimit(maxlimit);

            log.error("********** RequestDTO **********" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.error("********** ResponceDTO **********" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responceDTO));

            return responceDTO;

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public static BigDecimal getRandomBigDecimalInRange(BigDecimal min, BigDecimal max) {
        Random random = new Random();
        BigDecimal range = max.subtract(min);
        BigDecimal randomFactor = new BigDecimal(random.nextDouble());
        BigDecimal randomValue = min.add(range.multiply(randomFactor));
        return randomValue.setScale(2, RoundingMode.HALF_UP); // Устанавливаем точность до 2 знаков после запятой
    }
}
