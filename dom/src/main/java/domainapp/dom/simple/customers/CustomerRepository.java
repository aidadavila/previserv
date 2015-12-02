package domainapp.dom.simple.customers;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by Administrator on 10/12/2015.
 */
@DomainService(repositoryFor = Customer.class)
@DomainServiceLayout(named = "Customers",menuOrder = "20")

public class CustomerRepository {

    //region > listAll (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<Customer> listAllCustomers() {
        return container.allInstances(Customer.class);
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
    public List<Customer> findCustomerById(
            @ParameterLayout(named="Customer Id")
            final String customerId
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        Customer.class,
                        "findCustomerById",
                        "customerId", customerId));
    }
    //endregion

    //region > findCustomersByName (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "3")
    public List<Customer> findCustomersByName(
            @ParameterLayout(named="Last Name")
            final String lastName
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        Customer.class,
                        "findCustomersByName",
                        "lastName", lastName));
    }
    //endregion

    //region > addCustomer (action)
    public static class CreateDomainEvent extends ActionDomainEvent<CustomerRepository> {
        public CreateDomainEvent(final CustomerRepository source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(sequence = "4")
    public Customer addCustomer(
            final @ParameterLayout(named="Customer Id") String customerId,
            final @ParameterLayout(named="First Name") String firstName,
            final @ParameterLayout(named="Middle Name") @Parameter(optionality = Optionality.OPTIONAL)String middleName,
            final @ParameterLayout(named="Last Name") String lastName,
            final @ParameterLayout(named="Last Name 2") @Parameter(optionality = Optionality.OPTIONAL)String lastName2
    )
    {
        final Customer obj = container.newTransientInstance(Customer.class);
        obj.setCustomerId(customerId);
        obj.setFirstName(firstName);
        obj.setMiddleName(middleName);
        obj.setLastName(lastName);
        obj.setLastName2(lastName2);
        obj.setCreationTime(clockService.nowAsDateTime());
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
