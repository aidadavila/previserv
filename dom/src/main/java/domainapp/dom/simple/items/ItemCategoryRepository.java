package domainapp.dom.simple.items;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;

import java.util.List;

/**
 * Created by Administrator on 10/15/2015.
 */
@DomainService(repositoryFor = Item.class, nature = NatureOfService.DOMAIN)

public class ItemCategoryRepository {

    //region > findItemCategoriesByName (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @MemberOrder(sequence = "3")
    public List<ItemCategory> findItemCategoriesByName(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        ItemCategory.class,
                        "findItemCategoriesByName",
                        "name", name));
    }
    //endregion

    @javax.inject.Inject
    DomainObjectContainer container;

}
