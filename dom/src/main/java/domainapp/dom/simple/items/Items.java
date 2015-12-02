package domainapp.dom.simple.items;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;

import java.util.List;

/**
 * Created by Administrator on 10/14/2015.
 */

@DomainService(repositoryFor = Item.class,nature = NatureOfService.DOMAIN)

public class Items {

    //region > findByName (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @MemberOrder(sequence = "3")
    public List<Item> findItemsByName(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        Item.class,
                        "findItemsByName",
                        "name", name));
    }
    //endregion

    @javax.inject.Inject
    DomainObjectContainer container;


}
