package domainapp.dom.simple.businesses;

import java.math.BigDecimal;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Auditing;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;

import domainapp.dom.simple.customers.CustomerSubscription;

/**
 * @author cesar
 *
 */
@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "simple",
        table = "UserComment"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
        column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "findAllUserComments", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.UserComment "),
        @javax.jdo.annotations.Query(
                name = "findUserCommentsByComment", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.UserComment "
                        + "WHERE comment.indexOf(:comment) >= 0 "),
        @javax.jdo.annotations.Query(
                name = "findUserCommentById", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.modules.simple.UserComment "
                        + "WHERE commentId.indexOf(:commentId) >= 0 ")
})
@javax.jdo.annotations.Unique(name="UserComment_Id_UNQ", members = {"commentId"})
@DomainObject(autoCompleteAction = "findUserCommentsByComment", autoCompleteRepository = UserCommentRepository.class,auditing=Auditing.ENABLED )

@DomainObjectLayout(
        bookmarking = BookmarkPolicy.NEVER,
        cssClassFa = "fa-comment-o"
)


public class UserComment implements Comparable<UserComment> {
	

	private Long commentId;

    @Column(allowsNull="false")
    @MemberOrder(sequence = "1")
	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}
	
	private BusinessLocation businessLocation;
	
	@MemberOrder(sequence = "2")
	@Column(allowsNull = "false")
	@PropertyLayout(hidden = Where.REFERENCES_PARENT)
	@Persistent
	public BusinessLocation getBusinessLocation() {
		return businessLocation;
	}

	public void setBusinessLocation(BusinessLocation businessLocation) {
		this.businessLocation = businessLocation;
	}

	private CustomerSubscription customerSubscription;

	@MemberOrder(sequence = "3")
	@Column(allowsNull = "false")
	@PropertyLayout(hidden = Where.REFERENCES_PARENT)
	@Persistent
	
	public CustomerSubscription getCustomerSubscription() {
		return customerSubscription;
	}

	public void setCustomerSubscription(CustomerSubscription customerSubscription) {
		this.customerSubscription = customerSubscription;
	}

	private String title;
	
    @Column(allowsNull="false", length = 40)
    @Title(sequence="1")
    @MemberOrder(name="Rating",sequence = "4")
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private String comment;
		
    @Column(allowsNull="false", length = 500)
    @MemberOrder(name="Rating",sequence = "4")
    @PropertyLayout(multiLine = 5)
    
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	private BigDecimal rating;

    @Column(allowsNull="false")
    @MemberOrder(name="Rating",sequence = "5")
	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}
	
	
	
    //region > compareTo

    @Override
    public int compareTo(final UserComment other) {
        return ObjectContracts.compare(this, other, "commentId");
    }

    //endregion

    
	
	

}
