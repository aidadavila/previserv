package domainapp.dom.simple.customerorders;

/**
 * Created by Administrator on 10/15/2015.
 */

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
import org.apache.isis.applib.annotation.Contributed;

import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;
import org.isisaddons.wicket.gmap3.cpt.service.LocationLookupService;

import domainapp.dom.simple.customers.CustomerSubscription;

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "ShoppingCart"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findShoppingCartById", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.simple.customerorders.ShoppingCart "
                        + "WHERE shoppingCartId.indexOf(:shoppingCartId) >= 0 ")

})

@javax.jdo.annotations.Unique(name="Shopping_Cart_Id_UNQ", members = {"shoppingCartId"})
@DomainObject(auditing= Auditing.ENABLED )

@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-shopping-cart"
)

public class ShoppingCart implements Comparable<ShoppingCart>, Locatable{

    // region > shoppingCartId
    private Long shoppingCartId;

    @Column(allowsNull="false")
    @MemberOrder(sequence = "1")
    @Title(sequence = "1")

    public Long getShoppingCartId() {
            return shoppingCartId;
    }

    public void setShoppingCartId(final Long shoppingCartId) {
            this.shoppingCartId = shoppingCartId;
    }
    //endregion

    //region > customerSubscription
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

    //region > status (property)
    private ShoppingCartStatus status;

   @Property(
        editing = Editing.DISABLED,
        editingDisabledReason = "Status is set by using actions"
   )
    @Column(allowsNull = "false")
    @MemberOrder(name="Status",sequence = "3")
    public ShoppingCartStatus getStatus() {
            return status;
    }
    public void setStatus(ShoppingCartStatus status) {
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

    //region > deliverToCity (property)
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

    //region > Geo Location (property)
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

    //region > totalRetailAmount (property)
    private BigDecimal totalRetailAmount;

    @Column(allowsNull="false", length=10, scale=2, defaultValue = "0")
    @MemberOrder(name="Totals", sequence = "11")
    public BigDecimal getTotalRetailAmount() {return totalRetailAmount;}
    public void setTotalRetailAmount(BigDecimal totalRetailAmount) {
    this.totalRetailAmount = totalRetailAmount;
}
    //endregion

    //region > totalTaxAmount (property)
    private BigDecimal totalTaxAmount;

    @Column(allowsNull="false", length=10, scale=2, defaultValue = "0")
    @MemberOrder(name="Totals",sequence = "6")
    public BigDecimal getTotalTaxAmount() {return totalTaxAmount;}
    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }
    //endregion

    //region > orderedTime (Property)
        private DateTime orderTime;

        @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Order time is automatically set when using actions"
        )
        @Column(allowsNull = "true")
        @MemberOrder(name="Status",sequence = "7")
        public DateTime getOrderTime() {
                return orderTime;
        }
        public void setOrderTime(DateTime orderTime) {
                this.orderTime = orderTime;
        }
        //endregion

    //region > cancellationTime (Property)
    private DateTime cancellationTime;

    @Property(
        editing = Editing.DISABLED,
        editingDisabledReason = "Cancellation time is automatically set when using actions"
    )
    @Column(allowsNull = "true")
    @MemberOrder(name="Status",sequence = "8")
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
    @MemberOrder(name="Status",sequence = "9")
    @Persistent
    public DateTime getCreationTime() {
            return creationTime;
    }
    public void setCreationTime(DateTime creationTime) {
            this.creationTime = creationTime;
    }
    //endregion

    //region > shoppingCartItems (collection)
    @Persistent(mappedBy = "shoppingCart", dependentElement = "false")
    private SortedSet<ShoppingCartItem> shoppingCartItems = new TreeSet<ShoppingCartItem>();

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "100")
    public SortedSet<ShoppingCartItem> getShoppingCartItems() {
        return shoppingCartItems;
    }

    public void setShoppingCartItems(final SortedSet<ShoppingCartItem> shoppingCartItems) {
        this.shoppingCartItems = shoppingCartItems;
    }
    //endregion

    //region > customerOrders (collection)
    @Persistent(mappedBy = "shoppingCart", dependentElement = "false")
    private SortedSet<CustomerOrder> customerOrders = new TreeSet<CustomerOrder>();

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "200")
    public SortedSet<CustomerOrder> getCustomerOrders() {
        return customerOrders;
    }

    public void setCustomerOrders(final SortedSet<CustomerOrder> customerOrders) {
        this.customerOrders = customerOrders;
    }
    //endregion

    //region > version (derived property)
    public Long getVersionSequence() {
        return (Long) JDOHelper.getVersion(this);
    }
    //endregion


    // region > deliverToLocation (action)
    @Action(semantics = SemanticsOf.IDEMPOTENT)
    // Associate this action to appear near the "location" field:
    @MemberOrder(name = "Location",sequence = "1")
    @ActionLayout(named = "Deliver To Address",position = ActionLayout.Position.PANEL)
    public ShoppingCart deliverToLocation(
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

    //region > order (action)
    @MemberOrder(name="status",sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public ShoppingCart order() {
        setOrderTime(clockService.nowAsDateTime());
        setStatus(ShoppingCartStatus.ORDERED);
        shoppingCartRepository.createCustomerOrdersFromShoppingCart(this);
        return this;
    }

    // disable action dependent on state of object
    public String disableOrder() {
        return status.equals(ShoppingCartStatus.ORDERED) ? "Shopping Cart is already ordered" : null;
    }
    //endregion

    //region > cancel (action)
    @MemberOrder(name="status",sequence = "2")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public ShoppingCart cancel() {
        setCancellationTime(clockService.nowAsDateTime());
        setStatus(ShoppingCartStatus.CANCELLED);
        return this;
    }

    // disable action dependent on state of object
    public String disableCancel() {
        return status.equals(ShoppingCartStatus.CANCELLED) ? "Shopping Cart already cancelled" : null;
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


    //region > compareTo
    @Override
    public int compareTo(final ShoppingCart other) {
            return ObjectContracts.compare(this, other, "shoppingCartId");
    }
    //endregion

    //region > injected services

    @Inject
    LocationLookupService locationLookupService;

    @Inject
    private ClockService clockService;

    @Inject
    private ShoppingCartRepository shoppingCartRepository;

    //endregion

}
