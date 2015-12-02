package domainapp.dom.simple.businesses;

import domainapp.dom.simple.financials.PaymentMediaType;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.util.ObjectContracts;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

/**
 * Created by Administrator on 9/30/2015.
 */
@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "BusinessPaymentMediaType"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")

@javax.jdo.annotations.Unique(name="BusinessPaymentMediaType_Id_UNQ", members = {"businessLocation","paymentMediaType"})
@DomainObject(auditing= Auditing.ENABLED)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-credit-card"
)

public class BusinessPaymentMediaType implements Comparable<BusinessPaymentMediaType>{

         //businessLocation (property)
        private BusinessLocation businessLocation;

        @MemberOrder(sequence = "2")
        @Title(sequence = "1")
        @Column(allowsNull = "false")
        @PropertyLayout(hidden = Where.REFERENCES_PARENT)
        public BusinessLocation getBusinessLocation() {
                return businessLocation;
        }

        public void setBusinessLocation(final BusinessLocation businessLocation) {
                this.businessLocation = businessLocation;
        }
        //endregion

        //paymentMediaType (property)
        private PaymentMediaType paymentMediaType;

        @MemberOrder(sequence = "3")
        @Title(sequence = "2",prepend = "-")
        @Column(allowsNull = "false")
        @PropertyLayout(hidden = Where.REFERENCES_PARENT)
        public PaymentMediaType getPaymentMediaType() {
                return paymentMediaType;
        }

        public void setPaymentMediaType(final PaymentMediaType paymentMediaType) {
                this.paymentMediaType = paymentMediaType;
        }
        //endregion


        //region > version (derived property)
        public Long getVersionSequence() {
                return (Long) JDOHelper.getVersion(this);
        }
        //endregion

    //region > compareTo

    @Override
    public int compareTo(final BusinessPaymentMediaType other) {
        return ObjectContracts.compare(this, other, "businessLocation","paymentMediaType");
    }

    //endregion
}
