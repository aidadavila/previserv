package domainapp.dom.simple.customerorders;

import java.math.BigDecimal;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.JDOHelper;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.DateTime;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.util.ObjectContracts;

import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;
import org.isisaddons.wicket.gmap3.cpt.service.LocationLookupService;

import domainapp.dom.simple.businesses.BusinessLocation;
import domainapp.dom.simple.businesses.Deliverer;
import domainapp.dom.simple.customers.CustomerSubscription;

/**
 * Created by Administrator on 10/16/2015.
 */

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "CustomerOrder"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findCustomerOrderById", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.CustomerOrder "
                        + "WHERE customerOrderId.indexOf(:customerOrderId) >= 0 "),
        @javax.jdo.annotations.Query(
        name = "findShoppingCartOrderByLocation", language = "JDOQL",
        value = "SELECT "
                + "FROM domainapp.dom.simple.customerorders.CustomerOrder "
                + "WHERE shoppingCart == :parShoppingCart && businessLocation == :myBusinessLocation "),
        @javax.jdo.annotations.Query(
                name = "findRequestedCustomerOrders", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.simple.customerorders.CustomerOrder "
                        + "WHERE businessLocation == :myBusinessLocation && status == 'REQUESTED'"),
        @javax.jdo.annotations.Query(
        name = "findOrderedCustomerOrders", language = "JDOQL",
        value = "SELECT "
                + "FROM domainapp.dom.simple.customerorders.CustomerOrder "
                + "WHERE businessLocation == :myBusinessLocation && status == 'ORDERED'"),
        @javax.jdo.annotations.Query(
                name = "findAssignForDeliveryCustomerOrders", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.simple.customerorders.CustomerOrder "
                        + "WHERE "
                       // + "businessLocation == :myBusinessLocation && "
                        + " deliveredBy == :myDeliveredBy && status == 'ASSIGNED_FOR_DELIVERY'")
})
@javax.jdo.annotations.Unique(name="Customer_Order_Id_UNQ", members = {"businessLocation","customerOrderId"})
@DomainObject(auditing= Auditing.ENABLED )

@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_CHILD,
        cssClassFa = "fa-pencil-square-o"
)

public class CustomerOrder implements Comparable<CustomerOrder>, Locatable {

    //region > businessLocation (property)
    private BusinessLocation businessLocation;

    @MemberOrder(sequence = "2")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Title(sequence = "1")
    @Persistent
    public BusinessLocation getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(final BusinessLocation businessLocation) {
        this.businessLocation = businessLocation;
    }
    //endregion

    // region > customerOrderId (property)
    private Long customerOrderId;

    @Column(allowsNull="false")
    @MemberOrder(sequence = "1")
    @Title(sequence = "2",prepend = " [",append = "]")
    public Long getCustomerOrderId() {
        return customerOrderId;
    }

    public void setCustomerOrderId(final Long customerOrderId) {
        this.customerOrderId = customerOrderId;
    }
    //endregion

    //region > customerSubscription (property)
    private CustomerSubscription customerSubscription;

    @MemberOrder(sequence = "2")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Persistent
    public CustomerSubscription getCustomerSubscription() {
        return customerSubscription;
    }

    public void setCustomerSubscription(final CustomerSubscription customerSubscription) {
        this.customerSubscription = customerSubscription;
    }
    //endregion

    //region > shoppingCart (property)
    private ShoppingCart shoppingCart;

    @MemberOrder(sequence = "2")
    @Column(allowsNull = "true")
    @Property(optionality = Optionality.OPTIONAL)
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Persistent
    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(final ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
    //endregion

    //region > status (property)
    private CustomerOrderStatus status;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Status is set by using actions"
    )
    @Column(allowsNull = "false")
    @MemberOrder(name="Status",sequence = "3")
    public CustomerOrderStatus getStatus() {
        return status;
    }
    public void setStatus(CustomerOrderStatus status) {
        this.status = status;
    }
    // endregion

    //region > street (property)
    private String deliverToStreet;

    @Column(allowsNull="true", length = 200)
    @Property(optionality = Optionality.OPTIONAL, editing = Editing.DISABLED, editingDisabledReason = "Use action to set address")
    @MemberOrder(name = "Deliver To",sequence = "4")
    @PropertyLayout(multiLine = 2)
    public String getDeliverToStreet() {
        return deliverToStreet;
    }

