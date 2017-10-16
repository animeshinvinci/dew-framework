package com.tairanchina.csp.dew.jdbc.test.ds;

import com.ecfront.dew.common.Page;
import com.tairanchina.csp.dew.Dew;
import com.tairanchina.csp.dew.jdbc.DewDS;
import com.tairanchina.csp.dew.jdbc.DewSB;
import com.tairanchina.csp.dew.jdbc.test.ds.entity.BasicEntity;
import com.tairanchina.csp.dew.jdbc.test.ds.entity.EmptyEntity;
import com.tairanchina.csp.dew.jdbc.test.ds.entity.FullEntity;
import com.tairanchina.csp.dew.jdbc.test.ds.util.TxService;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JDBCTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TxService txService;

    @Autowired
    @Qualifier("test2JdbcTemplate")
    private JdbcTemplate jdbcTemplate2;


    /**
     * 测试没有配置多数据库的情况
     * 在配置中注释掉multi-datasources:
     */
   /* @Test
    public void testNotDynamic(){
        int temp = ((DewDS)Dew.ds()).ds().queryForList("select * from basic_entity").size();
        Assert.assertTrue(temp == 1);
    }*/
    private void init() {
        ((DewDS)Dew.ds()).jdbc().execute("CREATE TABLE basic_entity\n" +
                "(\n" +
                "id int primary key auto_increment,\n" +
                "field_a varchar(255)\n" +
                ")");
        ((DewDS)Dew.ds()).jdbc().execute("CREATE TABLE full_entity\n" +
                "(\n" +
                "id int primary key auto_increment,\n" +
                "code varchar(255),\n" +
                "field_a varchar(255),\n" +
                "field_c varchar(255) not null,\n" +
                "create_user varchar(255) not null,\n" +
                "create_time datetime,\n" +
                "update_user varchar(255) not null,\n" +
                "update_time datetime,\n" +
                "enabled bool\n" +
                ")");
    }

    public void testAll() throws Exception {
        testEntity();
        testMultiDS();
        testTx();
        multiplyData();
    }

    private void testEntity() throws InterruptedException {
        // 没有Entity注解的类，异常
        try {
            ((DewDS)Dew.ds()).findAll(EmptyEntity.class);
            Assert.assertTrue(false);
        } catch (Throwable e) {
            Assert.assertTrue(true);
        }
        // =========== Basic Test
        init();
        // findAll
        Assert.assertEquals(0, ((DewDS)Dew.ds()).findAll(BasicEntity.class).size());
        // insert
        BasicEntity basicEntity = new BasicEntity();
        basicEntity.setFieldA("测试A");
        basicEntity.setFieldB("测试B");
        Object id = ((DewDS)Dew.ds()).insert(basicEntity);
        // getById
        Assert.assertEquals("测试A", ((DewDS)Dew.ds()).getById(id, BasicEntity.class).getFieldA());
        // updateById
        basicEntity.setFieldA("测试C");
        ((DewDS)Dew.ds()).updateById(id, basicEntity);
        Assert.assertEquals("测试C", ((DewDS)Dew.ds()).getById(id, BasicEntity.class).getFieldA());
        // findAll
        Assert.assertEquals(1, ((DewDS)Dew.ds()).findAll(BasicEntity.class).size());
        try {
            ((DewDS)Dew.ds()).findEnabled(BasicEntity.class);
            Assert.assertTrue(false);
        } catch (Throwable e) {
            Assert.assertTrue(true);
        }
        // =========== Full Test
        FullEntity fullEntity = new FullEntity();
        fullEntity.setFieldA("测试A");
        // insert
        try {
            ((DewDS)Dew.ds()).insert(fullEntity);
            Assert.assertTrue(false);
        } catch (Throwable e) {
            Assert.assertTrue(true);
        }
        fullEntity.setFieldB("测试B");
        id = ((DewDS)Dew.ds()).insert(fullEntity);
        // getById
        fullEntity = ((DewDS)Dew.ds()).getById("11", FullEntity.class);
        Assert.assertNull(fullEntity);
        fullEntity = ((DewDS)Dew.ds()).getById(id, FullEntity.class);
        Assert.assertTrue(!fullEntity.getCode().isEmpty());
        Assert.assertEquals("测试A", fullEntity.getFieldA());
        Assert.assertEquals("测试B", fullEntity.getFieldB());
        // getByCode
        fullEntity = ((DewDS)Dew.ds()).getByCode(fullEntity.getCode(), FullEntity.class);
        Assert.assertEquals("", fullEntity.getCreateUser());
        Assert.assertEquals("", fullEntity.getUpdateUser());
        Assert.assertTrue(fullEntity.getCreateTime() != null);
        Assert.assertEquals(fullEntity.getCreateTime(), fullEntity.getUpdateTime());
        // updateById
        fullEntity.setFieldA("测试C");
        ((DewDS)Dew.ds()).updateById(id, fullEntity);
        Assert.assertEquals("测试C", ((DewDS)Dew.ds()).getById(id, FullEntity.class).getFieldA());
        // updateByCode
        fullEntity.setFieldA(null);
        fullEntity.setFieldB("测试D");
        // null不更新
        Thread.sleep(1000);
        ((DewDS)Dew.ds()).updateByCode(fullEntity.getCode(), fullEntity);
        fullEntity = ((DewDS)Dew.ds()).getById(id, FullEntity.class);
        Assert.assertEquals("测试C", fullEntity.getFieldA());
        Assert.assertEquals("测试D", fullEntity.getFieldB());
        Assert.assertNotEquals(fullEntity.getCreateTime(), fullEntity.getUpdateTime());
        Assert.assertEquals(true, fullEntity.getEnabled());
        // disableById
        ((DewDS)Dew.ds()).disableById(fullEntity.getId(), FullEntity.class);
        Assert.assertEquals(false, ((DewDS)Dew.ds()).getById(fullEntity.getId(), FullEntity.class).getEnabled());
        // enableById
        ((DewDS)Dew.ds()).enableById(fullEntity.getId(), FullEntity.class);
        Assert.assertEquals(true, ((DewDS)Dew.ds()).getById(fullEntity.getId(), FullEntity.class).getEnabled());
        // disableByCode
        ((DewDS)Dew.ds()).disableByCode(fullEntity.getCode(), FullEntity.class);
        Assert.assertEquals(false, ((DewDS)Dew.ds()).getById(fullEntity.getId(), FullEntity.class).getEnabled());
        // enableByCode
        ((DewDS)Dew.ds()).enableByCode(fullEntity.getCode(), FullEntity.class);
        Assert.assertEquals(true, ((DewDS)Dew.ds()).getById(fullEntity.getId(), FullEntity.class).getEnabled());
        // existById
        Assert.assertEquals(true, ((DewDS)Dew.ds()).existById(fullEntity.getId(), FullEntity.class));
        Assert.assertEquals(false, ((DewDS)Dew.ds()).existById(11111, FullEntity.class));
        // existByCode
        Assert.assertEquals(true, ((DewDS)Dew.ds()).existByCode(fullEntity.getCode(), FullEntity.class));
        Assert.assertEquals(false, ((DewDS)Dew.ds()).existByCode("11111", FullEntity.class));
        // findAll
        Assert.assertEquals(1, ((DewDS)Dew.ds()).findAll(FullEntity.class).size());
        Assert.assertEquals("测试C", ((DewDS)Dew.ds()).findAll(FullEntity.class).get(0).getFieldA());
        // findEnabled
        Assert.assertEquals(1, ((DewDS)Dew.ds()).findEnabled(FullEntity.class).size());
        // findDisabled
        Assert.assertEquals(0, ((DewDS)Dew.ds()).findDisabled(FullEntity.class).size());
        // countAll
        Assert.assertEquals(1, ((DewDS)Dew.ds()).countAll(FullEntity.class));
        // countEnabled
        Assert.assertEquals(1, ((DewDS)Dew.ds()).countEnabled(FullEntity.class));
        // countDisabled
        Assert.assertEquals(0, ((DewDS)Dew.ds()).countDisabled(FullEntity.class));
        // insert
        FullEntity fullEntity2 = new FullEntity();
        fullEntity2.setFieldA("测试A2");
        fullEntity2.setFieldB("测试B2");
        FullEntity fullEntity3 = new FullEntity();
        fullEntity3.setFieldA("测试A3");
        fullEntity3.setFieldB("测试B3");
        ((DewDS)Dew.ds()).insert(new ArrayList<FullEntity>() {{
            add(fullEntity2);
            add(fullEntity3);
        }});
        Assert.assertEquals(3, ((DewDS)Dew.ds()).countAll(FullEntity.class));
        // paging
        Page<FullEntity> fullEntities = ((DewDS)Dew.ds()).paging(1, 2, FullEntity.class);
        Assert.assertEquals(3, fullEntities.getRecordTotal());
        Assert.assertEquals(2, fullEntities.getPageSize());
        Assert.assertEquals(1, fullEntities.getPageNumber());
        Assert.assertEquals(2, fullEntities.getPageTotal());
        Assert.assertEquals(2, fullEntities.getObjects().size());
        // pagingEnabled
        ((DewDS)Dew.ds()).disableById(fullEntity.getId(), FullEntity.class);
        fullEntities = ((DewDS)Dew.ds()).pagingEnabled(1, 2, FullEntity.class);
        Assert.assertEquals(2, fullEntities.getRecordTotal());
        // pagingDisabled
        fullEntities = ((DewDS)Dew.ds()).pagingDisabled(1, 2, FullEntity.class);
        Assert.assertEquals(1, fullEntities.getRecordTotal());
        // Sql Builder
        fullEntities = ((DewDS)Dew.ds()).paging(
                DewSB.inst()
                        .eq("fieldA", "测试A2")
                        .like("fieldB", "%B2")
                        .notNull("code")
                        .desc("createTime"),
                1, 2, FullEntity.class);
        Assert.assertEquals(1, fullEntities.getRecordTotal());
        // deleteById
        ((DewDS)Dew.ds()).deleteById(fullEntity.getId(), FullEntity.class);
        // deleteByCode
        ((DewDS)Dew.ds()).deleteByCode(((DewDS)Dew.ds()).findAll(FullEntity.class).get(0).getCode(), FullEntity.class);
        Assert.assertEquals(1, ((DewDS)Dew.ds()).findAll(FullEntity.class).size());
        // selectForList
        Map<String, Object> params = new HashMap<>();
        params.put("code", "1");
        ((DewDS)Dew.ds()).selectForList(fullEntity.getClass(), params,
                "select f.*, b.* from full_entity f LEFT JOIN basic_entity b ON f.field_a = b.field_a where f.code = #{code}");
    }

    private void testTx() {
        txService.testCommit();
        try {
            txService.testRollBack();
        } catch (Exception ignored) {

        }
        txService.testMultiCommit();
        try {
            txService.testMultiRollBack();
        } catch (Exception ignored) {

        }
        int res = ((DewDS)Dew.ds()).jdbc().queryForList("select * from basic_entity where field_a = 'TransactionA1'").size();
        Assert.assertTrue(res > 0);
        res = ((DewDS)Dew.ds()).jdbc().queryForList("select * from basic_entity where field_a = 'TransactionA2'").size();
        Assert.assertTrue(res == 0);
        res = ((DewDS)Dew.ds("test2")).jdbc().queryForList("select * from basic_entity where field_a = 'TransactionA1'").size();
        Assert.assertTrue(res > 0);
        res = ((DewDS)Dew.ds("test2")).jdbc().queryForList("select * from basic_entity where field_a = 'TransactionA2'").size();
        Assert.assertTrue(res == 0);
    }

    private void testMultiDS() {
        ((DewDS)Dew.ds()).jdbc().queryForList("select * from basic_entity").size();
        try {
            ((DewDS)Dew.ds("test1")).jdbc().queryForList("select * from basic_entity").size();
            Assert.assertFalse(1 == 1);
        } catch (Exception e) {
            Assert.assertTrue(1 == 1);
        }
        ((DewDS)Dew.ds("test2")).jdbc().execute("DROP TABLE if EXISTS basic_entity");
        ((DewDS)Dew.ds("test2")).jdbc().execute("CREATE TABLE basic_entity\n" +
                "(\n" +
                "id int primary key ,\n" +
                "field_a varchar(255)\n" +
                ")");
        Assert.assertEquals(0, ((DewDS)Dew.ds("test2")).jdbc().queryForList("select * from basic_entity").size());

        // 测试spring 直接注入jdbcTemplate的情况，是否生效
        int temp = jdbcTemplate.queryForList("select * from basic_entity").size();
        Assert.assertTrue(temp == 1);
        temp = jdbcTemplate2.queryForList("select * from basic_entity").size();
        Assert.assertTrue(temp == 0);
    }


    private void multiplyData() {
        multiplyInit();
        testPool();
        testPoolA();
        destory();
    }

    private void destory() {
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("test1");
        list.add("test2");
        for (String data : list) {
            ((DewDS)Dew.ds(data)).jdbc().execute("DROP TABLE if EXISTS test_select_entity");
            ((DewDS)Dew.ds(data)).jdbc().execute("DROP TABLE if EXISTS basic_entity");
        }
    }

    private void multiplyInit() {
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("test1");
        list.add("test2");
        for (String data : list) {
            ((DewDS)Dew.ds(data)).jdbc().execute("DROP TABLE if EXISTS test_select_entity");
            String sql = "CREATE TABLE IF NOT EXISTS test_select_entity\n" +
                    "(\n" +
                    "id int primary key auto_increment,\n" +
                    "code varchar(32),\n" +
                    "field_a varchar(255),\n" +
                    "field_c varchar(255) not null,\n" +
                    "create_user varchar(32) not null,\n" +
                    "create_time datetime,\n" +
                    "update_user varchar(32) not null,\n" +
                    "update_time datetime,\n" +
                    "enabled bool\n" +
                    ")";
            if (data.equals("test2")) {
                ((DewDS)Dew.ds(data)).jdbc().execute(sql.replaceAll("datetime", "timestamp").replaceFirst("auto_increment", ""));
                ((DewDS)Dew.ds(data)).jdbc().execute("INSERT  INTO  test_select_entity " +
                        "(id,code,field_a,field_c,create_user,create_time,update_user,update_time,enabled) VALUES " +
                        "(1,'A','A-a','A-b','ding',NOW(),'ding',NOW(),TRUE )");
            } else {
                ((DewDS)Dew.ds(data)).jdbc().execute(sql);
                ((DewDS)Dew.ds(data)).jdbc().execute("INSERT  INTO  test_select_entity " +
                        "(code,field_a,field_c,create_user,create_time,update_user,update_time,enabled) VALUES " +
                        "('A','A-a','A-b','ding',NOW(),'ding',NOW(),TRUE )");
            }

        }

    }

    @Transactional
    public void testPool() {
        Boolean[] hasFinish = {false};
        ((DewDS)Dew.ds()).jdbc().queryForList("select * from test_select_entity").size();
        new Thread(() -> {
            ((DewDS)Dew.ds()).jdbc().queryForList("select * from test_select_entity").size();
            Assert.assertTrue(hasFinish[0]);
        }).start();
        try {
            Thread.sleep(1);
            hasFinish[0] = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Transactional("test2TransactionManager")
    public void testPoolA() {
        Boolean[] hasFinish = {false};
        ((DewDS)Dew.ds("test2")).jdbc().queryForList("select * from test_select_entity").size();
        new Thread(() -> {
            ((DewDS)Dew.ds("test2")).jdbc().queryForList("select * from test_select_entity").size();
            Assert.assertTrue(hasFinish[0]);
        }).start();
        try {
            Thread.sleep(1);
            hasFinish[0] = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
