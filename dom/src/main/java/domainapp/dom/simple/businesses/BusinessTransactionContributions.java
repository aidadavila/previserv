package domainapp.dom.simple.businesses;

import domainapp.dom.simple.applications.Application;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.clock.ClockService;

import javax.inject.Inject;
import java.math.BigDecimal;

/**
 * Created by Administrator on 10/12/2015.
 */

@DomainService(repositoryFor = BusinessTransaction.class,
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)

public class BusinessTransactionContributions {

    @Action
    @MemberOrder(name = "businessTransactions",sequence = "4")
    public Business addBusinessTransaction(
            final @ParameterLayout(named="Business") Business business,
            final @ParameterLayout(named="Transaction ID") Long transactionId,
            final @ParameterLayout(named="Application") Application application,
            final @ParameterLayout(named="Location") BusinessLocation location,
            final @ParameterLayout(named="Transaction Type") BusinessTransactionType businessTransactionType,
            final @ParameterLayout(named="Amount") BigDecimal amount
    )
    {
        final BusinessTransaction obj = container.newTransientInstance(BusinessTransaction.class);
        obj.setBusiness(business);
        obj.setTransactionId(transactionId);
        obj.setApplication(application);
        obj.setBusinessLocation(location);
        obj.setBusinessTransactionType(businessTransactionType);
        obj.setAmount(amount);
        obj.setTransactionDate(clockService.nowAsDateTime());
        container.persistIfNotAlready(obj);
        return obj.getBusiness();
    }

    //endregion

    //region > injected services

    @Inject
    DomainObjectContainer container;

    @Inject
    private ClockService clockService;

    //endregion


}
