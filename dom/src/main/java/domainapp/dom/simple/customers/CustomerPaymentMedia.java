package domainapp.dom.simple.customers;

/**
 * Created by Administrator on 10/14/2015.
 */

import domainapp.dom.DomainAppDomainModule;
import domainapp.dom.simple.financials.PaymentMediaType;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import java.util.List;

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "CustomerPaymentMedia"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")

@javax.jdo.annotations.Unique(name="CustomerPaymentMedia_Id_UNQ", members = {"customerSubscription","pmCcPaymentMediaType"})
@DomainObject(auditing= Auditing.ENABLED)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_CHILD,
        cssClassFa = "fa-credit-card"
)

public class CustomerPaymentMedia implements Comparable<CustomerPaymentMedia>{

    //region customerSubscription (property)
    private CustomerSubscription customerSubscription;

    @MemberOrder(sequence = "2")
    @Title(sequence = "1")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public CustomerSubscription getCustomerSubscription() {
        return customerSubscription;
    }

    public void setCustomerSubscription(final CustomerSubscription customerSubscription) {
        this.customerSubscription = customerSubscription;
    }
    //endregion

    //region pmCcPaymentMediaType Payment Media's paymentMediaType (property)
    private PaymentMediaType pmCcPaymentMediaType;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Set payment media info using actions"
    )
    @MemberOrder(name="Subscription Payment Media",sequence = "1")
    @Column(allowsNull = "true")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Title(sequence = "2", prepend = "-")
    public PaymentMediaType getPmCcPaymentMediaType() {
        return pmCcPaymentMediaType;
    }

    public void setPmCcPaymentMediaType(final PaymentMediaType pmCcPaymentMediaType) {
        this.pmCcPaymentMediaType = pmCcPaymentMediaType;
    }
    //endregion

    //region > pmCreditCard (property) payment media credit card number
    private String pmCreditCard;

    @Property(
            editing = Editing.DISABLED,
            regexPattern = "\\b[1-9][0-9]{15}\\b", //Validates that the credit card number starts with a digit followed by 15 more digits
            editingDisabledReason = "Set payment media info using actions"
    )
    @MemberOrder(name="Subscription Payment Media",sequence = "2")
    @Column(allowsNull = "true", length = 16)
    public String getPmCreditCard() {
        return pmCreditCard;
    }

    public void setPmCreditCard(final String pmCreditCard) {
        this.pmCreditCard = pmCreditCard;
    }
    // endregion

    //region > cm_name (property) name associated with the collection media
    private String pmName;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Set payment media info using actions"
    )
    @MemberOrder(name="Subscription Payment Media",sequence = "3")
    @Column(allowsNull = "true", length = 100)
    public String getPmName() {
        return pmName;
    }

    public void setPmName(final String pmName) {
        this.pmName = pmName;
    }
    // endregion

    //region > pmExpirationMonth (property)
    private Integer pmExpirationMonth;

    @Property(
            editing = Editing.DISABLED,
            maxLength = 2,
            editingDisabledReason = "Set payment media info using actions"
    )
    @MemberOrder(name="Subscription Payment Media",sequence = "4")
    @Column(allowsNull = "true")
    public Integer getPmExpirationMonth() {
        return pmExpirationMonth;
    }

    public void setPmExpirationMonth(final Integer pmExpirationMonth) {
        this.pmExpirationMonth = pmExpirationMonth;
    }
    // endregion

    //region > pmExpirationYear (property)
    private Integer pmExpirationYear;

    @Property(
            editing = Editing.DISABLED,
            maxLength = 2,
            editingDisabledReason = "Set payment media info using actions"
    )
    @MemberOrder(name="Subscription Payment Media",sequence = "5")
    @Column(allowsNull = "true")
    public Integer getPmExpirationYear() {
        return pmExpirationYear;
    }

    public void setPmExpirationYear(final Integer pmExpirationYear) {
        this.pmExpirationYear = pmExpirationYear;
    }
    // endregion

    //region > pmCcCvv (property)
    private Integer pmCcCvv;

    @MemberOrder(name="Subscription Payment Media",sequence = "6")
    @Column(allowsNull = "true")
    @Property(
            editing = Editing.DISABLED,
            maxLength = 3,
            editingDisabledReason = "Set collection media info using actions"
    )
    public Integer getPmCcCvv() {
        return pmCcCvv;
    }

    public void setPmCcCvv(final Integer pmCcCvv) {
        this.pmCcCvv = pmCcCvv;
    }
    // endregion

    //region > pmCcZip (property)
    private Integer pmCcZip;

    @Property(
            editing = Editing.DISABLED,
            maxLength = 10,
            editingDisabledReason = "Set payment media info using actions"
    )
    @MemberOrder(name="Subscription Payment Media",sequence = "20")
    @Column(allowsNull = "true")
    public Integer getPmCcZip() {
        return pmCcZip;
    }

    public void setPmCcZip(final Integer pmCcZip) {
        this.pmCcZip = pmCcZip;
    }

    // endregion

    //region > version (derived property)
    public Long getVersionSequence() {
        return (Long) JDOHelper.getVersion(this);
    }
    //endregion


    //region > definePaymentMedia (action)
    @MemberOrder(name="pmCreditCard",sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public CustomerPaymentMedia definePaymentMedia(
            final @ParameterLayout(named = "Credit Card Type") PaymentMediaType pmCcPaymentMediaType,
            final @Parameter(maxLength = 16, regexPattern = "\\b[1-9][0-9]{15}\\b") @ParameterLayout(named = "Credit Card Number", typicalLength = 16) String pmCreditCard,
            final @ParameterLayout(named = "Name on Credit Card", typicalLength = 40) String pmName,
            final @Parameter(maxLength = 2, regexPattern = "\\b[0-9][0-9]{1}\\b") @ParameterLayout(named = "Expiration Month", typicalLength = 2) Integer pmExpirationMonth,
            final @Parameter(maxLength = 2, regexPattern = "\\b[0-9][0-9]{1}\\b") @ParameterLayout(named = "Expiration Year", typicalLength = 2) Integer pmExpirationYear,
            final @Parameter(maxLength = 5,regexPattern = "\\b[0-9][0-9]{4}\\b") @ParameterLayout(named = "Zip Code", typicalLength = 5) Integer pmCcZip,
            final @Parameter(maxLength =3) @ParameterLayout(named = "CVV", typicalLength = 5) Integer pmCcCvv
    )
    {
        setPmCcPaymentMediaType(pmCcPaymentMediaType);
        setPmCreditCard(pmCreditCard);
        setPmName(pmName);
        setPmExpirationMonth(pmExpirationMonth);
        setPmExpirationYear(pmExpirationYear);
        setPmCcZip(pmCcZip);
        setPmCcCvv(pmCcCvv);
        return this;
    }

    // provide a default value for argument #0
    public PaymentMediaType default0DefinePaymentMedia() {
        return getPmCcPaymentMediaType();
    }

    // provide a default value for argument #1
    public String default1DefinePaymentMedia() {
        return getPmCreditCard();
    }

    // provide a default value for argument #2
    public String default2DefinePaymentMedia() {
        return getPmName();
    }

    // provide a default value for argument #3
    public Integer default3DefinePaymentMedia() {
        return getPmExpirationMonth();
    }

    // provide a default value for argument #4
    public Integer default4DefinePaymentMedia() {
        return getPmExpirationYear();
    }

    // provide a default value for argument #5
    public Integer default5DefinePaymentMedia() {
        return getPmCcZip();
    }

    // provide a default value for argument #6
    public Integer default6DefinePaymentMedia() {
        return getPmCcCvv();
    }
    //endregion

    //region > delete (action)

    public static class DeletedEvent extends CustomerPaymentMedia.ActionDomainEvent {
        private static final long serialVersionUID = 1L;
        public DeletedEvent(
                final CustomerPaymentMedia source,
                final Identifier identifier,
                final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = DeletedEvent.class,
            invokeOn = InvokeOn.OBJECT_AND_COLLECTION
    )
    public List<CustomerPaymentMedia> delete() {

        // obtain title first, because cannot reference object after deleted
        final String title = container.titleOf(this);

        final List<CustomerPaymentMedia> returnList = null;
        container.removeIfNotAlready(this);
        container.informUser(
                TranslatableString.tr("Deleted {title}", "title", title), this.getClass(), "delete");

        return returnList;
    }
    //endregion

    //region > events

    public static abstract class PropertyDomainEvent<T> extends DomainAppDomainModule.PropertyDomainEvent<CustomerPaymentMedia, T> {

        public PropertyDomainEvent(final CustomerPaymentMedia source, final Identifier identifier) {
            super(source, identifier);
        }

        public PropertyDomainEvent(final CustomerPaymentMedia source, final Identifier identifier, final T oldValue, final T newValue) {
            super(source, identifier, oldValue, newValue);
        }
    }

    public static abstract class ActionDomainEvent extends DomainAppDomainModule.ActionDomainEvent<CustomerPaymentMedia> {
        private static final long serialVersionUID = 1L;
        public ActionDomainEvent(
                final CustomerPaymentMedia source,
                final Identifier identifier,
                final Object... arguments) {
            super(source, identifier, arguments);
        }
    }
    //endregion

    //region > compareTo

    @Override
    public int compareTo(final CustomerPaymentMedia other) {
        return ObjectContracts.compare(this, other, "customerSubscription","pmCcPaymentMediaType");
    }

    //endregion

    //region > injected services

    @javax.inject.Inject
    DomainObjectContainer container;

    //endregion


}
