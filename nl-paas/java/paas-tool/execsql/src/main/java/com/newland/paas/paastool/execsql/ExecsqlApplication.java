package com.newland.paas.paastool.execsql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.newland.paas.paastool.execsql.service.ExecSqlService;

@SpringBootApplication
public class ExecsqlApplication implements CommandLineRunner {

    @Autowired
    private ExecSqlService execSqlService;
    @Value("${scripts:none}")
    private String scripts;

    public static void main(String[] args) {
        SpringApplication.run(ExecsqlApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (args.length > 0) {
            for (String string : args) {
                int index = string.indexOf("scripts=");
                if (index != -1) {
                    scripts = string.split("scripts=")[1];
                    break;
                }
            }
        }
        if (scripts == null || scripts.equals("none")) {
            throw new ExitException();
        }
        System.out.println("scripts:" + scripts);
        execSqlService.runScriptsByPath(scripts);
    }
}
