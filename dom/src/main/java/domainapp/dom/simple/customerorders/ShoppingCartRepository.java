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
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import domainapp.dom.simple.businesses.BusinessLocation;
import domainapp.dom.simple.customers.CustomerSubscription;
import domainapp.dom.simple.systementities.AutoNumberRepository;

/**
 * Created by Administrator on 10/15/2015.
 */

@DomainService(repositoryFor = ShoppingCart.class,
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)

public class ShoppingCartRepository {

    //region > newShoppingCart (action)
    public static class CreateDomainEvent extends ActionDomainEvent<ShoppingCartRepository> {
        public CreateDomainEvent(final ShoppingCartRepository source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name="shoppingCarts",sequence = "1")
    public ShoppingCart newShoppingCart(
            //final @ParameterLayout(named="Shopping Cart Id") Long shoppingCartId,
            final @ParameterLayout(named="Customer Subscription Id") CustomerSubscription customerSubscription
    )
    {
        final ShoppingCart obj = container.newTransientInstance(ShoppingCart.class);

        //Obtener Folio
        autoNumberRepository = new AutoNumberRepository();
        autoNumberRepository.setContainer(container);
        Long shoppingCartId =  autoNumberRepository.nextAutoNumber("SHOPPING CART ID" , null);
        obj.setShoppingCartId(shoppingCartId);
        obj.setCustomerSubscription(customerSubscription);
        obj.setCreationTime(clockService.nowAsDateTime());
        obj.setStatus(ShoppingCartStatus.SHOPPING);
        container.persistIfNotAlready(obj);
        return obj;
    }
    //endregion

    //region > shoppingCartItems

    //region > findShoppingCartItemsGroupedByLocation (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            contributed = Contributed.AS_NEITHER,
            hidden = Where.EVERYWHERE
    )
    @MemberOrder(sequence = "2")
    public List<ShoppingCartItem> findShoppingCartItemsGroupedByLocation(
            @ParameterLayout(named="Shopping Cart")
            final ShoppingCart parShoppingCart
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        ShoppingCartItem.class,
                        "findShoppingCartItemsGroupedByLocation",
                        "parShoppingCart", parShoppingCart));
    }
    //endregion

    //region > createCustomerOrdersFromShoppingCart
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(named = "Create my Orders",
            contributed = Contributed.AS_NEITHER,
            hidden = Where.EVERYWHERE
    )
    @MemberOrder(sequence = "1", name = "myCustomerOrderItems")
    public ShoppingCart createCustomerOrdersFromShoppingCart(
            final @ParameterLayout(named="Shopping Cart") ShoppingCart shoppingCart

    ) {
        List<ShoppingCartItem> listAllMyItems = this.findShoppingCartItemsGroupedByLocation(shoppingCart);
        for (ShoppingCartItem MyItem : listAllMyItems) {
            BusinessLocation myItemBusinessLocation = MyItem.getBusinessLocation();


            customerOrderContributions = new CustomerOrderContributions();
            customerOrderContributions.setContainer(container);
            customerOrderContributions.setclockService(clockService);
            //Busca CO
            CustomerOrder newCustomerOrder = null;
            List<CustomerOrder> listMyCustomerOrder = customerOrderContributions.findShoppingCartOrderByLocation(shoppingCart, myItemBusinessLocation);

            //Obtiene Folio CO
            if (listMyCustomerOrder.isEmpty())
                newCustomerOrder = customerOrderContributions.newCustomerOrder(shoppingCart, myItemBusinessLocation);
             else
                newCustomerOrder = listMyCustomerOrder.get(0);

            //Genera Detalle
            customerOrderItemContributions = new CustomerOrderItemContributions();
            customerOrderItemContributions.setContainer(container);
            customerOrderItemContributions.addAnItemToOrder(newCustomerOrder, MyItem.getItem(), MyItem.getQuantityOrdered());
            //System.out.println("Generó Detalle: ");

        }

        return shoppingCart;
    }
//endregion//endregion


    //region > injected services

    @Inject
    DomainObjectContainer container;

    @Inject
    private ClockService clockService;

    @Inject
    CustomerOrderContributions customerOrderContributions;
    CustomerOrderItemContributions customerOrderItemContributions;
    AutoNumberRepository autoNumberRepository;



    //endregion

}
