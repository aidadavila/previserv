package domainapp.dom.simple.applications;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import java.math.BigDecimal;

/**
 * Created by Administrator on 9/29/2015.
 */
@DomainService(repositoryFor = ApplicationSubscriptionType.class,
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)
@DomainServiceLayout(menuOrder = "40")
public class ApplicationSubscriptionTypeRepository {


    //region > addApplicationSubscriptionType (action)
    public static class CreateDomainEvent extends ActionDomainEvent<ApplicationSubscriptionTypeRepository> {
        public CreateDomainEvent(final ApplicationSubscriptionTypeRepository source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name="applicationSubscriptionTypes",sequence = "1")
    public Application addApplicationSubscriptionType(
            final @ParameterLayout(named="Application") Application application,
            final @ParameterLayout(named="Subscription Type") SubscriptionType subscriptionType,
            final @ParameterLayout(named="Price") BigDecimal price){
        final ApplicationSubscriptionType obj = container.newTransientInstance(ApplicationSubscriptionType.class);
        obj.setApplication(application);
        obj.setSubscriptionType(subscriptionType);
        obj.setPrice(price);
        container.persistIfNotAlready(obj);
        return obj.getApplication();
    }

    //endregion

    //region > injected services

    @javax.inject.Inject
    DomainObjectContainer container;

    //endregion

}
