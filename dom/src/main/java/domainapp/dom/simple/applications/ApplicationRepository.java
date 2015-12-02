package domainapp.dom.simple.applications;

/**
 * Created by Administrator on 9/23/2015.
 */

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;

import java.util.List;

@DomainService(repositoryFor = Application.class)
@DomainServiceLayout(named = "Applications",menuBar = DomainServiceLayout.MenuBar.SECONDARY ,menuOrder = "10")
public class ApplicationRepository {

    //region > title
    public TranslatableString title() {
        return TranslatableString.tr("Applications");
    }
    //endregion

    //region > listAll (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<Application> listAllApplications() {
        return container.allInstances(Application.class);
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
    public List<Application> findApplicationById(
            @ParameterLayout(named="Application Id")
            final String applicationId
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        Application.class,
                        "findApplicationById",
                        "applicationId", applicationId));
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
    public List<Application> findApplicationsByName(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        Application.class,
                        "findApplicationsByName",
                        "name", name));
    }
    //endregion

    //region > create (action)
    public static class CreateDomainEvent extends ActionDomainEvent<ApplicationRepository> {
        public CreateDomainEvent(final ApplicationRepository source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(sequence = "4")
    public Application addApplication(
            final @ParameterLayout(named="Applications Id") String applicationId,
            final @ParameterLayout(named="Name") String name){
        final Application obj = container.newTransientInstance(Application.class);
        obj.setApplicationId(applicationId);
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

