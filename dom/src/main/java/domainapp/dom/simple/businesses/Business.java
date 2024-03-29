package domainapp.dom.simple.businesses;

/**
 * Created by Administrator on 9/23/2015.
 */

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
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.value.Blob;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.WithApplicationTenancy;
import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;
import org.isisaddons.wicket.gmap3.cpt.service.LocationLookupService;

import domainapp.dom.simple.items.ItemCategory;

@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "simple",
        table = "Business"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findAllBusinesses", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.Business "),
        @javax.jdo.annotations.Query(
                name = "findBusinessesByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.Business "
                        + "WHERE name.indexOf(:name) >= 0 "),
        @javax.jdo.annotations.Query(
                name = "findBusinessById", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.Business "
                        + "WHERE businessId.indexOf(:businessId) >= 0 ")
})
@javax.jdo.annotations.Unique(name="Business_Id_UNQ", members = {"businessId"})
@DomainObject(autoCompleteAction = "findBusinessesByName", autoCompleteRepository = BusinessRepository.class,auditing=Auditing.ENABLED )

@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-building"
)

public class Business implements Comparable<Business>  , Locatable, WithApplicationTenancy {

/*
    //region > identification
    public TranslatableString title() {
        return TranslatableString.tr("Business: {name}", "name", getName());
    }
    //endregion
*/
    //region > ID (property)
    private String businessId;

    @Column(allowsNull="false", length = 10)
    @MemberOrder(sequence = "1")

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(final String businessId) {
        this.businessId = businessId;
    }
    // endregion

    //region > name (property)
    private String name;

    @Column(allowsNull="false", length = 40)
    @Title(sequence="1")
    @MemberOrder(sequence = "2")

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
    // endregion

    //region > status (property)
    private BusinessStatus status;

    @Column(allowsNull = "false")
    @MemberOrder(name="Status",sequence = "3")
    public BusinessStatus getStatus() {
        return status;
    }
    public void setStatus(BusinessStatus status) {
        this.status = status;
    }
    // endregion

    //region > createdTime (Property)
    private DateTime creationTime;

