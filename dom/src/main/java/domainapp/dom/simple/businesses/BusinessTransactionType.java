package domainapp.dom.simple.businesses;

import org.apache.isis.applib.annotation.*;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

/**
 * Created by Administrator on 10/12/2015.
 */

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "BusinessTransactionType"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findAllBusinessTransactionTypes", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.BusinessTransactionType "),
        @javax.jdo.annotations.Query(
                name = "findBusinessTransactionTypesByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.BusinessTransactionType "
                        + "WHERE name.indexOf(:name) >= 0 "),
        @javax.jdo.annotations.Query(
                name = "findBusinessTransactionTypesById", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.BusinessTransactionType "
                        + "WHERE businessTransactionTypeId.indexOf(:subscriptionTypeId) >= 0 ")
})
@javax.jdo.annotations.Unique(name="BusinessTransactionType_Id_UNQ", members = {"businessTransactionTypeId"})
@DomainObject(autoCompleteAction = "findBusinessTransactionTypesByName", autoCompleteRepository = BusinessTransactionTypeRepository.class)

@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-flag"
)
public class BusinessTransactionType {


    //region > ID (property)
    private String businessTransactionTypeId;

    @javax.jdo.annotations.Column(allowsNull="false", length = 10)
    @MemberOrder(sequence = "1")
    public String getBusinessTransactionTypeId() {
        return businessTransactionTypeId;
    }
    public void setBusinessTransactionTypeId(final String businessTransactionTypeId) {
        this.businessTransactionTypeId = businessTransactionTypeId;
    }
    // endregion

    //region > name (property)
    private String name;

    @javax.jdo.annotations.Column(allowsNull="false", length = 40)
    @Title(sequence="1")
    @MemberOrder(sequence = "2")
    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }
    // endregion

    //region > name (property)
    private String description;

    @javax.jdo.annotations.Column(allowsNull="true", length = 300)
    @MemberOrder(sequence = "3")
    @PropertyLayout(multiLine = 3)
    public String getDescription() {
        return description;
    }
    public void setDescription(final String description) {
        this.description = description;
    }
    // endregion

    //region > transactionClass (property)
    private TransactionClass transactionClass;

    @javax.jdo.annotations.Column(allowsNull="false")
    @MemberOrder(sequence = "4")
    public TransactionClass getTransactionClass() {
        return transactionClass;
    }
    public void setTransactionClass(final TransactionClass transactionClass) {
        this.transactionClass = transactionClass;
    }
    // endregion


}
