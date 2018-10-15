package com.irille.core.web.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import irille.pub.Log;
import irille.pub.svr.DbPool;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class AppConfig {
    private static final Log LOG = new Log(AppConfig.class);

    private static final Boolean default_dev = false;
    private static final Integer default_query_fetchsize = 500;
    private static final String default_db_config = null;
    public static final String default_i18n_messagefile = "message";

    public static final Boolean dev;
    public static final Integer query_fetchsize;
    public static final String db_config;
    public static final String i18n_messagefile;

    public static final ObjectMapper objectMapper;

    public static String translateAppKey = null;

    static {
        String filepath = "appconfig.properties";
        try {
            InputStream inStream = AppConfig.class.getClassLoader().getResourceAsStream(filepath);
            if (inStream == null) {
                dev = default_dev;
                query_fetchsize = default_query_fetchsize;
                db_config = default_db_config;
                i18n_messagefile = default_i18n_messagefile;
            } else {
                Properties properties = new Properties();
                properties.load(inStream);
                dev = Boolean.valueOf(properties.getProperty("dev", default_dev.toString()));
                query_fetchsize = Integer.valueOf(properties.getProperty("query.fetchsize", default_query_fetchsize.toString()));
                db_config = properties.getProperty("db.config", default_db_config);
                i18n_messagefile = properties.getProperty("i18n.messagefile", default_i18n_messagefile);
                translateAppKey = properties.getProperty("i18n.translateAppKey", null);
            }

            objectMapper = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);
        } catch (Exception e) {
            LOG.err("properties is not exists", "配置文件【{0}】不存在", filepath);
            throw new AssertionError();
        }
    }

    public static void db_connection_close() {
        DbPool.getInstance().removeConn();
    }

    public static void dbpool_release() {
        DbPool.getInstance().releaseAll();
    }

    public static void db_connection_commit() throws SQLException {
        DbPool.getInstance().getConn().commit();
    }

    public static void db_connection_rollback() throws SQLException {
        DbPool.getInstance().getConn().rollback();
    }

}
