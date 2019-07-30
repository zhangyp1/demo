package com.newland.paas.paastool.execsql.util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class PaasResourceDatabasePopulator implements DatabasePopulator {
    List<Resource> scripts = new ArrayList<>();

    @Nullable
    private String sqlScriptEncoding;

    private String separator = ScriptUtils.DEFAULT_STATEMENT_SEPARATOR;

    private String commentPrefix = ScriptUtils.DEFAULT_COMMENT_PREFIX;

    private String blockCommentStartDelimiter = ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER;

    private String blockCommentEndDelimiter = ScriptUtils.DEFAULT_BLOCK_COMMENT_END_DELIMITER;

    private boolean continueOnError = false;

    private boolean ignoreFailedDrops = false;

    public void populate(Connection connection) throws ScriptException {
        Assert.notNull(connection, "Connection must not be null");
        for (Resource script : scripts) {
            EncodedResource encodedScript = new EncodedResource(script, this.sqlScriptEncoding);
            PaasScriptUtils.executeSqlScript(connection, encodedScript, this.continueOnError, this.ignoreFailedDrops,
                this.commentPrefix, this.separator, this.blockCommentStartDelimiter, this.blockCommentEndDelimiter);
        }
    }

    public String getSqlScriptEncoding() {
        return sqlScriptEncoding;
    }

    public void setSqlScriptEncoding(String sqlScriptEncoding) {
        this.sqlScriptEncoding = sqlScriptEncoding;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getCommentPrefix() {
        return commentPrefix;
    }

    public void setCommentPrefix(String commentPrefix) {
        this.commentPrefix = commentPrefix;
    }

    public String getBlockCommentStartDelimiter() {
        return blockCommentStartDelimiter;
    }

    public void setBlockCommentStartDelimiter(String blockCommentStartDelimiter) {
        this.blockCommentStartDelimiter = blockCommentStartDelimiter;
    }

    public String getBlockCommentEndDelimiter() {
        return blockCommentEndDelimiter;
    }

    public void setBlockCommentEndDelimiter(String blockCommentEndDelimiter) {
        this.blockCommentEndDelimiter = blockCommentEndDelimiter;
    }

    public boolean isContinueOnError() {
        return continueOnError;
    }

    public void setContinueOnError(boolean continueOnError) {
        this.continueOnError = continueOnError;
    }

    public boolean isIgnoreFailedDrops() {
        return ignoreFailedDrops;
    }

    public void setIgnoreFailedDrops(boolean ignoreFailedDrops) {
        this.ignoreFailedDrops = ignoreFailedDrops;
    }

    public void addScript(Resource script) {
        Assert.notNull(script, "Script must not be null");
        this.scripts.add(script);
    }

}
