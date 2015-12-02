package domainapp.dom.simple.customerorders;

import java.math.BigDecimal;
import java.util.List;

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
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

import domainapp.dom.DomainAppDomainModule;
import domainapp.dom.simple.items.Item;

/**
 * Created by Administrator on 10/21/2015.
 */

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "CustomerOrderItem"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")

@javax.jdo.annotations.Unique(name="Customer_Order_Item_Id_UNQ", members = {"customerOrder", "item"})
@DomainObject(auditing= Auditing.ENABLED )

@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-pencil-square"
)

public class CustomerOrderItem implements Comparable<CustomerOrderItem> {

    //region > customerOrder (property)
    private CustomerOrder customerOrder;

    @MemberOrder(sequence = "1")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Title(sequence = "1")
    @Persistent

    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(final CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }
    //endregion

    //region > item (property)
    public Item item;

    @MemberOrder(sequence = "2")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Title(sequence = "2")
    @Persistent
    public Item getItem() {
        return item;
    }

    public void setItem(final Item item) {
        this.item = item;
    }
    //endregion

    //region > quantitySold (property)
    private BigDecimal quantitySold;

    @Column(allowsNull="false", length=8, scale=0, defaultValue = "0")
    @MemberOrder(sequence = "3")
    public BigDecimal getQuantitySold() {return quantitySold;}
    public void setQuantitySold(BigDecimal quantitySold) {
        this.quantitySold = quantitySold;
    }

    public void modifyQuantitySold(BigDecimal quantitySold){
        this.getCustomerOrder().addToTotalRetailAmount((quantitySold.doubleValue() - this.getQuantitySold().doubleValue()) * this.getRetailPrice().doubleValue());
        this.setQuantitySold(quantitySold);
    }

    //endregion

    //region > retailPrice (property)
    private BigDecimal retailPrice;

    @Property(editing = Editing.DISABLED)
    @Column(allowsNull="false", length=8, scale=2)
    @MemberOrder(sequence = "4")
    public BigDecimal getRetailPrice() {return retailPrice;}
    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }
    //endregion

    //region > retailPriceDiscount (property)
    private BigDecimal retailPriceDiscount;

    @Property(editing = Editing.DISABLED)
    @Column(allowsNull="false", length=8, scale=2, defaultValue = "0")
    @MemberOrder(sequence = "5")
    public BigDecimal getRetailPriceDiscount() {return retailPriceDiscount;}
    public void setRetailPriceDiscount(BigDecimal retailPriceDiscount) {
        this.retailPriceDiscount = retailPriceDiscount;
    }
    //endregion

    //region > taxAmount (property)
    private BigDecimal taxAmount;

    @Property(editing = Editing.DISABLED)
    @Column(allowsNull="false", length=8, scale=2, defaultValue = "0")
    @MemberOrder(name = "status",sequence = "6")
    public BigDecimal getTaxAmount() {return taxAmount;}
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
    //endregion

    //region > status (property)
    private CustomerOrderStatus status;

    @Property(
            editing = Editing.DISABLED,
            editingDisabledReason = "Status is set by using actions"
    )
    @Column(allowsNull = "false")
    @MemberOrder(name="Status",sequence = "10")
    public CustomerOrderStatus getStatus() {
        return status;
    }
    public void setStatus(CustomerOrderStatus status) {
        this.status = status;
    }
    // endregion





    //region > persisted
    public void persisted() {
        //System.out.println("Grabe Detalle");
        this.getCustomerOrder().addToTotalRetailAmount(this.getQuantitySold().doubleValue() * this.getRetailPrice().doubleValue());
    }
    // endregion

    //region > removing
    public void removing() {
        //System.out.println("Borre Detalle");
        this.getCustomerOrder().addToTotalRetailAmount(-this.getQuantitySold().doubleValue() * this.getRetailPrice().doubleValue());
    }
    // endregion


    //region > version (derived property)
    @MemberOrder(sequence = "1000")
    public Long getVersionSequence() {
        return (Long) JDOHelper.getVersion(this);
    }
    //endregion

    //region > compareTo
    @Override
    public int compareTo(final CustomerOrderItem other) {
        return ObjectContracts.compare(this, other, "customerOrder", "item");
    }
    //endregion

    //region > delete (action)
    public static class DeletedEvent extends CustomerOrderItem.ActionDomainEvent {
        private static final long serialVersionUID = 1L;
        public DeletedEvent(
                final CustomerOrderItem source,
                final Identifier identifier,
                final Object... arguments) {
            super(source, identifier, arguments);
        }
    }


    public List<CustomerOrderItem> delete() {

        // obtain title first, because cannot reference object after deleted
        final String title = container.titleOf(this);

        final List<CustomerOrderItem> returnList = null;
        container.removeIfNotAlready(this);
        container.informUser(
                TranslatableString.tr("Deleted {title}", "title", title), this.getClass(), "delete");

        return returnList;
    }
    //endregion

    //region > events

    public static abstract class PropertyDomainEvent<T> extends DomainAppDomainModule.PropertyDomainEvent<CustomerOrderItem, T> {

        public PropertyDomainEvent(final CustomerOrderItem source, final Identifier identifier) {
            super(source, identifier);
        }

        public PropertyDomainEvent(final CustomerOrderItem source, final Identifier identifier, final T oldValue, final T newValue) {
            super(source, identifier, oldValue, newValue);
        }
    }

    public static abstract class ActionDomainEvent extends DomainAppDomainModule.ActionDomainEvent<CustomerOrderItem> {
        private static final long serialVersionUID = 1L;
        public ActionDomainEvent(
                final CustomerOrderItem source,
                final Identifier identifier,
                final Object... arguments) {
            super(source, identifier, arguments);
        }
    }
    //endregion


    //region > Total (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            contributed = Contributed.AS_NEITHER,
            hidden = Where.EVERYWHERE
    )
    public BigDecimal total(BigDecimal quantitySold, BigDecimal retailPrice) {
        return (BigDecimal.valueOf(quantitySold.doubleValue()* retailPrice.doubleValue()));
    }
    // endregion




    //region > injected services

    @Inject
    DomainObjectContainer container;

    //endregion



}
