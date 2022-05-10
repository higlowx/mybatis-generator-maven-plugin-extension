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
package com.higlowx.mybatis.generator.plugin;

import com.higlowx.mybatis.generator.plugin.hook.IModelColumnPluginHook;
import com.higlowx.mybatis.generator.plugin.tools.BasePlugin;
import com.higlowx.mybatis.generator.plugin.tools.FormatTools;
import com.higlowx.mybatis.generator.plugin.tools.JavaElementGeneratorTools;
import com.higlowx.mybatis.generator.plugin.tools.PluginTools;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.internal.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ModelColumnPlugin
 *
 * @author Dylan.Li
 */
public class ModelColumnPlugin extends BasePlugin {

    private static final Logger logger = LoggerFactory.getLogger(ModelColumnPlugin.class);
    /**
     * 内部Enum名
     */
    public static final String ENUM_NAME = "Column";

    /**
     * 自定义方法
     */
    public static final String METHOD_EXCLUDES = "excludes";
    public static final String METHOD_ALL = "all";
    public static final String METHOD_GET_ESCAPED_COLUMN_NAME = "getEscapedColumnName";
    public static final String METHOD_GET_ALIASED_ESCAPED_COLUMN_NAME = "getAliasedEscapedColumnName";

    public static final String CONST_BEGINNING_DELIMITER = "BEGINNING_DELIMITER";
    public static final String CONST_ENDING_DELIMITER = "ENDING_DELIMITER";

