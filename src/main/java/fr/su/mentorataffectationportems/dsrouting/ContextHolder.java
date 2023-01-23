package fr.su.mentorataffectationportems.dsrouting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ContextHolder {
    private static ContextHolder holder;
    private static ThreadLocal<String> context = new ThreadLocal<>();

    @Value("${DATASOURCE_LIST}")
    private String[] listDatasource;

    private static String[] listDatasourceStatic;

    @Value("${DATASOURCE_LIST}")
    public void setListDatasourceStatic(String[] listDatasource) {
        ContextHolder.listDatasourceStatic = listDatasource;
    }


    public static ContextHolder getInstance() {
        if (holder == null) {
            holder = new ContextHolder();
            context.set(Arrays.asList(listDatasourceStatic).get(0));
        }
        return holder;
    }

    public String getLastUsedDataSourceIdentifier() {
        return context.get();
    }

    public void use(String dataSourceId) {
        context.set(dataSourceId);
    }

    public void unload() {
        context.remove();
    }
}