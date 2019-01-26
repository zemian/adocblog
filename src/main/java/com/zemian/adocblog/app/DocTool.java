package com.zemian.adocblog.app;

import com.zemian.adocblog.CommonConfig;
import com.zemian.adocblog.app.support.CmdOpts;
import com.zemian.adocblog.service.DocService;
import com.zemian.adocblog.service.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

public class DocTool {
    @Configuration
    @Import({ServiceConfig.class, CommonConfig.class})
    public static class Config {
        @Bean
        public DocTool docTool() {
            return new DocTool();
        }
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext spring = new AnnotationConfigApplicationContext(Config.class);
        DocTool main = spring.getBean(DocTool.class);
        main.run(args);
        spring.close();
    }
    private static Logger LOG = LoggerFactory.getLogger(DocTool.class);

    @Autowired
    private DocService docService;

    private void printHelp() {
        System.out.println("Document management tool.\n" +
                "\n" +
                "Usage:\n" +
                "  DocTool removeMarkForDelete [--sinceMonths=6]\n" +
                "    Remove marked for delete docs older than N months.\n" +
                "  DocTool removeMarkForDelete [sinceDatetime=YYYY-MM-DD'T'HH:MM]\n" +
                "    Remove marked for delete docs since a DATE.\n" +
                "\n" +
                "  DocTool exportLatest [--path=/path/to/store/export]\n" +
                "    Export all latest docs into a directory path.\n" +
                "\n");
    }

    public void run(String[] args) {
        CmdOpts opts = new CmdOpts(args);

        String cmd = opts.getArgOrError(0, "Missing command argument.");

        if ("help".equals(cmd)) {
            printHelp();
            System.exit(1);
        } else if ("removeMarkForDelete".equals(cmd)) {
            LocalDateTime sinceDt;
            if (opts.hasOpt("sinceMonths")) {
                sinceDt = LocalDateTime.now().minusMonths(opts.getIntOpt("sinceMonths"));
            } else {
                sinceDt = LocalDateTime.parse(opts.getOpt("sinceDatetime"));
            }
            docService.removeOldDocs(sinceDt);
        }  else if ("exportLatest".equals(cmd)) {
            String path = opts.getOpt("path", "target/export");
            docService.export(path);
        } else {
            System.out.println("ERROR: invalid arguments");
            printHelp();
            System.exit(1);
        }
    }
}
