package domainapp.dom.simple.businesses;

/**
 * Created by Administrator on 10/12/2015.
 */

import domainapp.dom.simple.applications.Application;
import domainapp.dom.simple.applications.SubscriptionStatus;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.util.ObjectContracts;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEvent;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEventable;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import java.math.BigDecimal;

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "BusinessTransaction"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findBusinessTransactionsByBusiness", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.BusinessTransaction "
                        + "WHERE business.name.indexOf(:name) >= 0 "),
})

@DomainObjectLayout(
        bookmarking = BookmarkPolicy.NEVER,
        cssClassFa = "fa-money"
)
@javax.jdo.annotations.Unique(name="BusinessTransaction_UNQ", members = {"business", "transactionId"})

public class BusinessTransaction implements Comparable<BusinessTransaction>, CalendarEventable {

    //region > business (property)
    private Business business;

    @MemberOrder(sequence = "1")
    @Title(sequence = "1")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Persistent
    public Business getBusiness() {
        return business;
    }

    public void setBusiness(final Business business) {
        this.business = business;
    }

    //endregion

    //region > ID (property)
    private Long transactionId;

    @Column(allowsNull="false")
    @MemberOrder(sequence = "2")
    @Title(sequence = "2",prepend = "-")
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(final Long transactionId) {
        this.transactionId = transactionId;
    }
    // endregion

    //region > businessLocation (property)
    private BusinessLocation businessLocation;

    @MemberOrder(sequence = "3")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public BusinessLocation getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(final BusinessLocation businessLocation) {
        this.businessLocation = businessLocation;
    }
    //endregion

    //region > application (property)
    private Application application;

    @MemberOrder(sequence = "4")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public Application getApplication() {
        return application;
    }

    public void setApplication(final Application application) {
        this.application = application;
    }
    //endregion

    //region > status (property)
    private TransactionStatus status;
    @MemberOrder(name="Transaction Status",sequence = "5")
    @Column(allowsNull = "false", defaultValue = "Active")
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Status is set by using Actions"
    )
    public TransactionStatus getStatus() {
        return status;
    }
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
    // endregion

    //region > transactionDate (Property)
    private DateTime transactionDate;

    @MemberOrder(name="Transaction Status",sequence = "6")
    @Column(allowsNull = "false")
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Date is automatically when by using Actions"
    )
    public DateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(DateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    //endregion

    //region > paymentDate (Property)
    private DateTime paymentDate;

    @MemberOrder(name="Transaction Status",sequence = "7")
    @Column(allowsNull = "true")
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Date is automatically set when using Actions"
    )
    public DateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(DateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
    //endregion

    //region > cancellationDate (Property)
    private DateTime cancellationDate;

    @MemberOrder(name="Transaction Status",sequence = "8")
    @Column(allowsNull = "true")
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Date is automatically set when using Actions"
    )
    public DateTime getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(DateTime cancellationDate) {
        this.cancellationDate = cancellationDate;
    }
    //endregion

    //region > businessTransactionType (property)
    private BusinessTransactionType businessTransactionType;

    @MemberOrder(name="Amount",sequence = "10")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public BusinessTransactionType getBusinessTransactionType() {
        return businessTransactionType;
    }

    public void setBusinessTransactionType(final BusinessTransactionType businessTransactionType) {
        this.businessTransactionType = businessTransactionType;
    }
    //endregion

    //region > amount (property)
    private BigDecimal amount;

    @Column(allowsNull="false", length=10, scale=2)
    @MemberOrder(name="Amount",sequence = "11")
    public BigDecimal getAmount() {return amount;}
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    //endregion


    //region > applyTransaction (action)
    @MemberOrder(name="status",sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public BusinessTransaction apply() {
        setTransactionDate(clockService.nowAsDateTime());
        setStatus(TransactionStatus.APPLIED);
        return this;
    }

    // disable action dependent on state of object
    public String disableApply() {
        return status.equals(TransactionStatus.APPLIED) ? "Transaction is already applied" : null;
    }
    //endregion

    //region > payTransaction (action)
    @MemberOrder(name="status",sequence = "2")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public BusinessTransaction pay() {
        setPaymentDate(clockService.nowAsDateTime());
        setStatus(TransactionStatus.PAID);
        return this;
    }
    // disable action dependent on state of object
    public String disablePay() {
        return status.equals(SubscriptionStatus.CANCELLED) ? "Can't pay cancelled transactions" : null;
    }
    //endregion

    //region > cancelTransaction (action)
    @MemberOrder(name="status",sequence = "3")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public BusinessTransaction cancel() {
        setCancellationDate(clockService.nowAsDateTime());
        setStatus(TransactionStatus.CANCELLED);
        return this;
    }

    // disable action dependent on state of object
    public String disableCancel() {
        return status.equals(SubscriptionStatus.CANCELLED) ? "Transaction is already cancelled" : null;
    }
    //endregion


    //region > calendar (module)
    @Programmatic
    @Override
    public String getCalendarName() {
        return "Applied on";
    }

    @Programmatic
    @Override
    public CalendarEvent toCalendarEvent() {
        return new CalendarEvent(getTransactionDate(), "", container.titleOf(this));
    }
    //endregion

    //region > compareTo

    @Override
    public int compareTo(final BusinessTransaction other) {
        return ObjectContracts.compare(this, other, "business", "transactionId");
    }

    //endregion

    //region > injected services

    @Inject
    private DomainObjectContainer container;

    @Inject
    private ClockService clockService;


    //endregion

}