    public void setDeliverToStreet(final String deliverToStreet) {
        this.deliverToStreet = deliverToStreet;
    }
    // endregion

    //region > deliverToStreet2 (property)
    private String deliverToStreet2;

    @Column(allowsNull="true", length = 200)
    @Property(optionality = Optionality.OPTIONAL, editing = Editing.DISABLED, editingDisabledReason = "Use action to set address")
    @MemberOrder(name = "Deliver To",sequence = "5")
    @PropertyLayout(multiLine = 2)
    public String getDeliverToStreet2() {
        return deliverToStreet2;
    }

    public void setDeliverToStreet2(final String deliverToStreet2) {
        this.deliverToStreet2 = deliverToStreet2;
    }
    //endregion

    // region > deliverToCity (poperty)
    private String deliverToCity;

    @Column(allowsNull="true", length = 40)
    @Property(optionality = Optionality.OPTIONAL, editing = Editing.DISABLED, editingDisabledReason = "Use action to set address")
    @MemberOrder(name = "Deliver To",sequence = "6")
    public String getDeliverToCity() {
        return deliverToCity;
    }

    public void setDeliverToCity(final String deliverToCity) {
        this.deliverToCity = deliverToCity;
    }
    //endregion

    //region > deliverToZipCode (property)
    private String deliverToZipCode;

    @Column(allowsNull="true", length = 40)
    @Property(optionality = Optionality.OPTIONAL, editing = Editing.DISABLED, editingDisabledReason = "Use action to set address")
    @MemberOrder(name = "Deliver To",sequence = "7")
    public String getDeliverToZipCode() {
        return deliverToZipCode;
    }

    public void setDeliverToZipCode(final String deliverToZipCode) {
        this.deliverToZipCode = deliverToZipCode;
    }
    //endregion

    //region > deliverToCountry (property)
    private String deliverToCountry;

    @Column(allowsNull="true", length = 40)
    @Property(optionality = Optionality.OPTIONAL, editing = Editing.DISABLED, editingDisabledReason = "Use action to set address")
    @MemberOrder(name = "Deliver To",sequence = "8")
    public String getDeliverToCountry() {
        return deliverToCountry;
    }

    public void setDeliverToCountry(final String deliverToCountry) {
        this.deliverToCountry = deliverToCountry;
    }
    //endregion

    //region > DeliverToGeoLocation (property)

    @Persistent
    private Location DeliverToGeoLocation;

    @Property(optionality = Optionality.OPTIONAL, hidden = Where.ALL_TABLES,editing = Editing.DISABLED, editingDisabledReason = "Location set automatically when setting address")
    @MemberOrder(name = "Deliver To",sequence = "10")
    public Location getLocation() {
        return DeliverToGeoLocation;
    }

    public void setDeliverToGeoLocation(final Location geoLocation) {
        this.DeliverToGeoLocation = geoLocation;
    }
    //endregion

