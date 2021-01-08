package org.guohai.javasqlweb.service.operation;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.guohai.javasqlweb.beans.*;
import org.guohai.javasqlweb.controller.BaseDataController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DBOperationOracle implements DBOperation  {

    private static final Logger LOG  = LoggerFactory.getLogger(DBOperationOracle.class);

    private static DataSource sqlDs;

    private String  connConfigName;

    DBOperationOracle(ConnectConfigBean conn) throws Exception {
        connConfigName = conn.getDbServerName();
        Map dbConfig = new HashMap();
        dbConfig.put("url",String.format("jdbc:mysql://%s:%s",conn.getDbServerHost(),conn.getDbServerPort()));
        dbConfig.put("username",conn.getDbServerUsername());
        dbConfig.put("password",conn.getDbServerPassword());
        dbConfig.put("initialSize","5");
        dbConfig.put("validationQuery","SELECT now();");
        sqlDs = DruidDataSourceFactory.createDataSource(dbConfig);
    }
    /**
     * 获得实例服务器库列表
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Override
    public List<DatabaseNameBean> getDbList() throws SQLException, ClassNotFoundException {
        List<DatabaseNameBean> listDnb = new ArrayList<>();
        getActiveCount();
        Connection conn =sqlDs.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SHOW DATABASES;");
        while (rs.next()){
            listDnb.add(new DatabaseNameBean(rs.getString("Database")));
        }
        // 关闭rs和statement,释放连接回连接池
        closeResource(rs, st, conn);
        return listDnb;
    }

    /**
     * 获得实例指定库的所有表名
     *
     * @param dbName 库名
     * @return 返回集合
     * @throws SQLException 抛出异常
     */
    @Override
    public List<TablesNameBean> getTableList(String dbName) throws SQLException {
        return null;
    }

    /**
     * 获取所有列名
     *
     * @param dbName
     * @param tableName
     * @return
     * @throws SQLException 抛出异常
     */
    @Override
    public List<ColumnsNameBean> getColumnsList(String dbName, String tableName) throws SQLException {
        return null;
    }

    /**
     * 获取所有的索引数据
     *
     * @param dbName
     * @param tableName
     * @return
     * @throws SQLException 抛出异常
     */
    @Override
    public List<TableIndexesBean> getIndexesList(String dbName, String tableName) throws SQLException {
        return null;
    }

    /**
     * 获取指定库的所有存储过程列表
     *
     * @param dbName
     * @return
     * @throws SQLException
     */
    @Override
    public List<StoredProceduresBean> getStoredProceduresList(String dbName) throws SQLException {
        return null;
    }

    /**
     * 获取指定存储过程内容
     *
     * @param dbName
     * @param spName
     * @return
     * @throws SQLException
     */
    @Override
    public StoredProceduresBean getStoredProcedure(String dbName, String spName) throws SQLException {
        return null;
    }

    /**
     * 执行查询的SQL
     *
     * @param dbName
     * @param sql
     * @return
     * @throws SQLException 抛出异常
     */
    @Override
    public Object queryDatabaseBySql(String dbName, String sql) throws SQLException {
        return null;
    }

    private void getActiveCount(){
        DruidDataSource drs = (DruidDataSource) sqlDs;
        int activeCount = drs.getActiveCount();
        LOG.info(String.format("目前%s的连接数%d", connConfigName,activeCount));
    }

    /**
     * 关闭连接
     * @param resultSet
     * @param statement
     * @param connection
     */
    private void closeResource(ResultSet resultSet, Statement statement, Connection connection){
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if(null != connection){
                connection.close();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

    }
}
