/*
 * Copyright (c) 2018.
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

package com.higlowx.mybatis.generator.plugin;


import com.higlowx.mybatis.generator.plugin.tools.*;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.config.GeneratedKey;

import java.util.List;

/**
 * @author wmq
 */
@Deprecated
public class SelectiveEnhancedPlugin extends BasePlugin {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(List<String> warnings) {

        // 插件使用前提是使用了ModelColumnPlugin插件
        if (!PluginTools.checkDependencyPlugin(this.context, ModelColumnPlugin.class)) {
            warnings.add("higlowx:插件" + this.getClass().getTypeName() + "插件需配合" + ModelColumnPlugin.class.getTypeName() + "插件使用！");
            return false;
        }

        return super.validate(warnings);
    }

    /**
     * insertSelective 方法生成
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     * @param method
     * @param interfaze
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        method.getParameters().clear();

        FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();
        method.addParameter(new Parameter(parameterType, "record", "@Param(\"record\")"));

        // 找出全字段对应的Model
        FullyQualifiedJavaType fullFieldModel = introspectedTable.getRules().calculateAllFieldsClass();
        // column枚举
        FullyQualifiedJavaType selectiveType = new FullyQualifiedJavaType(fullFieldModel.getShortName() + "." + ModelColumnPlugin.ENUM_NAME);
        method.addParameter(new Parameter(selectiveType, "selective", "@Param(\"selective\")", true));

        FormatTools.replaceGeneralMethodComment(this.context.getCommentGenerator(), method, introspectedTable);

        return super.clientInsertSelectiveMethodGenerated(method, interfaze, introspectedTable);
    }

    /**
     * updateByExampleSelective
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     * @param method
     * @param interfaze
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientUpdateByExampleSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        method.getParameters().clear();

        FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();
        method.addParameter(new Parameter(parameterType, "record", "@Param(\"record\")"));

        FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        method.addParameter(new Parameter(exampleType, "example", "@Param(\"example\")"));

        // 找出全字段对应的Model
        FullyQualifiedJavaType fullFieldModel = introspectedTable.getRules().calculateAllFieldsClass();
        // column枚举
        FullyQualifiedJavaType selectiveType = new FullyQualifiedJavaType(fullFieldModel.getShortName() + "." + ModelColumnPlugin.ENUM_NAME);
        method.addParameter(new Parameter(selectiveType, "selective", "@Param(\"selective\")", true));

        FormatTools.replaceGeneralMethodComment(this.context.getCommentGenerator(), method, introspectedTable);
        return super.clientUpdateByExampleSelectiveMethodGenerated(method, interfaze, introspectedTable);
    }

    /**
     * updateByPrimaryKeySelective
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     * @param method
     * @param interfaze
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        method.getParameters().clear();

        FullyQualifiedJavaType parameterType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = new FullyQualifiedJavaType(introspectedTable.getRecordWithBLOBsType());
        } else {
            parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        }

        method.addParameter(new Parameter(parameterType, "record", "@Param(\"record\")"));

        // 找出全字段对应的Model
        FullyQualifiedJavaType fullFieldModel = introspectedTable.getRules().calculateAllFieldsClass();
        // column枚举
        FullyQualifiedJavaType selectiveType = new FullyQualifiedJavaType(fullFieldModel.getShortName() + "." + ModelColumnPlugin.ENUM_NAME);
        method.addParameter(new Parameter(selectiveType, "selective", "@Param(\"selective\")", true));

        FormatTools.replaceGeneralMethodComment(this.context.getCommentGenerator(), method, introspectedTable);

        return super.clientUpdateByPrimaryKeySelectiveMethodGenerated(method, interfaze, introspectedTable);
    }

    /**
     * insertSelective
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        // 清空
        XmlElement answer = new XmlElement("insert");
        answer.addAttribute(new Attribute("id", introspectedTable.getInsertSelectiveStatementId()));
        answer.addAttribute(new Attribute("parameterType", "map"));

        this.context.getCommentGenerator().addComment(answer);

        GeneratedKey gk = introspectedTable.getGeneratedKey().get();
        if (gk != null) {
            IntrospectedColumn introspectedColumn = introspectedTable.getColumn(gk.getColumn()).get();
            // if the column is null, then it's a configuration error. The
            // warning has already been reported
            if (introspectedColumn != null) {
                if (gk.isJdbcStandard()) {
                    XmlElementGeneratorTools.useGeneratedKeys(answer, introspectedTable, "record.");
                } else {
                    answer.addElement(XmlElementGeneratorTools.getSelectKey(introspectedColumn, gk, "record."));
                }
            }
        }

        StringBuilder sb = new StringBuilder();

        sb.append("insert into ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // columns
        answer.addElement(this.generateInsertColumnSelective(ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns())));
        // values
        answer.addElement(new TextElement("values"));
        answer.addElement(this.generateInsertValuesSelective(ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns())));

        XmlElementTools.replaceXmlElement(element, answer);

        return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
    }

    /**
     * updateByExampleSelective
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapUpdateByExampleSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        // 清空
        XmlElement answer = new XmlElement("update");
        answer.addAttribute(new Attribute("id", introspectedTable.getUpdateByExampleSelectiveStatementId()));
        answer.addAttribute(new Attribute("parameterType", "map"));

        this.context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // selective
        answer.addElement(new TextElement("SET"));
        answer.addElement(this.generateSetsSelective(ListUtilities.removeGeneratedAlwaysColumns(introspectedTable.getAllColumns())));

        answer.addElement(XmlElementGeneratorTools.getUpdateByExampleIncludeElement(introspectedTable));

        XmlElementTools.replaceXmlElement(element, answer);

        return super.sqlMapUpdateByExampleSelectiveElementGenerated(element, introspectedTable);
    }

    /**
     * updateByPrimaryKeySelective
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     * @param element
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        // 清空
        XmlElement answer = new XmlElement("update");
        answer.addAttribute(new Attribute("id", introspectedTable.getUpdateByPrimaryKeySelectiveStatementId()));
        answer.addAttribute(new Attribute("parameterType", "map"));

        this.context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("update ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // selective
        answer.addElement(new TextElement("SET"));
        answer.addElement(this.generateSetsSelective(ListUtilities.removeGeneratedAlwaysColumns(introspectedTable.getNonPrimaryKeyColumns())));

        XmlElementGeneratorTools.generateWhereByPrimaryKeyTo(answer, introspectedTable.getPrimaryKeyColumns(), "record.");

        XmlElementTools.replaceXmlElement(element, answer);
        return super.sqlMapUpdateByPrimaryKeySelectiveElementGenerated(element, introspectedTable);
    }



    // ====================================================== 一些私有节点生成方法 =========================================================

    /**
     * insert column selective
     * @param columns
     * @return
     */
    private XmlElement generateInsertColumnSelective(List<IntrospectedColumn> columns) {
        XmlElement insertColumnsChooseEle = new XmlElement("choose");

        XmlElement insertWhenEle = new XmlElement("when");
        insertWhenEle.addAttribute(new Attribute("test", "selective != null and selective.length > 0"));
        insertColumnsChooseEle.addElement(insertWhenEle);

        XmlElement insertForeachEle = new XmlElement("foreach");
        insertForeachEle.addAttribute(new Attribute("collection", "selective"));
        insertForeachEle.addAttribute(new Attribute("item", "column"));
        insertForeachEle.addAttribute(new Attribute("open", "("));
        insertForeachEle.addAttribute(new Attribute("separator", ","));
        insertForeachEle.addAttribute(new Attribute("close", ")"));
        insertForeachEle.addElement(new TextElement("${column.escapedColumnName}"));
        insertWhenEle.addElement(insertForeachEle);

        XmlElement insertOtherwiseEle = new XmlElement("otherwise");
        insertOtherwiseEle.addElement(XmlElementGeneratorTools.generateKeysSelective(columns, "record."));
        insertColumnsChooseEle.addElement(insertOtherwiseEle);

        XmlElement insertTrimElement = new XmlElement("trim");
        insertTrimElement.addAttribute(new Attribute("prefix", "("));
        insertTrimElement.addAttribute(new Attribute("suffix", ")"));
        insertTrimElement.addAttribute(new Attribute("suffixOverrides", ","));
        insertOtherwiseEle.addElement(insertTrimElement);

        return insertColumnsChooseEle;
    }

