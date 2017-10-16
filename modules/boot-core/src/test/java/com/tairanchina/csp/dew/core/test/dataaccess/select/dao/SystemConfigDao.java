package com.tairanchina.csp.dew.core.test.dataaccess.select.dao;

import com.tairanchina.csp.dew.jdbc.DewDao;
import com.tairanchina.csp.dew.jdbc.annotations.Param;
import com.tairanchina.csp.dew.jdbc.annotations.Select;
import com.tairanchina.csp.dew.core.test.dataaccess.select.dto.ModelDTO;
import com.tairanchina.csp.dew.core.test.dataaccess.select.entity.SystemConfig;



public interface SystemConfigDao extends DewDao<String ,SystemConfig> {

    @Select(value = "SELECT s.*,t.*" +
            "FROM system_config s " +
            "INNER JOIN test_select_entity t ON s.create_time = t.create_time " +
            "WHERE s.value = #{value}",entityClass = ModelDTO.class)
    ModelDTO testLink(@Param("value") String value);
}
