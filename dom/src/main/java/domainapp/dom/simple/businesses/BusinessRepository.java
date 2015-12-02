package domainapp.dom.simple.businesses;

/**
 * Created by Administrator on 9/23/2015.
 */

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancyRepository;

import javax.inject.Inject;
import java.util.List;

@DomainService(repositoryFor = Business.class)
@DomainServiceLayout(named = "Businesses",menuOrder = "10")
@DomainObjectLayout(
        cssClassFa = "fa-building"
)
public class BusinessRepository {

    //region > listAllBusinesses (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<Business> listAllBusinesses() {
        return container.allInstances(Business.class);
    }
    //endregion

    //region > findById (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "2")
    public List<Business> findBusinessById(
            @ParameterLayout(named="Business Id")
            final String businessId
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        Business.class,
                        "findBusinessById",
                        "businessId", businessId));
    }
    //endregion

    //region > findByName (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "3")
    public List<Business> findBusinessesByName(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        Business.class,
                        "findBusinessesByName",
                        "name", name));
    }
    //endregion

    //region > addBusiness (action)
    public static class CreateDomainEvent extends ActionDomainEvent<BusinessRepository> {
        /*
        public CreateDomainEvent(final Businesses source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
        */
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(sequence = "4")
    public Business addBusiness(
            final @ParameterLayout(named="Business Id") String businessId,
            final @ParameterLayout(named="Name") String name
    )
    {
        // final ApplicationTenancy applicationTenancy = new ApplicationTenancy();
        final ApplicationTenancy myApplicationTenancy =
                applicationTenancyRepository.newTenancy(name + " Tenancy","/"+name,null);
        final Business business = container.newTransientInstance(Business.class);
        business.setBusinessId(businessId);
        business.setName(name);
        business.setApplicationTenancy(myApplicationTenancy);
        business.setCreationTime(clockService.nowAsDateTime());
        business.setStatus(BusinessStatus.ACTIVE);
        container.persistIfNotAlready(business);
        return business;
    }

    //endregion



    //region > injected services

    @Inject
    DomainObjectContainer container;

    @Inject
    private ClockService clockService;

    @Inject
    ApplicationTenancyRepository applicationTenancyRepository;


    //endregion
}

