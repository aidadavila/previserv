package domainapp.dom.simple.businesses;

import domainapp.dom.simple.financials.PaymentMediaType;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;

/**
 * Created by Administrator on 9/30/2015.
 */

@DomainService(repositoryFor = BusinessPaymentMediaType.class,
                nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)
@DomainServiceLayout(menuOrder = "70")
public class BusinessPaymentMediaTypes {

    //region > title
    public TranslatableString title() {
        return TranslatableString.tr("Business Payment Media Types");
    }
    //endregion

    //region > create (action)
    public static class CreateDomainEvent extends ActionDomainEvent<BusinessPaymentMediaTypes> {
        public CreateDomainEvent(final BusinessPaymentMediaTypes source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name = "businessPaymentMediaTypes",sequence = "2")
    public BusinessLocation addBusinessPaymentMediaType(
            final @ParameterLayout(named="Business Location") BusinessLocation businessLocation,
            final @ParameterLayout(named="Payment Media Type") PaymentMediaType paymentMediaType){
        final BusinessPaymentMediaType obj = container.newTransientInstance(BusinessPaymentMediaType.class);
        obj.setBusinessLocation(businessLocation);
        obj.setPaymentMediaType(paymentMediaType);
        container.persistIfNotAlready(obj);
        return obj.getBusinessLocation();
    }

    //endregion

    //region > injected services

    @javax.inject.Inject
    DomainObjectContainer container;

    //endregion

}
