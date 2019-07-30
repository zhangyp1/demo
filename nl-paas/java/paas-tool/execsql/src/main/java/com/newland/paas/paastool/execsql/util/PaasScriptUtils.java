package com.newland.paas.paastool.execsql.util;

import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.CannotReadScriptException;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptStatementFailedException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * 非自动提交事务，执行SQL脚本
 * 
 * @author SongDi
 * @date 2018/12/14
 */
public class PaasScriptUtils extends ScriptUtils {
    private static final Log logger = LogFactory.getLog(PaasScriptUtils.class);

    public static void executeSqlScript(Connection connection, EncodedResource resource, boolean continueOnError,
        boolean ignoreFailedDrops, String commentPrefix, @Nullable String separator, String blockCommentStartDelimiter,
        String blockCommentEndDelimiter) throws ScriptException {

        try {
            connection.setAutoCommit(false);
            if (logger.isDebugEnabled()) {
                logger.debug("Executing SQL script from " + resource);
            }
            long startTime = System.currentTimeMillis();

            String script;
            try {
                script = readScript(resource, commentPrefix, separator);
            } catch (IOException ex) {
                throw new CannotReadScriptException(resource, ex);
            }

            if (separator == null) {
                separator = DEFAULT_STATEMENT_SEPARATOR;
            }
            if (!EOF_STATEMENT_SEPARATOR.equals(separator) && !containsSqlScriptDelimiters(script, separator)) {
                separator = FALLBACK_STATEMENT_SEPARATOR;
            }

            List<String> statements = new ArrayList<>();
            splitSqlScript(resource, script, separator, commentPrefix, blockCommentStartDelimiter,
                blockCommentEndDelimiter, statements);

            int stmtNumber = 0;
            Statement stmt = connection.createStatement();
            try {
                for (String statement : statements) {
                    stmtNumber++;
                    try {
                        stmt.execute(statement);
                        int rowsAffected = stmt.getUpdateCount();
                        if (logger.isDebugEnabled()) {
                            logger.debug(rowsAffected + " returned as update count for SQL: " + statement);
                            SQLWarning warningToLog = stmt.getWarnings();
                            while (warningToLog != null) {
                                logger.debug("SQLWarning ignored: SQL state '" + warningToLog.getSQLState()
                                    + "', error code '" + warningToLog.getErrorCode() + "', message ["
                                    + warningToLog.getMessage() + "]");
                                warningToLog = warningToLog.getNextWarning();
                            }
                        }
                    } catch (SQLException ex) {
                        boolean dropStatement = StringUtils.startsWithIgnoreCase(statement.trim(), "drop");
                        if (continueOnError || (dropStatement && ignoreFailedDrops)) {
                            if (logger.isDebugEnabled()) {
                                logger.debug(
                                    ScriptStatementFailedException.buildErrorMessage(statement, stmtNumber, resource),
                                    ex);
                            }
                        } else {
                            throw new ScriptStatementFailedException(statement, stmtNumber, resource, ex);
                        }
                    }
                }
                connection.commit();
            } catch (Exception ex) {
                connection.rollback();
                throw ex;
            } finally {
                try {
                    stmt.close();
                    connection.close();
                } catch (Throwable ex) {
                    logger.trace("Could not close JDBC Statement", ex);
                }
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            if (logger.isDebugEnabled()) {
                logger.debug("Executed SQL script from " + resource + " in " + elapsedTime + " ms.");
            }
        } catch (Exception ex) {
            try {
                connection.close();
            } catch (Throwable e) {
                logger.trace("Could not close JDBC Statement", ex);
            }
            if (ex instanceof ScriptException) {
                throw (ScriptException)ex;
            }
            throw new UncategorizedScriptException("Failed to execute database script from resource [" + resource + "]",
                ex);
        }
    }

    private static String readScript(EncodedResource resource, @Nullable String commentPrefix,
        @Nullable String separator) throws IOException {

        LineNumberReader lnr = new LineNumberReader(resource.getReader());
        try {
            return readScript(lnr, commentPrefix, separator);
        } finally {
            lnr.close();
        }
    }

}
