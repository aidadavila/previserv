package domainapp.dom.simple.financials;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import java.util.List;

/**
 * Created by Administrator on 10/1/2015.
 */
@DomainService(repositoryFor = FinancialInst.class)
@DomainServiceLayout(named = "Financial Entities",menuBar = DomainServiceLayout.MenuBar.SECONDARY,menuOrder = "30")
public class FinancialInstRepository {
/*
    //region > title
    public TranslatableString title() {
        return TranslatableString.tr("Financial Insts");
    }
    //endregion
*/

    //region > listAll (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<FinancialInst> listAllFinancialInst() {
        return container.allInstances(FinancialInst.class);
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
    public List<FinancialInst> findFinancialInstByName(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        FinancialInst.class,
                        "findFinancialInstByName",
                        "name", name));
    }
    //endregion

    //region > create (action)
    public static class CreateDomainEvent extends ActionDomainEvent<FinancialInstRepository> {
        /*
        public CreateDomainEvent(final FinancialInsts source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
        */
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(sequence = "4")
    public FinancialInst addFinancialInst(
            final @ParameterLayout(named="Financial Inst Id") String financialInstId,
            final @ParameterLayout(named="Name") String name){
        final FinancialInst obj = container.newTransientInstance(FinancialInst.class);
        obj.setFinancialInstId(financialInstId);
        obj.setName(name);
        container.persistIfNotAlready(obj);
        return obj;
    }

    //endregion

    //region > injected services

    @javax.inject.Inject
    DomainObjectContainer container;

    //endregion

}
