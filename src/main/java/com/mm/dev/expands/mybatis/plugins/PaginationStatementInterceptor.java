package com.mm.dev.expands.mybatis.plugins;

import com.mm.dev.expands.mybatis.dialect.Dialect;
import com.mm.dev.expands.mybatis.dialect.MySql5Dialect;
import com.mm.dev.expands.mybatis.dialect.OracleDialect;
import com.mm.dev.expands.mybatis.dialect.SQLServer2005Dialect;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.springframework.data.domain.Pageable;

import java.sql.Connection;
import java.util.Properties;

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PaginationStatementInterceptor implements Interceptor {

    private final static Log log = LogFactory
            .getLog(PaginationStatementInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

        ParameterHandler parameterHandler = statementHandler.getParameterHandler();
        Object parameterObject = parameterHandler.getParameterObject();

        Pageable pagination = null;

        if(parameterObject instanceof MapperMethod.ParamMap){

            MapperMethod.ParamMap paramMapObject = (MapperMethod.ParamMap)parameterObject ;


            if(paramMapObject != null){
                for(Object key : paramMapObject.keySet()){
                    if(paramMapObject.get(key) instanceof  Pageable){
                        pagination = (Pageable) paramMapObject.get(key);
                        break;
                    }
                }
            }
        }

        if (pagination != null) {

            MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, new DefaultObjectFactory(), new DefaultObjectWrapperFactory());
            Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");
            Dialect.Type databaseType = null;

            try {
                databaseType = Dialect.Type.valueOf(configuration.getVariables().getProperty("dialect").toUpperCase());
            } catch (Exception e) {
                throw new Exception("Generate SQL: Obtain DatabaseType Failed!");
            }

            Dialect dialect = null;
            switch (databaseType) {
                case MYSQL:
                    dialect = new MySql5Dialect();
                    break;
                case ORACLE:
                    dialect = new OracleDialect();
                    break;
                case SQLSERVER:
                    dialect = new SQLServer2005Dialect();
                    break;
            }

            String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
            metaStatementHandler.setValue("delegate.boundSql.sql", dialect.getLimitString(originalSql, pagination.getPageNumber(), pagination.getPageSize()));

            if (log.isDebugEnabled()) {
                BoundSql boundSql = statementHandler.getBoundSql();
                log.debug("Generate SQL : " + boundSql.getSql());
            }

            return invocation.proceed();
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
