package domainapp.dom.simple.customers;

import domainapp.dom.simple.financials.PaymentMediaType;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

/**
 * Created by Administrator on 10/14/2015.
 */
@DomainService(
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY,
        repositoryFor = CustomerPaymentMedia.class)
@DomainServiceLayout(named = "Customers",menuOrder = "30")

public class CustomerPaymentMediaRepository {

    //region > addPaymentMedia (action)
    public static class CreateDomainEvent extends ActionDomainEvent<CustomerPaymentMediaRepository> {
        public CreateDomainEvent(final CustomerPaymentMediaRepository source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name="customerPaymentMedias",sequence = "1")
    public CustomerSubscription addPaymentMedia(
            final @ParameterLayout(named="Customer Subscripion") CustomerSubscription customerSubscription,
            final @ParameterLayout(named = "Credit Card Type") PaymentMediaType pmCcPaymentMediaType,
            final @Parameter(maxLength = 16, regexPattern = "\\b[1-9][0-9]{15}\\b") @ParameterLayout(named = "Credit Card Number", typicalLength = 16) String pmCreditCard,
            final @ParameterLayout(named = "Name on Credit Card", typicalLength = 40) String pmName,
            final @Parameter(maxLength = 2, regexPattern = "\\b[0-9][0-9]{1}\\b") @ParameterLayout(named = "Expiration Month", typicalLength = 2) Integer pmExpirationMonth,
            final @Parameter(maxLength = 2, regexPattern = "\\b[0-9][0-9]{1}\\b") @ParameterLayout(named = "Expiration Year", typicalLength = 2) Integer pmExpirationYear,
            final @Parameter(maxLength = 5,regexPattern = "\\b[0-9][0-9]{4}\\b") @ParameterLayout(named = "Zip Code", typicalLength = 5) Integer pmCcZip,
            final @Parameter(maxLength =4) @ParameterLayout(named = "CVV", typicalLength = 4) Integer pmCcCvv
    )
    {
        final CustomerPaymentMedia obj = container.newTransientInstance(CustomerPaymentMedia.class);
        obj.setCustomerSubscription(customerSubscription);
        obj.setPmCcPaymentMediaType(pmCcPaymentMediaType);
        obj.setPmCreditCard(pmCreditCard);
        obj.setPmName(pmName);
        obj.setPmExpirationMonth(pmExpirationMonth);
        obj.setPmExpirationYear(pmExpirationYear);
        obj.setPmCcZip(pmCcZip);
        obj.setPmCcCvv(pmCcCvv);

        container.persistIfNotAlready(obj);
        return obj.getCustomerSubscription();
    }
    //endregion

    //region > injected services

    @javax.inject.Inject
    DomainObjectContainer container;

    //endregion


}
