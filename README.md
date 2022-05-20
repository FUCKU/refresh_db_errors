The project has a task to call /actuator/refresh to refresh the db pwd. In the Project also has quartz task to run other biz querys.
Sometimes when task to call /actuator/refresh endPoint automatically, we get some db Connection exceptions.
The old connection will be closed by refresh task, and before db re-connect we will get the exception.

Shouldn't database keep the connection , not be forced to close, right?

If the connection is forced to close, there is a problem with the business query.

Is this a bug?  Or is the way of use to be optimized?

Please see the repeatable validation Demo.
https://github.com/FUCKU/refresh_db_errors.git
logs like as follows(logs from demo):
```text
Hibernate: select verificati0_.id as id1_0_ from t_verification verificati0_
2022-05-20 21:50:36.628 INFO 18060 --- [nio-8080-exec-5] com.zaxxer.hikari.HikariDataSource : HikariPool-1 - Shutdown initiated...
2022-05-20 21:50:36.633 INFO 18060 --- [nio-8080-exec-5] com.zaxxer.hikari.HikariDataSource : HikariPool-1 - Shutdown completed.
2022-05-20 21:50:37.387 WARN 18060 --- [nio-8080-exec-2] com.zaxxer.hikari.pool.ProxyConnection : HikariPool-1 - Connection com.mysql.cj.jdbc.ConnectionImpl@35c2b713 marked as broken because of SQLSTATE(08003), ErrorCode(0)

java.sql.SQLException: Connection is closed
at com.zaxxer.hikari.pool.ProxyConnection$ClosedConnection.lambda$getClosedConnection$0(ProxyConnection.java:515) ~[HikariCP-4.0.3.jar:na]
at com.sun.proxy.$Proxy88.setAutoCommit(Unknown Source) ~[na:na]
at com.zaxxer.hikari.pool.ProxyConnection.setAutoCommit(ProxyConnection.java:414) ~[HikariCP-4.0.3.jar:na]
at com.zaxxer.hikari.pool.HikariProxyConnection.setAutoCommit(HikariProxyConnection.java) ~[HikariCP-4.0.3.jar:na]
at org.hibernate.resource.jdbc.internal.AbstractLogicalConnectionImplementor.begin(AbstractLogicalConnectionImplementor.java:72) ~[hibernate-core-5.6.9.Final.jar:5.6.9.Final]
at org.hibernate.resource.jdbc.internal.LogicalConnectionManagedImpl.begin(LogicalConnectionManagedImpl.java:285) ~[hibernate-core-5.6.9.Final.jar:5.6.9.Final]
at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl$TransactionDriverControlImpl.begin(JdbcResourceLocalTransactionCoordinatorImpl.java:246) ~[hibernate-core-5.6.9.Final.jar:5.6.9.Final]
at org.hibernate.engine.transaction.internal.TransactionImpl.begin(TransactionImpl.java:83) ~[hibernate-core-5.6.9.Final.jar:5.6.9.Final]
at org.springframework.orm.jpa.vendor.HibernateJpaDialect.beginTransaction(HibernateJpaDialect.java:164) ~[spring-orm-5.3.20.jar:5.3.20]
at org.springframework.orm.jpa.JpaTransactionManager.doBegin(JpaTransactionManager.java:421) ~[spring-orm-5.3.20.jar:5.3.20]
at org.springframework.transaction.support.AbstractPlatformTransactionManager.startTransaction(AbstractPlatformTransactionManager.java:400) ~[spring-tx-5.3.20.jar:5.3.20]
at org.springframework.transaction.support.AbstractPlatformTransactionManager.getTransaction(AbstractPlatformTransactionManager.java:373) ~[spring-tx-5.3.20.jar:5.3.20]

2022-05-20 21:56:55.234 WARN 22928 --- [nio-8080-exec-5] com.zaxxer.hikari.HikariConfig : HikariPool-2 - idleTimeout has been set but has no effect because the pool is operating as a fixed size pool.
2022-05-20 21:56:55.234 INFO 22928 --- [nio-8080-exec-5] com.zaxxer.hikari.HikariDataSource : HikariPool-2 - Starting...
2022-05-20 21:56:55.237 INFO 22928 --- [nio-8080-exec-5] com.zaxxer.hikari.HikariDataSource : HikariPool-2 - Start completed.
```

