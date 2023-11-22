package com.higlowx.mybatis.generator.plugin;

import com.higlowx.mybatis.generator.plugin.hook.IUpdateSelectiveEnhancedPluginHook;
import com.higlowx.mybatis.generator.plugin.tools.*;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;

import java.util.List;


/**
 * .
 *
 * @author : wmq
 * @version : 2023/11/21
 **/
public class UpdateSelectiveEnhancedPlugin extends BasePlugin implements IUpdateSelectiveEnhancedPluginHook{

    public static final String METHOD_UPDATE_BY_EXAMPLE_SELECTIVE_COLUMN = "updateByExampleSelectiveColumn";
    public static final String METHOD_UPDATE_BY_PRIMARY_KEY_SELECTIVE_COLUMN = "updateByPrimaryKeySelectiveColumn";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(List<String> warnings) {
        // 插件使用前提是使用了ModelColumnPlugin插件
        if (!PluginTools.checkDependencyPlugin(this.context, ModelEnumPlugin.class)) {
            warnings.add("higlowx:插件" + this.getClass().getTypeName() + "插件需配合" + ModelEnumPlugin.class.getTypeName() + "插件使用！");
            return false;
        }

        return super.validate(warnings);
    }

    /**
     *
     * @param interfaze
     *            the generated interface if any, may be null
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return
     */
    @Override
    public boolean clientGenerated(Interface interfaze,  IntrospectedTable introspectedTable) {

        Method methodNoKey = JavaElementGeneratorTools.generateMethod(METHOD_UPDATE_BY_EXAMPLE_SELECTIVE_COLUMN,
                JavaVisibility.DEFAULT,FullyQualifiedJavaType.getIntInstance());

        // hook
        if (PluginTools.getHook(IUpdateSelectiveEnhancedPluginHook.class).clientUpdateByExampleSelectiveColumnMethodGenerated(methodNoKey, interfaze, introspectedTable)) {
            // interface 增加方法
            FormatTools.replaceGeneralMethodComment(this.context.getCommentGenerator(), methodNoKey, introspectedTable);
            FormatTools.addMethodWithBestPosition(interfaze, methodNoKey, introspectedTable);
        }

        // =================================================primaryKey======================================================

        Method methodKey = JavaElementGeneratorTools.generateMethod(METHOD_UPDATE_BY_PRIMARY_KEY_SELECTIVE_COLUMN,
                JavaVisibility.DEFAULT,FullyQualifiedJavaType.getIntInstance());
        // hook
        if (PluginTools.getHook(IUpdateSelectiveEnhancedPluginHook.class).
                clientUpdateByPrimaryKeySelectiveColumnMethodGenerated(methodKey, interfaze, introspectedTable)) {
            // interface 增加方法
            FormatTools.replaceGeneralMethodComment(this.context.getCommentGenerator(), methodKey, introspectedTable);
            FormatTools.addMethodWithBestPosition(interfaze, methodKey, introspectedTable);
        }

        // 生成的新类导包
//        Set<FullyQualifiedJavaType> importTypes = new TreeSet<>();
//        FullyQualifiedJavaType importEnum = new FullyQualifiedJavaType(introspectedTable.getRules().calculateAllFieldsClass().getFullyQualifiedName() + ModelEnumPlugin.ENUM);
//        importTypes.add(importEnum);
//        interfaze.addImportedTypes(importTypes);

        return super.clientGenerated(interfaze,introspectedTable);
    }

    /**
     *
     * @param document
     *            the generated document (note that this is the MyBatis generator's internal
     *            Document class - not the w3c XML Document class)
     * @param introspectedTable
     *            The class containing information about the table as
     *            introspected from the database
     * @return
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement answerNoKey = new XmlElement("update");
        answerNoKey.addAttribute(new Attribute("id", METHOD_UPDATE_BY_EXAMPLE_SELECTIVE_COLUMN));
        answerNoKey.addAttribute(new Attribute("parameterType", "map"));

        // hook
        if (PluginTools.getHook(IUpdateSelectiveEnhancedPluginHook.class).sqlMapUpdateByExampleSelectiveColumnElementGenerated(document,answerNoKey,introspectedTable)) {
            document.getRootElement().addElement(answerNoKey);
        }

        // =================================================primaryKey======================================================

        XmlElement answerKey = new XmlElement("update");
        answerKey.addAttribute(new Attribute("id", METHOD_UPDATE_BY_PRIMARY_KEY_SELECTIVE_COLUMN));
        answerKey.addAttribute(new Attribute("parameterType", "map"));

        // hook
        if (PluginTools.getHook(IUpdateSelectiveEnhancedPluginHook.class).sqlMapUpdateByPrimaryKeySelectiveColumnElementGenerated(document,answerKey,introspectedTable)) {
            document.getRootElement().addElement(answerKey);
        }

        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }


    @Override
    public boolean clientUpdateByExampleSelectiveColumnMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {

        FullyQualifiedJavaType parameterType = introspectedTable.getRules().calculateAllFieldsClass();
        method.addParameter(new Parameter(parameterType, "record", "@Param(\"record\")"));

        FullyQualifiedJavaType exampleType = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        method.addParameter(new Parameter(exampleType, "example", "@Param(\"example\")"));

        // 找出全字段对应的Model
        FullyQualifiedJavaType fullFieldModel = introspectedTable.getRules().calculateAllFieldsClass();
        // column枚举
        FullyQualifiedJavaType selectiveType = new FullyQualifiedJavaType(fullFieldModel.getShortName() +  ModelEnumPlugin.ENUM);
        method.addParameter(new Parameter(selectiveType, "selective", "@Param(\"selective\")", true));
        return true;
    }

    @Override
    public boolean sqlMapUpdateByExampleSelectiveColumnElementGenerated(Document document, XmlElement element, IntrospectedTable introspectedTable) {
        this.context.getCommentGenerator().addComment(element);
        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        element.addElement(new TextElement(sb.toString()));
        // selective
        element.addElement(new TextElement("SET"));
        element.addElement(this.generateSetsSelective(ListUtilities.removeGeneratedAlwaysColumns(introspectedTable.getAllColumns())));
        element.addElement(XmlElementGeneratorTools.getUpdateByExampleIncludeElement(introspectedTable));
        return true;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveColumnMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
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
        FullyQualifiedJavaType selectiveType = new FullyQualifiedJavaType(fullFieldModel.getShortName() +  ModelEnumPlugin.ENUM);
        method.addParameter(new Parameter(selectiveType, "selective", "@Param(\"selective\")", true));
        // 生成的新类导包
        interfaze.addImportedType(selectiveType);
        return true;
    }



    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveColumnElementGenerated(Document document, XmlElement element, IntrospectedTable introspectedTable) {
        this.context.getCommentGenerator().addComment(element);
        StringBuilder sb = new StringBuilder();

        sb.append("update ");
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        element.addElement(new TextElement(sb.toString()));

        // selective
        element.addElement(new TextElement("SET"));
        element.addElement(this.generateSetsSelective(ListUtilities.removeGeneratedAlwaysColumns(introspectedTable.getNonPrimaryKeyColumns())));

        XmlElementGeneratorTools.generateWhereByPrimaryKeyTo(element, introspectedTable.getPrimaryKeyColumns(), "record.");
        return true;
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
