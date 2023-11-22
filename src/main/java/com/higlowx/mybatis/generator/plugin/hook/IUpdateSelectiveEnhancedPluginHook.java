package com.higlowx.mybatis.generator.plugin.hook;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * .提供新的update方法添加列名可选项参数
 *
 * @author : wmq
 * @version : 2023/11/21
 **/
public interface IUpdateSelectiveEnhancedPluginHook extends IPluginHook {

     /**
      * u
      * @param method
      * @param interfaze
      * @param introspectedTable
      * @return
      */
     boolean clientUpdateByExampleSelectiveColumnMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable);

     /**
      * u
      * @param method
      * @param interfaze
      * @param introspectedTable
      * @return
      */
     boolean clientUpdateByPrimaryKeySelectiveColumnMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable);


     /**
      * i
      * @param document
      * @param element
      * @param introspectedTable
      * @return
      */
     boolean sqlMapUpdateByExampleSelectiveColumnElementGenerated(Document document, XmlElement element, IntrospectedTable introspectedTable);

     /**
      * u
      * @param document
      * @param element
      * @param introspectedTable
      * @return
      */
     boolean sqlMapUpdateByPrimaryKeySelectiveColumnElementGenerated(Document document, XmlElement element, IntrospectedTable introspectedTable);

}