OR like this 

```text
Hibernate: select verificati0_.id as id1_0_ from t_verification verificati0_
2022-05-20 21:50:36.628 INFO 18060 --- [nio-8080-exec-5] com.zaxxer.hikari.HikariDataSource : HikariPool-1 - Shutdown initiated...
2022-05-20 21:50:37.387 WARN 18060 --- [nio-8080-exec-2] com.zaxxer.hikari.pool.ProxyConnection : HikariPool-1 - Connection com.mysql.cj.jdbc.ConnectionImpl@35c2b713 marked as broken because of SQLSTATE(08003), ErrorCode(0)

java.sql.SQLException: Connection is closed
at com.zaxxer.hikari.pool.ProxyConnection$ClosedConnection.lambda$getClosedConnection$0(ProxyConnection.java:515) ~[HikariCP-4.0.3.jar:na]
at com.sun.proxy.$Proxy88.setAutoCommit(Unknown Source) ~[na:na]
at com.zaxxer.hikari.pool.ProxyConnection.setAutoCommit(ProxyConnection.java:414) ~[HikariCP-4.0.3.jar:na]
at com.zaxxer.hikari.pool.HikariProxyConnection.setAutoCommit(HikariProxyConnection.java) ~[HikariCP-4.0.3.jar:na]
at org.hibernate.resource.jdbc.internal.AbstractLogicalConnectionImplementor.begin(AbstractLogicalConnectionImplementor.java:72) ~[hibernate-core-5.6.9.Final.jar:5.6.9.Final]
at org.hibernate.resource.jdbc.internal.LogicalConnectionManagedImpl.begin(LogicalConnectionManagedImpl.java:285) ~[hibernate-core-5.6.9.Final.jar:5.6.9.Final]
at org.hibernate.resource.transaction.backend.jdbc.internal.JdbcResourceLocalTransactionCoordinatorImpl$TransactionDriverControlImpl.begin(JdbcResourceLocalTransactionCoordinatorImpl.java:246) ~[hibernate-core-5.6.9.Final.jar:5.6.9.Final]
at org.hibernate.engine.transaction.internal.TransactionImpl.begin(TransactionImpl.java:83) ~[hibernate-core-5.6.9.Final.jar:5.6.9.Final]
at org.springframework.orm.jpa.vendor.HibernateJpaDialect.beginTransaction(HibernateJpaDialect.java:164) ~[spring-orm-5.3.20.jar:5.3.20]
at org.springframework.orm.jpa.JpaTransactionManager.doBegin(JpaTransactionManager.java:421) ~[spring-orm-5.3.20.jar:5.3.20]
at org.springframework.transaction.support.AbstractPlatformTransactionManager.startTransaction(AbstractPlatformTransactionManager.java:400) ~[spring-tx-5.3.20.jar:5.3.20]
at org.springframework.transaction.support.AbstractPlatformTransactionManager.getTransaction(AbstractPlatformTransactionManager.java:373) ~[spring-tx-5.3.20.jar:5.3.20]
2022-05-20 21:56:55.234 INFO 18060 --- [nio-8080-exec-5] com.zaxxer.hikari.HikariDataSource : HikariPool-1 - Shutdown completed.
2022-05-20 21:56:55.234 WARN 22928 --- [nio-8080-exec-5] com.zaxxer.hikari.HikariConfig : HikariPool-2 - idleTimeout has been set but has no effect because the pool is operating as a fixed size pool.
2022-05-20 21:56:55.234 INFO 22928 --- [nio-8080-exec-5] com.zaxxer.hikari.HikariDataSource : HikariPool-2 - Starting...
2022-05-20 21:56:55.237 INFO 22928 --- [nio-8080-exec-5] com.zaxxer.hikari.HikariDataSource : HikariPool-2 - Start completed.

```
