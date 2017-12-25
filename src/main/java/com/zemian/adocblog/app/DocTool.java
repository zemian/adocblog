package com.zemian.adocblog.app;

import com.zemian.adocblog.AppException;
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
        AnnotationConfigApplicationContext spring = new AnnotationConfigApplicationContext(UserTool.Config.class);
        DocTool main = spring.getBean(DocTool.class);
        main.run(args);
        spring.close();
    }
    private static Logger LOG = LoggerFactory.getLogger(UserTool.class);

    @Autowired
    private DocService docService;

    private void printHelp() {
        System.out.println("Document managment tool.\n" +
                "\n" +
                "Usage: (remove marked for delete docs)\n" +
                "  DocTool --sinceMonths=6 removeMarkForDelete\n" +
                "  DocTool --sinceDatetime=YYYY-MM-DD'T'HH:MM removeMarkForDelete\n" +
                "\n");
    }

    public void run(String[] args) {
        CmdOpts opts = new CmdOpts(args);

        String cmd = opts.getArgOrError(0, "Missing command argument.");

        if ("removeMarkForDelete".equals(cmd)) {
            LocalDateTime sinceDt;
            if (opts.hasOpt("sinceMonths")) {
                sinceDt = LocalDateTime.now().minusMonths(opts.getIntOpt("sinceMonths"));
            } else {
                sinceDt = LocalDateTime.parse(opts.getOpt("sinceDatetime"));
            }
            docService.removeOldDocs(sinceDt);
        } else {
            throw new AppException("Invalid command argument: " + cmd);
        }
    }
}
