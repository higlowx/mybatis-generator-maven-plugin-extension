package com.higlowx.mybatis.generator.plugin.hook;

import com.higlowx.mybatis.generator.plugin.tools.BeanUtils;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.PluginAggregator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 扩展插件Hook聚合器
 *
 * @author Dylan.Li
 * @date 2022/5/7
 */
public class HookAggregator implements ISelectSelectivePluginHook, ISelectOneByExamplePluginHook, IModelColumnPluginHook {

    private final static Logger logger = LoggerFactory.getLogger(HookAggregator.class);
    private final static HookAggregator INSTANCE = new HookAggregator();
    private Context context;

    /**
     * constructor
     */
    public HookAggregator() {
    }

    /**
     * Getter method for property <tt>instance</tt>.
     *
     * @return property value of instance
     */
    public static HookAggregator getInstance() {
        return INSTANCE;
    }

    /**
     * Setter method for property <tt>context</tt>.
     *
     * @param context value to be assigned to property context
     */
    public void setContext(Context context) {
        this.context = context;
    }


    /**
     * 获取实现埋下目标Hook的插件
     */
    @SuppressWarnings("unchecked")
    private <T extends IPluginHook> List<T> getPluginsWithIPluginHook(Class<T> clazz) {
        List<T> list = new ArrayList<>();
        // 反射获取插件列表，不能用单例去弄，不然因为类释放的问题而导致测试用例出问题
        try {
            PluginAggregator pluginAggregator = (PluginAggregator) this.context.getPlugins();
            List<Plugin> plugins = (List<Plugin>) BeanUtils.getProperty(pluginAggregator, "plugins");
            for (Plugin p : plugins) {
                if (clazz.isInstance(p)) {
                    T plugin = (T) p;
                    list.add(plugin);
                }
            }
        } catch (Exception e) {
            logger.error("获取插件列表失败！", e);
        }
        return list;
    }

    // ============================================= ISelectSelectivePluginHook ==============================================

    @Override
    public boolean sqlMapSelectByExampleSelectiveElementGenerated(Document document, XmlElement element, IntrospectedTable introspectedTable) {
        for (ISelectSelectivePluginHook plugin : this.getPluginsWithIPluginHook(ISelectSelectivePluginHook.class)) {
            if (!plugin.sqlMapSelectByExampleSelectiveElementGenerated(document, element, introspectedTable)) {
                return false;
            }
        }
        return true;
    }

    // ============================================= IModelColumnPluginHook ==============================================

    @Override
    public boolean modelColumnEnumGenerated(InnerEnum innerEnum, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        for (IModelColumnPluginHook plugin : this.getPluginsWithIPluginHook(IModelColumnPluginHook.class)) {
            if (!plugin.modelColumnEnumGenerated(innerEnum, topLevelClass, introspectedTable)) {
                return false;
            }
        }
        return true;
    }

    // ============================================= ISelectOneByExamplePluginHook ==============================================

    @Override
    public boolean clientSelectOneByExampleWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        for (ISelectOneByExamplePluginHook plugin : this.getPluginsWithIPluginHook(ISelectOneByExamplePluginHook.class)) {
            if (!plugin.clientSelectOneByExampleWithBLOBsMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean clientSelectOneByExampleWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        for (ISelectOneByExamplePluginHook plugin : this.getPluginsWithIPluginHook(ISelectOneByExamplePluginHook.class)) {
            if (!plugin.clientSelectOneByExampleWithoutBLOBsMethodGenerated(method, interfaze, introspectedTable)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean sqlMapSelectOneByExampleWithoutBLOBsElementGenerated(Document document, XmlElement element, IntrospectedTable introspectedTable) {
        for (ISelectOneByExamplePluginHook plugin : this.getPluginsWithIPluginHook(ISelectOneByExamplePluginHook.class)) {
            if (!plugin.sqlMapSelectOneByExampleWithoutBLOBsElementGenerated(document, element, introspectedTable)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean sqlMapSelectOneByExampleWithBLOBsElementGenerated(Document document, XmlElement element, IntrospectedTable introspectedTable) {
        for (ISelectOneByExamplePluginHook plugin : this.getPluginsWithIPluginHook(ISelectOneByExamplePluginHook.class)) {
            if (!plugin.sqlMapSelectOneByExampleWithBLOBsElementGenerated(document, element, introspectedTable)) {
                return false;
            }
        }
        return true;
    }
}
