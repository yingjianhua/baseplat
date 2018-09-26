package com.irille.core.commons.i18n.translateLanguage;

import java.util.List;

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
        	List<PubTrantslate> list = Query.SELECT(PubTrantslate.class).WHERE(T.HASHCODE, "=?", sourceText.hashCode()).WHERE(T.TARGET, "=?", targetLanguage).queryList();
            if (list.size() > 0) {
                PubTrantslate pubTrantslate = list.get(0);
                TranslateBean translateBean = new TranslateBean();
                translateBean.setTargetLanguage(pubTrantslate.getTarget());
                translateBean.setText(pubTrantslate.getTargetText());
                return translateBean;
            }
            return null;
        }
    }

    public static class Ins extends IduIns<Ins, PubTrantslate> {
        @Override
        public void before() {
            getB().setCreatedTime(Env.getSystemTime());
            super.before();
        }
    }

}