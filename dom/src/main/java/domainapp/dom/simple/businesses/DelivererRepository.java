package domainapp.dom.simple.businesses;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.clock.ClockService;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Administrator on 10/19/2015.
 */

@DomainService(repositoryFor = Deliverer.class)
@DomainServiceLayout(named = "Businesses",menuOrder = "30.1")

public class DelivererRepository {

    //region > listAllDeliverers (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.NEVER
    )
    @MemberOrder(sequence = "21")
    public List<Deliverer> listAllDeliverers() {
        return container.allInstances(Deliverer.class);
    }
    //endregion

    //region > findDelivererById (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.NEVER
    )
    @MemberOrder(sequence = "22")
    public List<Deliverer> findDelivererById(
            @ParameterLayout(named="Deliverer Id")
            final String delivererId
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        Deliverer.class,
                        "findDelivererById",
                        "delivererId", delivererId));
    }
    //endregion

    //region > findDeliverersByName (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.NEVER
    )
    @MemberOrder(sequence = "23")
    public List<Deliverer> findDeliverersByName(
            @ParameterLayout(named="Last Name")
            final String lastName
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        Deliverer.class,
                        "findDeliverersByName",
                        "lastName", lastName));
    }
    //endregion

    //region > injected services

    @Inject
    DomainObjectContainer container;

    @Inject
    private ClockService clockService;

    //endregion

}
