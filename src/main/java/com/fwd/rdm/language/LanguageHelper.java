package com.fwd.rdm.language;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 19:23 2018/11/28
 */
@Component
public class LanguageHelper implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * 获取国际化消息
     *
     * @param messageCode
     * @return
     */
    public String getMessage(MessageCode messageCode) {
        return applicationContext.getMessage(messageCode.getCode(), null, LocaleContextHolder.getLocale());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
