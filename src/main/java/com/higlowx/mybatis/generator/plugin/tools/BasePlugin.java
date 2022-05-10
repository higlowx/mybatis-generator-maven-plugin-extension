package com.higlowx.mybatis.generator.plugin.tools;

import com.higlowx.mybatis.generator.plugin.hook.HookAggregator;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;

/**
 * @author Dylan.Li
 * @date 2022/5/4
 */
public class BasePlugin extends PluginAdapter {

    protected List<String> warnings;

    /**
     * mybatis 版本
     */
    public static final String PRO_MYBATIS_VERSION = "mybatisVersion";
    protected String mybatisVersion = "3.5.0";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(List<String> warnings) {
        this.warnings = warnings;

        if (StringUtility.stringHasValue(this.context.getTargetRuntime())
                && !"MyBatis3".equalsIgnoreCase(this.context.getTargetRuntime())) {

            warnings.add("Mybatis Generator Plugin: " + this.getClass().getTypeName() + " need the targetRuntime to be MyBatis3.");

            return false;
        }

        return true;
    }

    /**
     * Set the context under which this plugin is running.
     *
     * @param context the new context
     */
    @Override
    public void setContext(Context context) {
        super.setContext(context);

        // 添加插件
        HookAggregator.getInstance().setContext(context);

        // mybatis版本
        if (StringUtility.stringHasValue(context.getProperty(PRO_MYBATIS_VERSION))) {
            this.mybatisVersion = context.getProperty(PRO_MYBATIS_VERSION);
        }
    }
}
