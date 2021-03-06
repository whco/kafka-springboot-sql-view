package main;

import config.AppCtx;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainForSpring {
//    private static Assembler assembler = new Assembler();
    private static ApplicationContext ctx = null;

    private static void processNewCommand(String[] arg) {
        if (arg.length != 5) {
            printHelp();
            return;
        }

        MemberRegisterService registerService = ctx.getBean(MemberRegisterService.class);
        RegisterRequest request = new RegisterRequest();
        request.setEmail(arg[1]);
        request.setName(arg[2]);
        request.setPassword(arg[3]);
        request.setConfirmPassword(arg[4]);

        if (!request.isPasswordEqualToConfirmPassword()) {
            System.out.println("암호와 확인이 일치하지 않습니다");
            return;
        }

        try {
            registerService.regist(request);
            System.out.println("등록했습니다");
        } catch (DuplicateMemberException e) {
            System.out.println("이미 존재하는 이메일입니다.");
        }
    }

    private static void processChangeCommand(String[] arg) {
        if (arg.length != 4) {
            printHelp();
            return;
        }

        ChangePasswordService changePasswordService = ctx.getBean(ChangePasswordService.class);

        try {
            changePasswordService.changePassword(arg[1], arg[2], arg[3]);
            System.out.println("암호를 변경했습니다");
        } catch (MemberNotFoundException e) {
            System.out.println("존재하지 않는 이메일입니다");
        } catch (WrongIdPasswordException e) {
            System.out.println("이메일과 암호가 일치하지 않습니다");
        }
    }

    private static void processGetCommand(String[] arg) {
//        if (arg.length != 3) {
//            printHelp();
//            return;
//        }
//
//        MemberLookUpService memberLookUpService = assembler.getMemberLookUpService();
//
//        Member member = memberLookUpService.getMember(arg[1], arg[2]);
//
//        System.out.println(member.toString());
//        return;

    }

    private static void processListCommand() {
        MemberListPrinter memberListPrinter = ctx.getBean("listPrinter", MemberListPrinter.class);
        memberListPrinter.printAll();
    }

    private static void processInfoCommand(String[] arg) {
        if (arg.length != 2) {
            printHelp();
            return;
        }

        MemberInfoPrinter memberInfoPrinter = ctx.getBean("infoPrinter", MemberInfoPrinter.class);
        memberInfoPrinter.printMemberInfo(arg[1]);
    }

    private static void processVersionCommand() {
        VersionPrinter versionPrinter = ctx.getBean("versionPrinter", VersionPrinter.class);
        versionPrinter.print();
    }

    private static void printHelp() {
        System.out.println();
        System.out.println("잘못된 명령입니다. 사용법을 확인하세요");
        System.out.println("명령어 사용법 : ");
        System.out.println("new 이메일 이름 암호 암호확인");
        System.out.println("change 이메일 현재비번 변경비번");
        System.out.println("get 이메일 암호");
        System.out.println();
    }



    public static void main(String[] args) throws IOException {
        ctx = new AnnotationConfigApplicationContext(AppCtx.class);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("명령어를 입력하세요");
            String command = reader.readLine();

            if (command.equalsIgnoreCase("exit")) {
                System.out.println("종료합니다");
                break;
            }

            if (command.startsWith("new ")) {
                processNewCommand(command.split(" "));
                continue;
            } else if (command.startsWith("change ")) {
                processChangeCommand(command.split(" "));
                continue;
            } else if (command.startsWith("get ")) {
                processGetCommand(command.split(" "));
                continue;
            } else if (command.equals("list")) {
                processListCommand();
                continue;
            } else if (command.startsWith("info ")) {
                processInfoCommand(command.split(" "));
                continue;
            } else if (command.equals("version")) {
                processVersionCommand();
                continue;
            }
            printHelp();
        }
    }
}
