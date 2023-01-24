package com.kezbek.transaction.feign;

import com.kezbek.transaction.model.request.PotentialCashbackRequest;
import com.kezbek.transaction.model.response.PotentialCashbackResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "http://DROOLS-SERVICE")
public interface KezbekDroolsClientService {
    @RequestMapping(value = "/cashback", method = RequestMethod.POST)
    PotentialCashbackResponse getCashback(@RequestBody PotentialCashbackRequest droolsCashback);
}
