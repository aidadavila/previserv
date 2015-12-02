package domainapp.dom.simple.businesses;

import java.math.BigDecimal;
import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import domainapp.dom.simple.customerorders.CustomerOrder;

/**
 * Created by Administrator on 10/1/2015.
 */

@DomainService(repositoryFor = BusinessLocation.class,
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)
public class BusinessLocationContributions {

    //region > create (action)
    public static class CreateDomainEvent extends ActionDomainEvent<BusinessLocationContributions> {
        public CreateDomainEvent(final BusinessLocationContributions source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name="businessLocations",sequence = "4")
    public BusinessLocation addBusinessLocation(
            final @ParameterLayout(named="Business") Business business,
            final @ParameterLayout(named="Business Location Id") String businessLocationId,
            final @ParameterLayout(named="Name") String name,
            final @ParameterLayout(named="Max Delivery Distance") BigDecimal maxDeliveryDistance,
            final @ParameterLayout(named="Business Category") BusinessCategory businessCategory)
    {
        final BusinessLocation obj = container.newTransientInstance(BusinessLocation.class);
        obj.setBusiness(business);
        obj.setBusinessLocationId(businessLocationId);
        obj.setName(name);
        obj.setMaxDeliveryDistance(maxDeliveryDistance);
        obj.setBusinessCategory(businessCategory);
        container.persistIfNotAlready(obj);
        return obj;
    }

    //endregion


    //region > findRequestedCustomerOrders (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            contributed = Contributed.AS_BOTH
    )
    @CollectionLayout (
            render = RenderType.EAGERLY
    )
    @MemberOrder(sequence = "2")
    public List<CustomerOrder> findRequestedCustomerOrders(
            @ParameterLayout(named="Business Location")
            final BusinessLocation myBusinessLocation
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        CustomerOrder.class,
                        "findRequestedCustomerOrders",
                        "myBusinessLocation" , myBusinessLocation
                ));

    }
    //endregion


    //region > findOrderedCustomerOrders (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            contributed = Contributed.AS_BOTH
    )
    @CollectionLayout (
            render = RenderType.EAGERLY
    )
    @MemberOrder(sequence = "3")
    public List<CustomerOrder> findOrderedCustomerOrders(
            @ParameterLayout(named="Business Location")
            final BusinessLocation myBusinessLocation
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        CustomerOrder.class,
                        "findOrderedCustomerOrders",
                        "myBusinessLocation" , myBusinessLocation
                ));

    }
    //endregion



    //region > injected services

    @javax.inject.Inject
    DomainObjectContainer container;

    //endregion
}

