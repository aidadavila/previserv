package domainapp.dom.simple.customerorders;

/**
 * Created by Administrator on 10/21/2015.
 */

import java.math.BigDecimal;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.DateTime;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

import domainapp.dom.DomainAppDomainModule;
import domainapp.dom.simple.customers.CustomerSubscription;
import domainapp.dom.simple.financials.PaymentMediaType;

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "CustomerOrderPayment"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")

@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findCustomerOrderPayment", language = "JDOQL",
                value = "SELECT  "
                        + "FROM domainapp.dom.simple.customerorders.CustomerOrderPayment "
                        + "WHERE customerOrder == :parCustomerOrder "),
})


@javax.jdo.annotations.Unique(name="Customer_Order_Payment_Id_UNQ", members = {"customerOrder","pmCreditCard"})
@DomainObject(auditing= Auditing.ENABLED )

@DomainObjectLayout(
        bookmarking = BookmarkPolicy.NEVER,
        cssClassFa = "fa-money"
)

public class CustomerOrderPayment implements Comparable<CustomerOrderPayment> {

    //region > customerOrder (property)
    private CustomerOrder customerOrder;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Payment info cannot be changed"
    )
    @MemberOrder(sequence = "1")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Title(sequence = "1")
    @Persistent
    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(final CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }


    //endregion

    //region > pmCcPaymentMediaType Payment Media's paymentMediaType (property)
    private PaymentMediaType pmCcPaymentMediaType;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Payment info cannot be changed"
    )
    @MemberOrder(sequence = "3")
    @Column(allowsNull = "true")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Title(sequence = "3", prepend = "-")
    public PaymentMediaType getPmCcPaymentMediaType() {
        return pmCcPaymentMediaType;
    }

    public void setPmCcPaymentMediaType(final PaymentMediaType pmCcPaymentMediaType) {
        this.pmCcPaymentMediaType = pmCcPaymentMediaType;
    }
    //endregion

    //region > pmCreditCard (property) payment media credit card number
    private String pmCreditCard;

    @Property(
            regexPattern = "\\b[1-9][0-9]{15}\\b", //Validates that the credit card number starts with a digit followed by 15 more digits
            editing = Editing.DISABLED,
            editingDisabledReason = "Payment info cannot be changed"
    )

    @MemberOrder(sequence = "4")
    @Column(allowsNull = "true", length = 16)
    public String getPmCreditCard() {
        return pmCreditCard;
    }

    public void setPmCreditCard(final String pmCreditCard) {
        this.pmCreditCard = pmCreditCard;
    }
    // endregion

    //region > cm_name (property) name associated with the collection media
    private String pmName;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Payment info cannot be changed"
    )
    @MemberOrder(sequence = "5")
    @Column(allowsNull = "true", length = 100)
    public String getPmName() {
        return pmName;
    }

    public void setPmName(final String pmName) {
        this.pmName = pmName;
    }
    // endregion

    //region > pmExpirationMonth (property)
    private Integer pmExpirationMonth;

    @Property(
            editing = Editing.DISABLED,
            maxLength = 2,
            editingDisabledReason = "Payment info cannot be changed"
    )
    @MemberOrder(sequence = "6")
    @Column(allowsNull = "true")
    public Integer getPmExpirationMonth() {
        return pmExpirationMonth;
    }

    public void setPmExpirationMonth(final Integer pmExpirationMonth) {
        this.pmExpirationMonth = pmExpirationMonth;
    }
    // endregion

    //region > pmExpirationYear (property)
    private Integer pmExpirationYear;

    @Property(
            editing = Editing.DISABLED,
            maxLength = 2,
            editingDisabledReason = "Payment info cannot be changed"
    )
    @MemberOrder(sequence = "7")
    @Column(allowsNull = "true")
    public Integer getPmExpirationYear() {
        return pmExpirationYear;
    }

    public void setPmExpirationYear(final Integer pmExpirationYear) {
        this.pmExpirationYear = pmExpirationYear;
    }
    // endregion

    //region > pmCcCvv (property)
    private Integer pmCcCvv;

    @MemberOrder(sequence = "8")
    @Column(allowsNull = "true")
    @Property(
            editing = Editing.DISABLED,
            maxLength = 3,
            editingDisabledReason = "Payment info cannot be changed"
    )
    public Integer getPmCcCvv() {
        return pmCcCvv;
    }

    public void setPmCcCvv(final Integer pmCcCvv) {
        this.pmCcCvv = pmCcCvv;
    }
    // endregion

    //region > pmCcZip (property)
    private Integer pmCcZip;

    @Property(
            editing = Editing.DISABLED,
            maxLength = 10,
            editingDisabledReason = "Payment info cannot be changed"
    )
    @MemberOrder(sequence = "9")
    @Column(allowsNull = "true")
    public Integer getPmCcZip() {
        return pmCcZip;
    }

    public void setPmCcZip(final Integer pmCcZip) {
        this.pmCcZip = pmCcZip;
    }
    // endregion

    //region > amountPaid (property)
    private BigDecimal amountPaid;

    @Column(allowsNull="false", length=10, scale=2, defaultValue = "0")
    @MemberOrder(sequence = "10")
    public BigDecimal getAmountPaid() {return amountPaid;}

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String validateAmountPaid(BigDecimal amountPaid) {
        return amountPaid.compareTo(BigDecimal.ZERO) < 0
                ? "Amount Paid cannot be negative"
                : null;
    }

    public void modifyAmountPaid(BigDecimal amountPaid){
        this.getCustomerOrder().addToTotalAmountPaid(amountPaid.doubleValue() - this.getAmountPaid().doubleValue());
        this.setAmountPaid(amountPaid);
        this.getCustomerOrder().order();
    }
    //endregion

    //region > customerSubscription (property)
    private CustomerSubscription customerSubscription;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Payment info cannot be changed",
            optionality = Optionality.OPTIONAL
    )
    @MemberOrder(sequence = "11")
    @Column(allowsNull = "true")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public CustomerSubscription getCustomerSubscription() {
        return customerSubscription;
    }

    public void setCustomerSubscription(final CustomerSubscription customerSubscription) {
        this.customerSubscription = customerSubscription;
    }
    //endregion

    //region > paymentTime (Property)
    private DateTime paymentTime;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Payment time is automatically set when using actions"
    )
    @Column(allowsNull = "true")
    @MemberOrder(name="Status",sequence = "12")
    public DateTime getPaymentTime() {
        return paymentTime;
    }
    public void setPaymentTime(DateTime paymentTime) {
        this.paymentTime = paymentTime;
    }
    //endregion

    //region > version (derived property)
    @MemberOrder(sequence = "1000")
    public Long getVersionSequence() {
            return (Long) JDOHelper.getVersion(this);
    }
    //endregion

    //region > compareTo
    @Override
    public int compareTo(final CustomerOrderPayment other) {
            return ObjectContracts.compare(this, other, "customerOrder","pmCreditCard");
    }
    //endregion

  //region > delete (events)
    public static class DeletedEvent extends CustomerOrderPayment.ActionDomainEvent {
        private static final long serialVersionUID = 1L;
        public DeletedEvent(
                final CustomerOrderPayment source,
                final Identifier identifier,
                final Object... arguments) {
            super(source, identifier, arguments);
        }
    }


    public List<CustomerOrderPayment> delete() {

        // obtain title first, because cannot reference object after deleted
        final String title = container.titleOf(this);

        final List<CustomerOrderPayment> returnList = null;
        container.removeIfNotAlready(this);
        container.informUser(
                TranslatableString.tr("Deleted {title}", "title", title), this.getClass(), "delete");

        return returnList;
    }
    //endregion

    //region > events
    public static abstract class PropertyDomainEvent<T> extends DomainAppDomainModule.PropertyDomainEvent<CustomerOrderPayment, T> {

        public PropertyDomainEvent(final CustomerOrderPayment source, final Identifier identifier) {
            super(source, identifier);
        }

        public PropertyDomainEvent(final CustomerOrderPayment source, final Identifier identifier, final T oldValue, final T newValue) {
            super(source, identifier, oldValue, newValue);
        }
    }

    public static abstract class ActionDomainEvent extends DomainAppDomainModule.ActionDomainEvent<CustomerOrderPayment> {
        private static final long serialVersionUID = 1L;
        public ActionDomainEvent(
                final CustomerOrderPayment source,
                final Identifier identifier,
                final Object... arguments) {
            super(source, identifier, arguments);
        }
    }
    //endregion

    //region > persisted
    public void persisted() {
        //System.out.println("Grabe Detalle Pago");
        this.getCustomerOrder().addToTotalAmountPaid(this.getAmountPaid().doubleValue());
        this.getCustomerOrder().order();
    }
    // endregion

    //region > removing
    public void removing() {
        System.out.println("Borre Detalle Pago" );
        this.getCustomerOrder().addToTotalAmountPaid(-this.getAmountPaid().doubleValue());
    }
    // endregion

    //region > injected services
    @javax.inject.Inject
    DomainObjectContainer container;
    //endregion


}
