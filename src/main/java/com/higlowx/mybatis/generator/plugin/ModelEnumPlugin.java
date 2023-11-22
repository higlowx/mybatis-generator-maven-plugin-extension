package com.higlowx.mybatis.generator.plugin;

import com.higlowx.mybatis.generator.plugin.tools.BasePlugin;
import com.higlowx.mybatis.generator.plugin.tools.FormatTools;
import com.higlowx.mybatis.generator.plugin.tools.JavaElementGeneratorTools;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.internal.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * .
 *
 * @author : wmq
 * @version : 2023/11/21
 **/
public class ModelEnumPlugin extends BasePlugin {


    private static final Logger logger = LoggerFactory.getLogger(ModelEnumPlugin.class);
    /**
     * Enum名后缀
     */
    public static final String ENUM = "Enum";

    /**
     * 自定义方法
     */
    public static final String METHOD_EXCLUDES = "excludes";
    public static final String METHOD_ALL = "all";
    public static final String METHOD_GET_ESCAPED_COLUMN_NAME = "getEscapedColumnName";
    public static final String METHOD_GET_ALIASED_ESCAPED_COLUMN_NAME = "getAliasedEscapedColumnName";

    public static final String CONST_BEGINNING_DELIMITER = "BEGINNING_DELIMITER";
    public static final String CONST_ENDING_DELIMITER = "ENDING_DELIMITER";

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }


    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {

        List<GeneratedJavaFile> generatedFiles = new ArrayList<>();
        // 获取表名，作为枚举类的名字
        String tableName = introspectedTable.getRules().calculateAllFieldsClass().getShortName();
        String enumName = tableName + ENUM;

//        String targetProject = StringUtility.stringHasValue(properties.getProperty("targetProject"))
//                ? properties.getProperty("targetProject")
//                : context.getJavaModelGeneratorConfiguration().getTargetProject();
//
//        String targetPackage = StringUtility.stringHasValue(properties.getProperty("targetPackage"))
//                ? properties.getProperty("targetPackage")
//                : context.getJavaModelGeneratorConfiguration().getTargetPackage();

        String targetProject =  context.getJavaModelGeneratorConfiguration().getTargetProject();
        String targetPackage = context.getJavaModelGeneratorConfiguration().getTargetPackage();

        // 创建 TopLevelClass 对象
        TopLevelEnumeration topLevelEnumeration = new TopLevelEnumeration(new FullyQualifiedJavaType(targetPackage+"." + enumName));
        generateColumnEnum(topLevelEnumeration,introspectedTable);

        // 创建 GeneratedJavaFile 对象
        GeneratedJavaFile generatedFile = new GeneratedJavaFile(
                topLevelEnumeration,
                targetProject,
                Charset.defaultCharset().name(),
                new DefaultJavaFormatter()
        );
        // 将生成的文件添加到列表中
        generatedFiles.add(generatedFile);
        return generatedFiles;
    }


    /**
     * 生成Column字段枚举
     */
    private TopLevelEnumeration generateColumnEnum(TopLevelEnumeration topLevelEnumeration, IntrospectedTable introspectedTable) {
        String enumName = topLevelEnumeration.getType().getShortName();

        topLevelEnumeration.setVisibility(JavaVisibility.PUBLIC);
        // 生成常量
        Field beginningDelimiterField = JavaElementGeneratorTools.generateField(
                CONST_BEGINNING_DELIMITER,
                JavaVisibility.PRIVATE,
                FullyQualifiedJavaType.getStringInstance(),
                "\"" + StringUtility.escapeStringForJava(context.getBeginningDelimiter()) + "\""
        );
        beginningDelimiterField.setStatic(true);
        beginningDelimiterField.setFinal(true);
        topLevelEnumeration.addField(beginningDelimiterField);

        Field endingDelimiterField = JavaElementGeneratorTools.generateField(
                CONST_ENDING_DELIMITER,
                JavaVisibility.PRIVATE,
                FullyQualifiedJavaType.getStringInstance(),
                "\"" + StringUtility.escapeStringForJava(context.getEndingDelimiter()) + "\""
        );
        endingDelimiterField.setStatic(true);
        endingDelimiterField.setFinal(true);
        topLevelEnumeration.addField(endingDelimiterField);

        // 生成属性和构造函数
        Field columnField = new Field("column", FullyQualifiedJavaType.getStringInstance());
        columnField.setVisibility(JavaVisibility.PRIVATE);
        columnField.setFinal(true);
        topLevelEnumeration.addField(columnField);

        Field isColumnNameDelimitedField = new Field("isColumnNameDelimited", FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        isColumnNameDelimitedField.setVisibility(JavaVisibility.PRIVATE);
        isColumnNameDelimitedField.setFinal(true);
        topLevelEnumeration.addField(isColumnNameDelimitedField);

        Field javaPropertyField = new Field("javaProperty", FullyQualifiedJavaType.getStringInstance());
        javaPropertyField.setVisibility(JavaVisibility.PRIVATE);
        javaPropertyField.setFinal(true);
        topLevelEnumeration.addField(javaPropertyField);

        Field jdbcTypeField = new Field("jdbcType", FullyQualifiedJavaType.getStringInstance());
        jdbcTypeField.setVisibility(JavaVisibility.PRIVATE);
        jdbcTypeField.setFinal(true);
        topLevelEnumeration.addField(jdbcTypeField);

        Method mValue = new Method("value");
        mValue.setVisibility(JavaVisibility.PUBLIC);
        mValue.setReturnType(FullyQualifiedJavaType.getStringInstance());
        mValue.addBodyLine("return this.column;");
        FormatTools.addMethodWithBestPosition(topLevelEnumeration, mValue);

        Method mGetValue = new Method("getValue");
        mGetValue.setVisibility(JavaVisibility.PUBLIC);
        mGetValue.setReturnType(FullyQualifiedJavaType.getStringInstance());
        mGetValue.addBodyLine("return this.column;");
        FormatTools.addMethodWithBestPosition(topLevelEnumeration, mGetValue);

        Method mGetJavaProperty = JavaElementGeneratorTools.generateGetterMethod(javaPropertyField);
        FormatTools.addMethodWithBestPosition(topLevelEnumeration, mGetJavaProperty);

        Method mGetJdbcType = JavaElementGeneratorTools.generateGetterMethod(jdbcTypeField);
        FormatTools.addMethodWithBestPosition(topLevelEnumeration, mGetJdbcType);

        Method constructor = new Method(enumName);
        constructor.setConstructor(true);
        constructor.addBodyLine("this.column = column;");
        constructor.addBodyLine("this.javaProperty = javaProperty;");
        constructor.addBodyLine("this.jdbcType = jdbcType;");
        constructor.addBodyLine("this.isColumnNameDelimited = isColumnNameDelimited;");
        constructor.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "column"));
        constructor.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "javaProperty"));
        constructor.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(), "jdbcType"));
        constructor.addParameter(new Parameter(FullyQualifiedJavaType.getBooleanPrimitiveInstance(), "isColumnNameDelimited"));
        FormatTools.addMethodWithBestPosition(topLevelEnumeration, constructor);
        logger.debug(topLevelEnumeration.getType().getShortName() + " 增加 构造方法 和 column 属性");

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

            topLevelEnumeration.addEnumConstant(sb.toString());
            logger.debug(topLevelEnumeration.getType().getShortName() + " 增加 " + field.getName() + " 枚举");
        }

        // asc 和 desc 方法
        Method desc = new Method("desc");
        desc.setVisibility(JavaVisibility.PUBLIC);
        desc.setReturnType(FullyQualifiedJavaType.getStringInstance());
        desc.addBodyLine("return this." + METHOD_GET_ESCAPED_COLUMN_NAME + "() + \" DESC\";");
        FormatTools.addMethodWithBestPosition(topLevelEnumeration, desc);

        Method asc = new Method("asc");
        asc.setVisibility(JavaVisibility.PUBLIC);
        asc.setReturnType(FullyQualifiedJavaType.getStringInstance());
        asc.addBodyLine("return this." + METHOD_GET_ESCAPED_COLUMN_NAME + "() + \" ASC\";");
        FormatTools.addMethodWithBestPosition(topLevelEnumeration, asc);
        logger.debug(topLevelEnumeration.getType().getShortName() + " 增加 asc() 和 desc() 方法");

        // excludes
        topLevelEnumeration.addImportedType(new FullyQualifiedJavaType("java.util.Arrays"));
        topLevelEnumeration.addImportedType(FullyQualifiedJavaType.getNewArrayListInstance());
        Method mExcludes = JavaElementGeneratorTools.generateMethod(
                METHOD_EXCLUDES,
                JavaVisibility.PUBLIC,
                new FullyQualifiedJavaType(enumName + "[]"),
                new Parameter(topLevelEnumeration.getType(), "excludes", true)
        );
        mExcludes.setStatic(true);
        JavaElementGeneratorTools.generateMethodBody(
                mExcludes,
                "ArrayList<"+enumName+"> columns = new ArrayList<>(Arrays.asList("+enumName+".values()));",
                "if (excludes != null && excludes.length > 0) {",
                "columns.removeAll(new ArrayList<>(Arrays.asList(excludes)));",
                "}",
                "return columns.toArray(new "+enumName+"[]{});"
        );
        FormatTools.addMethodWithBestPosition(topLevelEnumeration, mExcludes);
        logger.debug(topLevelEnumeration.getType().getShortName() + " 增加 excludes() 方法");

        // all
        Method mAll = JavaElementGeneratorTools.generateMethod(
                METHOD_ALL,
                JavaVisibility.PUBLIC,
                new FullyQualifiedJavaType(enumName + "[]")
        );
        mAll.setStatic(true);
        mAll.addBodyLine("return "+enumName+".values();");
        FormatTools.addMethodWithBestPosition(topLevelEnumeration, mAll);
        logger.debug(topLevelEnumeration.getType().getShortName() + " 增加 all() 方法。");

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
        FormatTools.addMethodWithBestPosition(topLevelEnumeration, mGetEscapedColumnName);
        logger.debug(topLevelEnumeration.getType().getShortName() + " 增加 getEscapedColumnName() 方法");

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
        FormatTools.addMethodWithBestPosition(topLevelEnumeration, mGetAliasedEscapedColumnName);

        // hook
       // PluginTools.getHook(IModelColumnPluginHook.class).modelColumnEnumGenerated(topLevelEnumeration, topLevelClass, introspectedTable);

        return topLevelEnumeration;
    }
}
