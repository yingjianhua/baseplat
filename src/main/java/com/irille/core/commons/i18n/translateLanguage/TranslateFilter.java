package com.irille.core.commons.i18n.translateLanguage;

import irille.pub.tb.FldLanguage;

import java.util.List;
import java.util.function.Function;

/**
 * Created by IntelliJ IDEA.
 * User: lijie@shoestp.cn
 * Date: 2018/8/28
 * Time: 10:15
 */
public class TranslateFilter {
    //模式  1 过滤模式 0 白名单  2 精准修改
    private int mode;
    private FldLanguage.Language baseLanguage = FldLanguage.Language.en;
    //源语言
    private String sourceLanguage;
    //需要翻译字段
    private List<FldLanguage.Language> LanguageList;
    //需要翻译的字段
    private List<String> fld;
    private List<String> CacheFilter;


    //值 过滤器  true 通过 false 失败
    private Function<String, Boolean> fldValueFilter;


    public List<FldLanguage.Language> getLanguageList() {
        return LanguageList;
    }

    public void setLanguageList(List<FldLanguage.Language> languageList) {
        LanguageList = languageList;
    }

    public List<String> getFld() {
        return fld;
    }

    public void setFld(List<String> fld) {
        this.fld = fld;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public FldLanguage.Language getBaseLanguage() {
        return baseLanguage;
    }

    public void setBaseLanguage(FldLanguage.Language baseLanguage) {
        this.baseLanguage = baseLanguage;
    }

    public Function<String, Boolean> getFldValueFilter() {
        return fldValueFilter;
    }

    public void setFldValueFilter(Function<String, Boolean> fldValueFilter) {
        this.fldValueFilter = fldValueFilter;
    }

    public List<String> getCacheFilter() {
        return CacheFilter;
    }

    public void setCacheFilter(List<String> cacheFilter) {
        CacheFilter = cacheFilter;
    }
}
