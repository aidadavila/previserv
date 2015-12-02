package domainapp.dom.simple.businesses;

/**
 * Created by Administrator on 9/24/2015.
 */

import domainapp.dom.simple.applications.Application;
import domainapp.dom.simple.applications.SubscriptionStatus;
import domainapp.dom.simple.applications.SubscriptionType;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.util.ObjectContracts;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEvent;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEventable;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.jdo.JDOHelper;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "LocationSubscription"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")
@DomainObject
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_CHILD,
        cssClassFa = "fa-file-text"

)
@javax.jdo.annotations.Unique(name="LocationSubscription_Loc_App_UNQ", members = {"businessLocation","application"})
public class LocationSubscription implements Comparable<LocationSubscription>, CalendarEventable {

    //region > businessLocation (property)
    private BusinessLocation businessLocation;

    @MemberOrder(sequence = "1")
    @Title(sequence="1")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public BusinessLocation getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(final BusinessLocation businessLocation) {
        this.businessLocation = businessLocation;
    }
    //endregion

    //region > Application (property)
    private Application application;

    @MemberOrder(sequence = "2")
    @Title(sequence="2", prepend = "-")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public Application getApplication() {
        return application;
    }

    public void setApplication(final Application application) {
        this.application = application;
    }
    //endregion

    //region > SubscriptionType (property)
    private SubscriptionType subscriptionType;

    @MemberOrder(sequence = "3")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(final SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
    //endregion

    //region > status (property)
    private SubscriptionStatus status;
    @MemberOrder(name="Subscription Status",sequence = "4")
    @Column(allowsNull = "true", defaultValue = "Active")
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

    //region > SubscriptionTime (Property)
    private DateTime subscriptionTime;

    @MemberOrder(name="Subscription Status",sequence = "5")
    @Column(allowsNull = "false")
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

    //region > SubscriptionTime (Property)
    private DateTime cancellationTime;

    @MemberOrder(name="Subscription Status",sequence = "6")
    @Column(allowsNull = "true")
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Date is automatically when by using Actions"
    )
    public DateTime getCancellationTime() {
        return cancellationTime;
    }

    public void setCancellationTime(DateTime cancellationTime) {
        this.cancellationTime = cancellationTime;
    }
    //endregion

    //region > version (derived property)
    public Long getVersionSequence() {
        return (Long) JDOHelper.getVersion(this);
    }
    //endregion


    //region > activateSubscription (action)
    @MemberOrder(name="status",sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public LocationSubscription activate() {
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
    public LocationSubscription suspend() {
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
    public LocationSubscription cancel() {
        setCancellationTime(clockService.nowAsDateTime());
        setStatus(SubscriptionStatus.CANCELLED);
        return this;
    }

    // disable action dependent on state of object
    public String disableCancel() {
        return status.equals(SubscriptionStatus.CANCELLED) ? "Subscription is already cancelled" : null;
    }
    //endregion

    //region > calendar (module)
    @Programmatic
    @Override
    public String getCalendarName() {
        return "Subscription";
    }

    @Programmatic
    @Override
    public CalendarEvent toCalendarEvent() {
        return new CalendarEvent(getSubscriptionTime(), "", container.titleOf(this));
    }
    //endregion

    //region > compareTo

    @Override
    public int compareTo(final LocationSubscription other) {
        return ObjectContracts.compare(this, other, "businessLocation","application");
    }

    //endregion

    //region > injected services

    @Inject
    private DomainObjectContainer container;

    @Inject
    private ClockService clockService;
    //endregion


}

