package domainapp.dom.simple.applications;

/**
 * Created by Administrator on 9/23/2015.
 */

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.util.ObjectContracts;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;
import java.util.SortedSet;
import java.util.TreeSet;

@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "simple",
        table = "Application"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findAllApplications", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.Application "),
        @javax.jdo.annotations.Query(
                name = "findApplicationsByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.Application "
                        + "WHERE name.indexOf(:name) >= 0 "),
        @javax.jdo.annotations.Query(
                name = "findApplicationById", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.Application "
                        + "WHERE applicationId.indexOf(:applicationId) >= 0 ")
})
@javax.jdo.annotations.Unique(name="Application_Id_UNQ", members = {"applicationId"})
@DomainObject(autoCompleteAction = "findApplicationsByName", autoCompleteRepository = ApplicationRepository.class, auditing=Auditing.ENABLED)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-mobile"

)

public class Application implements Comparable<Application> {

/*
    //region > identification
    public TranslatableString title() {
        return TranslatableString.tr("Application: {name}", "name", getName());
    }
    //endregion
*/
    //region > ID (property)

    private String applicationId;

    @javax.jdo.annotations.Column(allowsNull="false", length = 10)

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(final String applicationId) {
        this.applicationId = applicationId;
    }

    // endregion

    //region > name (property)

    private String name;

    @javax.jdo.annotations.Column(allowsNull="false", length = 40)
    @Title(sequence="1")

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    // endregion


    //region > applicationSubscriptionTypes (collection)
    @Persistent(mappedBy = "application", dependentElement = "false")
    private SortedSet<ApplicationSubscriptionType> applicationSubscriptionTypes = new TreeSet<ApplicationSubscriptionType>();

    @CollectionLayout(render = RenderType.EAGERLY)
    public SortedSet<ApplicationSubscriptionType> getApplicationSubscriptionTypes() {
        return applicationSubscriptionTypes;
    }

    public void setApplicationSubscriptionTypes(final SortedSet<ApplicationSubscriptionType> applicationSubscriptionTypes) {
        this.applicationSubscriptionTypes = applicationSubscriptionTypes;
    }

    //endregion


    //region > version (derived property)
    public Long getVersionSequence() {
        return (Long) JDOHelper.getVersion(this);
    }
    //endregion

    //region > compareTo

    @Override
    public int compareTo(final Application other) {
        return ObjectContracts.compare(this, other, "applicationId");
    }

    //endregion

    //region > injected services

    @javax.inject.Inject
    @SuppressWarnings("unused")
    private DomainObjectContainer container;

    //endregion


}

