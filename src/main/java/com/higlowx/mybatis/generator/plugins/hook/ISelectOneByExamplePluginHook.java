package com.higlowx.mybatis.generator.plugins.hook;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;

public interface ISelectOneByExamplePluginHook extends IPluginHook {

    /**
     * selectOneByExampleWithBLOBs 接口方法生成
     *
     * @param method            method
     * @param interfaze         interfaze
     * @param introspectedTable introspectedTable
     * @return boolean
     */
    boolean clientSelectOneByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * selectOneByExample 接口方法生成
     *
     * @param method            method
     * @param interfaze         interfaze
     * @param introspectedTable introspectedTable
     * @return boolean
     */
    boolean clientSelectOneByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * selectOneByExample 方法sqlMap实现
     *
     * @param document          document
     * @param element           element
     * @param introspectedTable introspectedTable
     * @return boolean
     */
    boolean sqlMapSelectOneByExampleWithoutBLOBsElementGenerated(Document document, XmlElement element, IntrospectedTable introspectedTable);

    /**
     * selectOneByExampleWithBLOBs 方法sqlMap实现
     *
     * @param document          document
     * @param element           element
     * @param introspectedTable introspectedTable
     * @return boolean
     */
    boolean sqlMapSelectOneByExampleWithBLOBsElementGenerated(Document document, XmlElement element, IntrospectedTable introspectedTable);

}