    /**
     * insert column selective
     * @param columns
     * @return
     */
    private XmlElement generateInsertValuesSelective(List<IntrospectedColumn> columns) {
        return generateInsertValuesSelective(columns, true);
    }

    /**
     * insert column selective
     * @param columns
     * @param bracket
     * @return
     */
    private XmlElement generateInsertValuesSelective(List<IntrospectedColumn> columns, boolean bracket) {
        XmlElement insertValuesChooseEle = new XmlElement("choose");

        XmlElement valuesWhenEle = new XmlElement("when");
        valuesWhenEle.addAttribute(new Attribute("test", "selective != null and selective.length > 0"));
        insertValuesChooseEle.addElement(valuesWhenEle);

        XmlElement valuesForeachEle = new XmlElement("foreach");
        valuesForeachEle.addAttribute(new Attribute("collection", "selective"));
        valuesForeachEle.addAttribute(new Attribute("item", "column"));
        valuesForeachEle.addAttribute(new Attribute("separator", ","));
        if (bracket) {
            valuesForeachEle.addAttribute(new Attribute("open", "("));
            valuesForeachEle.addAttribute(new Attribute("close", ")"));
        }
        valuesForeachEle.addElement(new TextElement("#{record.${column.javaProperty},jdbcType=${column.jdbcType}}"));
        valuesWhenEle.addElement(valuesForeachEle);

        XmlElement valuesOtherwiseEle = new XmlElement("otherwise");
        insertValuesChooseEle.addElement(valuesOtherwiseEle);
        valuesOtherwiseEle.addElement(XmlElementGeneratorTools.generateValuesSelective(columns, "record.", bracket));

        return insertValuesChooseEle;
    }

