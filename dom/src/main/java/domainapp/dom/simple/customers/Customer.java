package domainapp.dom.simple.customers;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.util.ObjectContracts;
import org.joda.time.DateTime;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Administrator on 10/12/2015.
 */

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "Customer"
)

@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findAllCustomers", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.Customer "),
        @javax.jdo.annotations.Query(
                name = "findCustomersByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.Customer "
                        + "WHERE lastName.indexOf(:lastName) >= 0 "),
        @javax.jdo.annotations.Query(
                name = "findCustomerById", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.Customer "
                        + "WHERE customerId.indexOf(:customerId) >= 0 ")
})

@javax.jdo.annotations.Unique(name="Customer_Id_UNQ", members = {"customerId"})

@DomainObject(autoCompleteAction = "findCustomersByName", autoCompleteRepository = CustomerRepository.class,auditing= Auditing.ENABLED )

@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-male"
)

public class Customer implements Comparable<Customer>{

    //region > ID (property)
    private String customerId;

    @Column(allowsNull="false", length = 10)
    @MemberOrder(sequence = "1")

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(final String customerId) {
        this.customerId = customerId;
    }

    // endregion

    //region > firstName (property)
    private String firstName;

    @Column(allowsNull="false", length = 20)
    @Title(sequence="1")
    @MemberOrder(sequence = "2")

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    // endregion

    //region > middleName (property)
    private String middleName;

    @Column(allowsNull="true", length = 20)
    @Title(sequence="2")
    @MemberOrder(sequence = "2")

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }
    // endregion

    //region > lastName (property)
    private String lastName;

    @Column(allowsNull="false", length = 20)
    @Title(sequence="3")
    @MemberOrder(sequence = "2")

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    // endregion

    //region > lastName2 (property)
    private String lastName2;

    @Column(allowsNull="true", length = 20)
    @Title(sequence="4")
    @MemberOrder(sequence = "2")

    public String getLastName2() {
        return lastName2;
    }

    public void setLastName2(final String lastName2) {
        this.lastName2 = lastName2;
    }
    // endregion

    //region > creationTime (Property)
    private DateTime creationTime;

    @Column(allowsNull = "false")
    @MemberOrder(name="Status",sequence = "4")
    public DateTime getCreationTime() {
        return creationTime;
    }
    public void setCreationTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }
    //endregion

    //region > customerSubscription (collection)
    @Persistent(mappedBy = "customer", dependentElement = "false")
    private SortedSet<CustomerSubscription> customerSubscriptions = new TreeSet<CustomerSubscription>();

    @CollectionLayout(render = RenderType.EAGERLY)
    @MemberOrder(sequence = "100")
    public SortedSet<CustomerSubscription> getCustomerSubscriptions() {
        return customerSubscriptions;
    }

    public void setCustomerSubscriptions(final SortedSet<CustomerSubscription> customerSubscriptions) {
        this.customerSubscriptions = customerSubscriptions;
    }
    //endregion


    //region > compareTo
    @Override
    public int compareTo(final Customer other) {
        return ObjectContracts.compare(this, other, "customerId");
    }
    //endregion

}
