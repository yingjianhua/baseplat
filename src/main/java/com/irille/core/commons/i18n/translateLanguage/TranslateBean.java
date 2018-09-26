package com.irille.core.commons.i18n.translateLanguage;

import com.google.common.base.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: lijie@shoestp.cn
 * Date: 2018/8/13
 * Time: 10:46
 */
public class TranslateBean {
    private String sourceLanguage;
    private String targetLanguage;
    private String text;
    private boolean cache=true;

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public TranslateBean setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
        return this;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public TranslateBean setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
        return this;
    }

    public String getText() {
        return text;
    }

    public TranslateBean setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TranslateBean that = (TranslateBean) o;
        return Objects.equal(sourceLanguage, that.sourceLanguage) &&
                Objects.equal(targetLanguage, that.targetLanguage) &&
                Objects.equal(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sourceLanguage, targetLanguage, text);
    }
}
