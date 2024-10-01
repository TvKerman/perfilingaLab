package tech.reliab.course.perfilinga.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tech.reliab.course.perfilinga.bank.builders.BankBuilder;
import tech.reliab.course.perfilinga.bank.entity.Bank;
import tech.reliab.course.perfilinga.bank.entity.BankAtm;
import tech.reliab.course.perfilinga.bank.entity.BankOffice;
import tech.reliab.course.perfilinga.bank.entity.CreditAccount;
import tech.reliab.course.perfilinga.bank.entity.Employee;
import tech.reliab.course.perfilinga.bank.entity.PaymentAccount;
import tech.reliab.course.perfilinga.bank.entity.User;
import tech.reliab.course.perfilinga.bank.service.BankAtmService;
import tech.reliab.course.perfilinga.bank.service.BankOfficeService;
import tech.reliab.course.perfilinga.bank.service.BankService;
import tech.reliab.course.perfilinga.bank.service.CreditAccountService;
import tech.reliab.course.perfilinga.bank.service.EmployeeService;
import tech.reliab.course.perfilinga.bank.service.PaymentAccountService;
import tech.reliab.course.perfilinga.bank.service.UserService;
import tech.reliab.course.perfilinga.bank.service.impl.BankAtmServiceImpl;
import tech.reliab.course.perfilinga.bank.service.impl.BankOfficeServiceImpl;
import tech.reliab.course.perfilinga.bank.service.impl.BankServiceImpl;
import tech.reliab.course.perfilinga.bank.service.impl.CreditAccountServiceImpl;
import tech.reliab.course.perfilinga.bank.service.impl.EmployeeServiceImpl;
import tech.reliab.course.perfilinga.bank.service.impl.PaymentAccountServiceImpl;
import tech.reliab.course.perfilinga.bank.service.impl.UserServiceImpl;

import java.time.LocalDate;

@SpringBootApplication
public class PerfilinLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerfilinLabApplication.class, args);
        var bankBuilder = new BankBuilder();
        var bank = bankBuilder.setBankName("Великий Китай Банк").createBank();


        UserService userService = new UserServiceImpl();
        User user = userService.createUser("Перфилин Григорий Александрович", LocalDate.now(), "Программист/Товарищ/Член Партии/");

        BankService bankService = new BankServiceImpl(userService);
        bankService.registerBank(bank);

        BankOfficeService bankOfficeService = new BankOfficeServiceImpl(bankService);
        BankOffice bankOffice = bankOfficeService.createBankOffice(
                "Народный банк Китая",
                "город центрального подчинения Пекин, район Сичэн",
                true,
                true,
                true,
                true,
                500,
                bank
        );

        EmployeeService employeeService = new EmployeeServiceImpl(bankService);
        Employee employee = employeeService.createEmployee(
                "Товарищ Си",
                LocalDate.now(),
                "Товарищ Си",
                bank,
                false,
                bankOffice,
                true,
                30000
        );

        BankAtmService bankAtmService = new BankAtmServiceImpl(bankService);
        BankAtm bankAtm = bankAtmService.createBankAtm(
                "Банкомат",
                "Кутузовский 35",
                bank,
                bankOffice,
                employee,
                true,
                true,
                500
        );

        PaymentAccountService paymentAccountService = new PaymentAccountServiceImpl(userService, bankService);
        PaymentAccount paymentAccount = paymentAccountService.createPaymentAccount(user, bank);

        CreditAccountService creditAccountService = new CreditAccountServiceImpl(userService, bankService);
        CreditAccount creditAccount = creditAccountService.createCreditAccount(
                user,
                bank,
                LocalDate.now(),
                8,
                500000,
                14,
                employee,
                paymentAccount
        );

        System.out.println(bank);
        System.out.println(user);
        System.out.println(bankOffice);
        System.out.println(employee);
        System.out.println(bankAtm);
        System.out.println(paymentAccount);
        System.out.println(creditAccount);
    }
}