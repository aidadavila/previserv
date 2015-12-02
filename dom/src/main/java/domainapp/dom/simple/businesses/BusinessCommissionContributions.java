package domainapp.dom.simple.businesses;

import domainapp.dom.simple.financials.PaymentMediaType;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import java.math.BigDecimal;

/**
 * Created by Administrator on 10/2/2015.
 */
@DomainService(repositoryFor = BusinessCommission.class,
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)
public class BusinessCommissionContributions {

    //region > create (action)
    public static class CreateDomainEvent extends ActionDomainEvent<BusinessCommissionContributions> {
        public CreateDomainEvent(final BusinessCommissionContributions source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }


    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name = "businessCommissions",sequence = "5")
    public BusinessCommission addBusinessCommission(
            final @ParameterLayout(named="Business Category") BusinessCategory businessCategory,
            final @ParameterLayout(named="Payment Media Type") PaymentMediaType paymentMediaType,
            final @ParameterLayout(named="Discount %") BigDecimal discountPercent,
            final @ParameterLayout(named="Financial Institution Fee %") BigDecimal financialInstFee
    )
    {
        final BusinessCommission obj = container.newTransientInstance(BusinessCommission.class);
        obj.setBusinessCategory(businessCategory);
        obj.setPaymentMediaType(paymentMediaType);
        obj.setDiscountPercent(discountPercent);
        obj.setFinancialInstFee(financialInstFee);
        container.persistIfNotAlready(obj);
        return obj;
    }

    //endregion

    //region > injected services

    @javax.inject.Inject
    DomainObjectContainer container;

    //endregion

}
