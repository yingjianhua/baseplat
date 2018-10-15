package com.irille.core.commons.i18n.translateLanguage;

import java.util.Date;

import com.irille.core.commons.i18n.translateLanguage.PubTrantslate.T;
import com.irille.core.repository.Query;

import irille.pub.idu.IduIns;
import irille.pub.idu.IduOther;
import irille.pub.svr.Env;

/**
 * Created by IntelliJ IDEA.
 * User: lijie@shoestp.cn
 * Date: 2018/9/7
 * Time: 13:26
 */
public class PubTrantslateDAO {
    public static class Select extends IduOther<Select, PubTrantslate> {
    	
        public TranslateBean getTransLatesByHashCode(String sourceText, String  targetLanguage) {
        	PubTrantslate bean = Query.SELECT(PubTrantslate.class).WHERE(T.HASHCODE, "=?", sourceText.hashCode()).WHERE(T.TARGET, "=?", targetLanguage).query();
        	if(bean!=null) {
        		TranslateBean translateBean = new TranslateBean();
        		translateBean.setTargetLanguage(bean.getTarget());
        		translateBean.setText(bean.getTargetText());
        		return translateBean;
        	} else {
        		return null;
        	}
        }
    }
    public static void ins(PubTrantslate bean) {
    	bean.setCreatedTime(new Date());
    	bean.ins();
    }

    public static class Ins extends IduIns<Ins, PubTrantslate> {
        @Override
        public void before() {
            getB().setCreatedTime(Env.getSystemTime());
            super.before();
        }
    }

}