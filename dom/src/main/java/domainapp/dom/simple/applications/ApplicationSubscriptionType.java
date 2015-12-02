package domainapp.dom.simple.applications;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.util.ObjectContracts;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import java.math.BigDecimal;

/**
 * Created by Administrator on 9/29/2015.
 */


@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "ApplicationSubscriptionType"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")

@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "find", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.ApplicationSubscriptionType "),
})
@javax.jdo.annotations.Unique(name="ApplicationSubscriptionType_UNQ", members = {"application","subscriptionType"})
/*
@DomainObject(autoCompleteAction = "findByName", autoCompleteRepository = SubscriptionTypes.class)
 */

@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-flag"
)
public class ApplicationSubscriptionType implements Comparable<ApplicationSubscriptionType> {

     //region > Application (property)
    private Application application;

    @MemberOrder(sequence = "1")
    @Title(sequence = "1")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public Application getApplication() {
        return application;
    }

    public void setApplication(final Application application) {
        this.application = application;
    }
    //endregion

    //region > SubscriptionType (property)
    private SubscriptionType subscriptionType;

    @MemberOrder(sequence = "2")
    @Title(sequence = "2",prepend = "-")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }
    public void setSubscriptionType(final SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
    //endregion

    //region price (property)
    private BigDecimal price;

    @Column(allowsNull="false", length=10, scale=2)
    @MemberOrder(name="Pricing",sequence = "5")
    public BigDecimal getPrice() {return price;}
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    //endregion


    //region > compareTo

    @Override
    public int compareTo(final ApplicationSubscriptionType other) {
        return ObjectContracts.compare(this, other, "application","subscriptionType");
//        return ObjectContracts.compare(this, other, "applicationSubscriptionTypeId");

    }

    //endregion

}
