package domainapp.dom.simple.systementities;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.*;

import domainapp.dom.simple.businesses.BusinessLocation;

/**
 * Created by Admin on 10/11/2015. */

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "AutoNumber"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findAll", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.simple.systementities.AutoNumber"),
        @javax.jdo.annotations.Query(
                name = "findByLocationType", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.simple.systementities.AutoNumber "
                        + "WHERE type == :mytype && businessLocation == :myBusinessLocation "),
        @javax.jdo.annotations.Query(
        name = "findByType", language = "JDOQL",
        value = "SELECT "
                + "FROM domainapp.dom.simple.systementities.AutoNumber "
                + "WHERE type == :mytype")
}

)
@javax.jdo.annotations.Unique(name="AutoNumber_Id_UNQ", members = {"type","businessLocation"})
@DomainObject(autoCompleteAction = "findByLocationType", autoCompleteRepository = AutoNumberRepository.class, auditing= Auditing.ENABLED)


@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-cutlery"
)

public class AutoNumber {
    //region > type (property)
    private String type;

    @Column(allowsNull="false", length = 40)
    @Title(sequence="1")
    @MemberOrder(sequence = "1")

    public String getType() {
        return type;
    }
    public void setType(final String ptype) {
        this.type = ptype;
    }
    // endregion

    //region > businessLocation (property)
    private BusinessLocation businessLocation;

    @MemberOrder(sequence = "2")
    @Title(sequence="2", prepend = " in ")
    @Column(allowsNull = "true")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public BusinessLocation getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(final BusinessLocation businessLocation) {
        this.businessLocation = businessLocation;
    }
    //endregion


    //region > description (property)
    private String description;

    @Column(allowsNull="false", length = 40)
    @Title(sequence="3")
    @MemberOrder(sequence = "3")

    public String getDescription() {
        return description;
    }
    public void setDescription(final String sdescription) {
        this.description = sdescription;
    }
    // endregion

    // region > lastNumber (property)
    private Long lastNumber;

    @Column(allowsNull="false")
    @MemberOrder(sequence = "4")
    @Title(sequence = "4")
    public Long getLastNumber() {
        return lastNumber;
    }

    public void setLastNumber(final Long lastNumber) {
        this.lastNumber = lastNumber;
    }
    //endregion

    //region > version (derived property)
    public Long getVersionSequence() {
        return (Long) JDOHelper.getVersion(this);
    }
    //endregion

}
