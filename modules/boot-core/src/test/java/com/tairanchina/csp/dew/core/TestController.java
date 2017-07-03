package com.tairanchina.csp.dew.core;

import com.ecfront.dew.common.Resp;
import com.ecfront.dew.common.StandardCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Api(value = "测试", description = "test")
@RequestMapping(value = "/test/")
public class TestController {

    @GetMapping(value = "t")
    @ApiOperation(value = "fun1")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "q", value = "query", paramType = "query", dataType = "string", required = true),
    })
    public Resp<String> t1(@RequestParam String q) {
        return Resp.success("successful");
    }

    @GetMapping(value = "t2")
    @ApiOperation(value = "fun2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "q", value = "query", paramType = "query", dataType = "string", required = true),
    })
    public Resp<String> t2(@RequestParam String q) {
        return Resp.badRequest("badrequest");
    }

    @GetMapping(value = "t3")
    @ApiOperation(value = "fun3")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "q", value = "query", paramType = "query", dataType = "string", required = true),
    })
    public Resp<String> t3(@RequestParam String q) throws Exception {
        throw Dew.e("A000", new Exception("io error"));
    }

    @GetMapping(value = "t4")
    @ApiOperation(value = "fun4")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "q", value = "query", paramType = "query", dataType = "string", required = true),
    })
    public Resp<String> t4(@RequestParam String q) throws IOException {
        throw Dew.e("A000", new IOException("io error"), StandardCode.UNAUTHORIZED);
    }

}
