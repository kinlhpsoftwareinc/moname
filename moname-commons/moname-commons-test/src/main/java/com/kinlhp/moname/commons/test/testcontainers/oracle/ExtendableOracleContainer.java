//package com.kinlhp.moname.commons.test.testcontainers.oracle;
//
//import org.testcontainers.containers.JdbcDatabaseContainer;
//import org.testcontainers.containers.OracleContainer;
//
//import com.kinlhp.moname.commons.test.testcontainers.JdbcDatabaseUriExtractableContainer;
//
///**
// * This is a necessary workaround because of the <s>fucking</s> poor implementation of class
// * {@link org.testcontainers.containers.OracleContainer OracleContainer}, which simply stops the propagation of generics
// * {@link SELF SELF} defined in classes {@link JdbcDatabaseContainer<SELF> JdbcDatabaseContainer}.
// */
//@SuppressWarnings({"rawtypes", "java:S119"})
//interface ExtendableOracleContainer<SELF extends OracleContainer & ExtendableOracleContainer<SELF>>
//		extends JdbcDatabaseUriExtractableContainer {
//}
