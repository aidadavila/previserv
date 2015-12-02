package domainapp.dom.simple.items;

import java.math.BigDecimal;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.joda.time.DateTime;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.value.Blob;

import domainapp.dom.simple.businesses.Business;

/**
 * Created by Administrator on 10/14/2015.
 */


@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "Item"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findItemsByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.simple.items.Item "
                        + "WHERE name.indexOf(:name) >= 0 "),
        @javax.jdo.annotations.Query(
                name = "findItemsPerBusinessByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.simple.items.Item "
                        + "WHERE (name.indexOf(:name) >= 0 && business == :parBusiness)"),
        @javax.jdo.annotations.Query(
                name = "findItemPerBusinessById", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom..simple.items.Item "
                        + "WHERE (itemId.indexOf(:itemId) >= 0 && business == :parBusiness)")
})
@javax.jdo.annotations.Unique(name="ItemCategory_Id_UNQ", members = {"business","itemId"})
@DomainObject(autoCompleteAction = "findItemsByName", autoCompleteRepository = Items.class,auditing= Auditing.ENABLED )

@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-cutlery"
)
public class Item implements Comparable<Item> {

    //region > business (property)
    private Business business;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Business always remains the same as the item's category's"
    )
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
    private String itemId;

    @Column(allowsNull = "false", length = 14)
    @MemberOrder(sequence = "2")
    @Title(sequence = "3", prepend = " [", append = "]")

    public String getItemId() {
        return itemId;
    }

    public void setItemId(final String itemId) {
        this.itemId = itemId;
    }
    // endregion

    //region > name (property)
    private String name;

    @Column(allowsNull = "false", length = 40)
    @Title(sequence = "2", prepend = "-")
    @MemberOrder(sequence = "3")

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
    // endregion

    //region > description (property)
    private String description;

    @Column(allowsNull = "true", length = 300)
    @MemberOrder(sequence = "4")
    @PropertyLayout(multiLine = 4)

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
    // endregion

    //region > itemCategory (property)
    public ItemCategory itemCategory;

    @MemberOrder(sequence = "5")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Persistent

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(final ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }
    //endregion

    //region > retailPrice (property)
    private BigDecimal retailPrice;

    @Column(allowsNull = "false", length = 10, scale = 2)
    @MemberOrder(sequence = "6")
    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }
    //endregion

    //region > status (property)
    public ItemStatus status;

    @Column(allowsNull = "false", defaultValue = "Active")
    @MemberOrder(name = "Status", sequence = "7")
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

    @MemberOrder(name = "Status", sequence = "8")
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

    @MemberOrder(name = "Status", sequence = "9")
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

    //region > itemImage (property)
    public Blob itemImage;

    @Column(allowsNull = "true")
    @Property(optionality = Optionality.OPTIONAL)
    @MemberOrder(name = "Image", sequence = "120")
    @Persistent(defaultFetchGroup = "false", columns = {
            @Column(name = "itemImage_name"),
            @Column(name = "itemImage_mimetype"),
            @Column(name = "itemImage_bytes",
                    jdbcType = "BLOB", sqlType = "VARBINARY")
    })

    public Blob getItemImage() {
        return itemImage;
    }

    public void setItemImage(final Blob itemImage) {
        this.itemImage = itemImage;
    }
    //endregion

    //region > itemBusinessLocations (collection)
    @Persistent(mappedBy = "item", dependentElement = "false")
    private SortedSet<ItemBusinessLocation> itemBusinessLocations = new TreeSet<ItemBusinessLocation>();

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "200")
    public SortedSet<ItemBusinessLocation> getItemBusinessLocations() {
        return itemBusinessLocations;
    }

    public void setItemBusinessLocations(final SortedSet<ItemBusinessLocation> itemBusinessLocations) {
        this.itemBusinessLocations = itemBusinessLocations;
    }

    //endregion

    //region > assignImage (action)
    @MemberOrder(name = "itemImage", sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public Item assignImage(
            final @ParameterLayout(named = "Image") Blob image
    ) {
        setItemImage(image);
        return this;
    }
    //endregion

    //region > activate (action)
    @MemberOrder(name = "status", sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public Item activate() {
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
    @MemberOrder(name = "status", sequence = "2")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public Item suspend() {
        setStatus(ItemStatus.SUSPENDED);
        return this;
    }

    // disable action dependent on state of object
    public String disableSuspend() {
        return status.equals(ItemStatus.CANCELLED) ? "Can't suspend cancelled items" : null;
    }
    //endregion

    //region > cancel (action)
    @MemberOrder(name = "status", sequence = "3")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public Item cancel() {
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
    public int compareTo(final Item other) {
        return ObjectContracts.compare(this, other, "business","itemId");
    }
    //endregion

    //region > injected services

    @Inject
    private ClockService clockService;

    @Inject
    DomainObjectContainer container;


    //endregion


}
