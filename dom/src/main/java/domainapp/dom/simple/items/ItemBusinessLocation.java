package domainapp.dom.simple.items;

import domainapp.dom.simple.businesses.BusinessLocation;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.util.ObjectContracts;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;
import java.math.BigDecimal;

/**
 * Created by Administrator on 10/22/2015.
 */

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "ItemBusinessLocation"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Unique(name="Item+Business_Location_Id_UNQ", members = {"item","businessLocation"})
@DomainObject(auditing= Auditing.ENABLED )

@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-cutlery"
)

public class ItemBusinessLocation implements Comparable<ItemBusinessLocation>{

    //region > item (property)
    private Item item;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Item cannot be changed"
    )
    @MemberOrder(sequence = "1")
    @Title(sequence = "1")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Persistent
    public Item getItem() {
        return item;
    }

    public void setItem(final Item item) {
        this.item = item;
    }
    //endregion

    //region > businessLocation (property)
    private BusinessLocation businessLocation;

    @MemberOrder(sequence = "1")
    @Title(sequence="2", prepend = " in ")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public BusinessLocation getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(final BusinessLocation businessLocation) {
        this.businessLocation = businessLocation;
    }
    //endregion

    //region > getItemName
    @MemberOrder(name="Status",sequence = "3")
    public String getItemName() {return this.getItem().getName();}
    //endregion

    //region > retailPrice (property)
    private BigDecimal retailPrice;

    @Column(allowsNull="false", length=10, scale=2)
    @MemberOrder(sequence = "4")
    public BigDecimal getRetailPrice() {return retailPrice;}
    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }
    //endregion

    //region > status (property)
    public ItemStatus status;

    @Column(allowsNull="false",defaultValue = "Active")
    @MemberOrder(name="Status",sequence = "7")
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Status is set by using actions"
    )
    public ItemStatus getStatus() {
        return status;
    }
    public void setStatus(final ItemStatus status) {
        this.status = status;
    }
    //endregion

    //region > activationTime (Property)
    private DateTime activationTime;

    @MemberOrder(name="Status",sequence = "8")
    @Column(allowsNull = "true")
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Date is automatically set when using actions"
    )
    public DateTime getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(DateTime activationTime) {
        this.activationTime = activationTime;
    }
    //endregion

    //region > cancellationTime (Property)
    private DateTime cancellationTime;

    @MemberOrder(name="Status",sequence = "9")
    @Column(allowsNull = "true")
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Date is automatically set when using actions"
    )
    public DateTime getCancellationTime() {
        return cancellationTime;
    }

    public void setCancellationTime(DateTime cancellationTime) {
        this.cancellationTime = cancellationTime;
    }
    //endregion

    //region > activate (action)
    @MemberOrder(name="status",sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public ItemBusinessLocation activate() {
        setActivationTime(clockService.nowAsDateTime());
        setStatus(ItemStatus.ACTIVE);
        return this;
    }

    // disable action dependent on state of object
    public String disableActivate() {
        return status.equals(ItemStatus.ACTIVE) ? "Item is already active" : null;
    }
    //endregion

    //region > suspend (action)
    @MemberOrder(name="status",sequence = "2")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public ItemBusinessLocation suspend() {
        setStatus(ItemStatus.SUSPENDED);
        return this;
    }
    // disable action dependent on state of object
    public String disableSuspend() {
        return status.equals(ItemStatus.CANCELLED) ? "Can't suspend cancelled items" : null;
    }
    //endregion

    //region > cancel (action)
    @MemberOrder(name="status",sequence = "3")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public ItemBusinessLocation cancel() {
        setCancellationTime(clockService.nowAsDateTime());
        setStatus(ItemStatus.CANCELLED);
        return this;
    }

    // disable action dependent on state of object
    public String disableCancel() {
        return status.equals(ItemStatus.CANCELLED) ? "Item is already cancelled" : null;
    }
    //endregion

    //region > compareTo
    @Override
    public int compareTo(final ItemBusinessLocation other) {
        return ObjectContracts.compare(this, other, "item","businessLocation");
    }
    //endregion

    //region > injected services

    @Inject
    private ClockService clockService;
    //endregion

}
