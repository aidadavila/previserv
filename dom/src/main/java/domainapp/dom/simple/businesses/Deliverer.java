package domainapp.dom.simple.businesses;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.DateTime;

import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.value.Blob;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.WithApplicationTenancy;

/**
 * Created by Administrator on 10/19/2015.
 */

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "Deliverer"
)

@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findDeliverersByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.simple.businesses.Deliverer "
                        + "WHERE lastName.indexOf(:lastName) >= 0 "),
        @javax.jdo.annotations.Query(
                name = "findDelivererById", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.simple.businesses.Deliverer "
                        + "WHERE delivererId.indexOf(:delivererId) >= 0 ")
})

@javax.jdo.annotations.Unique(name="Deliverer_Id_UNQ", members = {"businessLocation","delivererId"})
@DomainObject(autoCompleteAction = "findDeliverersByName", autoCompleteRepository = DelivererRepository.class,auditing= Auditing.ENABLED )
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-motorcycle"
)

public class Deliverer implements Comparable<Deliverer>, WithApplicationTenancy {

    //region > businessLocation (property)
    private BusinessLocation businessLocation;

    @MemberOrder(sequence = "1")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public BusinessLocation getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(final BusinessLocation businessLocation) {
        this.businessLocation = businessLocation;
    }
    //endregion

    //region > delivererId (property)
    private String delivererId;

    @Column(allowsNull="false", length = 10)
    @MemberOrder(sequence = "1")
    @Title(sequence="1")
    public String getDelivererId() {
        return delivererId;
    }

    public void setDelivererId(final String delivererId) {
        this.delivererId = delivererId;
    }
    // endregion

    //region > firstName (property)
    private String firstName;

    @Column(allowsNull="false", length = 20)
    @Title(sequence="2", prepend = "-")
    @MemberOrder(sequence = "3")

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    // endregion

    //region > middleName (property)
    private String middleName;

    @Column(allowsNull="true", length = 20)
    @Title(sequence="3")
    @MemberOrder(sequence = "4")

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }
    // endregion

    //region > lastName (property)
    private String lastName;

    @Column(allowsNull="false", length = 20)
    @Title(sequence="4")
    @MemberOrder(sequence = "5")

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    // endregion

    //region > lastName2 (property)
    private String lastName2;

    @Column(allowsNull="true", length = 20)
    @Title(sequence="5")
    @MemberOrder(sequence = "6")

    public String getLastName2() {
        return lastName2;
    }

    public void setLastName2(final String lastName2) {
        this.lastName2 = lastName2;
    }
    // endregion

    //region > contactPhone (property)
    private Long contactPhone;

    @Column(allowsNull="true")
    //    @Property(maxLength = 11)
    @MemberOrder(sequence = "7")

    public Long getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(final Long contactPhone) {
        this.contactPhone = contactPhone;
    }
    //endregion )

    //region > deliveryPhone (property)
    private Long deliveryPhone;

    @Column(allowsNull="true")
    //    @Property(maxLength = 11)
    @MemberOrder(sequence = "8")

    public Long getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(final Long deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }
    //endregion

    //region > creationTime (Property)
    private DateTime creationTime;

    @Column(allowsNull = "false")
    @MemberOrder(name="Status",sequence = "4")
    public DateTime getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }
    //endregion

    //region > delivererPicture (property)
    public Blob delivererPicture;

    @Column(allowsNull="true")
    @Property(optionality = Optionality.OPTIONAL)
    @MemberOrder(name = "Picture",sequence = "120")
    @javax.jdo.annotations.Persistent(defaultFetchGroup="false", columns = {
            @Column(name = "delivererPicture_name"),
            @Column(name = "delivererPicture_mimetype"),
            @Column(name = "delivererPicture_bytes",
                    jdbcType = "BLOB", sqlType = "VARBINARY")
    })

    public Blob getDelivererPicture() {
        return delivererPicture;
    }

    public void setDelivererPicture(final Blob delivererPicture) {
        this.delivererPicture = delivererPicture;
    }
    //endregion

    //region applicationTenancy (property)
    @MemberOrder(sequence = "130")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)

    @Override
    public ApplicationTenancy getApplicationTenancy() {
        return this.getBusinessLocation().getBusiness().getApplicationTenancy();
    }
    //endregion


    //region > assignPicture (action)
    @MemberOrder(name="delivererPicture",sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public Deliverer assignPicture(
            final @ParameterLayout(named = "Deliverer Picture") Blob image
    ) {
        setDelivererPicture(image);
        return this;
    }
    //endregion


    //region > compareTo
    @Override
    public int compareTo(final Deliverer other) {
        return ObjectContracts.compare(this, other, "businessLocation","delivererId");
    }
    //endregion

}
