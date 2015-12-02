package domainapp.dom.simple.customerorders;

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

import domainapp.dom.simple.businesses.BusinessLocation;
import domainapp.dom.simple.systementities.AutoNumberRepository;

/**
 * Created by Administrator on 10/16/2015.
 */

@DomainService(repositoryFor = CustomerOrder.class,
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)

public class CustomerOrderContributions {

    //region > newCustomerOrder (action)
    public static class CreateDomainEvent extends ActionDomainEvent<CustomerOrderContributions> {
        public CreateDomainEvent(final CustomerOrderContributions source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name="customerOrders",sequence = "10")
    public CustomerOrder newCustomerOrder(
            final @ParameterLayout(named="Shopping Cart") @Parameter(optionality = Optionality.OPTIONAL) ShoppingCart shoppingCart,
            final @ParameterLayout(named="Business Location") BusinessLocation businessLocation
    )
    {

        CustomerOrder obj = this.getContainer().newTransientInstance(CustomerOrder.class);
        //Obtiene Folio Customer Order
        autoNumberRepository = new AutoNumberRepository();
        autoNumberRepository.setContainer(container);
        long customerOrderId = autoNumberRepository.nextAutoNumber("CUSTOMER ORDER", businessLocation);
        obj.setCustomerOrderId(customerOrderId);
        obj.setShoppingCart(shoppingCart);
        obj.setBusinessLocation(businessLocation);
        obj.setCustomerSubscription(obj.getShoppingCart().getCustomerSubscription());
        obj.setDeliverToStreet(obj.getShoppingCart().getDeliverToStreet());
        obj.setDeliverToStreet2(obj.getShoppingCart().getDeliverToStreet2());
        obj.setDeliverToCity(obj.getShoppingCart().getDeliverToCity());
        obj.setDeliverToCountry(obj.getShoppingCart().getDeliverToCountry());
        obj.setDeliverToZipCode(obj.getShoppingCart().getDeliverToZipCode());
        obj.setDeliverToGeoLocation(obj.getShoppingCart().getLocation());
        obj.setCreationTime(clockService.nowAsDateTime());
        obj.setOrderTime(clockService.nowAsDateTime());
        obj.setStatus(CustomerOrderStatus.REQUESTED);
        this.getContainer().persistIfNotAlready(obj);
        return obj;
    }
    //endregion


    //region > findShoppingCartOrderByLocation (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            contributed = Contributed.AS_ACTION
    )
    @MemberOrder(sequence = "2")
    public List<CustomerOrder> findShoppingCartOrderByLocation(
            @ParameterLayout(named="Shopping Cart")
            final ShoppingCart parShoppingCart,
            @ParameterLayout(named="Business Location")
            final BusinessLocation myBusinessLocation
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        CustomerOrder.class,
                        "findShoppingCartOrderByLocation",
                        "parShoppingCart", parShoppingCart, "myBusinessLocation" , myBusinessLocation
                ));

        //"myBusinessLocation", myBusinessLocation
    }
    //endregion



    //region > injected services

    @Inject
    private ClockService clockService;
    protected ClockService getclockService() {
        return clockService;
    }
    public final void setclockService(final ClockService clockService) {
        this.clockService = clockService;
    }


    @Inject
    private DomainObjectContainer container;
    protected DomainObjectContainer getContainer() {
        return container;
    }
    public final void setContainer(final DomainObjectContainer container) {
        this.container = container;
    }

    AutoNumberRepository autoNumberRepository;

    //endregion

}
