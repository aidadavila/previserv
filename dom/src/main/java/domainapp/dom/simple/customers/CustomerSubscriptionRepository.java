package domainapp.dom.simple.customers;

import domainapp.dom.simple.applications.SubscriptionStatus;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import java.util.List;

/**
 * Created by Administrator on 10/13/2015.
 */
@DomainService(
        nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY,
        repositoryFor = CustomerSubscription.class)
@DomainServiceLayout(named = "Customers",menuOrder = "20")

public class CustomerSubscriptionRepository {

    //region > listAllCustomerSubscriptions (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(name="customerSubscriptions",sequence = "1")
    public List<CustomerSubscription> listAllCustomerSubscriptions() {
            return container.allInstances(CustomerSubscription.class);
    }
    //endregion

    //region > findCustomerSubscriptionsById (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.NEVER
    )
    @MemberOrder(sequence = "3")
    public List<CustomerSubscription> findCustomerSubscriptionById(
            @ParameterLayout(named="Customer Subscription Id")
            final Long parCustomerSubscriptionId
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        CustomerSubscription.class,
                        "findCustomerSubscriptionsById",
                        "parCustomerSubscriptionId", parCustomerSubscriptionId));
    }
    //endregion

    //region > findCustomerSubscriptionsByCustomerName (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.NEVER
    )
    @MemberOrder(sequence = "4")
    public List<CustomerSubscription> findCustomerSubscriptionsByCustomerName(
            @ParameterLayout(named="Customer's Last Name")
            final String lastName
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        CustomerSubscription.class,
                        "findCustomerSubscriptionsByCustomerName",
                        "lastName", lastName));
    }
    //endregion

    
    //region > addSubscription (action)
    public static class CreateDomainEvent extends ActionDomainEvent<CustomerSubscriptionRepository> {
            public CreateDomainEvent(final CustomerSubscriptionRepository source, final Identifier identifier, final Object... arguments) {
                    super(source, identifier, arguments);
            }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name="customerSubscriptions",sequence = "2")
    public CustomerSubscription addSubscription(
            final @ParameterLayout(named="Customer") @Parameter() Customer customer,
            final @ParameterLayout(named="Customer Subscription Id") Long customerSubscriptionId
    )
    {
            final CustomerSubscription obj = container.newTransientInstance(CustomerSubscription.class);
            obj.setCustomerSubscriptionId(customerSubscriptionId);
            obj.setCustomer(customer);
            obj.setStatus(SubscriptionStatus.REGISTRATION);
            container.persistIfNotAlready(obj);
            return obj;
    }
    //endregion


    //region > injected services

    @javax.inject.Inject
    DomainObjectContainer container;

    //endregion


}
