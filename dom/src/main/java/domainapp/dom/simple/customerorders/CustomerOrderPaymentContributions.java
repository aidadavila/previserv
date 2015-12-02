package domainapp.dom.simple.customerorders;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import domainapp.dom.simple.customers.CustomerSubscription;
import domainapp.dom.simple.financials.PaymentMediaType;

/**
 * Created by Administrator on 10/21/2015.
 */

@DomainService(repositoryFor = CustomerOrderPayment.class,
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)


public class CustomerOrderPaymentContributions {

    private final Class<CustomerOrderPayment> customerOrderPaymentClass = CustomerOrderPayment.class;

    //region > addPaymentMedia (action)
    public static class CreateDomainEvent extends ActionDomainEvent<CustomerOrderPaymentContributions> {
        public CreateDomainEvent(final CustomerOrderPaymentContributions source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name="customerOrderPayments",sequence = "1")
    public CustomerOrder addCustomerOrderPayment(
            final @ParameterLayout(named="Customer Order") CustomerOrder customerOrder,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named="Customer Subscription") CustomerSubscription customerSubscription,
            final @ParameterLayout(named = "Credit Card Type") PaymentMediaType pmCcPaymentMediaType,
            final @Parameter(maxLength = 16, regexPattern = "\\b[1-9][0-9]{15}\\b") @ParameterLayout(named = "Credit Card Number", typicalLength = 16) String pmCreditCard,
            final @ParameterLayout(named = "Name on Credit Card", typicalLength = 40) String pmName,
            final @Parameter(maxLength = 2, regexPattern = "\\b[0-9][0-9]{1}\\b") @ParameterLayout(named = "Expiration Month", typicalLength = 2) Integer pmExpirationMonth,
            final @Parameter(maxLength = 2, regexPattern = "\\b[0-9][0-9]{1}\\b") @ParameterLayout(named = "Expiration Year", typicalLength = 2) Integer pmExpirationYear,
            final @Parameter(maxLength = 5,regexPattern = "\\b[0-9][0-9]{4}\\b") @ParameterLayout(named = "Zip Code", typicalLength = 5) Integer pmCcZip,
            final @Parameter(maxLength =4) @ParameterLayout(named = "CVV", typicalLength = 4) Integer pmCcCvv,
            final @ParameterLayout(named = "Amount Paid", typicalLength = 40) BigDecimal amountPaid

    )
    {
        final CustomerOrderPayment obj = container.newTransientInstance(CustomerOrderPayment.class);
        obj.setCustomerOrder(customerOrder);
        obj.setCustomerSubscription(customerSubscription);
        obj.setPmCcPaymentMediaType(pmCcPaymentMediaType);
        obj.setPmCreditCard(pmCreditCard);
        obj.setPmName(pmName);
        obj.setPmExpirationMonth(pmExpirationMonth);
        obj.setPmExpirationYear(pmExpirationYear);
        obj.setPmCcZip(pmCcZip);
        obj.setPmCcCvv(pmCcCvv);
        obj.setAmountPaid(amountPaid);
        obj.setPaymentTime(clockService.nowAsDateTime());
        container.persistIfNotAlready(obj);
        return obj.getCustomerOrder();
    }
    //endregion


    //region > findCustomerOrderPayment (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            contributed = Contributed.AS_ACTION
    )
    @MemberOrder(sequence = "2")
    public List<CustomerOrderPayment> findCustomerOrderPayment(
            @ParameterLayout(named="Customer Order")
            final CustomerOrder parCustomerOrder
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        CustomerOrderPayment.class,
                        "findCustomerOrderPayment",
                        "parCustomerOrder", parCustomerOrder
                ));
    }
    //endregion

    public String validate0AddCustomerOrderPayment(
            final CustomerOrder customerOrder) {
        return !customerOrder.getStatus().equals(CustomerOrderStatus.PREORDERED)
                ? "Customer Order is not in PREORDERED status"
                : null;
    }

    public String validate9AddCustomerOrderPayment(
            final BigDecimal amountPaid) {
        return amountPaid.compareTo(BigDecimal.ZERO) < 0
                ? "Amount Paid cannot be negative"
                : null;
    }

    //region > injected services

    @Inject
    DomainObjectContainer container;

    @Inject
    private ClockService clockService;


    //endregion


}
