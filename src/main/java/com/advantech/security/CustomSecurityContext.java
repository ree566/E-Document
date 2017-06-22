/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

/**
 *
 * @author Wei.Cheng
 */
public class CustomSecurityContext {

    private static final Map<String, Collection<ConfigAttribute>> METADATA_SOURCE_MAP;

    static {
        METADATA_SOURCE_MAP = new HashMap();

        try {
            initMetadataSource();
        } catch (IOException ex) {
            System.out.println(ex);
            throw new RuntimeException("Properties init fail, please try again.");
        }
    }

    public static Map<String, Collection<ConfigAttribute>> getMetadataSource() {
        return METADATA_SOURCE_MAP;
    }

    private static void initMetadataSource() throws IOException {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resourcePatternResolver.getResources("classpath*:/security/*.properties");
        if (ArrayUtils.isEmpty(resources)) {
            return;
        }
        Properties properties = new Properties();
        for (Resource resource : resources) {
            properties.load(resource.getInputStream());
        }
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            String[] values = StringUtils.split(value, ",");
            Collection<ConfigAttribute> configAttributes = new ArrayList<>();
            ConfigAttribute configAttribute = new SecurityConfig(key);
            configAttributes.add(configAttribute);
            for (String v : values) {
                METADATA_SOURCE_MAP.put(StringUtils.trim(v), configAttributes);
            }
        }
    }

}
