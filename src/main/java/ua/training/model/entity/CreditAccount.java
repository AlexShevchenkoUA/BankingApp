package ua.training.model.entity;

import ua.training.model.exception.NonActiveAccountException;
import ua.training.model.exception.NotEnoughMoneyException;
import ua.training.model.service.FixerExchangeService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


/**
 * This realization of abstract class {@link Account} that implements Credit Policy. That means that on the account is
 * possible negative balance, but the absolute value of it must be not greater than credit limit. If the balance is
 * negative the loan interest should be accrued to the account every update period due to credit rate.
 * @author Oleksii Shevhenko
 * @see ua.training.model.entity.Account
 */
public class CreditAccount extends Account {
    private BigDecimal creditRate;
    private BigDecimal creditLimit;

    private CreditAccount(long id, BigDecimal balance, Currency currency, LocalDate expiresEnd,
                          Status status, List<Long> holders, BigDecimal creditRate, BigDecimal creditLimit) {
        super(id, balance, currency, expiresEnd, status, holders);
        this.creditRate = creditRate;
        this.creditLimit = creditLimit;
    }

    public BigDecimal getCreditRate() {
        return creditRate;
    }

    public void setCreditRate(BigDecimal creditRate) {
        this.creditRate = creditRate;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    /**
     * This class realize pattern Builder for class {@link CreditAccount}
     * @author Oleksii Shevchenko
     * @see CreditAccount
     */
    public static class CreditAccountBuilder {
        private long id;
        private BigDecimal balance;
        private Currency currency;
        private LocalDate expiresEnd;
        private Status status;
        private List<Long> holders;
        private BigDecimal creditRate;
        private BigDecimal creditLimit;

        public CreditAccountBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public CreditAccountBuilder setBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public CreditAccountBuilder setCurrency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public CreditAccountBuilder setExpiresEnd(LocalDate expiresEnd) {
            this.expiresEnd = expiresEnd;
            return this;
        }

        public CreditAccountBuilder setHolders(List<Long> holders) {
            this.holders = holders;
            return this;
        }

        public CreditAccountBuilder setCreditRate(BigDecimal creditRate) {
            this.creditRate = creditRate;
            return this;
        }

        public CreditAccountBuilder setCreditLimit(BigDecimal creditLimit) {
            this.creditLimit = creditLimit;
            return this;
        }

        public CreditAccountBuilder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public CreditAccount build() {
            return new CreditAccount(id, balance, currency, expiresEnd, status, holders, creditRate, creditLimit);
        }
    }

    /**
     * The method for obtaining builder for class {@link CreditAccount}
     * @return The CreditAccountBuilder instance, that are builder for class {@link CreditAccount}
     * @see CreditAccount
     * @see CreditAccountBuilder
     */
    public static CreditAccountBuilder getBuilder() {
        return new CreditAccountBuilder();
    }

    /**
     * Implementation withdrawing money from account according to credit policy.
     * @param transaction Transaction according to witch withdraw must be done.
     * @return Balance of account after withdraw.
     * @throws NonActiveAccountException Thrown if and only if {@code isNonActive()} return true.
     * @throws NotEnoughMoneyException Throw if and only if absolute balance of account after withdraw more than credit limit.
     */
    @Override
    public BigDecimal withdrawFromAccount(Transaction transaction) throws NonActiveAccountException, NotEnoughMoneyException{
        if (isNonActive()) {
            throw new NonActiveAccountException();
        }

        BigDecimal exchangeRate = new FixerExchangeService().exchangeRate(transaction.getCurrency(), getCurrency());
        BigDecimal balance = getBalance().subtract(transaction.getAmount().multiply(exchangeRate));

        if (balance.abs().compareTo(getCreditLimit()) >= 0) {
            throw new NotEnoughMoneyException();
        } else {
            setBalance(balance.subtract(balance.abs().multiply(getCreditRate())));
        }

        return getBalance();
    }
}
