package domainapp.dom.simple.financials;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.apache.isis.applib.annotation.*;

import javax.jdo.JDOHelper;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

/**
 * Created by Administrator on 10/1/2015.
 */

@javax.jdo.annotations.PersistenceCapable(
        identityType= IdentityType.DATASTORE,
        schema = "simple",
        table = "FinancialInst"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy= VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findAllFinancialInst", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.FinancialInst "),
        @javax.jdo.annotations.Query(
                name = "findFinancialInstByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.FinancialInst "
                        + "WHERE name.indexOf(:name) >= 0 "),
        @javax.jdo.annotations.Query(
                name = "findFinancilInstById", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.FinancialInst "
                        + "WHERE financialInstId.indexOf(:financialInstId) >= 0 ")
})
@javax.jdo.annotations.Unique(name="FinancialInst_Id_UNQ", members = {"financialInstId"})
@DomainObject(autoCompleteAction = "findFinancialInstByName", autoCompleteRepository = FinancialInstRepository.class, auditing= Auditing.ENABLED)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT,
        cssClassFa = "fa-university"

)
public class FinancialInst {

    //region > ID (property)


    private String financialInstId;

    @javax.jdo.annotations.Column(allowsNull="false", length = 10)


    public String getFinancialInstId() {
        return financialInstId;
    }

    public void setFinancialInstId(final String financialInstId) {
        this.financialInstId = financialInstId;
    }

    // endregion

    //region > name (property)

    private String name;

    @javax.jdo.annotations.Column(allowsNull="false", length = 40)
    @Title(sequence="1")

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    // endregion


    //region > version (derived property)
    public Long getVersionSequence() {
        return (Long) JDOHelper.getVersion(this);
    }
    //endregion

}