    @Column(allowsNull = "false")
    @MemberOrder(name="Status",sequence = "4")
    @Persistent
    public DateTime getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }
    //endregion

    //region > street (property)
    private String street;

    @Column(allowsNull="true", length = 200)
    @Property(optionality = Optionality.OPTIONAL, editing = Editing.DISABLED, editingDisabledReason = "Use action to set address")
    @MemberOrder(name = "Location",sequence = "100")
    @PropertyLayout(multiLine = 2)
    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }
    // endregion

    //region > street2 (property)
    private String street2;

    @Column(allowsNull="true", length = 200)
    @Property(optionality = Optionality.OPTIONAL, editing = Editing.DISABLED, editingDisabledReason = "Use action to set address")
    @MemberOrder(name = "Location",sequence = "101")
    @PropertyLayout(multiLine = 2)
    public String getStreet2() {
        return street2;
    }

    public void setStreet2(final String street2) {
        this.street2 = street2;
    }
    //endregion

    //region > city (property)
    private String city;

    @Column(allowsNull="true", length = 40)
    @Property(optionality = Optionality.OPTIONAL, editing = Editing.DISABLED, editingDisabledReason = "Use action to set address")
    @MemberOrder(name = "Location",sequence = "103")
    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }
    //endregion

    //region > zipCode (property)
    private String zipCode;

    @Column(allowsNull="true", length = 40)
    @Property(optionality = Optionality.OPTIONAL, editing = Editing.DISABLED, editingDisabledReason = "Use action to set address")
    @MemberOrder(name = "Location",sequence = "104")
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }
    //endregion

    //region > country (property)
    private String country;

    @Column(allowsNull="true", length = 40)
    @Property(optionality = Optionality.OPTIONAL, editing = Editing.DISABLED, editingDisabledReason = "Use action to set address")
    @MemberOrder(name = "Location",sequence = "105")
    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }
    //endregion

    //region > image (property)
    public Blob businessImage;

    @Column(allowsNull="true")
    @Property(optionality = Optionality.OPTIONAL)
    @MemberOrder(name = "Image",sequence = "120")
    @Persistent(defaultFetchGroup="false", columns = {
    @Column(name = "businessImage_name"),
    @Column(name = "businessImage_mimetype"),
    @Column(name = "businessImage_bytes",
             jdbcType = "BLOB", sqlType = "VARBINARY")
             })

    public Blob getBusinessImage() {
        return businessImage;
    }

    public void setBusinessImage(final Blob businessImage) {
        this.businessImage = businessImage;
    }
    //endregion

    //region > Geo Location (property)
    @Persistent
    private Location geoLocation;

    @Property(optionality = Optionality.OPTIONAL, hidden = Where.ALL_TABLES,editing = Editing.DISABLED, editingDisabledReason = "Location set automatically when setting address")
    @MemberOrder(name = "Location",sequence = "106")
    public Location getLocation() {
        return geoLocation;
    }

    public void setGeoLocation(final Location geoLocation) {
        this.geoLocation = geoLocation;
    }
    //endregion

    //region tenancy (property)
    private ApplicationTenancy applicationTenancy;

    @MemberOrder(sequence = "107")
    @Column(allowsNull = "true")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Persistent

    @Override
    public ApplicationTenancy getApplicationTenancy() {
        return applicationTenancy;
    }

    public void setApplicationTenancy(ApplicationTenancy applicationTenancy) {
        this.applicationTenancy = applicationTenancy;
    }
    //endregion

    //region > businessSubscription (collection)
    @Persistent(mappedBy = "business", dependentElement = "false")
    private SortedSet<BusinessSubscription> businessSubscriptions = new TreeSet<BusinessSubscription>();

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "100")
    public SortedSet<BusinessSubscription> getBusinessSubscriptions() {
        return businessSubscriptions;
    }

    public void setBusinessSubscriptions(final SortedSet<BusinessSubscription> businessSubscriptions) {
        this.businessSubscriptions = businessSubscriptions;
    }
    //endregion

    //region > businessLocations (collection)
    @Persistent(mappedBy = "business", dependentElement = "false")
    private SortedSet<BusinessLocation> businessLocations = new TreeSet<BusinessLocation>();

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "110")
    public SortedSet<BusinessLocation> getBusinessLocations() {
        return businessLocations;
    }

    public void setBusinessLocations(final SortedSet<BusinessLocation> businessLocations) {
        this.businessLocations = businessLocations;
    }
    //endregion

    //region > itemCategories (collection)
    @Persistent(mappedBy = "business", dependentElement = "false")
    private SortedSet<ItemCategory> itemCategories = new TreeSet<ItemCategory>();

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "120")
    public SortedSet<ItemCategory> getItemCategories() {
        return itemCategories;
    }

    public void setItemCategories(final SortedSet<ItemCategory> itemCategories) {
        this.itemCategories = itemCategories;
    }
    //endregion

    //region > businessTransactions (collection)
    @Persistent(mappedBy = "business", dependentElement = "false")
    private SortedSet<BusinessTransaction> businessTransactions = new TreeSet<BusinessTransaction>();

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "200")
    public SortedSet<BusinessTransaction> getBusinessTransactions() {
        return businessTransactions;
    }

    public void setBusinessTransactions(final SortedSet<BusinessTransaction> businessTransactions) {
        this.businessTransactions = businessTransactions;
    }
    //endregion

    //region > version (derived property)
    @MemberOrder(sequence = "1000")
    public Long getVersionSequence() {
        return (Long) JDOHelper.getVersion(this);
    }
    //endregion

	//region > assignImage (action)
    @MemberOrder(name="BusinessImage",sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public Business assignImage(
            final @ParameterLayout(named = "Business Image") Blob businessImage
    ) {
        setBusinessImage(businessImage);
        return this;
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    // Associate this action to appear near the "location" field:
    @MemberOrder(name = "location",sequence = "1")
    @ActionLayout(named = "Set Location Address",position = ActionLayout.Position.PANEL)
    public Business updateBusinessLocation(
            final @ParameterLayout(named="Street", multiLine = 2) String street,
            final @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named="Street 2", multiLine = 2) String street2,
            final @ParameterLayout(named="City") String city,
            final @Parameter(maxLength = 5,regexPattern = "\\b[0-9][0-9]{4}\\b") @ParameterLayout(named="Zip Code") String zipCode,
            final @ParameterLayout(named="Country")String country
    )
    {
        final Location geoLocation = this.locationLookupService.lookup(street + " " + street2 + "," + city + "," + "," + zipCode + "," + country );
        setGeoLocation(geoLocation);
        setStreet(street);
        setStreet2(street2);
        setCity(city);
        setZipCode(zipCode);
        setCountry(country);
        return this;
    }


    // provide a default value for argument #0
    public String default0UpdateBusinessLocation() {
        return getStreet();
    }

    // provide a default value for argument #1
    public String default1UpdateBusinessLocation() {
        return getStreet2();
    }

    // provide a default value for argument #2
    public String default2UpdateBusinessLocation() {
        return getCity();
    }

    // provide a default value for argument #3
    public String default3UpdateBusinessLocation() {
        return getZipCode();
    }

    // provide a default value for argument #4
    public String default4UpdateBusinessLocation() {
        return getCountry();
    }
    //endregion


    //region > compareTo
    @Override
    public int compareTo(final Business other) {
        return ObjectContracts.compare(this, other, "businessId");
    }
    //endregion

    //region > injected services
    @Inject
    LocationLookupService locationLookupService;
    //endregion

}