    /**
     * Model Methods 生成
     * 具体执行顺序 http://www.mybatis.org/generator/reference/pluggingIn.html
     */

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        InnerEnum innerEnum = this.generateColumnEnum(topLevelClass, introspectedTable);
        topLevelClass.addInnerEnum(innerEnum);
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addInnerEnum(this.generateColumnEnum(topLevelClass, introspectedTable));
        return super.modelRecordWithBLOBsClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addInnerEnum(this.generateColumnEnum(topLevelClass, introspectedTable));
        return super.modelPrimaryKeyClassGenerated(topLevelClass, introspectedTable);
    }

    /**
     * 生成Column字段枚举
     */
    private InnerEnum generateColumnEnum(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        InnerEnum innerEnum = new InnerEnum(new FullyQualifiedJavaType(ENUM_NAME));
        innerEnum.setVisibility(JavaVisibility.PUBLIC);
        logger.debug(topLevelClass.getType().getShortName() + "增加内部 Column 类");

        // 生成常量
        Field beginningDelimiterField = JavaElementGeneratorTools.generateField(
                CONST_BEGINNING_DELIMITER,
                JavaVisibility.PRIVATE,
                FullyQualifiedJavaType.getStringInstance(),
                "\"" + StringUtility.escapeStringForJava(context.getBeginningDelimiter()) + "\""
        );
        beginningDelimiterField.setStatic(true);
        beginningDelimiterField.setFinal(true);
        innerEnum.addField(beginningDelimiterField);

        Field endingDelimiterField = JavaElementGeneratorTools.generateField(
                CONST_ENDING_DELIMITER,
                JavaVisibility.PRIVATE,
                FullyQualifiedJavaType.getStringInstance(),
                "\"" + StringUtility.escapeStringForJava(context.getEndingDelimiter()) + "\""
        );
        endingDelimiterField.setStatic(true);
        endingDelimiterField.setFinal(true);
        innerEnum.addField(endingDelimiterField);

        // 生成属性和构造函数
        Field columnField = new Field("column", FullyQualifiedJavaType.getStringInstance());
        columnField.setVisibility(JavaVisibility.PRIVATE);
        columnField.setFinal(true);
        innerEnum.addField(columnField);

        Field isColumnNameDelimitedField = new Field("isColumnNameDelimited", FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        isColumnNameDelimitedField.setVisibility(JavaVisibility.PRIVATE);
        isColumnNameDelimitedField.setFinal(true);
        innerEnum.addField(isColumnNameDelimitedField);

        Field javaPropertyField = new Field("javaProperty", FullyQualifiedJavaType.getStringInstance());
        javaPropertyField.setVisibility(JavaVisibility.PRIVATE);
        javaPropertyField.setFinal(true);
        innerEnum.addField(javaPropertyField);

        Field jdbcTypeField = new Field("jdbcType", FullyQualifiedJavaType.getStringInstance());
        jdbcTypeField.setVisibility(JavaVisibility.PRIVATE);
        jdbcTypeField.setFinal(true);
        innerEnum.addField(jdbcTypeField);

        Method mValue = new Method("value");
        mValue.setVisibility(JavaVisibility.PUBLIC);
        mValue.setReturnType(FullyQualifiedJavaType.getStringInstance());
        mValue.addBodyLine("return this.column;");
        FormatTools.addMethodWithBestPosition(innerEnum, mValue);

        Method mGetValue = new Method("getValue");
        mGetValue.setVisibility(JavaVisibility.PUBLIC);
        mGetValue.setReturnType(FullyQualifiedJavaType.getStringInstance());
        mGetValue.addBodyLine("return this.column;");
        FormatTools.addMethodWithBestPosition(innerEnum, mGetValue);

        Method mGetJavaProperty = JavaElementGeneratorTools.generateGetterMethod(javaPropertyField);
        FormatTools.addMethodWithBestPosition(innerEnum, mGetJavaProperty);

        Method mGetJdbcType = JavaElementGeneratorTools.generateGetterMethod(jdbcTypeField);
        FormatTools.addMethodWithBestPosition(innerEnum, mGetJdbcType);

        Method constructor = new Method(ENUM_NAME);
        constructor.setConstructor(true);
        constructor.addBodyLine("this.column = column;");
        constructor.addBodyLine("this.javaProperty = javaProperty;");
        constructor.addBodyLine("this.jdbcType = jdbcType;");
        constructor.addBodyLine("this.isColumnNameDelimited = isColumnNameDelimited;");
        constructor.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "column"));
        constructor.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "javaProperty"));
        constructor.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "jdbcType"));
        constructor.addParameter(new Parameter(FullyQualifiedJavaType.getBooleanPrimitiveInstance(), "isColumnNameDelimited"));
        FormatTools.addMethodWithBestPosition(innerEnum, constructor);
        logger.debug(topLevelClass.getType().getShortName() + ".Column 增加 构造方法 和 column 属性");

        // Enum枚举
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            Field field = JavaBeansUtil.getJavaBeansField(introspectedColumn, context, introspectedTable);

            StringBuffer sb = new StringBuffer();
            sb.append(field.getName());
            sb.append("(\"");
            sb.append(introspectedColumn.getActualColumnName());
            sb.append("\", \"");
            sb.append(introspectedColumn.getJavaProperty());
            sb.append("\", \"");
            sb.append(introspectedColumn.getJdbcTypeName());
            sb.append("\", ");
            sb.append(introspectedColumn.isColumnNameDelimited());
            sb.append(")");

            innerEnum.addEnumConstant(sb.toString());
            logger.debug(topLevelClass.getType().getShortName() + ".Column 增加 " + field.getName() + " 枚举");
        }

        // asc 和 desc 方法
        Method desc = new Method("desc");
        desc.setVisibility(JavaVisibility.PUBLIC);
        desc.setReturnType(FullyQualifiedJavaType.getStringInstance());
        desc.addBodyLine("return this." + METHOD_GET_ESCAPED_COLUMN_NAME + "() + \" DESC\";");
        FormatTools.addMethodWithBestPosition(innerEnum, desc);

        Method asc = new Method("asc");
        asc.setVisibility(JavaVisibility.PUBLIC);
        asc.setReturnType(FullyQualifiedJavaType.getStringInstance());
        asc.addBodyLine("return this." + METHOD_GET_ESCAPED_COLUMN_NAME + "() + \" ASC\";");
        FormatTools.addMethodWithBestPosition(innerEnum, asc);
        logger.debug(topLevelClass.getType().getShortName() + ".Column 增加 asc() 和 desc() 方法");

        // excludes
        topLevelClass.addImportedType("java.util.Arrays");
        topLevelClass.addImportedType(FullyQualifiedJavaType.getNewArrayListInstance());
        Method mExcludes = JavaElementGeneratorTools.generateMethod(
                METHOD_EXCLUDES,
                JavaVisibility.PUBLIC,
                new FullyQualifiedJavaType(ENUM_NAME + "[]"),
                new Parameter(innerEnum.getType(), "excludes", true)
        );
        mExcludes.setStatic(true);
        JavaElementGeneratorTools.generateMethodBody(
                mExcludes,
                "ArrayList<Column> columns = new ArrayList<>(Arrays.asList(Column.values()));",
                "if (excludes != null && excludes.length > 0) {",
                "columns.removeAll(new ArrayList<>(Arrays.asList(excludes)));",
                "}",
                "return columns.toArray(new Column[]{});"
        );
        FormatTools.addMethodWithBestPosition(innerEnum, mExcludes);
        logger.debug(topLevelClass.getType().getShortName() + ".Column 增加 excludes() 方法");

        // all
        Method mAll = JavaElementGeneratorTools.generateMethod(
                METHOD_ALL,
                JavaVisibility.PUBLIC,
                new FullyQualifiedJavaType(ENUM_NAME + "[]")
        );
        mAll.setStatic(true);
        mAll.addBodyLine("return Column.values();");
        FormatTools.addMethodWithBestPosition(innerEnum, mAll);
        logger.debug(topLevelClass.getType().getShortName() + ".Column 增加 all() 方法。");

        // getEscapedColumnName
        Method mGetEscapedColumnName = JavaElementGeneratorTools.generateMethod(
                METHOD_GET_ESCAPED_COLUMN_NAME,
                JavaVisibility.PUBLIC,
                FullyQualifiedJavaType.getStringInstance()
        );
        JavaElementGeneratorTools.generateMethodBody(
                mGetEscapedColumnName,
                "if (this.isColumnNameDelimited) {",
                "return new StringBuilder().append(" + CONST_BEGINNING_DELIMITER + ").append(this.column).append(" + CONST_ENDING_DELIMITER + ").toString();",
                "} else {",
                "return this.column;",
                "}"
        );
        FormatTools.addMethodWithBestPosition(innerEnum, mGetEscapedColumnName);
        logger.debug(topLevelClass.getType().getShortName() + ".Column 增加 getEscapedColumnName() 方法");

        // getAliasedEscapedColumnName
        Method mGetAliasedEscapedColumnName = JavaElementGeneratorTools.generateMethod(
                METHOD_GET_ALIASED_ESCAPED_COLUMN_NAME,
                JavaVisibility.PUBLIC,
                FullyQualifiedJavaType.getStringInstance()
        );
        if (StringUtility.stringHasValue(introspectedTable.getTableConfiguration().getAlias())) {
            String alias = introspectedTable.getTableConfiguration().getAlias();
            mGetAliasedEscapedColumnName.addBodyLine("StringBuilder sb = new StringBuilder();");
            mGetAliasedEscapedColumnName.addBodyLine("sb.append(\"" + alias + ".\");");
            mGetAliasedEscapedColumnName.addBodyLine("sb.append(this." + METHOD_GET_ESCAPED_COLUMN_NAME + "());");
            mGetAliasedEscapedColumnName.addBodyLine("sb.append(\" as \");");
            mGetAliasedEscapedColumnName.addBodyLine("if (this.isColumnNameDelimited) {");
            mGetAliasedEscapedColumnName.addBodyLine("sb.append(" + CONST_BEGINNING_DELIMITER + ");");
            mGetAliasedEscapedColumnName.addBodyLine("}");
            mGetAliasedEscapedColumnName.addBodyLine("sb.append(\"" + alias + "_\");");
            mGetAliasedEscapedColumnName.addBodyLine("sb.append(this.column);");
            mGetAliasedEscapedColumnName.addBodyLine("if (this.isColumnNameDelimited) {");
            mGetAliasedEscapedColumnName.addBodyLine("sb.append(" + CONST_BEGINNING_DELIMITER + ");");
            mGetAliasedEscapedColumnName.addBodyLine("}");
            mGetAliasedEscapedColumnName.addBodyLine("return sb.toString();");
        } else {
            mGetAliasedEscapedColumnName.addBodyLine("return this." + METHOD_GET_ESCAPED_COLUMN_NAME + "();");
        }
        FormatTools.addMethodWithBestPosition(innerEnum, mGetAliasedEscapedColumnName);
        logger.debug(topLevelClass.getType().getShortName() + ".Column 增加 getAliasedEscapedColumnName() 方法");

        // hook
        PluginTools.getHook(IModelColumnPluginHook.class).modelColumnEnumGenerated(innerEnum, topLevelClass, introspectedTable);

        return innerEnum;
    }
}
