package com.mm.dev.expands.mybatis.plugins;

import com.mm.dev.expands.mybatis.dialect.Dialect;
import com.mm.dev.expands.mybatis.dialect.MySql5Dialect;
import com.mm.dev.expands.mybatis.dialect.OracleDialect;
import com.mm.dev.expands.mybatis.dialect.SQLServer2005Dialect;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
public class PaginationResultSetInterceptor implements Interceptor {

    private final static Log log = LogFactory.getLog(PaginationResultSetInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        DefaultResultSetHandler resultSetHandler = (DefaultResultSetHandler) invocation.getTarget();
        MetaObject metaResultSetHandler = MetaObject.forObject(resultSetHandler, new DefaultObjectFactory(), new DefaultObjectWrapperFactory());

        try {
            ParameterHandler parameterHandler = (ParameterHandler) metaResultSetHandler.getValue("parameterHandler");
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

                BoundSql boundSql = (BoundSql) metaResultSetHandler.getValue("parameterHandler.boundSql");
                Configuration configuration = (Configuration) metaResultSetHandler.getValue("configuration");
                String originalSql = boundSql.getSql();


                Dialect.Type databaseType = Dialect.Type.valueOf(configuration.getVariables().getProperty("dialect").toUpperCase());
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


                // 修改sql，用于返回总记录数
                String sql = dialect.getCountString(originalSql);
                Long totalRecord = getTotalRecord(configuration, sql);


                Object result = invocation.proceed();
                Page page = new PageImpl((List)result, pagination, totalRecord);


//                // 设置返回对象类型
//                metaResultSetHandler.setValue("mappedStatement.resultMaps[0].type.name", Page.class.getName());

                // 设置返回值
                List<Page> pageList = new ArrayList<Page>();
                pageList.add(page);

                return pageList;
            }
        } catch (Exception e) {
            throw new Exception("Overwrite SQL : Fail!");
        }

        return invocation.proceed();
    }

    /**
     * 获取总记录数
     * @param sql
     * @return
     */
    private Long getTotalRecord(Configuration configuration, String sql){

        Long result = 0L;

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        SqlSession session = sqlSessionFactory.openSession();

        try {
            PreparedStatement statement = session.getConnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                result = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
