package com.tairanchina.csp.dew.core.controller;


import com.ecfront.dew.common.Page;
import com.ecfront.dew.common.Resp;
import com.tairanchina.csp.dew.core.service.CRUSService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

public interface CRUSVOController<T extends CRUSService, V, E> extends CRUVOController<T, V, E> {

    @GetMapping(value = "", params = {"enabled"})
    @ApiOperation(value = "根据状态获取记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "enabled", value = "状态", paramType = "query", dataType = "boolean"),
    })
    default Resp<List<V>> findByStatus(@RequestParam(required = false) Boolean enabled) {
        Resp<List<E>> result;
        if (enabled == null) {
            result = getDewService().find();
        } else if (enabled) {
            result = getDewService().findEnabled();
        } else {
            result = getDewService().findDisabled();
        }
        if (result.ok()) {
            List<V> body = result.getBody().stream().map(this::entityToVO).collect(Collectors.toList());
            return Resp.success(body);
        } else {
            return Resp.customFail(result.getCode(), result.getMessage());
        }
    }

    @GetMapping(value = "{pageNumber}/{pageSize}", params = {"enabled"})
    @ApiOperation(value = "根据状态获取记录分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "当前页（从0开始）", paramType = "path", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页显示记录数", paramType = "path", dataType = "int", required = true),
            @ApiImplicitParam(name = "enabled", value = "状态", paramType = "query", dataType = "boolean"),
    })
    default Resp<Page<V>> pagingByStatus(@PathVariable int pageNumber, @PathVariable int pageSize, @RequestParam(required = false) Boolean enabled) {
        Resp<Page<E>> result;
        if (enabled == null) {
            result = getDewService().paging(pageNumber, pageSize);
        } else if (enabled) {
            result = getDewService().pagingEnabled(pageNumber, pageSize);
        } else {
            result = getDewService().pagingDisabled(pageNumber, pageSize);
        }
        if (result.ok()) {
            List<V> body = result.getBody().getObjects().stream().map(this::entityToVO).collect(Collectors.toList());
            Page<V> page = Page.build(pageNumber, pageSize, result.getBody().getRecordTotal(), body);
            return Resp.success(page);
        } else {
            return Resp.customFail(result.getCode(), result.getMessage());
        }
    }


    @PutMapping("{id}/enable")
    @ApiOperation(value = "根据ID启用记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "记录ID", paramType = "path", dataType = "int", required = true),
    })
    default Resp<Void> enableById(@PathVariable long id) {
        return getDewService().enableById(id);
    }

    @DeleteMapping("{id}/disable")
    @ApiOperation(value = "根据ID禁用记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "记录ID", paramType = "path", dataType = "int", required = true),
    })
    default Resp<Void> disableById(@PathVariable long id) {
        return getDewService().disableById(id);
    }

    @PutMapping("code/{code}/enable")
    @ApiOperation(value = "根据Code启用记录", notes = "记录实体必须存在@Code注解")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "记录Code", paramType = "path", dataType = "string", required = true),
    })
    default Resp<Void> enableByCode(@PathVariable String code) {
        return getDewService().enableByCode(code);
    }

    @DeleteMapping("code/{code}/disable")
    @ApiOperation(value = "根据Code禁用记录", notes = "记录实体必须存在@Code注解")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "记录Code", paramType = "path", dataType = "string", required = true),
    })
    default Resp<Void> disableByCode(@PathVariable String code) {
        return getDewService().disableByCode(code);
    }


}

