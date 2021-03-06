package ua.training.model.dao.mapper.factory;

import ua.training.model.dao.mapper.Mapper;
import ua.training.model.entity.*;

/**
 * Abstract mappers factory interface.
 * @see Mapper
 * @author Oleksii Shevchenko
 */
public interface MapperFactory {
    Mapper<Account> getAccountMapper();
    Mapper<Invoice> getInvoiceMapper();
    Mapper<Request> getRequestMapper();
    Mapper<Transaction> getTransactionMapper();
    Mapper<User> getUserMapper();
}
