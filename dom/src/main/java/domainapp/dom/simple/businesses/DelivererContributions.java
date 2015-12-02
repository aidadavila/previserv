package domainapp.dom.simple.businesses;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.value.Blob;

import domainapp.dom.simple.customerorders.CustomerOrder;

/**
 * Created by Administrator on 10/19/2015.
 */

@DomainService(repositoryFor = Deliverer.class,
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)

public class DelivererContributions {

    //region > addDeliverer (action)
    public static class CreateDomainEvent extends ActionDomainEvent<DelivererContributions> {
        public CreateDomainEvent(final DelivererContributions source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name = "deliverers",sequence = "24")
    public BusinessLocation addDeliverer(
            final @ParameterLayout(named="Business Location") BusinessLocation businessLocation,
            final @ParameterLayout(named="Deliverer Id") String delivererId,
            final @ParameterLayout(named="First Name") String firstName,
            final @ParameterLayout(named="Middle Name") @Parameter(optionality = Optionality.OPTIONAL)String middleName,
            final @ParameterLayout(named="Last Name") String lastName,
            final @ParameterLayout(named="Last Name 2") @Parameter(optionality = Optionality.OPTIONAL)String lastName2,
            final @ParameterLayout(named="Contact Phone") @Parameter(optionality = Optionality.OPTIONAL)Long contactPhone,
            final @ParameterLayout(named="Delivery Phone") @Parameter(optionality = Optionality.OPTIONAL)Long deliveryPhone,
            final @ParameterLayout(named="Deliverer Picture") @Parameter(optionality = Optionality.OPTIONAL) Blob delivererPicture
    )
    {
        final Deliverer obj = container.newTransientInstance(Deliverer.class);
        obj.setBusinessLocation(businessLocation);
        obj.setDelivererId(delivererId);
        obj.setFirstName(firstName);
        obj.setMiddleName(middleName);
        obj.setLastName(lastName);
        obj.setLastName2(lastName2);
        obj.setContactPhone(contactPhone);
        obj.setDeliveryPhone(deliveryPhone);
        obj.setDelivererPicture(delivererPicture);
        obj.setCreationTime(clockService.nowAsDateTime());
        container.persistIfNotAlready(obj);
        return obj.getBusinessLocation();
    }
    //endregion

    //region > findAssignForDeliveryCustomerOrders (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            contributed = Contributed.AS_BOTH
    )
    @CollectionLayout(
            render = RenderType.EAGERLY
    )
    @MemberOrder(sequence = "25")
    public List<CustomerOrder> findAssignForDeliveryCustomerOrders(
            //@ParameterLayout(named="Business Location")
            //final BusinessLocation myBusinessLocation,
            @ParameterLayout(named="Delivered By")
            final Deliverer myDeliveredBy

    ) {
        return container.allMatches(
                new QueryDefault<>(
                        CustomerOrder.class,
                        "findAssignForDeliveryCustomerOrders",
                        //"myBusinessLocation" , myBusinessLocation,
                        "myDeliveredBy", myDeliveredBy
                ));

    }
    //endregion



    //region > injected services

    @Inject
    DomainObjectContainer container;

    @Inject
    private ClockService clockService;

    //endregion

}
