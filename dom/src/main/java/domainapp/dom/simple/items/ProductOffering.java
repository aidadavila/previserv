package domainapp.dom.simple.items;

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
 * Created by Administrator on 10/22/2015.
 */
@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "ProductOffering"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findProductOfferingsByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.ProductOffering "
                        + "WHERE name.indexOf(:name) >= 0 "),
        @javax.jdo.annotations.Query(
                name = "findProductOfferingById", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.ProductOffering "
                        + "WHERE productOfferingId.indexOf(:productOfferingId) >= 0 ")
})
@javax.jdo.annotations.Unique(name="Product_Offering_Id_UNQ", members = {"productOfferingId"})
@DomainObject(autoCompleteAction = "findProductOfferingsByName", autoCompleteRepository = ProductOfferingRepository.class,auditing= Auditing.ENABLED )
@DomainObjectLayout(
        named = "Cuisine Type",
        bookmarking = BookmarkPolicy.NEVER,
        cssClassFa = "fa-th-list"
)

public class ProductOffering implements Comparable<ProductOffering>{


    //region > ID (property)
    private String productOfferingId;

    @Column(allowsNull="false", length = 14)
    @MemberOrder(sequence = "2")
    public String getProductOfferingId() {
        return productOfferingId;
    }

    public void setProductOfferingId(final String productOfferingId) {
        this.productOfferingId = productOfferingId;
    }
    // endregion

    //region > name (property)
    private String name;

    @Column(allowsNull="false", length = 40)
    @Title(sequence="2")
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

    //region > productOfferingImage (property)
    public Blob productOfferingImage;

    @Column(allowsNull="true")
    @Property(optionality = Optionality.OPTIONAL)
    @MemberOrder(name = "Image",sequence = "120")
    @Persistent(defaultFetchGroup="false", columns = {
            @Column(name = "productOfferingImage_name"),
            @Column(name = "productOfferingImage_mimetype"),
            @Column(name = "productOfferingImage_bytes",
                    jdbcType = "BLOB", sqlType = "VARBINARY")
    })

    public Blob getProductOfferingImage() {
        return productOfferingImage;
    }

    public void setProductOfferingImage(final Blob productOfferingImage) {
        this.productOfferingImage = productOfferingImage;
    }
    //endregion

    //region > itemCategories (collection)
    @Persistent(mappedBy = "productOffering", dependentElement = "false")
    private SortedSet<ItemCategory> itemCategories = new TreeSet<ItemCategory>();

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "100")
    public SortedSet<ItemCategory> getItemCategories() {
        return itemCategories;
    }

    public void setItemCategories(final SortedSet<ItemCategory> itemCategories) {
        this.itemCategories = itemCategories;
    }

    //endregion

    //region > assignImage (action)
    @MemberOrder(name="productOfferingImage",sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public ProductOffering assignImage(
            final @ParameterLayout(named = "Image") Blob ItemCategoryImage
    ) {
        setProductOfferingImage(ItemCategoryImage);
        return this;
    }
    //endregion

    //region > activate (action)
    @MemberOrder(name="status",sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public ProductOffering activate() {
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
    public ProductOffering suspend() {
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
    public ProductOffering cancel() {
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
    public int compareTo(final ProductOffering other) {
        return ObjectContracts.compare(this, other, "productOfferingId");
    }
    //endregion

    //region > injected services

    @Inject
    private ClockService clockService;
    //endregion


}
