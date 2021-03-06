/*
 * Copyright 2022 Higlowx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.higlowx.mybatis.generator.plugin.hook;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;


/**
 * SelectOneByExamplePluginHook
 *
 * @author Dylan.Li
 */
public interface ISelectOneByExamplePluginHook extends IPluginHook {

    /**
     * java client selectOneByExampleWithBLOBs
     *
     * @param method            method
     * @param interfaze         interfaze
     * @param introspectedTable introspectedTable
     * @return boolean
     */
    boolean clientSelectOneByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * java client selectOneByExample
     *
     * @param method            method
     * @param interfaze         interfaze
     * @param introspectedTable introspectedTable
     * @return boolean
     */
    boolean clientSelectOneByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable);

    /**
     * sql mapper selectOneByExample
     *
     * @param document          document
     * @param element           element
     * @param introspectedTable introspectedTable
     * @return boolean
     */
    boolean sqlMapSelectOneByExampleWithoutBLOBsElementGenerated(Document document, XmlElement element, IntrospectedTable introspectedTable);

    /**
     * sql mapper selectOneByExampleWithBLOBs
     *
     * @param document          document
     * @param element           element
     * @param introspectedTable introspectedTable
     * @return boolean
     */
    boolean sqlMapSelectOneByExampleWithBLOBsElementGenerated(Document document, XmlElement element, IntrospectedTable introspectedTable);

}