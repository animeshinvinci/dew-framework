package com.tairanchina.csp.dew.core.test.web;

import com.ecfront.dew.common.$;
import com.tairanchina.csp.dew.core.test.web.controller.TestController;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class WebTest {

    private String URL = "http://127.0.0.1:8080/test/";

    private Logger logger = LoggerFactory.getLogger(WebTest.class);

    public void testAll() throws Exception {
        testValidation();
        testResponseFormat();
    }

    public void testSwagger() throws IOException {
        String result = $.http.get("http://127.0.0.1:8080/swagger-ui.html");
        logger.info(result);
    }


    private void testValidation() throws Exception {
        // group:create
        TestController.User user = new TestController.User();
        user.setAge(12);
        user.setIdCard("331023199507123150");
        user.setPhone("1597199704");
        String createResult = $.http.post(URL + "valid-create", user);
        Assert.assertEquals("400", $.json.toJson(createResult).get("code").asText());
        // group:update
        Map<String, Object> map = new HashMap<>();
        map.put("age", 12);
        map.put("idCard", "331023199507123150");
        map.put("phone", "1597199704");
        String updateResult = $.http.put(URL + "valid-update", map);
        Assert.assertEquals("400", $.json.toJson(updateResult).get("code").asText());
        String pathResult = $.http.get(URL + "valid-method/1");
        Assert.assertEquals("500", $.json.toJson(pathResult).get("code").asText());
        String authResult = $.http.get(URL + "error-mapping");
        logger.info(authResult);
    }

    private void testResponseFormat() throws Exception {
        String tResult = $.http.get(URL + "t?q=TEST");
        logger.info(tResult);
        String t2Result = $.http.get(URL + "t2?q=TEST");
        logger.info(t2Result);
        String t3Result = $.http.get(URL + "t3?q=TEST");
        logger.info(t3Result);
        String t4Result = $.http.get(URL + "t4?q=TEST");
        logger.info(t4Result);
    }

}
