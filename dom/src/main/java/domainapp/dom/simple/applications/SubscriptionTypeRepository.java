package domainapp.dom.simple.applications;


import domainapp.dom.simple.businesses.BusinessStatus;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;

import javax.inject.Inject;
import java.util.List;

@DomainService(repositoryFor = SubscriptionType.class)
@DomainServiceLayout(named = "Applications",menuBar = DomainServiceLayout.MenuBar.SECONDARY,menuOrder = "20")
public class SubscriptionTypeRepository {

    //region > title
    public TranslatableString title() {
        return TranslatableString.tr("Subscription Types");
    }
    //endregion

    //region > listAllSubscriptionTypes (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "11")
    public List<SubscriptionType> listAllSubscriptionTypes() {
        return container.allInstances(SubscriptionType.class);
    }
    //endregion

    //region > findSubscriptionTypeById (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "12")
    public List<SubscriptionType> findSubscriptionTypeById(
            @ParameterLayout(named="Subscription Type Id")
            final String subscriptionTypeId
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        SubscriptionType.class,
                        "findById",
                        "subscriptionTypeId", subscriptionTypeId));
    }
    //endregion

    //region > findSubscriptionTypeByName (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "13")
    public List<SubscriptionType> findSubscriptionTypeByName(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        SubscriptionType.class,
                        "findByName",
                        "name", name));
    }
    //endregion

    //region > create (action)
    public static class CreateDomainEvent extends ActionDomainEvent<SubscriptionTypeRepository> {
        public CreateDomainEvent(final SubscriptionTypeRepository source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(sequence = "14")
    public SubscriptionType addSubscriptionType(
            final @ParameterLayout(named="Subscription Type Id") String subscriptionTypeId,
            final @ParameterLayout(named="Name") String name){
        final SubscriptionType obj = container.newTransientInstance(SubscriptionType.class);
        obj.setSubscriptionTypeId(subscriptionTypeId);
        obj.setName(name);
        obj.setCreationTime(clockService.nowAsDateTime());
        obj.setStatus(BusinessStatus.ACTIVE);
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

