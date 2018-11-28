package com.fwd.rdm.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 19:14 2018/11/28
 */
@Configuration
public class LanguageConfig {

    /**
     * 加载国际化文件
     *
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
        resourceBundleMessageSource.setBasenames("language.message", "language.ui");
        return resourceBundleMessageSource;
    }

}
