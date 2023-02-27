#!/usr/bin/env bash
# Author: Luis Henrique Pereira <lhp.kin@gmail.com>
# Description: Executes the initial-setup.sql script
# Version: 1.0.0.BUILD-SNAPSHOT

# In the background in a subshell, wait to be sure that SQL Server came up then
# run initial setup for DATABASE and LOGIN credentials
# Note: make sure that the SA password matches with ${MSSQL_SA_PASSWORD}
sleep 5s && \
/opt/mssql-tools/bin/sqlcmd -S sqlserver -U sa -P ${MSSQL_SA_PASSWORD} \
-d master -i ${HOME}/initial-setup.sql &

# Run SQl Server
/opt/mssql/bin/sqlservr

# //TODO: Shutdown with no graceful degradation
#
# Expected behavior (log):
# ...
# 2023-03-04 06:15:49.80 spid27s     Recovery is complete. This is an informational message only. No user action is required.
# 2023-03-04 06:15:49.81 spid35s     The default language (LCID 0) has been set for engine and full-text services.
# 2023-03-04 06:15:49.97 spid35s     The tempdb database has 8 data file(s).
# 2023-03-04 06:17:51.78 spid73      [5]. Feature Status: PVS: 0. CTR: 0. ConcurrentPFSUpdate: 1. ConcurrentGAMUpdate: 1. ConcurrentSGAMUpdate: 1, CleanupUnderUserTransaction: 0. TranLevelPVS: 0
# 2023-03-04 06:17:51.78 spid73      Starting up database 'moname_commons_jpa_test'.
# 2023-03-04 06:17:51.78 spid73      RemoveStaleDbEntries: Cleanup of stale DB entries called for database ID: [5]
# 2023-03-04 06:17:51.78 spid73      RemoveStaleDbEntries: Cleanup of stale DB entries skipped because master db is not memory optimized. DbId: 5.
# 2023-03-04 06:17:51.80 spid73      Parallel redo is started for database 'moname_commons_jpa_test' with worker pool size [4].
# 2023-03-04 06:17:51.81 spid73      Parallel redo is shutdown for database 'moname_commons_jpa_test' with worker pool size [4].
# 2023-03-04 06:18:41.20 spid27s     Always On: The availability replica manager is going offline because SQL Server is shutting down. This is an informational message only. No user action is required.
# 2023-03-04 06:18:41.20 spid27s     SQL Server is terminating in response to a 'stop' request from Service Control Manager. This is an informational message only. No user action is required.
# 2023-03-04 06:18:41.21 spid27s     .NET Framework runtime has been stopped.
# RecoveryUnit::Shutdown. IsOnline: 02023-03-04 06:18:41.22 spid27s     RBPEX::NotifyFileShutdown: Called for database ID: [32762], file Id [0]
# RecoveryUnit::Shutdown. IsOnline: 02023-03-04 06:18:41.23 spid27s     RBPEX::NotifyFileShutdown: Called for database ID: [32761], file Id [0]
# RecoveryUnit::Shutdown. IsOnline: 02023-03-04 06:18:41.24 spid27s     RBPEX::NotifyFileShutdown: Called for database ID: [5], file Id [0]
# RecoveryUnit::Shutdown. IsOnline: 02023-03-04 06:18:41.26 spid27s     RBPEX::NotifyFileShutdown: Called for database ID: [4], file Id [0]
# RecoveryUnit::Shutdown. IsOnline: 02023-03-04 06:18:41.26 spid27s     RBPEX::NotifyFileShutdown: Called for database ID: [3], file Id [0]
# 2023-03-04 06:18:41.28 spid27s     SQL Trace was stopped due to server shutdown. Trace ID = '1'. This is an informational message only; no user action is required.
# sqlserver exited with code 255