    //region > totalTaxAmount (property)
    private BigDecimal totalTaxAmount;
    @Column(allowsNull="false", length=10, scale=2, defaultValue = "0")
    @MemberOrder(name="Totals", sequence = "11")
    public BigDecimal getTotalTaxAmount() {return totalTaxAmount;}
    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }
    //endregion

    //region > totalRetailAmount (property)
    private BigDecimal totalRetailAmount;
    @Column(allowsNull="false", length=10, scale=2, defaultValue = "0")
    @MemberOrder(name="Totals",sequence = "6")
    public BigDecimal getTotalRetailAmount() {return totalRetailAmount;}
    public void setTotalRetailAmount(BigDecimal totalRetailAmount) {
        this.totalRetailAmount = totalRetailAmount;
    }
    //endregion

    //region > totalAmountPaid (property)
    private BigDecimal totalAmountPaid;
    @Column(allowsNull="false", length=10, scale=2, defaultValue = "0")
    @MemberOrder(name="Total Amount Paid",sequence = "7")
    public BigDecimal getTotalAmountPaid() {return totalAmountPaid;}
    public void setTotalAmountPaid(BigDecimal totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public String validateTotalAmountPaid(BigDecimal totalAmountPaid) {
        System.out.println("Entre a validar");
        return totalAmountPaid.doubleValue() != this.getTotalAmountPaid().doubleValue() && this.getStatus().equals(CustomerOrderStatus.ORDERED)
                ? "Amount Paid cannot be modify, the Customer Order Status is" + this.getStatus()
                : null;
    }

    //endregion

    //region > orderedTime (Property)
    private DateTime orderTime;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Order time is automatically set when using actions"
    )
    @Column(allowsNull = "true")
    @MemberOrder(name="Status",sequence = "8")
    public DateTime getOrderTime() {
        return orderTime;
    }
    public void setOrderTime(DateTime orderTime) {
        this.orderTime = orderTime;
    }
    //endregion

    //region > assignedForDeliveryTime (Property)
    private DateTime assignedForDeliveryTime;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Cancellation time is automatically set when using actions"
    )
    @Column(allowsNull = "true")
    @MemberOrder(name="Status",sequence = "9")
    public DateTime getAssignedForDeliveryTime() {
        return assignedForDeliveryTime;
    }
    public void setAssignedForDeliveryTime(DateTime assignedForDeliveryTime) {
        this.assignedForDeliveryTime = assignedForDeliveryTime;
    }
    //endregion

    //region > deliveryStartedTime (Property)
    private DateTime deliveryStartedTime;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Cancellation time is automatically set when using actions"
    )
    @Column(allowsNull = "true")
    @MemberOrder(name="Status",sequence = "10")
    public DateTime getDeliveryStartedTime() {
        return deliveryStartedTime;
    }
    public void setDeliveryStartedTime(DateTime deliveryStartedTime) {
        this.deliveryStartedTime = deliveryStartedTime;
    }
    //endregion

    //region > deliveryTime (Property)
    private DateTime deliveryTime;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Cancellation time is automatically set when using actions"
    )
    @Column(allowsNull = "true")
    @MemberOrder(name="Status",sequence = "11")
    public DateTime getDeliveryTime() {
        return deliveryTime;
    }
    public void setDeliveryTime(DateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
    //endregion

    //region > cancellationTime (Property)
    private DateTime cancellationTime;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Cancellation time is automatically set when using actions"
    )
    @Column(allowsNull = "true")
    @MemberOrder(name="Status",sequence = "12")
    public DateTime getCancellationTime() {
        return cancellationTime;
    }
    public void setCancellationTime(DateTime cancellationTime) {
        this.cancellationTime = cancellationTime;
    }
    //endregion

    //region > creationTime (Property)
    private DateTime creationTime;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Creation time is automatically set when using actions"
    )
    @Column(allowsNull = "false")
    @MemberOrder(name="Status",sequence = "13")
    @Persistent
    public DateTime getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }
    //endregion

    //region > deliverToLocation (action)
    @Action(semantics = SemanticsOf.IDEMPOTENT)
    // Associate this action to appear near the "location" field:
    @MemberOrder(name = "Location",sequence = "1")
    @ActionLayout(named = "Deliver To Address",position = ActionLayout.Position.PANEL)
    public CustomerOrder deliverToLocation(
            final @ParameterLayout(named="Street", multiLine = 2) String street,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named="Street 2", multiLine = 2) String street2,
            final @ParameterLayout(named="City") String city,
            final @Parameter(maxLength = 5,regexPattern = "\\b[0-9][0-9]{4}\\b") @ParameterLayout(named="Zip Code") String zipCode,
            final @ParameterLayout(named="Country")String country
    )
    {
        final Location geoLocation = this.locationLookupService.lookup(street + " " + street2 + "," + city + "," + "," + zipCode + "," + country );
        setDeliverToGeoLocation(geoLocation);
        setDeliverToStreet(street);
        setDeliverToStreet2(street2);
        setDeliverToCity(city);
        setDeliverToZipCode(zipCode);
        setDeliverToCountry(country);
        setCreationTime(clockService.nowAsDateTime());
        return this;
    }

    // provide a default value for argument #0
    public String default0DeliverToLocation() {
        return getDeliverToStreet();
    }

    // provide a default value for argument #1
    public String default1DeliverToLocation() {
        return getDeliverToStreet2();
    }

    // provide a default value for argument #2
    public String default2DeliverToLocation() {
        return getDeliverToCity();
    }

    // provide a default value for argument #3
    public String default3DeliverToLocation() {
        return getDeliverToZipCode();
    }

    // provide a default value for argument #4
    public String default4DeliverToLocation() {
        return getDeliverToCountry();
    }
    //endregion

    //region > deliveredBy (property)
    private Deliverer deliveredBy;

    @MemberOrder(name="Status",sequence = "9")
    @Column(allowsNull = "true")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Persistent
    public Deliverer getDeliveredBy() {
        return deliveredBy;
    }

    public void setDeliveredBy(final Deliverer deliveredBy) {
        this.deliveredBy = deliveredBy;
    }
    //endregion

    //region > customerOrderItems (collection)
    @Persistent(mappedBy = "customerOrder", dependentElement = "false")
    private SortedSet<CustomerOrderItem> customerOrderItems = new TreeSet<CustomerOrderItem>();

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "100")
    public SortedSet<CustomerOrderItem> getCustomerOrderItems() {
        return customerOrderItems;
    }

    public void setCustomerOrderItems(final SortedSet<CustomerOrderItem> customerOrderItems) {
        this.customerOrderItems = customerOrderItems;
    }
    //endregion

    //region > customerOrderPayments (collection)
    @Persistent(mappedBy = "customerOrder", dependentElement = "false")
    private SortedSet<CustomerOrderPayment> customerOrderPayments = new TreeSet<CustomerOrderPayment>();

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "110")
    public SortedSet<CustomerOrderPayment> getCustomerOrderPayments() {
        return customerOrderPayments;
    }

    public void setCustomerOrderPayments(final SortedSet<CustomerOrderPayment> customerOrderPayments) {
        this.customerOrderPayments = customerOrderPayments;
    }
    //endregion

    //region > accept (action)
    @MemberOrder(name="status",sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public CustomerOrder accept() {
        setStatus(CustomerOrderStatus.PREORDERED);
        return this;
    }

    // disable action dependent on state of object
    public String disableAccept() {
               return status.equals(CustomerOrderStatus.PREORDERED) ? "Customer order is already PreOrdered" : null;
    }

    // hide action dependent on state of object
    public boolean hideAccept() {
        return status.equals(CustomerOrderStatus.REQUESTED) ? false : true;
    }
    //endregion


    //region > assignForDelivery (action)
    @MemberOrder(name="status",sequence = "2")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public CustomerOrder assignForDelivery(
            final @ParameterLayout(named = "Delivered By") Deliverer deliverer
    ) {
        setDeliveredBy(deliverer);
        setAssignedForDeliveryTime(clockService.nowAsDateTime());
        setStatus(CustomerOrderStatus.ASSIGNED_FOR_DELIVERY);
        return this;
    }


    // disable action dependent on state of object
    public String disableAssignForDelivery(Deliverer deliverer) {
        return status.equals(CustomerOrderStatus.ASSIGNED_FOR_DELIVERY) ? "Customer order is already assigned for delivery" : null;
    }

    // hide action dependent on state of object
    public boolean hideAssignForDelivery(Deliverer deliverer) {
        return status.equals(CustomerOrderStatus.ORDERED) ? false : true;
    }
    //endregion


    //region > deliveryStarted (action)
    @MemberOrder(name="status",sequence = "3")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public CustomerOrder deliveryStarted() {
        setDeliveryStartedTime(clockService.nowAsDateTime());
        setStatus(CustomerOrderStatus.IN_TRANSIT);
        return this;
    }

    // disable action dependent on state of object
    public String disableDeliveryStarted() {
        return status.equals(CustomerOrderStatus.IN_TRANSIT) ? "Customer order is already being delivered" : null;
    }

    // hide action dependent on state of object
    public boolean hideDeliveryStarted() {
        return status.equals(CustomerOrderStatus.ASSIGNED_FOR_DELIVERY) ? false : true;
    }
    //endregion



    //region > deliveryCompleted (action)
    @MemberOrder(name="status",sequence = "4")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public CustomerOrder deliveryCompleted() {
        setDeliveryTime(clockService.nowAsDateTime());
        setStatus(CustomerOrderStatus.DELIVERED);
        return this;
    }

    // disable action dependent on state of object
    public String disableDeliveryCompleted() {
        return status.equals(CustomerOrderStatus.DELIVERED) ? "Customer order is already delivered" : null;
    }

    // hide action dependent on state of object
    public boolean hideDeliveryCompleted() {
        return status.equals(CustomerOrderStatus.IN_TRANSIT) ? false : true;
    }
    //endregion

    //region > cancelOrder (action)
    @MemberOrder(name="status",sequence = "5")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public CustomerOrder cancelOrder() {
        setCancellationTime(clockService.nowAsDateTime());
        setStatus(CustomerOrderStatus.CANCELED);
        return this;
    }

    // disable action dependent on state of object
    public String disableCancelOrder() {
        return status.equals(CustomerOrderStatus.CANCELED) ? "Customer order is already cancelled" : null;
    }

    // hide action dependent on state of object
    public boolean hideCancelOrder() {
        return status.equals(CustomerOrderStatus.CANCELED) ? true : false;
    }
    //endregion


    //region > rejectDelivery (action)
    @MemberOrder(name="status",sequence = "5")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public CustomerOrder rejectDelivery() {
        setDeliveredBy(null);
        setDeliveryStartedTime(null);
        setStatus(CustomerOrderStatus.ORDERED);
        return this;
    }

    // disable action dependent on state of object
    public String disableRejectDelivery() {
        return status.equals(CustomerOrderStatus.ASSIGNED_FOR_DELIVERY) ? null : "Customer order is already in ASSIGNED_FOR_DELIVERY";
    }

    // hide action dependent on state of object
    public boolean hideRejectDelivery() {
        return status.equals(CustomerOrderStatus.ASSIGNED_FOR_DELIVERY) ? false : true;
    }
    //endregion


    //region > reject (action)
    @MemberOrder(name="status",sequence = "5")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public CustomerOrder reject() {
        setStatus(CustomerOrderStatus.CANCELED);
        return this;
    }

    // disable action dependent on state of object
    public String disableReject() {
        return status.equals(CustomerOrderStatus.CANCELED) ? "Customer order is already Canceled" : null;
    }

    // hide action dependent on state of object
    public boolean hideReject() {
        return status.equals(CustomerOrderStatus.REQUESTED) ? false : true;
    }
    //endregion


    //region > order (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            contributed = Contributed.AS_NEITHER,
            hidden = Where.EVERYWHERE
    )
    public CustomerOrder order() {
        if ((this.getTotalRetailAmount().doubleValue()> 0 && this.getTotalAmountPaid().doubleValue()>0)  && this.getTotalRetailAmount().doubleValue() == this.getTotalAmountPaid().doubleValue()) {
            setStatus(CustomerOrderStatus.ORDERED);
            //System.out.println("ORDERED");
        }
        return this;
    }
    // endregion


    //region > version (derived property)
    public Long getVersionSequence() {
        return (Long) JDOHelper.getVersion(this);
    }
    //endregion

    //region > compareTo
    @Override
    public int compareTo(final CustomerOrder other) {
        return ObjectContracts.compare(this, other, "businessLocation","customerOrderId");
    }
    //endregion

    //region addToTotalRetailAmount
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            contributed = Contributed.AS_BOTH,
            hidden = Where.EVERYWHERE
    )
    public void addToTotalRetailAmount(double amount){
          System.out.println();
          this.setTotalRetailAmount(BigDecimal.valueOf(this.getTotalRetailAmount().doubleValue() + amount));
    }
    //endregion


    //region addToTotalAmountPaid
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            contributed = Contributed.AS_BOTH,
            hidden = Where.EVERYWHERE
    )
    public void addToTotalAmountPaid(double amount){
        System.out.println("AddTotalAmountPaid");
        this.setTotalAmountPaid(BigDecimal.valueOf(this.getTotalAmountPaid().doubleValue() + amount));
    }
    //endregion

   //region > injected services

    @Inject
    LocationLookupService locationLookupService;

    @Inject
    private ClockService clockService;

    //endregion



}
