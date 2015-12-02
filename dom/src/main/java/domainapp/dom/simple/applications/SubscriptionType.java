package domainapp.dom.simple.applications;

/**
 * Created by Administrator on 9/24/2015.
 */

import domainapp.dom.simple.businesses.BusinessStatus;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.util.ObjectContracts;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.jdo.JDOHelper;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "simple",
        table = "SubscriptionType"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "find", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.SubscriptionType "),
        @javax.jdo.annotations.Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.SubscriptionType "
                        + "WHERE name.indexOf(:name) >= 0 "),
        @javax.jdo.annotations.Query(
                name = "findById", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.SubscriptionType "
                        + "WHERE subscriptionTypeId.indexOf(:subscriptionTypeId) >= 0 ")
})
@javax.jdo.annotations.Unique(name="SubscriptionType_Id_UNQ", members = {"subscriptionTypeId"})
@DomainObject(autoCompleteAction = "findSubscriptionTypeByName", autoCompleteRepository = SubscriptionTypeRepository.class)

@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-flag"
)
public class SubscriptionType  implements Comparable<SubscriptionType> {


    //region > ID (property)
    private String subscriptionTypeId;

    @Column(allowsNull="false", length = 10)
    @MemberOrder(sequence = "1")
    public String getSubscriptionTypeId() {
        return subscriptionTypeId;
    }
    public void setSubscriptionTypeId(final String subscriptionTypeId) {
        this.subscriptionTypeId = subscriptionTypeId;
    }
    // endregion

    //region > name (property)

    private String name;

    @Column(allowsNull="false", length = 40)
    @Title(sequence="1")
    @MemberOrder(sequence = "2")
    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }

    // endregion

    //region > status (property)

    private BusinessStatus status;

    @Column(allowsNull = "false")
    @MemberOrder(name="Status",sequence = "3")
    public BusinessStatus getStatus() {
        return status;
    }

    public void setStatus(BusinessStatus status) {
        this.status = status;
    }

    // endregion

    //region > creationTime (Property)
    private DateTime creationTime;

    @Column(allowsNull = "false")
    @MemberOrder(name="Status",sequence = "4")
    @Persistent
    public DateTime getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }
    //endregion

    //region > version (derived property)
    public Long getVersionSequence() {
        return (Long) JDOHelper.getVersion(this);
    }
    //endregion

@Override
public int compareTo(final SubscriptionType other) {
        return ObjectContracts.compare(this, other, "subscriptionTypeId");
        }


    //region > injected services

    @Inject
    @SuppressWarnings("unused")
    private DomainObjectContainer container;

    //endregion

    @Inject
    private ClockService clockService;

}


