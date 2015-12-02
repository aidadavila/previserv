package domainapp.dom.simple.businesses;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import java.util.List;

/**
 * Created by Administrator on 10/12/2015.
 */
@DomainService(repositoryFor = BusinessTransactionType.class)
@DomainServiceLayout(named = "Business Entities",menuBar = DomainServiceLayout.MenuBar.SECONDARY,menuOrder = "50")

public class BusinessTransactionTypeRepository {

    //region > listAllBusinessTransactionTypes (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<BusinessTransactionType> listAllBusinessTransactionTypes() {
        return container.allInstances(BusinessTransactionType.class);
    }
    //endregion

    //region > findSubscriptionTypeById (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "2")
    public List<BusinessTransactionType> findBusinessTransactionTypeById(
            @ParameterLayout(named="Business Transaction Type Id")
            final String subscriptionTypeId
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        BusinessTransactionType.class,
                        "findBusinessTransactionTypeById",
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
    @MemberOrder(sequence = "3")
    public List<BusinessTransactionType> findBusinessTransactionTypesByName(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        BusinessTransactionType.class,
                        "findBusinessTransactionTypesByName",
                        "name", name));
    }
    //endregion

    //region > create (action)
    public static class CreateDomainEvent extends ActionDomainEvent<BusinessTransactionTypeRepository> {
        public CreateDomainEvent(final BusinessTransactionTypeRepository source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(sequence = "4")
    public BusinessTransactionType addBusinessTransactionType(
            final @ParameterLayout(named="Business Transaction Type Id") String businessTransactionTypeId,
            final @ParameterLayout(named="Name") String name,
            final @ParameterLayout(named="Description") String description,
            final @ParameterLayout(named="Transaction Class") TransactionClass transactionClass
    )
    {
        final BusinessTransactionType obj = container.newTransientInstance(BusinessTransactionType.class);
        obj.setBusinessTransactionTypeId(businessTransactionTypeId);
        obj.setName(name);
        obj.setDescription(description);
        obj.setTransactionClass(transactionClass);
        container.persistIfNotAlready(obj);
        return obj;
    }
    //endregion

    //region > injected services

    @javax.inject.Inject
    DomainObjectContainer container;


    //endregion
}


