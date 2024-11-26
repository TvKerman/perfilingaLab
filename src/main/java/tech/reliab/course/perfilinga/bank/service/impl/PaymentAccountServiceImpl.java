package tech.reliab.course.perfilinga.bank.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.reliab.course.perfilinga.bank.entity.PaymentAccount;
import tech.reliab.course.perfilinga.bank.model.PaymentAccountRequest;
import tech.reliab.course.perfilinga.bank.repository.PaymentAccountRepository;
import tech.reliab.course.perfilinga.bank.service.BankService;
import tech.reliab.course.perfilinga.bank.service.PaymentAccountService;
import tech.reliab.course.perfilinga.bank.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PaymentAccountServiceImpl implements PaymentAccountService {

    private final PaymentAccountRepository paymentAccountRepository;
    private final UserService userService;
    private final BankService bankService;

    public PaymentAccount createPaymentAccount(PaymentAccountRequest paymentAccountRequest) {
        PaymentAccount paymentAccount = new PaymentAccount(userService.getUserById(paymentAccountRequest.getUserId()),
                bankService.getBankById(paymentAccountRequest.getBankId()));
        return paymentAccountRepository.save(paymentAccount);
    }

    public PaymentAccount getPaymentAccountById(int id) {
        return paymentAccountRepository.findById(id).orElseThrow(() -> new NoSuchElementException("PaymentAccount was not found"));
    }

    public PaymentAccount getPaymentAccountDtoById(int id) {
        return getPaymentAccountById(id);
    }

    public List<PaymentAccount> getAllPaymentAccounts() {
        return paymentAccountRepository.findAll();
    }

    public PaymentAccount updatePaymentAccount(int id, int bankId) {
        PaymentAccount paymentAccount = getPaymentAccountById(id);
        paymentAccount.setBank(bankService.getBankById(bankId));
        return paymentAccountRepository.save(paymentAccount);
    }

    public void deletePaymentAccount(int id) {
        paymentAccountRepository.deleteById(id);
    }
}