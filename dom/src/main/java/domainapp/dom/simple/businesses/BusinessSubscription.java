package domainapp.dom.simple.businesses;

import domainapp.dom.simple.applications.SubscriptionStatus;
import domainapp.dom.simple.financials.FinancialInst;
import domainapp.dom.simple.financials.PaymentMediaType;
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

/**
 * Created by Administrator on 10/1/2015.
 */

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "BusinessSubscription"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")

@javax.jdo.annotations.Unique(name="BusinessSubcription_Id_UNQ", members = {"businessSubscriptionId"})
@DomainObject(auditing= Auditing.ENABLED)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_CHILD,
        cssClassFa = "fa-key"
)

@MemberGroupLayout(
        columnSpans={4,4,4,12},
        left={"General", "Subscription Status"},
        middle={"Subscription Payment Media", "Statements"},
        right={"Subscription Collection Media"}
)

public class BusinessSubscription implements Comparable<BusinessSubscription>, CalendarEventable {

    //region > ID (property)
    private Long businessSubscriptionId;

    @Column(allowsNull="false")
    @MemberOrder(sequence = "1")
    public Long getBusinessSubscriptionId() {
        return businessSubscriptionId;
    }

    public void setBusinessSubscriptionId(final Long businessSubscriptionId) {
        this.businessSubscriptionId = businessSubscriptionId;
    }
    // endregion


    //region business (property)
    private Business business;

    @MemberOrder(sequence = "2")
    @Column(allowsNull = "true")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Title(sequence = "1")
    public Business getBusiness() {
        return business;
    }

    public void setBusiness(final Business business) {
        this.business = business;
    }
    //endregion

