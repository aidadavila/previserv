package domainapp.dom.simple.items;

import domainapp.dom.simple.businesses.BusinessLocation;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import javax.inject.Inject;
import java.math.BigDecimal;

/**
 * Created by Administrator on 10/22/2015.
 */

@DomainService(repositoryFor = ItemBusinessLocation.class,
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)

public class ItemBusinessLocationContributions {

    //region > addItemToLocation (action)
    public static class CreateDomainEvent extends ActionDomainEvent<ItemBusinessLocationContributions> {
        public CreateDomainEvent(final ItemBusinessLocationContributions source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name="itemBusinessLocations",sequence = "1")
    public Item addItemToLocation(
            final @ParameterLayout(named="Item") Item item,
            final @ParameterLayout(named="Business Location") BusinessLocation businessLocation,
            final @ParameterLayout(named="Retail Price" )BigDecimal retailPrice

            )
    {
        final ItemBusinessLocation obj = container.newTransientInstance(ItemBusinessLocation.class);
        obj.setItem(item);
        obj.setBusinessLocation(businessLocation);
        obj.setRetailPrice(retailPrice);
        obj.setActivationTime(clockService.nowAsDateTime());
        obj.setStatus(ItemStatus.ACTIVE);
        container.persistIfNotAlready(obj);
        return obj.getItem();
    }
    //endregion

    //region > injected services

    @Inject
    DomainObjectContainer container;

    @Inject
    private ClockService clockService;

    //endregion


}
