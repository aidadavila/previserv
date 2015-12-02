package domainapp.dom.simple.systementities;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;

import domainapp.dom.simple.businesses.BusinessLocation;

/**
 * Created by Admin on 10/11/2015.
 */

@DomainService(repositoryFor = AutoNumber.class)
@DomainServiceLayout(named = "Applications",menuBar = DomainServiceLayout.MenuBar.SECONDARY,menuOrder = "40")


public class AutoNumberRepository {

/*
    //region > title
    public TranslatableString title() {
        return TranslatableString.tr("AutoNumber");
    }
    //endregion
*/

    //region > listAll (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.NEVER
    )
    @MemberOrder(sequence = "1")
    public List<AutoNumber> listAllAutoNumbers() {
        return container.allInstances(AutoNumber.class);
    }
    //endregion

    //region > findByLocType (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.NEVER
    )
    @MemberOrder(sequence = "2")
    public List<AutoNumber> findByLocationType(
            @ParameterLayout(named="Type")
            final String mytype,
            @ParameterLayout(named="Business Location")
            final BusinessLocation myBusinessLocation
    ) {
        return container.allMatches(
                new QueryDefault<>(
                        AutoNumber.class,
                        "findByLocationType",
                        "mytype", mytype,
                        "myBusinessLocation", myBusinessLocation));
    }
    //endregion


    //region > findByType (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.NEVER
    )
    @MemberOrder(sequence = "3")
    public List<AutoNumber> findByType(
            @ParameterLayout(named="Type")
            final String mytype
                ) {
        return container.allMatches(
                new QueryDefault<>(
                        AutoNumber.class,
                        "findByType",
                        "mytype", mytype));
    }
    //endregion

    //region > addAutoNumbers (action)
    public static class CreateDomainEvent extends ActionDomainEvent<AutoNumberRepository> {
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @ActionLayout(
            hidden = Where.EVERYWHERE
    )
    public AutoNumber addAutoNumber(
            final @ParameterLayout(named="Type") String type,
            final @ParameterLayout(named="Business Location") BusinessLocation businessLocation,
            final @ParameterLayout(named="Description" ) String description,
            final @ParameterLayout(named="Last Number" )Long lastNumber

    )
    {
        final AutoNumber obj = container.newTransientInstance(AutoNumber.class);
        obj.setType(type);
        obj.setBusinessLocation(businessLocation);
        obj.setDescription(description);
        obj.setLastNumber(lastNumber);
        container.persistIfNotAlready(obj);

        return obj;
    }


    public AutoNumber addAutoNumber(
            final @ParameterLayout(named="Type") String type,
            final @ParameterLayout(named="Description" ) String description,
            final @ParameterLayout(named="Last Number" )Long lastNumber

    )
    {
        final AutoNumber obj = container.newTransientInstance(AutoNumber.class);
        obj.setType(type);
        //obj.setBusinessLocation(businessLocation);
        obj.setDescription(description);
        obj.setLastNumber(lastNumber);
        container.persistIfNotAlready(obj);

        return obj;
    }

    //endregion


    //region > NextAutonumber (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            hidden = Where.EVERYWHERE
    )

    public long nextAutoNumber(String type, BusinessLocation businessLocation)
    {
        List<AutoNumber> myAutoNumbers = this.findByLocationType(type, businessLocation);
        AutoNumber myAutoNumber = null;
        if (myAutoNumbers.isEmpty())
        {
            long l = 0;
            myAutoNumber = this.addAutoNumber(type, businessLocation, "GENERATED",l);
        }
        else
            myAutoNumber = myAutoNumbers.get(0);

        long lNumber = myAutoNumber.getLastNumber() + 1;
        myAutoNumber.setLastNumber(lNumber);

        return (lNumber);
    }


    public long nextAutoNumber(String type)
    {
        List<AutoNumber> myAutoNumbers = this.findByType(type);
        AutoNumber myAutoNumber = null;
        if (myAutoNumbers.isEmpty())
        {
            long l = 0;
            myAutoNumber = this.addAutoNumber(type, "GENERATED",l);
        }
        else
            myAutoNumber = myAutoNumbers.get(0);

        long lNumber = myAutoNumber.getLastNumber() + 1;
        myAutoNumber.setLastNumber(lNumber);

        return (lNumber);
    }

    //endregion

    //region > injected services

    @Inject
    DomainObjectContainer container;

    protected DomainObjectContainer getContainer() {
        return container;
    }

    public final void setContainer(final DomainObjectContainer container) {
        this.container = container;
    }

    //endregion
}
