package domainapp.dom.simple.customers;

/**
 * Created by Administrator on 10/13/2015.
 */

import domainapp.dom.simple.applications.SubscriptionStatus;
import domainapp.dom.simple.customerorders.ShoppingCart;
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
import java.util.SortedSet;
import java.util.TreeSet;

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "CustomerSubscription"
)

@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")

@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findCustomerSubscriptionsById", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.CustomerSubscription "
                        + "WHERE customerSubscriptionId == :parCustomerSubscriptionId"),
        @javax.jdo.annotations.Query(
                name = "findCustomerSubscriptionsByCustomerName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.CustomerSubscription "
                        + "WHERE this.customer.lastName.indexOf(:lastName) >= 0 ")

        
})

@javax.jdo.annotations.Unique(name="CustomerSubscription_Id_UNQ", members = {"customerSubscriptionId"})
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_CHILD,
        cssClassFa = "fa-key"
)
@DomainObject(autoCompleteAction = "findCustomerSubscriptionsByCustomerName", autoCompleteRepository = CustomerSubscriptionRepository.class,auditing= Auditing.ENABLED )

public class CustomerSubscription implements Comparable <CustomerSubscription>{

    //region > customerSubscriptionId (property)
    private Long customerSubscriptionId;

    @Column(allowsNull="false")
    @MemberOrder(sequence = "1")
    @Title(sequence = "2", prepend = "-")
    public Long getCustomerSubscriptionId() {
        return customerSubscriptionId;
    }

    public void setCustomerSubscriptionId(final Long customerSubscriptionId) {
        this.customerSubscriptionId = customerSubscriptionId;
    }
    // endregion

    //region > customer (property)
    private Customer customer;
    @Title(sequence = "1")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @MemberOrder(sequence = "2")
    @Persistent
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    //endregion

    //region > status (property)
    private SubscriptionStatus status;
    @MemberOrder(name="Subscription Status",sequence = "4")
    @Column(allowsNull = "false", defaultValue = "Registration")
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Status is set by using Actions"
    )
    public SubscriptionStatus getStatus() {
        return status;
    }
    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }
    // endregion

    //region > subscriptionTime (Property)
    private DateTime subscriptionTime;

    @MemberOrder(name="Subscription Status",sequence = "5")
    @Column(allowsNull = "true")
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Date is automatically set when using Actions"
    )
    public DateTime getSubscriptionTime() {
        return subscriptionTime;
    }

    public void setSubscriptionTime(DateTime subscriptionTime) {
        this.subscriptionTime = subscriptionTime;
    }
    //endregion

    //region > cancellationTime (Property)
    private DateTime cancellationTime;

    @MemberOrder(name="Subscription Status",sequence = "6")
    @Column(allowsNull = "true")
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Date is automatically set when using Actions"
    )
    public DateTime getCancellationTime() {
        return cancellationTime;
    }

    public void setCancellationTime(DateTime cancellationTime) {
        this.cancellationTime = cancellationTime;
    }
    //endregion

    //region > shoppingCarts (collection)
    @Persistent(mappedBy = "customerSubscription", dependentElement = "false")
    public SortedSet<ShoppingCart> shoppingCarts;

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "100")
    public SortedSet<ShoppingCart> getShoppingCarts() {
        return shoppingCarts;
    }

    public void setShoppingCarts(final SortedSet<ShoppingCart> shoppingCarts) {
        this.shoppingCarts = shoppingCarts;
    }
    //endregion

    //region > version (derived property)
    public Long getVersionSequence() {
        return (Long) JDOHelper.getVersion(this);
    }
    //endregion

    //region > customerPaymentMedia (collection)
    @Persistent(mappedBy = "customerSubscription", dependentElement = "false")
    private SortedSet<CustomerPaymentMedia> customerPaymentMedias = new TreeSet<CustomerPaymentMedia>();

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "200")
    public SortedSet<CustomerPaymentMedia> getCustomerPaymentMedias() {
        return customerPaymentMedias;
    }

    public void setCustomerPaymentMedias(final SortedSet<CustomerPaymentMedia> customerPaymentMedias) {
        this.customerPaymentMedias = customerPaymentMedias;
    }
    //endregion


    //region > activateSubscription (action)
    @MemberOrder(name="status",sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public CustomerSubscription activate() {
        setSubscriptionTime(clockService.nowAsDateTime());
        setStatus(SubscriptionStatus.ACTIVE);
        return this;
    }

    // disable action dependent on state of object
    public String disableActivate() {
        return status.equals(SubscriptionStatus.ACTIVE) ? "Subscription is already active" : null;
    }
    //endregion

    //region > suspendSubscription (action)
    @MemberOrder(name="status",sequence = "2")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public CustomerSubscription suspend() {
        setStatus(SubscriptionStatus.SUSPENDED);
        return this;
    }
    // disable action dependent on state of object
    public String disableSuspend() {
        return status.equals(SubscriptionStatus.CANCELLED) ? "Can't suspend cancelled subscriptions" : null;
    }
    //endregion

    //region > cancelSubscription (action)
    @MemberOrder(name="status",sequence = "3")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public CustomerSubscription cancel() {
        setCancellationTime(clockService.nowAsDateTime());
        setStatus(SubscriptionStatus.CANCELLED);
        return this;
    }

    // disable action dependent on state of object
    public String disableCancel() {
        return status.equals(SubscriptionStatus.CANCELLED) ? "Subscription is already cancelled" : null;
    }
    //endregion


    //region > compareTo

    @Override
    public int compareTo(final CustomerSubscription other) {
        return ObjectContracts.compare(this, other, "customerSubscriptionId");
    }

    //endregion

    //region > injected services

    @Inject
    private ClockService clockService;

    //endregion


}
