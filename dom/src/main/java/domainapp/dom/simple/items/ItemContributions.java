package domainapp.dom.simple.items;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.value.Blob;

import javax.inject.Inject;
import java.math.BigDecimal;

/**
 * Created by Administrator on 10/14/2015.
 */

@DomainService(repositoryFor = Item.class,
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)

public class ItemContributions {

    //region > addItem (action)
    public static class CreateDomainEvent extends ActionDomainEvent<ItemContributions> {
        public CreateDomainEvent(final ItemContributions source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name="items",sequence = "10")
    public Item addItem(
            final @ParameterLayout(named="Item Id") String itemId,
            final @ParameterLayout(named="Name") String name,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named="Description", multiLine = 4) String description,
            final @ParameterLayout(named="Category") ItemCategory itemCategory,
            final @ParameterLayout(named="Retail Price" )BigDecimal retailPrice,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Image") Blob itemImage
    )
    {
        final Item obj = container.newTransientInstance(Item.class);
        obj.setItemId(itemId);
        obj.setName(name);
        obj.setDescription(description);
        obj.setItemCategory(itemCategory);
        obj.setRetailPrice(retailPrice);
        obj.setItemImage(itemImage);
        obj.setBusiness(obj.getItemCategory().getBusiness());
        obj.setActivationTime(clockService.nowAsDateTime());
        obj.setStatus(ItemStatus.ACTIVE);
        container.persistIfNotAlready(obj);
        return obj;
    }
    //endregion


    //region > injected services

    @Inject
    DomainObjectContainer container;

    @Inject
    private ClockService clockService;


    //endregion

}
