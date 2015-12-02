package domainapp.dom.simple.customerorders;

import domainapp.dom.simple.items.Item;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import java.math.BigDecimal;

/**
 * Created by Administrator on 10/21/2015.
 */

@DomainService(repositoryFor = CustomerOrderItem.class,
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)


public class CustomerOrderItemContributions {

    public void setContainer(DomainObjectContainer container) {
        this.container = container;
    }

    //region > addAnItemToOrder (action)
    public static class CreateDomainEvent extends ActionDomainEvent<CustomerOrderItemContributions> {
        public CreateDomainEvent(final CustomerOrderItemContributions source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name="customerOrderItems",sequence = "1")
    public CustomerOrder addAnItemToOrder(
            final @ParameterLayout(named="Customer Order") CustomerOrder customerOrder,
            final @ParameterLayout(named="Item") Item item,
            final @ParameterLayout(named="Quantity Sold")BigDecimal quantitySold
    )
    {
        final CustomerOrderItem obj = container.newTransientInstance(CustomerOrderItem.class);
        obj.setCustomerOrder(customerOrder);
        obj.setItem(item);
        obj.setRetailPrice(obj.getItem().getRetailPrice());
        obj.setQuantitySold(quantitySold);
        obj.setStatus(CustomerOrderStatus.ORDERED);
        container.persistIfNotAlready(obj);
        return obj.getCustomerOrder();
    }
    //endregion

    public BigDecimal default2AddAnItemToOrder() {
        return BigDecimal.ONE;  //By default quantitySold is 1
    }

    //region > injected services

    @javax.inject.Inject
    DomainObjectContainer container;

    protected DomainObjectContainer getContainer() {
        return container;
    }


    //endregion


}
