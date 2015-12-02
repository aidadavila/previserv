package domainapp.dom.simple.financials;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.util.ObjectContracts;

import domainapp.dom.simple.businesses.BusinessCommission;

/**
 * Created by Administrator on 9/30/2015.
 */

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "PaymentMediaType"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findAllPaymentMediaTypes", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.simple.financials.PaymentMediaType "),
        @javax.jdo.annotations.Query(
                name = "findPaymentMediaTypesByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.simple.financials.PaymentMediaType "
                        + "WHERE name.indexOf(:name) >= 0 "),
        @javax.jdo.annotations.Query(
                name = "findPaymentMediaTypeById", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.simple.financials.PaymentMediaType "
                        + "WHERE paymentMediaTypeId.indexOf(:paymentMediaTypeId) >= 0 ")
})


@javax.jdo.annotations.Unique(name="PaymentMediaType_Id_UNQ", members = {"paymentMediaTypeId"})
@DomainObject(autoCompleteAction = "findPaymentMediaTypesByName", autoCompleteRepository = PaymentMediaTypeRepository.class, auditing= Auditing.ENABLED)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-credit-card"

)
public class PaymentMediaType implements Comparable<PaymentMediaType> {

    //region > ID (property)

    private String paymentMediaTypeId;

    @javax.jdo.annotations.Column(allowsNull="false", length = 10)

    public String getPaymentMediaTypeId() {
        return paymentMediaTypeId;
    }

    public void setPaymentMediaTypeId(final String paymentMediaTypeId) {
        this.paymentMediaTypeId = paymentMediaTypeId;
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


    //region > businessCommissions (collection)
    @Persistent(mappedBy = "paymentMediaType", dependentElement = "false")
    private SortedSet<BusinessCommission> businessCommissions = new TreeSet<BusinessCommission>();

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "4")
    public SortedSet<BusinessCommission> getBusinessCommissions() {
        return businessCommissions;
    }

    public void setBusinessCommissions(final SortedSet<BusinessCommission> businessCommissions) {
        this.businessCommissions = businessCommissions;
    }
    //endregion


    //region > version (derived property)
    public Long getVersionSequence() {
        return (Long) JDOHelper.getVersion(this);
    }
    //endregion

    //region > compareTo

    @Override
    public int compareTo(final PaymentMediaType other) {
        return ObjectContracts.compare(this, other, "paymentMediaTypeId");
    }

    //endregion
}
