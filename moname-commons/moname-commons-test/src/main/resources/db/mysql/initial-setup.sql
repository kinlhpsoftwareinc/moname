-- keycloak schema:
SELECT CONCAT(NOW(), ' [init] creating keycloak''s schema, and user and grating privileges') AS '';
CREATE SCHEMA IF NOT EXISTS `keycloak_test` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
CREATE USER IF NOT EXISTS 'keycloak'@'%' IDENTIFIED BY 'moname';
GRANT ALL PRIVILEGES ON TABLE `keycloak_test`.* TO 'keycloak'@'%';

-- moname schemas:
-- SELECT CONCAT(NOW(), ' [init] grating privileges to the user ''moname''') AS '';
-- GRANT ALL PRIVILEGES ON TABLE *.* TO 'moname'@'%';

-- privileges flushing:
FLUSH PRIVILEGES;
