package com.tairanchina.csp.dew.core.controller;


import com.tairanchina.csp.dew.core.entity.IdEntity;
import com.tairanchina.csp.dew.core.service.CRUDService;

public interface CRUDController<T extends CRUDService, E extends IdEntity> extends CRUDVOController<T, E, E> {

}

