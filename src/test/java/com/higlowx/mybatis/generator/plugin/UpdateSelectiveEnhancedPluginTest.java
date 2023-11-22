package com.higlowx.mybatis.generator.plugin;

import com.higlowx.mybatis.generator.plugin.tools.*;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLException;

/**
 * .
 *
 * @author : wmq
 * @version : 2023/11/21
 **/
public class UpdateSelectiveEnhancedPluginTest {

    /**
     * 初始化数据库
     */
    @BeforeEach
    public  void init() throws SQLException, IOException, ClassNotFoundException {
        DBHelper.createDB("scripts/UpdateSelectiveEnhancedPlugin/init.sql");
    }

    /**
     * 测试配置异常
     */
    @Test
    public void testWarnings() throws IOException, XMLParserException, InvalidConfigurationException, InterruptedException, SQLException {
        // 1. 没有配置ModelColumnPlugin插件
        MyBatisGeneratorTool tool = MyBatisGeneratorTool.create("scripts/UpdateSelectiveEnhancedPlugin/mybatis-generator-without-ModelColumnPlugin.xml");
        tool.generate();
        Assertions.assertEquals(tool.getWarnings().get(0), "higlowx:插件com.higlowx.mybatis.generator.plugin.UpdateSelectiveEnhancedPlugin" +
                "插件需配合com.higlowx.mybatis.generator.plugin.ModelEnumPlugin插件使用！");
    }


    /**
     * 测试 updateByExampleSelective
     */
    @Test
    public void testUpdateByExampleSelective() throws Exception {
        MyBatisGeneratorTool tool = MyBatisGeneratorTool.create("scripts/UpdateSelectiveEnhancedPlugin/mybatis-generator.xml");
        tool.generate(new AbstractShellCallback() {
            @Override
            public void reloadProject(SqlSession sqlSession, ClassLoader loader, String packagz) throws Exception {
                ObjectUtil tbMapper = new ObjectUtil(sqlSession.getMapper(loader.loadClass(packagz + ".TbMapper")));

                ObjectUtil TbExample = new ObjectUtil(loader, packagz + ".TbExample");
                ObjectUtil criteria = new ObjectUtil(TbExample.invoke("createCriteria"));
                criteria.invoke("andIdEqualTo", 1l);

                ObjectUtil tb = new ObjectUtil(loader, packagz + ".Tb");
                tb.set("incF3", 10l);
                tb.set("tsIncF2", 5l);
                // selective
                ObjectUtil TbEnumField1 = new ObjectUtil(loader, packagz + ".TbEnum#field1");
                ObjectUtil TbEnumTsIncF2 = new ObjectUtil(loader, packagz + ".TbEnum#tsIncF2");
                Object columns = Array.newInstance(TbEnumField1.getCls(), 2);
                Array.set(columns, 0, TbEnumField1.getObject());
                Array.set(columns, 1, TbEnumTsIncF2.getObject());

                // sql
                String sql = SqlHelper.getFormatMapperSql(tbMapper.getObject(), "updateByExampleSelectiveColumn", tb.getObject(), TbExample.getObject(), columns);
                Assertions.assertEquals(sql, "update tb SET field_1 = 'null' , inc_f2 = 5 WHERE ( id = '1' )");
                Object result = tbMapper.invoke("updateByExampleSelectiveColumn", tb.getObject(), TbExample.getObject(), columns);
                Assertions.assertEquals(result, 1);
            }
        });
    }

    /**
     * 测试 updateByPrimaryKeySelective
     */
    @Test
    public void testUpdateByPrimaryKeySelective() throws Exception {
        MyBatisGeneratorTool tool = MyBatisGeneratorTool.create("scripts/UpdateSelectiveEnhancedPlugin/mybatis-generator.xml");
        tool.generate(new AbstractShellCallback() {
            @Override
            public void reloadProject(SqlSession sqlSession, ClassLoader loader, String packagz) throws Exception {
                ObjectUtil tbMapper = new ObjectUtil(sqlSession.getMapper(loader.loadClass(packagz + ".TbMapper")));

                ObjectUtil tb = new ObjectUtil(loader, packagz + ".Tb");
                tb.set("id", 2l);
                tb.set("incF3", 10l);
                tb.set("tsIncF2", 5l);
                // selective
                ObjectUtil TbEnumField1 = new ObjectUtil(loader, packagz + ".TbEnum#field1");
                ObjectUtil TbEnumTsIncF2 = new ObjectUtil(loader, packagz + ".TbEnum#tsIncF2");
                Object columns = Array.newInstance(TbEnumField1.getCls(), 2);
                Array.set(columns, 0, TbEnumField1.getObject());
                Array.set(columns, 1, TbEnumTsIncF2.getObject());

                // sql
                String sql = SqlHelper.getFormatMapperSql(tbMapper.getObject(), "updateByPrimaryKeySelectiveColumn", tb.getObject(), columns);
                Assertions.assertEquals(sql, "update tb SET field_1 = 'null' , inc_f2 = 5 where id = 2");
                Object result = tbMapper.invoke("updateByPrimaryKeySelectiveColumn", tb.getObject(), columns);
                Assertions.assertEquals(result, 1);
            }
        });
    }
}
