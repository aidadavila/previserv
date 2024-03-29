/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package domainapp.dom.simple.businesses;

import java.math.BigDecimal;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.JDOHelper;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.InvokeOn;
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
import org.apache.isis.applib.services.actinvoc.ActionInvocationContext;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;
import org.apache.isis.applib.value.Blob;

import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.WithApplicationTenancy;
import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;
import org.isisaddons.wicket.gmap3.cpt.service.LocationLookupService;

import domainapp.dom.DomainAppDomainModule;
import domainapp.dom.simple.customerorders.CustomerOrder;
import domainapp.dom.simple.items.ItemBusinessLocation;

@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "simple",
        table = "BusinessLocation"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findAllBusinessLocations", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.simple.businesses.BusinessLocation "),
        @javax.jdo.annotations.Query(
                name = "findBusinessLocationsByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.simple.businesses.BusinessLocation "
                        + "WHERE name.indexOf(:name) >= 0 "),
        @javax.jdo.annotations.Query(
                name = "findBusinessLocationsByBusiness", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.simple.businesses.BusinessLocation "
                        + "WHERE business.name.indexOf(:name) >= 0 "),
        @javax.jdo.annotations.Query(
        name = "findBusinessLocationById", language = "JDOQL",
        value = "SELECT "
                + "FROM domainapp.dom.simple.BusinessLocation "
                + "WHERE businessLocationId.indexOf(:businessLocationId) >= 0 ")
})
@javax.jdo.annotations.Unique(name="BusinessLocation_bus_loc_UNQ", members = {"business","businessLocationId"})
@DomainObject(autoCompleteAction = "findBusinessLocationsByName", autoCompleteRepository = BusinessLocations.class)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-building-o"
)
public class BusinessLocation implements Comparable<BusinessLocation>, Locatable, WithApplicationTenancy {

//region > business (property)
    private Business business;

    @MemberOrder(sequence = "3")
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


    //region > businessLocationId (property)
    private String businessLocationId;

    @Column(allowsNull="false", length = 10)
    @MemberOrder(sequence = "1")
    public String getBusinessLocationId() {
        return businessLocationId;
    }

    public void setBusinessLocationId(final String businessLocationId) {
        this.businessLocationId = businessLocationId;
    }
    // endregion

    //region > name (property)
    private String name;

    @Column(allowsNull="false", length = 40)
    @Title(sequence="2", prepend = ":")
    @MemberOrder(sequence = "2")
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
    // endregion

    //region > maxDeliveryDistance (property)
    private BigDecimal maxDeliveryDistance;
    @Column(allowsNull="false", length=10, scale=2)
    @MemberOrder(name="Delivery",sequence = "4")
    public BigDecimal getMaxDeliveryDistance() {
        return maxDeliveryDistance;
    }

    public void setMaxDeliveryDistance(final BigDecimal maxDeliveryDistance) {
        this.maxDeliveryDistance = maxDeliveryDistance;
    }
    // endregion

    //region > businessCategory (property)
    private BusinessCategory businessCategory;

    @MemberOrder(sequence = "3")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public BusinessCategory getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(final BusinessCategory businessCategory) {
        this.businessCategory = businessCategory;
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

    // region > country (property)
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


    //region businessImage (property)
    @MemberOrder(name = "Business Image", sequence = "107")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    
    public Blob getBusinessImage() {
    	return this.getBusiness().getBusinessImage();
    }
    //endregion

    
    @Action(semantics = SemanticsOf.IDEMPOTENT)
    // Associate this action to appear near the "location" field:
    @MemberOrder(name = "location",sequence = "1")
    @ActionLayout(named = "Set Location Address",position = ActionLayout.Position.PANEL)

    public BusinessLocation updateBusinessLocation(
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

    //region applicationTenancy (property)
    @MemberOrder(sequence = "107")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)

    @Override
    public ApplicationTenancy getApplicationTenancy() {
        return this.getBusiness().getApplicationTenancy();
    }
    //endregion

    //region > locationSubscriptions (collection)
    @Persistent(mappedBy = "businessLocation", dependentElement = "false")
    private SortedSet<LocationSubscription> locationSubscriptions = new TreeSet<LocationSubscription>();

    @MemberOrder(sequence = "100")
    @CollectionLayout(render = RenderType.EAGERLY)
    public SortedSet<LocationSubscription> getLocationSubscriptions() {
        return locationSubscriptions;
    }

    public void setLocationSubscriptions(final SortedSet<LocationSubscription> locationSubscriptions) {
        this.locationSubscriptions = locationSubscriptions;
    }
    //endregion

    //region > businessPaymentMediaType (collection)
    @Persistent(mappedBy = "businessLocation", dependentElement = "false")
    private SortedSet<BusinessPaymentMediaType> businessPaymentMediaTypes = new TreeSet<BusinessPaymentMediaType>();

    @MemberOrder(sequence = "110")
    @CollectionLayout(render = RenderType.EAGERLY)
    public SortedSet<BusinessPaymentMediaType> getBusinessPaymentMediaTypes() {
        return businessPaymentMediaTypes;
    }

    public void setBusinessPaymentMediaTypes(final SortedSet<BusinessPaymentMediaType> businessPaymentMediaTypes) {
        this.businessPaymentMediaTypes = businessPaymentMediaTypes;
    }
    //endregion

    //region > customerOrders (collection)
    @Persistent(mappedBy = "businessLocation", dependentElement = "false")
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

    //region > deliverers (collection)
    @Persistent(mappedBy = "businessLocation", dependentElement = "false")
    private SortedSet<Deliverer> deliverers = new TreeSet<Deliverer>();

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "210")
    public SortedSet<Deliverer> getDeliverers() {
        return deliverers;
    }

