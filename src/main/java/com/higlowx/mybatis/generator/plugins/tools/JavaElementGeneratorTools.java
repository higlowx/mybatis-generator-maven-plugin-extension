package com.higlowx.mybatis.generator.plugins.tools;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 *
 * Java ele 生成工具
 *
 */
public class JavaElementGeneratorTools {

    /**
     * 生成静态常量
     * @param fieldName  常量名称
     * @param javaType   类型
     * @param initString 初始化字段
     * @return
     */
    public static Field generateStaticFinalField(String fieldName, FullyQualifiedJavaType javaType, String initString) {
        Field field = new Field(fieldName, javaType);
        field.setVisibility(JavaVisibility.PUBLIC);
        field.setStatic(true);
        field.setFinal(true);
        if (initString != null) {
            field.setInitializationString(initString);
        }
        return field;
    }

    /**
     * 生成属性
     * @param fieldName  常量名称
     * @param visibility 可见性
     * @param javaType   类型
     * @param initString 初始化字段
     * @return
     */
    public static Field generateField(String fieldName, JavaVisibility visibility, FullyQualifiedJavaType javaType, String initString) {
        Field field = new Field(fieldName, javaType);
        field.setVisibility(visibility);
        if (initString != null) {
            field.setInitializationString(initString);
        }
        return field;
    }

    /**
     * 生成方法
     * @param methodName 方法名
     * @param visibility 可见性
     * @param returnType 返回值类型
     * @param parameters 参数列表
     * @return
     */
    public static Method generateMethod(String methodName, JavaVisibility visibility, FullyQualifiedJavaType returnType, Parameter... parameters) {
        Method method = new Method(methodName);
        method.setVisibility(visibility);
        method.setReturnType(returnType);
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                method.addParameter(parameter);
            }
        }

        return method;
    }

    /**
     * 生成方法实现体
     * @param method    方法
     * @param bodyLines 方法实现行
     * @return
     */
    public static Method generateMethodBody(Method method, String... bodyLines) {
        if (bodyLines != null) {
            for (String bodyLine : bodyLines) {
                method.addBodyLine(bodyLine);
            }
        }
        return method;
    }

    /**
     * 生成Filed的Set方法
     * @param field field
     * @return
     */
    public static Method generateSetterMethod(Field field) {
        Method method = generateMethod(
                "set" + FormatTools.upFirstChar(field.getName()),
                JavaVisibility.PUBLIC,
                null,
                new Parameter(field.getType(), field.getName())
        );
        return generateMethodBody(method, "this." + field.getName() + " = " + field.getName() + ";");
    }

    /**
     * 生成Filed的Get方法
     * @param field field
     * @return
     */
    public static Method generateGetterMethod(Field field) {
        Method method = generateMethod(
                "get" + FormatTools.upFirstChar(field.getName()),
                JavaVisibility.PUBLIC,
                field.getType()
        );
        return generateMethodBody(method, "return this." + field.getName() + ";");
    }

    /**
     * 获取Model没有BLOBs类时的类型
     * @param introspectedTable
     * @return
     */
    public static FullyQualifiedJavaType getModelTypeWithoutBLOBs(IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType type;
        if (introspectedTable.getRules().generateBaseRecordClass()) {
            type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        } else if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            type = new FullyQualifiedJavaType(introspectedTable.getPrimaryKeyType());
        } else {
            throw new RuntimeException(getString("RuntimeError.12"));
        }
        return type;
    }

    /**
     * 获取Model有BLOBs类时的类型
     * @param introspectedTable
     * @return
     */
    public static FullyQualifiedJavaType getModelTypeWithBLOBs(IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType type;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            type = new FullyQualifiedJavaType(introspectedTable.getRecordWithBLOBsType());
        } else {
            // the blob fields must be rolled up into the base class
            type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        }
        return type;
    }
}
