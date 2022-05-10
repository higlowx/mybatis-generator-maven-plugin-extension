package com.higlowx.mybatis.generator.plugin.hook;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;


public interface ISelectSelectivePluginHook extends IPluginHook {

    boolean sqlMapSelectByExampleSelectiveElementGenerated(Document document, XmlElement element, IntrospectedTable introspectedTable);
}
