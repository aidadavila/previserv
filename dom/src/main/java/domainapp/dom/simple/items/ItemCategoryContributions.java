package domainapp.dom.simple.items;

import domainapp.dom.simple.businesses.Business;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.value.Blob;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Administrator on 10/15/2015.
 */

@DomainService(repositoryFor = ItemCategory.class,
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)

public class ItemCategoryContributions {

    //region > addItemCategory (action)
    public static class CreateDomainEvent extends ActionDomainEvent<ItemCategoryContributions> {
        public CreateDomainEvent(final ItemCategoryContributions source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name="itemCategories",sequence = "10")
    public ItemCategory addItemCategory(
            final @ParameterLayout(named="Business") Business business,
            final @ParameterLayout(named="Item Category Id") String itemCategoryId,
            final @ParameterLayout(named="Name") String name,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named="Description", multiLine = 4) String description,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Image") Blob itemCategoryImage,
            final @ParameterLayout(named = "Cuisine Type") ProductOffering productOffering
    )
    {
        final ItemCategory obj = container.newTransientInstance(ItemCategory.class);
        obj.setBusiness(business);
        obj.setItemCategoryId(itemCategoryId);
        obj.setName(name);
        obj.setDescription(description);
        obj.setItemCategoryImage(itemCategoryImage);
        obj.setProductOffering(productOffering);
        obj.setActivationTime(clockService.nowAsDateTime());
        obj.setStatus(ItemStatus.ACTIVE);
        container.persistIfNotAlready(obj);
        return obj;
    }
    //endregion

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_ASSOCIATION)
    @CollectionLayout(render = RenderType.EAGERLY)
    public List<Item> allItems() { return container.allInstances(Item.class);}

    //region > injected services

    @Inject
    DomainObjectContainer container;

    @Inject
    private ClockService clockService;

    //endregion

}
