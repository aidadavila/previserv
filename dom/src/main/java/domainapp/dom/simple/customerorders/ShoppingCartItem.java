package domainapp.dom.simple.customerorders;

import java.math.BigDecimal;
import java.util.List;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

import domainapp.dom.DomainAppDomainModule;
import domainapp.dom.simple.businesses.BusinessLocation;
import domainapp.dom.simple.items.Item;

/**
 * Created by Administrator on 10/16/2015.
 */

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "ShoppingCartItem"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")

@javax.jdo.annotations.Queries({
@javax.jdo.annotations.Query(
        name = "findShoppingCartItemsGroupedByLocation", language = "JDOQL",
        value = "SELECT "
                + "FROM domainapp.dom.simple.customerorders.ShoppingCartItem "
                + "WHERE shoppingCart == parShoppingCart PARAMETERS ShoppingCart parShoppingCart")
})

@javax.jdo.annotations.Unique(name="Shopping_Cart_Item_Id_UNQ", members = {"shoppingCart","item","businessLocation"})
@DomainObject(auditing= Auditing.ENABLED )

@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-shopping-cart"
)

public class ShoppingCartItem implements Comparable<ShoppingCartItem>{

    //region shoppingCart (property)
    private ShoppingCart shoppingCart;

    @MemberOrder(sequence = "1")
    @Column(allowsNull = "false")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Title(sequence = "1")
    @Persistent

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(final ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
    //endregion

    //region item (property)
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

    //region businessLocation (property)
    public BusinessLocation businessLocation;

    @Property(editing = Editing.DISABLED)
    @MemberOrder(sequence = "3")
    // @Column(allowsNull = "false")
    @Column(allowsNull = "true")
    @PropertyLayout(hidden = Where.REFERENCES_PARENT)
    @Persistent
    public BusinessLocation getBusinessLocation() {
        return businessLocation;
    }

    public void setBusinessLocation(final BusinessLocation businessLocation) {
        this.businessLocation = businessLocation;
    }
    //endregion

    //region quantityOrdered (property)
    private BigDecimal quantityOrdered;

    @Column(allowsNull="false", length=8, scale=0, defaultValue = "0")
    @MemberOrder(sequence = "4")
    public BigDecimal getQuantityOrdered() {return quantityOrdered;}
    public void setQuantityOrdered(BigDecimal quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }
    public String validateQuantityOrdered(BigDecimal quantityOrdered) {
        return quantityOrdered.compareTo(BigDecimal.ZERO) < 0
                ? "Quantity Ordered cannot be negative"
                : null;
    }
    public void modifyQuantityOrdered(BigDecimal quantityOrdered){
        this.getShoppingCart().addToTotalRetailAmount((quantityOrdered.doubleValue() - this.getQuantityOrdered().doubleValue()) * this.getRetailPrice().doubleValue());
        this.setQuantityOrdered(quantityOrdered);
    }
    //endregion

    //region retailPrice (property)
    private BigDecimal retailPrice;

    @Property(editing = Editing.DISABLED)
    @Column(allowsNull="false", length=8, scale=2)
    @MemberOrder(sequence = "4")
    public BigDecimal getRetailPrice() {return retailPrice;}
    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }
    //endregion

    //region retailPriceDiscount (property)
    private BigDecimal retailPriceDiscount;

    @Property(editing = Editing.DISABLED)
    @Column(allowsNull="false", length=8, scale=2, defaultValue = "0")
    @MemberOrder(sequence = "4")
    public BigDecimal getRetailPriceDiscount() {return retailPriceDiscount;}
    public void setRetailPriceDiscount(BigDecimal retailPriceDiscount) {
        this.retailPriceDiscount = retailPriceDiscount;
    }
    //endregion

    //region taxAmount (property)
    private BigDecimal taxAmount;

    @Property(editing = Editing.DISABLED)
    @Column(allowsNull="false", length=8, scale=2, defaultValue = "0")
    @MemberOrder(sequence = "4")
    public BigDecimal getTaxAmount() {return taxAmount;}
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
    //endregion
    
  //region > persisted
    public void persisted() {
        //System.out.println("Grabe Detalle");
        this.getShoppingCart().addToTotalRetailAmount(this.getQuantityOrdered().doubleValue() * this.getRetailPrice().doubleValue());
    }
    // endregion

    //region > removing
    public void removing() {
        //System.out.println("Borre Detalle");
        this.getShoppingCart().addToTotalRetailAmount(-this.getQuantityOrdered().doubleValue() * this.getRetailPrice().doubleValue());
    }
    // endregion


    //region > delete (events)
    public static class DeletedEvent extends ShoppingCartItem.ActionDomainEvent {
        private static final long serialVersionUID = 1L;
        public DeletedEvent(
                final ShoppingCartItem source,
                final Identifier identifier,
                final Object... arguments) {
            super(source, identifier, arguments);
        }
    }


    public List<ShoppingCartItem> delete() {

        // obtain title first, because cannot reference object after deleted
        final String title = container.titleOf(this);

        final List<ShoppingCartItem> returnList = null;
        container.removeIfNotAlready(this);
        container.informUser(
                TranslatableString.tr("Deleted {title}", "title", title), this.getClass(), "delete");

        return returnList;
    }
    //endregion

    //region > events
    public static abstract class PropertyDomainEvent<T> extends DomainAppDomainModule.PropertyDomainEvent<ShoppingCartItem, T> {

        public PropertyDomainEvent(final ShoppingCartItem source, final Identifier identifier) {
            super(source, identifier);
        }

        public PropertyDomainEvent(final ShoppingCartItem source, final Identifier identifier, final T oldValue, final T newValue) {
            super(source, identifier, oldValue, newValue);
        }
    }

    public static abstract class ActionDomainEvent extends DomainAppDomainModule.ActionDomainEvent<ShoppingCartItem> {
        private static final long serialVersionUID = 1L;
        public ActionDomainEvent(
                final ShoppingCartItem source,
                final Identifier identifier,
                final Object... arguments) {
            super(source, identifier, arguments);
        }
    }
    //endregion

    

    //region > compareTo
    @Override
    public int compareTo(final ShoppingCartItem other) {
        return ObjectContracts.compare(this, other, "shoppingCart","item","businessLocation");
    }
    //endregion
    
    //region > injected services
    @javax.inject.Inject
    DomainObjectContainer container;
    //endregion

}
