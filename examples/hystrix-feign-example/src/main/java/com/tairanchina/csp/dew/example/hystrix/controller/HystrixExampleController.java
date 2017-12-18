package com.tairanchina.csp.dew.example.hystrix.controller;

import com.tairanchina.csp.dew.example.hystrix.HelloHystrixCommand;
import com.tairanchina.csp.dew.example.hystrix.client.ExampleClient2;
import com.tairanchina.csp.dew.example.hystrix.client.NullClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.HashMap;


@RestController
@RequestMapping("example/")
public class HystrixExampleController {

    private static final Logger logger = LoggerFactory.getLogger(HystrixExampleController.class);

    public static int s=0;

    @Autowired
    private ExampleClient2 exampleClient2;

    @Autowired
    private NullClient nullClient;

    @GetMapping("get-exe/ori")
    public ResponseEntity getExe() throws InterruptedException {
        logger.info("post-exe   " );
        for (int m =0;m<80000;m++){
            exampleClient2.deleteExe(1,"post");
            exampleClient2.postExe(m,"post");

            //调helloHystrixCommand
            HelloHystrixCommand serviceD = HelloHystrixCommand.getInstance("dew",5,5);
            serviceD.model = serviceD.new Model("run");
            logger.info("main:      " + serviceD.model + "thread id: " + Thread.currentThread().getId());
            System.out.println(serviceD.execute());
//            nullClient.getExe();
            Thread.sleep(100);
        }
        exampleClient2.deleteExe(1,"post");
        return ResponseEntity.ok().build();
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/exe")
    public String exe() {
        /*logger.info("Controller Token:" + Dew.context().getToken());
        return hystrixExampleService.getStores(new HashMap<String, Object>() {{
        }},Dew.context());*/
        for (int i=0;i<1000;i++){
            try{
                restTemplate.getForObject("http://localhost:8000/hystrix-feign-example/sss",String.class,new HashMap<>());
            }catch (Exception e){

            }
        }
        return restTemplate.getForObject("http://localhost:8000/hystrix-feign-example/sss",String.class,new HashMap<>());
    }


    @GetMapping("get-exe")
    public ResponseEntity getExe(@RequestParam("i") int i,@RequestParam("str") String str) throws InterruptedException {
        logger.info("get-exe   " + "i=" + i + "str=" + str);
        for (int m =0;m<80000;m++){
            exampleClient2.deleteExe(m,"delete");
            Thread.sleep(100);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("post-exe")
    public ResponseEntity postExe(@RequestParam("i") int i, @RequestParam("str") String str) throws InterruptedException {
        logger.info("post-exe   " + "i=" + i + "str=" + str);
        Thread.sleep(100000000);
        if (i>600){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(404).build();
    }

    @PutMapping("put-exe")
    public ResponseEntity putExe(@RequestParam("i") int i, @RequestParam("str") String str) {
        logger.info("put-exe   " + "i=" + i + "str=" + str);
        return ResponseEntity.ok().build();
    }
}
