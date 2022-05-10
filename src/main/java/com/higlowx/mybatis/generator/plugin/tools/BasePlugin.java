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
package com.higlowx.mybatis.generator.plugin.tools;

import com.higlowx.mybatis.generator.plugin.hook.HookAggregator;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;

/**
 * Base Plugin
 *
 * @author Dylan.Li
 */
public class BasePlugin extends PluginAdapter {

    protected List<String> warnings;

    public static final String PRO_MYBATIS_VERSION = "mybatisVersion";
    protected String mybatisVersion = "3.5.0";


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


    @Override
    public void setContext(Context context) {
        super.setContext(context);

        HookAggregator.getInstance().setContext(context);

        if (StringUtility.stringHasValue(context.getProperty(PRO_MYBATIS_VERSION))) {
            this.mybatisVersion = context.getProperty(PRO_MYBATIS_VERSION);
        }
    }
}
