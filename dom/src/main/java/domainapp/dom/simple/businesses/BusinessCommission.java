package domainapp.dom.simple.businesses;

import domainapp.dom.simple.financials.PaymentMediaType;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.util.ObjectContracts;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import java.math.BigDecimal;

/**
 * Created by Administrator on 10/2/2015.
 */

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "BusinessCommission"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Unique(name="BusinessCommission_UNQ", members = {"businessCategory","paymentMediaType"})

@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_CHILD,
        cssClassFa = "fa-flag"
)
public class BusinessCommission implements Comparable<BusinessCommission>{

    //region > BusinessCategory (property)
    private BusinessCategory businessCategory;

    @MemberOrder(sequence = "1")
    @Title(sequence = "1")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public BusinessCategory getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(final BusinessCategory businessCategory) {
        this.businessCategory = businessCategory;
    }
    //endregion

    //region > paymentMediaType (property)
    private PaymentMediaType paymentMediaType;

    @MemberOrder(sequence = "2")
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

    //region discountPercent (property)
    private BigDecimal discountPercent;

    @Column(allowsNull="false", length=4, scale=2)
    @MemberOrder(name="Fees",sequence = "3")
    public BigDecimal getDiscountPercent() {return discountPercent;}
    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }
    //endregion

    //region financialInstFee (property)
    private BigDecimal financialInstFee;

    @Column(allowsNull="false", length=4, scale=2)
    @MemberOrder(name="Fees",sequence = "4")
    public BigDecimal getFinancialInstFee() {return financialInstFee;}
    public void setFinancialInstFee(BigDecimal financialInstFee) {
        this.financialInstFee = financialInstFee;
    }
    //endregion


    //region > compareTo

    @Override
    public int compareTo(final BusinessCommission other) {
        return ObjectContracts.compare(this, other, "businessCategory","paymentMediaType");

    }

    //endregion


}
