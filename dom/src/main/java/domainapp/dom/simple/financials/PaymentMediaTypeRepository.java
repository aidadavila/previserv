package domainapp.dom.simple.financials;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;

import java.util.List;

/**
 * Created by Administrator on 9/30/2015.
 */


@DomainService(repositoryFor = PaymentMediaType.class)
@DomainServiceLayout(named = "Financial Entities",menuBar = DomainServiceLayout.MenuBar.SECONDARY,menuOrder = "40")
public class PaymentMediaTypeRepository {


    //region > title
    public TranslatableString title() {
        return TranslatableString.tr("Payment Media Types");
    }
    //endregion

    //region > listAllPaymentMediaTypes (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<PaymentMediaType> listAllPaymentMediaTypes() {
        return container.allInstances(PaymentMediaType.class);
    }
    //endregion

    //region > findPaymentMediaTypeById (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "2")
    public List<PaymentMediaType> findPaymentMediaTypeById(
            @ParameterLayout(named="Payment Media Type Id")
            final String paymentMediaTypeId
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        PaymentMediaType.class,
                        "findPaymentMediaTypeById",
                        "paymentMediaTypeId", paymentMediaTypeId));
    }
    //endregion

    //region > findPaymentMediaTypeByName (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "3")
    public List<PaymentMediaType> findPaymentMediaTypesByName(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        PaymentMediaType.class,
                        "findPaymentMediaTypesByName",
                        "name", name));
    }
    //endregion

    //region > create (action)
    public static class CreateDomainEvent extends ActionDomainEvent<PaymentMediaTypeRepository> {
        public CreateDomainEvent(final PaymentMediaTypeRepository source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(sequence = "4")
    public PaymentMediaType addPaymentMediaType(
            final @ParameterLayout(named="Payment Media Type Id") String paymentMediaTypeId,
            final @ParameterLayout(named="Name") String name){
        final PaymentMediaType obj = container.newTransientInstance(PaymentMediaType.class);
        obj.setPaymentMediaTypeId(paymentMediaTypeId);
        obj.setName(name);
        container.persistIfNotAlready(obj);
        return obj;
    }

    //endregion

    //region > injected services

    @javax.inject.Inject
    DomainObjectContainer container;

    //endregion

}