    public void setDeliverers(final SortedSet<Deliverer> deliverers) {
        this.deliverers = deliverers;
    }
    //endregion

    //region > itemBusinessLocations (collection)
    @Persistent(mappedBy = "businessLocation", dependentElement = "false")
    private SortedSet<ItemBusinessLocation> itemBusinessLocations = new TreeSet<ItemBusinessLocation>();

    @CollectionLayout(render = RenderType.LAZILY)
    @MemberOrder(sequence = "220")
    public SortedSet<ItemBusinessLocation> getItemBusinessLocations() {
        return itemBusinessLocations;
    }

    public void setItemBusinessLocations(final SortedSet<ItemBusinessLocation> itemBusinessLocations) {
        this.itemBusinessLocations = itemBusinessLocations;
    }

    //endregion

    //region > userComments (collection)
    @Persistent(mappedBy = "businessLocation", dependentElement = "false")
    private SortedSet<UserComment> userComments = new TreeSet<UserComment>();

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "201")
    public SortedSet<UserComment> getUserComments() {
		return userComments;
	}

	public void setUserComments(SortedSet<UserComment> userComments) {
		this.userComments = userComments;
	}
    //endregion

    


    //region > delete (action)

    public static class DeletedEvent extends BusinessLocation.ActionDomainEvent {
        private static final long serialVersionUID = 1L;
        public DeletedEvent(
                final BusinessLocation source,
                final Identifier identifier,
                final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = DeletedEvent.class,
            invokeOn = InvokeOn.OBJECT_AND_COLLECTION
    )
    public List<BusinessLocation> delete() {

        // obtain title first, because cannot reference object after deleted
        final String title = container.titleOf(this);

        final List<BusinessLocation> returnList = null;
        container.removeIfNotAlready(this);
        container.informUser(
                TranslatableString.tr("Deleted {title}", "title", title), this.getClass(), "delete");

        return returnList;
    }
    //endregion

    //region > version (derived property)
    public Long getVersionSequence() {
        return (Long) JDOHelper.getVersion(this);
    }
    //endregion


    //region > events

    public static abstract class PropertyDomainEvent<T> extends DomainAppDomainModule.PropertyDomainEvent<BusinessLocation, T> {

        public PropertyDomainEvent(final BusinessLocation source, final Identifier identifier) {
            super(source, identifier);
        }

        public PropertyDomainEvent(final BusinessLocation source, final Identifier identifier, final T oldValue, final T newValue) {
            super(source, identifier, oldValue, newValue);
        }
    }

    public static abstract class ActionDomainEvent extends DomainAppDomainModule.ActionDomainEvent<BusinessLocation> {
        private static final long serialVersionUID = 1L;
        public ActionDomainEvent(
                final BusinessLocation source,
                final Identifier identifier,
                final Object... arguments) {
            super(source, identifier, arguments);
        }
    }
    //endregion

    //region > compareTo

    @Override
    public int compareTo(final BusinessLocation other) {
        return ObjectContracts.compare(this, other, "business","businessLocationId");
    }

    //endregion

    //region > injected services

    @Inject
    LocationLookupService locationLookupService;

    @Inject
    DomainObjectContainer container;

    @Inject
    public ActionInvocationContext actionInvocationContext;

    @Inject
    BusinessLocations businessLocations;


    //endregion


}
