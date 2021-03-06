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

import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import javax.sql.DataSource;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyBatisGeneratorTool {
    public final static String DAO_PACKAGE = "com.higlowx.mybatis.generator.plugin.dao";    // dao package
    private List<String> warnings;  // ????????????
    private Configuration config;   // ????????????
    private String targetProject;  // ??????
    private String targetPackage; // package

    /**
     * ??????
     * @param resource
     * @return
     */
    public static MyBatisGeneratorTool create(String resource) throws IOException, XMLParserException {
        MyBatisGeneratorTool tool = new MyBatisGeneratorTool();
        tool.warnings = new ArrayList<>();

        // MyBatisGenerator ??????
        ConfigurationParser cp = new ConfigurationParser(tool.warnings);
        tool.config = cp.parseConfiguration(Resources.getResourceAsStream(resource));
        // ??????????????????
        tool.fixConfigToTarget();
        return tool;
    }

    /**
     * ??????MyBatisGenerator
     * @param before
     * @param callback
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException
     */
    public MyBatisGenerator generate(IBeforeCallback before, AbstractShellCallback callback) throws Exception {
        before.run();
        callback.setTool(this);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null, null, null, true);
        return myBatisGenerator;
    }

    /**
     * ??????MyBatisGenerator
     * @param callback
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException
     */
    public MyBatisGenerator generate(AbstractShellCallback callback) throws Exception {
       return this.generate(() -> {

       }, callback);
    }

    /**
     * ??????MyBatisGenerator
     * @param before
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException
     */
    public MyBatisGenerator generate(IBeforeCallback before) throws Exception {
        before.run();
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, new DefaultShellCallback(true), warnings);
        myBatisGenerator.generate(null, null, null, false);
        return myBatisGenerator;
    }

    /**
     * ??????MyBatisGenerator(???????????????)
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws InterruptedException
     */
    public MyBatisGenerator generate() throws InvalidConfigurationException, InterruptedException, SQLException, IOException {
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, new DefaultShellCallback(true), warnings);
        myBatisGenerator.generate(null, null, null, false);
        return myBatisGenerator;
    }

    /**
     * ????????????????????? SqlSession
     * @return
     */
    public SqlSession compile() throws IOException, ClassNotFoundException {
        // ????????????java??????
        String target = targetProject + targetPackage.replaceAll("\\.", "/");
        List<File> javaFiles = getGeneratedFiles(new File(target), ".java");
        compileJavaFiles(javaFiles);
        return getSqlSession();
    }

    /**
     * ?????????????????????ClassLoader
     * @return
     */
    public ClassLoader getTargetClassLoader() throws MalformedURLException {
        return URLClassLoader.newInstance(new URL[]{
                new File(targetProject).toURI().toURL()
        });
    }

    /**
     * ??????SqlSession
     * @return
     * @throws IOException
     */
    public SqlSession getSqlSession() throws IOException, ClassNotFoundException {
        org.apache.ibatis.session.Configuration config = new org.apache.ibatis.session.Configuration();
        config.setCallSettersOnNulls(true); // ??????null??????setter??????
        config.setMapUnderscoreToCamelCase(true);   // ??????????????????

        // ??????mapper
        config.addMappers(targetPackage);
        // ????????????????????????
        PooledDataSourceFactory dataSourceFactory = new PooledDataSourceFactory();
        dataSourceFactory.setProperties(DBHelper.properties);
        DataSource dataSource = dataSourceFactory.getDataSource();
        JdbcTransactionFactory transactionFactory = new JdbcTransactionFactory();

        Environment environment = new Environment("test", transactionFactory, dataSource);
        config.setEnvironment(environment);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(config);
        return sqlSessionFactory.openSession(true);
    }

    /**
     * ????????????java??????
     * @param files
     */
    private void compileJavaFiles(List<File> files) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        //??????java???????????????
        StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
        //??????java?????????????????????
        Iterable<? extends JavaFileObject> it = manager.getJavaFileObjectsFromFiles(files);
        //??????????????????
        ArrayList<String> ops = new ArrayList<>();
        ops.add("-Xlint:unchecked");
        //??????????????????
        JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, ops, null, it);
        //??????????????????
        task.call();
    }

    /**
     * ???????????????????????????
     * @param file
     * @return
     */
    private List<File> getGeneratedFiles(File file, String ext) {
        List<File> list = new ArrayList<>();
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File childFile : files) {
                if (childFile.isDirectory()) {
                    list.addAll(getGeneratedFiles(childFile, ext));
                } else if (childFile.getName().endsWith(ext)) {
                    list.add(childFile);
                }
            }
        }
        return list;
    }

    /**
     * ?????????????????????target
     */
    private void fixConfigToTarget() {
        this.targetProject = this.getClass().getClassLoader().getResource("").getPath();
        this.targetPackage = DAO_PACKAGE + ".s" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        for (Context context : config.getContexts()) {
            context.getJavaModelGeneratorConfiguration().setTargetProject(targetProject);
            context.getJavaModelGeneratorConfiguration().setTargetPackage(targetPackage);
            context.getSqlMapGeneratorConfiguration().setTargetProject(targetProject);
            context.getSqlMapGeneratorConfiguration().setTargetPackage(targetPackage);
            context.getJavaClientGeneratorConfiguration().setTargetProject(targetProject);
            context.getJavaClientGeneratorConfiguration().setTargetPackage(targetPackage);
        }
    }

    /**
     * Getter method for property <tt>warnings</tt>.
     * @return property value of warnings
     * @author hewei
     */
    public List<String> getWarnings() {
        return warnings;
    }

    /**
     * Getter method for property <tt>config</tt>.
     * @return property value of config
     * @author hewei
     */
    public Configuration getConfig() {
        return config;
    }

    /**
     * Getter method for property <tt>targetPackage</tt>.
     * @return property value of targetPackage
     * @author hewei
     */
    public String getTargetPackage() {
        return targetPackage;
    }
}
