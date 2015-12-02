package domainapp.dom.simple.items;

import domainapp.dom.simple.businesses.Business;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.value.Blob;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Administrator on 10/15/2015.
 */

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "ItemCategory"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findItemCategoriesByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.ItemCategory "
                        + "WHERE name.indexOf(:name) >= 0  "),
        @javax.jdo.annotations.Query(
                name = "findItemCategoriesPerBusinessByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.ItemCategory "
                        + "WHERE (name.indexOf(:name) >= 0 && business == :parBusiness) "),
        @javax.jdo.annotations.Query(
                name = "findItemCategoryPerBusinessById", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.ItemCategory "
                        + "WHERE (itemCategoryId.indexOf(:itemCategoryId) >= 0 && business == :parBusiness) ")
})
@javax.jdo.annotations.Unique(name="Item_Id_UNQ", members = {"business","itemCategoryId"})
@DomainObject(autoCompleteAction = "findItemCategoriesByName", autoCompleteRepository = ItemCategoryRepository.class,auditing= Auditing.ENABLED )
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-th-list"
)
public class ItemCategory implements Comparable<ItemCategory>{


    //region > business (property)
    private Business business;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Business always remains the same as the business that created the item category"
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
    private String itemCategoryId;

    @Column(allowsNull="false", length = 14)
    @MemberOrder(sequence = "2")
    public String getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(final String itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }
    // endregion

    //region > name (property)
    private String name;

    @Column(allowsNull="false", length = 40)
    @Title(sequence="2",prepend = "-")
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

    @Column(allowsNull="true", length = 300)
    @MemberOrder(sequence = "4")
    @PropertyLayout(multiLine = 4)

    public String getDescription() {
        return description;
    }
    public void setDescription(final String description) {
        this.description = description;
    }
    // endregion

    //region > productOffering (property)
    private ProductOffering productOffering;

    @MemberOrder(sequence = "5")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public ProductOffering getProductOffering() {
        return productOffering;
    }

    public void setProductOffering(final ProductOffering productOffering) {
        this.productOffering = productOffering;
    }

    //endregion

    //region > status (property)
    public ItemStatus status;

    @Column(allowsNull="false",defaultValue = "Active")
    @MemberOrder(name="Status",sequence = "4")
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Status is set by using Actions"
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

    @MemberOrder(name="Status",sequence = "5")
    @Column(allowsNull = "true")
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Date is automatically set when using Actions"
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

    @MemberOrder(name="Status",sequence = "6")
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

    //region > itemCategoryImage (property)
    public Blob itemCategoryImage;

    @Column(allowsNull="true")
    @Property(optionality = Optionality.OPTIONAL)
    @MemberOrder(name = "Image",sequence = "120")
    @Persistent(defaultFetchGroup="false", columns = {
            @Column(name = "itemCategoryImage_name"),
            @Column(name = "itemCategoryImage_mimetype"),
            @Column(name = "itemCategoryImage_bytes",
                    jdbcType = "BLOB", sqlType = "VARBINARY")
    })

    public Blob getItemCategoryImage() {
        return itemCategoryImage;
    }

    public void setItemCategoryImage(final Blob itemCategoryImage) {
        this.itemCategoryImage = itemCategoryImage;
    }
    //endregion

    //region > items (collection)
    @Persistent(mappedBy = "itemCategory", dependentElement = "false")
    private SortedSet<Item> items = new TreeSet<Item>();

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "100")
    public SortedSet<Item> getItems() {
        return items;
    }

    public void setItems(final SortedSet<Item> items) {
        this.items = items;
    }

    //endregion

    //region > assignImage (action)
    @MemberOrder(name="itemCategoryImage",sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public ItemCategory assignImage(
            final @ParameterLayout(named = "Image") Blob ItemCategoryImage
    ) {
        setItemCategoryImage(ItemCategoryImage);
        return this;
    }
    //endregion

    //region > activate (action)
    @MemberOrder(name="status",sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public ItemCategory activate() {
        setActivationTime(clockService.nowAsDateTime());
        setStatus(ItemStatus.ACTIVE);
        return this;
    }

    // disable action dependent on state of object
    public String disableActivate() {
        return status.equals(ItemStatus.ACTIVE) ? "Category is already active" : null;
    }
    //endregion

    //region > suspend (action)
    @MemberOrder(name="status",sequence = "2")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public ItemCategory suspend() {
        setStatus(ItemStatus.SUSPENDED);
        return this;
    }
    // disable action dependent on state of object
    public String disableSuspend() {
        return status.equals(ItemStatus.CANCELLED) ? "Can't suspend cancelled categories" : null;
    }
    //endregion

    //region > cancel (action)
    @MemberOrder(name="status",sequence = "3")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public ItemCategory cancel() {
        setCancellationTime(clockService.nowAsDateTime());
        setStatus(ItemStatus.CANCELLED);
        return this;
    }

    // disable action dependent on state of object
    public String disableCancel() {
        return status.equals(ItemStatus.CANCELLED) ? "Category is already cancelled" : null;
    }
    //endregion


    //region > compareTo
    @Override
    public int compareTo(final ItemCategory other) {
        return ObjectContracts.compare(this, other, "business", "itemCategoryId");
    }
    //endregion

    //region > injected services

    @Inject
    private ClockService clockService;
    //endregion



}
