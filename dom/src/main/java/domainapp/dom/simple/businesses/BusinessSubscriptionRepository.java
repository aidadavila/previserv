package domainapp.dom.simple.businesses;

import domainapp.dom.simple.applications.SubscriptionStatus;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import java.util.List;

/**
 * Created by Administrator on 10/1/2015.
 */
@DomainService(
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY,
        repositoryFor = BusinessSubscription.class)
@DomainServiceLayout(named = "Businesses",menuOrder = "90")

public class BusinessSubscriptionRepository {

    //region > listAllBusinessSubscriptions (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(name="businessSubscriptions",sequence = "1")
    public List<BusinessSubscription> listAllBusinessSubscriptions() {
        return container.allInstances(BusinessSubscription.class);
    }
    //endregion



    //region > addSubscription (action)
    public static class CreateDomainEvent extends ActionDomainEvent<BusinessSubscriptionRepository> {
        public CreateDomainEvent(final BusinessSubscriptionRepository source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name="businessSubscriptions",sequence = "2")
    public BusinessSubscription addSubscription(
            final @ParameterLayout(named="Business") Business business,
            final @ParameterLayout(named="Business Subscription Id") Long businessSubscriptionId,
            final @ParameterLayout(named="Statement Day of Month") Integer statementDom
/*
            final @ParameterLayout(named="Payment Media Financial Institution") FinancialInst pmFinancialInst,
            final @ParameterLayout(named="Payment Media Number") String paymentMedia,
            final @ParameterLayout(named="Collection Media is Account") @Parameter(optionality = Optionality.OPTIONAL) Boolean cmIsAccount,
            final @ParameterLayout(named="Collection Media Financial Institution") @Parameter(optionality = Optionality.OPTIONAL) FinancialInst cmFinancialInst,
            final @ParameterLayout(named="Collection Media Number") @Parameter(optionality = Optionality.OPTIONAL) String collectionMedia,
            final @ParameterLayout(named="Collection Media is Credit Card") @Parameter(optionality = Optionality.OPTIONAL) Boolean cmIsCreditCard,
            final @ParameterLayout(named="Credit Card Media Type" ) @Parameter(optionality = Optionality.OPTIONAL) PaymentMediaType cmCcPaymentMediaType,
            final @ParameterLayout(named="Credit Card Number", typicalLength = 16) @Parameter(optionality = Optionality.OPTIONAL, maxLength = 16, regexPattern = "\\b[1-9][0-9]{15}\\b") String cmCreditCard,
            final @ParameterLayout(named="Name on Collection Media", typicalLength = 20) @Parameter(optionality = Optionality.OPTIONAL) String cmName,
            final @ParameterLayout(named="Expiration Month on Collection Media", typicalLength = 2)@Parameter(optionality = Optionality.OPTIONAL, maxLength = 2, regexPattern = "\\b[1-9][0-9]{1}\\b") Integer cmExpirationMonth,
           final @ParameterLayout(named="Expiration Year on Collection Media", typicalLength = 2)@Parameter(optionality = Optionality.OPTIONAL, maxLength = 2, regexPattern = "\\b[1-9][0-9]{1}\\b") Integer cmExpirationYear,
            final @ParameterLayout(named="ZIP code on Collection Media", typicalLength = 5)@Parameter(optionality = Optionality.OPTIONAL, maxLength = 5, regexPattern = "\\b[1-9][0-9]{4}\\b") Integer cmZip
*/
    )
    {
        final BusinessSubscription obj = container.newTransientInstance(BusinessSubscription.class);
        obj.setBusinessSubscriptionId(businessSubscriptionId);
        obj.setBusiness(business);
        obj.setStatementDom(statementDom);
/*
        obj.setPmFinancialInst(pmFinancialInst);

        obj.setPaymentMedia(paymentMedia);
        obj.setCmIsAccount(cmIsAccount);
        obj.setCmFinancialInst(cmFinancialInst);
        obj.setCollectionMedia(collectionMedia);
        obj.setCmIsCreditCard(cmIsCreditCard);
        obj.setCmCcPaymentMediaType(cmCcPaymentMediaType);
        obj.setCmCreditCard(cmCreditCard);
        obj.setCmName(cmName);
        obj.setCmExpirationMonth(cmExpirationMonth);
        obj.setCmExpirationYear(cmExpirationYear);
        obj.setCmZip(cmZip);
        obj.setSubscriptionTime(clockService.nowAsDateTime());
*/
        obj.setStatus(SubscriptionStatus.REGISTRATION);
        container.persistIfNotAlready(obj);
        return obj;
    }

    public Integer default2AddSubscription() {
        return 30;  //By default set Subscription's Statement Day of Month to the 30th of each month
    }
/*
    //Default for parameter 5 cmIsAccount above
    public Boolean default5addSubscription() {
        return Boolean.TRUE;
    }

    //Default for parameter 6 cmIsCreditCard above
    public Boolean default6addSubscription() {
        return Boolean.FALSE;
    }
*/

    //endregion

    //region > injected services

    @javax.inject.Inject
    DomainObjectContainer container;

    //endregion
}
