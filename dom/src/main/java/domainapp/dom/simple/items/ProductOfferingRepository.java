package domainapp.dom.simple.items;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.value.Blob;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Administrator on 10/22/2015.
 */

@DomainService(repositoryFor = ProductOffering.class)
@DomainObjectLayout(
        cssClassFa = "fa-th-list"
)

@DomainServiceLayout(named = "Applications",menuBar = DomainServiceLayout.MenuBar.SECONDARY,menuOrder = "30")

public class ProductOfferingRepository {

    //region > listAllProductOfferings (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.NEVER,
            named = "List All Cuisine Types"
    )
    @MemberOrder(sequence = "1")
    public List<ProductOffering> listAllProductOfferings() {
        return container.allInstances(ProductOffering.class);
    }
    //endregion

    //region > findProductOfferingById (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.NEVER,
            named = "Find Cuisine Type by Id"
    )
    @MemberOrder(sequence = "2")
    public List<ProductOffering> findProductOfferingById(
            @ParameterLayout(named="Cuisine Id")
            final String productOfferingId
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        ProductOffering.class,
                        "findProductOfferingById",
                        "productOfferingId", productOfferingId));
    }
    //endregion


    //region > findProductOfferingsByName (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.NEVER,
            named = "Find Cuisine Type by Name"
    )
    @MemberOrder(sequence = "3")
    public List<ProductOffering> findProductOfferingsByName(
            @ParameterLayout(named="Cuisine Type Id")
            final String name
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        ProductOffering.class,
                        "findProductOfferingsByName",
                        "name", name));
    }
    //endregion


    //region > addItemCategory (action)
    public static class CreateDomainEvent extends ActionDomainEvent<ProductOfferingRepository> {
        public CreateDomainEvent(final ProductOfferingRepository source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @ActionLayout(named = "Add Cuisine Type")
    public ProductOffering addProductOffering(
            final @ParameterLayout(named="Cuisine Type Id") String productOfferingId,
            final @ParameterLayout(named="Name") String name,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named="Description", multiLine = 4) String description,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Image") Blob productOfferingImage
    )
    {
        final ProductOffering obj = container.newTransientInstance(ProductOffering.class);
        obj.setProductOfferingId(productOfferingId);
        obj.setName(name);
        obj.setDescription(description);
        obj.setProductOfferingImage(productOfferingImage);
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



}
