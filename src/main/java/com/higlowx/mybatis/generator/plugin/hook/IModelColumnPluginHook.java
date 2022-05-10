package com.higlowx.mybatis.generator.plugin.hook;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public interface IModelColumnPluginHook extends IPluginHook {

    boolean modelColumnEnumGenerated(InnerEnum innerEnum, TopLevelClass topLevelClass, IntrospectedTable introspectedTable);
}
