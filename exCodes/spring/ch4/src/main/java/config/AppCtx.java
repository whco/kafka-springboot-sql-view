package config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;
import spring.*;

@Configuration
@ComponentScan(basePackages = {"spring"}, excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "spring\\..*Dao"))
public class AppCtx {
    @Bean
    public MemberDao memberDao() {
        return new MemberDao();
    }

    //    @Bean
//    public MemberPrinter memberPrinter() {
//        return new MemberPrinter();
//    }
    @Bean
    @Qualifier("memberPrinter")
    public MemberPrinter memberPrinter1() {
        return new MemberPrinter();
    }

    @Bean
    public MemberSummaryPrinter memberPrinter2() {
        return new MemberSummaryPrinter();
    }

    @Bean
    public VersionPrinter versionPrinter() {
        VersionPrinter versionPrinter = new VersionPrinter();
        versionPrinter.setMajorVersion(5);
        versionPrinter.setMinorVersion(0);
        return versionPrinter;
    }
}
