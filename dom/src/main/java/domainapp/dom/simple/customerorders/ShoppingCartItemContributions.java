package domainapp.dom.simple.customerorders;

import java.math.BigDecimal;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import domainapp.dom.simple.businesses.BusinessLocation;
import domainapp.dom.simple.items.Item;

/**
 * Created by Administrator on 10/16/2015.
 */

@DomainService(repositoryFor = ShoppingCartItem.class,
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)

public class ShoppingCartItemContributions {

    //region > addAnItem (action)
    public static class CreateDomainEvent extends ActionDomainEvent<ShoppingCartItemContributions> {
        public CreateDomainEvent(final ShoppingCartItemContributions source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name="shoppingCartItems",sequence = "1")
    public ShoppingCart addAnItemToCart(
            final @ParameterLayout(named="Shopping Cart") ShoppingCart shoppingCart,
            final @ParameterLayout(named="Item") Item item,
            final @ParameterLayout(named="Business Location") BusinessLocation businessLocation,
            final @ParameterLayout(named="Quantity")BigDecimal quantityOrdered
    )
    {
        final ShoppingCartItem obj = container.newTransientInstance(ShoppingCartItem.class);
        obj.setShoppingCart(shoppingCart);
        obj.setItem(item);
        obj.setBusinessLocation(businessLocation);
        obj.setQuantityOrdered(quantityOrdered);
        obj.setRetailPrice(obj.getItem().getRetailPrice());
        container.persistIfNotAlready(obj);
        return obj.getShoppingCart();
    }
    //endregion

    public BigDecimal default3AddAnItemToCart() {
        return BigDecimal.ONE;  //By default quantityOrdered is 1
    }

    public String validate3AddAnItemToCart(
            final BigDecimal quantityOrdered) {
        return quantityOrdered.compareTo(BigDecimal.ZERO) < 0
                ? "Quantity Ordered cannot be negative"
                : null;
    }

    //region > injected services

    @javax.inject.Inject
    DomainObjectContainer container;

    //endregion

}