    //region > status (property)
    private SubscriptionStatus status;
    @MemberOrder(name="Subscription Status",sequence = "4")
    @Column(allowsNull = "false", defaultValue = "Active")
    @Title(sequence = "2", prepend = "-")
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
    @Column(allowsNull = "true")
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
            editingDisabledReason = "Date is automatically set when using Actions"
    )
    public DateTime getCancellationTime() {
        return cancellationTime;
    }

    public void setCancellationTime(DateTime cancellationTime) {
        this.cancellationTime = cancellationTime;
    }
    //endregion

    //region > statementDom (Property)
    private Integer statementDom;

    @MemberOrder(name="Statements",sequence = "7")
    @Column(allowsNull = "false")
    public Integer getStatementDom() {
        return statementDom;
    }

    public void setStatementDom(Integer statementDom) {
        this.statementDom = statementDom;
    }
    //endregion

    //pmCcPaymentMediaType Payment Media's paymentMediaType (property)
    private PaymentMediaType pmCcPaymentMediaType;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Set payment media info using actions"
    )
    @MemberOrder(name="Subscription Payment Media",sequence = "1")
    @Column(allowsNull = "true")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
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

    //region > cmIsAccount (property) defines if collection media is a bank account
    private Boolean cmIsAccount;

    @MemberOrder(name="Subscription Collection Media",sequence = "10")
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Set collection media info using actions"
    )
    @PropertyLayout(labelPosition = LabelPosition.RIGHT)
    @Column(allowsNull = "true")
    public Boolean getCmIsAccount() {
        return cmIsAccount;
    }

    public void setCmIsAccount(final Boolean cmIsAccount) {
        this.cmIsAccount = cmIsAccount;
    }
    // endregion

    //region > cmIsCreditCard (property) defines if collection media is a credit card
    private Boolean cmIsCreditCard;

    @MemberOrder(name="Subscription Collection Media",sequence = "13")
    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Set collection media info using actions"
    )
    @PropertyLayout(labelPosition = LabelPosition.RIGHT)
    @Column(allowsNull = "true")
    public Boolean getCmIsCreditCard() {
        return cmIsCreditCard;
    }

    public void setCmIsCreditCard(final Boolean cmIsCreditCard) {
        this.cmIsCreditCard = cmIsCreditCard;
    }
    // endregion


    //region cmFinancialInst (property) collection media institution
    private FinancialInst cmFinancialInst;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Set collection media info using actions"
    )
    @MemberOrder(name="Subscription Collection Media",sequence = "11")
    @Column(allowsNull = "true")
    public FinancialInst getCmFinancialInst() {
        return cmFinancialInst;
    }

    public void setCmFinancialInst(FinancialInst cmFinancialInst) {
        this.cmFinancialInst = cmFinancialInst;
    }
    //endregion

    //region > collectionMedia (property) collection media account CLABE code
    private String collectionMedia;

    @Property(
            editing = Editing.DISABLED,
            regexPattern = "\\b[1-9][0-9]{15}\\b", //Validates that the account number starts with a digit followed by 15 more digits
            editingDisabledReason = "Set collection media info using actions"
    )
    @MemberOrder(name="Subscription Collection Media",sequence = "12")
    @Column(allowsNull = "true", length = 16)
    public String getCollectionMedia() {
        return collectionMedia;
    }

    public void setCollectionMedia(final String collectionMedia) {
        this.collectionMedia = collectionMedia;
    }
    // endregion

    //Collection Media's paymentMediaType (property)
    private PaymentMediaType cmCcPaymentMediaType;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Set collection media info using actions"
    )
    @MemberOrder(name="Subscription Collection Media",sequence = "14")
    @Column(allowsNull = "true")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    public PaymentMediaType getCmCcPaymentMediaType() {
        return cmCcPaymentMediaType;
    }

    public void setCmCcPaymentMediaType(final PaymentMediaType cmCcPaymentMediaType) {
        this.cmCcPaymentMediaType = cmCcPaymentMediaType;
    }
    //endregion

    //region > cmCreditCard (property) collection media credit card number
    private String cmCreditCard;

    @Property(
            editing = Editing.DISABLED,
            regexPattern = "\\b[1-9][0-9]{15}\\b", //Validates that the credit card number starts with a digit followed by 15 more digits
            editingDisabledReason = "Set collection media info using actions"
    )
    @MemberOrder(name="Subscription Collection Media",sequence = "15")
    @Column(allowsNull = "true", length = 16)
    public String getCmCreditCard() {
        return cmCreditCard;
    }

    public void setCmCreditCard(final String cmCreditCard) {
        this.cmCreditCard = cmCreditCard;
    }
    // endregion


    //region > cm_name (property) name associated with the collection media
    private String cmName;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Set collection media info using actions"
    )
    @MemberOrder(name="Subscription Collection Media",sequence = "16")
    @Column(allowsNull = "true", length = 100)
    public String getCmName() {
        return cmName;
    }

    public void setCmName(final String cmName) {
        this.cmName = cmName;
    }
    // endregion

    //region > cmExpirationMonth (property)
    private Integer cmExpirationMonth;

    @Property(
            editing = Editing.DISABLED,
            maxLength = 2,
            editingDisabledReason = "Set collection media info using actions"
    )
    @MemberOrder(name="Subscription Collection Media",sequence = "17")
    @Column(allowsNull = "true")
    public Integer getCmExpirationMonth() {
        return cmExpirationMonth;
    }

    public void setCmExpirationMonth(final Integer cmExpirationMonth) {
        this.cmExpirationMonth = cmExpirationMonth;
    }
    // endregion

    //region > cmExpirationYear (property)
    private Integer cmExpirationYear;

    @Property(
            editing = Editing.DISABLED,
            maxLength = 2,
            editingDisabledReason = "Set collection media info using actions"
    )
    @MemberOrder(name="Subscription Collection Media",sequence = "18")
    @Column(allowsNull = "true")
    public Integer getCmExpirationYear() {
        return cmExpirationYear;
    }

    public void setCmExpirationYear(final Integer cmExpirationYear) {
        this.cmExpirationYear = cmExpirationYear;
    }
    // endregion

    //region > cmCvv (property)
    private Integer cmCcCvv;

    @MemberOrder(name="Subscription Collection Media",sequence = "19")
    @Column(allowsNull = "true")
    @Property(
            editing = Editing.DISABLED,
            maxLength = 3,
            editingDisabledReason = "Set collection media info using actions"
    )
    public Integer getCmCcCvv() {
        return cmCcCvv;
    }

    public void setCmCcCvv(final Integer cmCcCvv) {
        this.cmCcCvv = cmCcCvv;
    }
    // endregion

    //region > cmCcZip (property)
    private Integer cmCcZip;

    @Property(
            editing = Editing.DISABLED,
            maxLength = 10,
            editingDisabledReason = "Set collection media info using actions"
    )
    @MemberOrder(name="Subscription Collection Media",sequence = "20")
    @Column(allowsNull = "true")
    public Integer getCmCcZip() {
        return cmCcZip;
    }

    public void setCmCcZip(final Integer cmCcZip) {
        this.cmCcZip = cmCcZip;
    }
    // endregion


    //region > version (derived property)
    public Long getVersionSequence() {
        return (Long) JDOHelper.getVersion(this);
    }
    //endregion


    //region > activateSubscription (action)
    @MemberOrder(name="status",sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public BusinessSubscription activate() {
        setSubscriptionTime(clockService.nowAsDateTime());
        setStatus(SubscriptionStatus.ACTIVE);
        return this;
    }

    // Validate invariants at the BusinessSubscription level
    public String validateActivate() {
        //if the status is Active, then the BusinessSubscription must have a payment media defined
        if(getStatus() == SubscriptionStatus.ACTIVE && getPmCreditCard().length() == 0) {
            return "A subscription must have a payment media defined to be active" ;
        }
        //if the status is Active, then the BusinessSubscription must have a payment media defined
        if(getStatus().equals(SubscriptionStatus.ACTIVE) && !(getCmIsAccount().equals(Boolean.TRUE) || getCmIsCreditCard().equals(Boolean.TRUE))) {
            return "A subscription must have a collection media defined to be active";
        }
        return null;
    }
    //endregion


    // disable action dependent on state of object
    public String disableActivate() {
        return status.equals(SubscriptionStatus.ACTIVE) ? "Subscription is already active" : null;
    }
    //endregion

    //region > suspendSubscription (action)
    @MemberOrder(name="status",sequence = "2")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public BusinessSubscription suspend() {
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
    public BusinessSubscription cancel() {
        setCancellationTime(clockService.nowAsDateTime());
        setStatus(SubscriptionStatus.CANCELLED);
        return this;
    }

    // disable action dependent on state of object
    public String disableCancel() {
        return status.equals(SubscriptionStatus.CANCELLED) ? "Subscription is already cancelled" : null;
    }
    //endregion

    //region > definePaymentMedia (action)
    @MemberOrder(name="pmCreditCard",sequence = "1")
    @ActionLayout(position = ActionLayout.Position.PANEL)
    public BusinessSubscription definePaymentMedia(
            final @ParameterLayout(named = "Credit Card Type") PaymentMediaType pmCcPaymentMediaType,
            final @Parameter(maxLength = 16, regexPattern = "\\b[1-9][0-9]{15}\\b") @ParameterLayout(named = "Credit Card Number", typicalLength = 16) String pmCreditCard,
            final @ParameterLayout(named = "Name on Credit Card", typicalLength = 40) String pmName,
            final @Parameter(maxLength = 2, regexPattern = "\\b[0-9][0-9]{1}\\b") @ParameterLayout(named = "Expiration Month", typicalLength = 2) Integer pmExpirationMonth,
            final @Parameter(maxLength = 2, regexPattern = "\\b[0-9][0-9]{1}\\b") @ParameterLayout(named = "Expiration Year", typicalLength = 2) Integer pmExpirationYear,
            final @Parameter(maxLength = 5,regexPattern = "\\b[0-9][0-9]{4}\\b") @ParameterLayout(named = "Zip Code", typicalLength = 5) Integer pmCcZip,
            final @Parameter(maxLength =4) @ParameterLayout(named = "CVV", typicalLength = 4) Integer pmCcCvv
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
    //endregion

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



    //region > collectionByAccount (action)
    @MemberOrder(name="cmIsAccount",sequence = "1")
    @ActionLayout(named = "Collect by Bank Account",position = ActionLayout.Position.BELOW)
    public BusinessSubscription collectionByAccount(
            final @ParameterLayout(named="Collection Media Financial Institution") FinancialInst cmFinancialInst,
            final @Parameter(maxLength = 16, regexPattern = "\\b[1-9][0-9]{15}\\b") @ParameterLayout(named = "Account Number") String collectionMedia)
    {
        setCmIsAccount(Boolean.TRUE);
        setCmIsCreditCard(Boolean.FALSE);
        setCmFinancialInst(cmFinancialInst);
        setCollectionMedia(collectionMedia);
        return this;
    }
    //endregion

    // provide a default value for argument #0
    public FinancialInst default0CollectionByAccount() {
        return getCmFinancialInst();
    }

    // provide a default value for argument #1
    public String default1CollectionByAccount() {
        return getCollectionMedia();
    }


    //region > collectionByCreditCard (action)
    @MemberOrder(name="cmIsCreditCard",sequence = "1")
    @ActionLayout(named = "Collect by Credit Card",position = ActionLayout.Position.BELOW)
    public BusinessSubscription collectionByCreditCard(
            final @ParameterLayout(named = "Credit Card Type") PaymentMediaType cmCcPaymentMediaType,
            final @Parameter(maxLength = 16, regexPattern = "\\b[1-9][0-9]{15}\\b") @ParameterLayout(named = "Credit Card Number", typicalLength = 16) String cmCreditCard,
            final @ParameterLayout(named = "Name on Credit Card", typicalLength = 40) String cmName,
            final @Parameter(maxLength = 2, regexPattern = "\\b[0-9][0-9]{1}\\b") @ParameterLayout(named = "Expiration Month", typicalLength = 2) Integer cmExpirationMonth,
            final @Parameter(maxLength = 2, regexPattern = "\\b[0-9][0-9]{1}\\b") @ParameterLayout(named = "Expiration Year", typicalLength = 2) Integer cmExpirationYear,
            final @Parameter(maxLength = 5,regexPattern = "\\b[0-9][0-9]{4}\\b") @ParameterLayout(named = "Zip Code", typicalLength = 5) Integer cmCcZip,
            final @Parameter(maxLength =3) @ParameterLayout(named = "CVV", typicalLength = 5) Integer cmCcCvv
    )
    {
        setCmCcPaymentMediaType(cmCcPaymentMediaType);
        setCmIsCreditCard(Boolean.TRUE);
        setCmIsAccount(Boolean.FALSE);
        setCmCreditCard(cmCreditCard);
        setCmName(cmName);
        setCmExpirationMonth(cmExpirationMonth);
        setCmExpirationYear(cmExpirationYear);
        setCmCcZip(cmCcZip);
        setCmCcCvv(cmCcCvv);
        return this;
    }
    //endregion


    // Validate invariants at the BusinessSubscription level
    public String validate() {
        //if the status is Active, then the BusinessSubscription must have a payment media defined
        if(getStatus() == SubscriptionStatus.ACTIVE && getPmCreditCard().length() == 0) {
            return "A subscription must have a payment media defined to be active" ;
        }
        //if the status is Active, then the BusinessSubscription must have a payment media defined
        if(getStatus().equals(SubscriptionStatus.ACTIVE) && !(getCmIsAccount().equals(Boolean.TRUE) || getCmIsCreditCard().equals(Boolean.TRUE))) {
            return "A subscription must have a collection media defined to be active";
        }
        return null;
    }
    //endregion

    // provide a default value for argument #0
    public PaymentMediaType default0CollectionByCreditCard() {
        return getCmCcPaymentMediaType();
    }

    // provide a default value for argument #1
    public String default1CollectionByCreditCard() {
        return getCmCreditCard();
    }

    // provide a default value for argument #2
    public String default2CollectionByCreditCard() {
        return getCmName();
    }

    // provide a default value for argument #3
    public Integer default3CollectionByCreditCard() {
        return getCmExpirationMonth();
    }

    // provide a default value for argument #4
    public Integer default4CollectionByCreditCard() {
        return getCmExpirationYear();
    }

    // provide a default value for argument #5
    public Integer default5CollectionByCreditCard() {
        return getCmCcZip();
    }

    // provide a default value for argument #6
    public Integer default6CollectionByCreditCard() {
        return getCmCcCvv();
    }


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
    public int compareTo(final BusinessSubscription other) {
        return ObjectContracts.compare(this, other, "businessSubscriptionId");
    }

    //endregion

    //region > injected services

    @Inject
    private DomainObjectContainer container;

    @Inject
    private ClockService clockService;
    //endregion

}
