package com.bix.config.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableTransactionManagement
@Configuration
@Slf4j
public class MybatisPlusConfig1 {

//    @Value("${spring.sqlSource}")
//    private String sqlSource;
//
//    @Autowired
//    private MysqlDataSource mysqlDataSource;


    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor4Oracle() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.ORACLE));
        return mybatisPlusInterceptor;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor4MySql() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return mybatisPlusInterceptor;
    }

    @Bean(name = "myDataSource")
    public DataSource myDataSource() {

        return DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://47.98.167.119:3306/zmx?useUnicode=true&characterEncoding=utf-8&useSSL=false")
                .password("Admin123!")
                .username("root")
                .build();

    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(DataSourceBuilder.create()
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .url("jdbc:mysql://47.98.167.119:3306/zmx?useUnicode=true&characterEncoding=utf-8&useSSL=false")
                .password("Admin123!")
                .username("root")
                .build());
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        //添加分页功能
        sqlSessionFactory.setPlugins(mybatisPlusInterceptor4MySql());
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper/*.xml"));

        //获取mybatis-plus全局配置
        GlobalConfig globalConfig = GlobalConfigUtils.defaults();
        //mybatis-plus全局配置设置元数据对象处理器为自己实现的那个
        globalConfig.setMetaObjectHandler(new CommonFieldMetaObjectHandler());
        sqlSessionFactory.setGlobalConfig(globalConfig);
        return sqlSessionFactory.getObject();
    }
}
