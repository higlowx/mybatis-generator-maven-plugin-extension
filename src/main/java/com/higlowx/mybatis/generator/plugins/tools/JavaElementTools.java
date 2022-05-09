package com.higlowx.mybatis.generator.plugins.tools;


import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TypeParameter;

/**
 *
 * <p>
 *
 */
public class JavaElementTools {
    /**
     * clone
     *
     * @param method
     * @return
     */
    public static Method clone(Method method) {
        Method dest = new Method(method.getName());
        // 注解
        for (String javaDocLine : method.getJavaDocLines()) {
            dest.addJavaDocLine(javaDocLine);
        }
        if (method.getReturnType().isPresent()) {
            dest.setReturnType(method.getReturnType().get());
        }
        for (Parameter parameter : method.getParameters()) {
            dest.addParameter(JavaElementTools.clone(parameter));
        }
        for (FullyQualifiedJavaType exception : method.getExceptions()) {
            dest.addException(exception);
        }
        for (TypeParameter typeParameter : method.getTypeParameters()) {
            dest.addTypeParameter(typeParameter);
        }
        dest.addBodyLines(method.getBodyLines());
        dest.setConstructor(method.isConstructor());
        dest.setNative(method.isNative());
        dest.setSynchronized(method.isSynchronized());
        dest.setDefault(method.isDefault());
        dest.setFinal(method.isFinal());
        dest.setStatic(method.isStatic());
        dest.setVisibility(method.getVisibility());
        return dest;
    }

    /**
     * clone
     *
     * @param parameter
     * @return
     */
    public static Parameter clone(Parameter parameter) {
        Parameter dest = new Parameter(parameter.getType(), parameter.getName(), parameter.isVarargs());
        for (String annotation : parameter.getAnnotations()) {
            dest.addAnnotation(annotation);
        }
        return dest;
    }
}