    /**
     * sets selective
     * @param columns
     * @return
     */
    private XmlElement generateSetsSelective(List<IntrospectedColumn> columns) {
        return generateSetsSelective(columns, null);
    }

    /**
     * sets selective
     * @param columns
     * @return
     */
    private XmlElement generateSetsSelective(List<IntrospectedColumn> columns, IntrospectedColumn versionColumn) {
        XmlElement setsChooseEle = new XmlElement("choose");

        XmlElement setWhenEle = new XmlElement("when");
        setWhenEle.addAttribute(new Attribute("test", "selective != null and selective.length > 0"));
        setsChooseEle.addElement(setWhenEle);

        XmlElement setForeachEle = new XmlElement("foreach");
        setWhenEle.addElement(setForeachEle);
        setForeachEle.addAttribute(new Attribute("collection", "selective"));
        setForeachEle.addAttribute(new Attribute("item", "column"));
        setForeachEle.addAttribute(new Attribute("separator", ","));

        // 3. 普通节点
        TextElement normalEle = new TextElement("${column.escapedColumnName} = #{record.${column.javaProperty},jdbcType=${column.jdbcType}}");
        setForeachEle.addElement(normalEle);
        // 普通Selective
        XmlElement setOtherwiseEle = new XmlElement("otherwise");
        setOtherwiseEle.addElement(XmlElementGeneratorTools.generateSetsSelective(columns, "record."));
        setsChooseEle.addElement(setOtherwiseEle);

        return setsChooseEle;
    }
}
