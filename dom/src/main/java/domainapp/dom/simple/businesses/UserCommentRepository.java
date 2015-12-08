package domainapp.dom.simple.businesses;

import java.math.BigDecimal;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.util.ObjectContracts;
import org.isisaddons.wicket.gmap3.cpt.service.LocationLookupService;

import domainapp.dom.simple.businesses.UserCommentRepository.CreateDomainEvent;
import domainapp.dom.simple.customers.CustomerSubscription;
import domainapp.dom.simple.systementities.AutoNumberRepository;


@DomainService(repositoryFor = UserComment.class, nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)



public class UserCommentRepository {
	

	    //region > create (action)
	    public static class CreateDomainEvent extends ActionDomainEvent<UserCommentRepository> {
	        public CreateDomainEvent(final UserCommentRepository source, final Identifier identifier, final Object... arguments) {
	            super(source, identifier, arguments);
	        }
	    }

	    @Action(
	            domainEvent = CreateDomainEvent.class
	    )
	    
	    @MemberOrder(name="userComments",sequence = "4")
	    public UserComment addUserComment(
	    		final @ParameterLayout(named="BusinessLocation") BusinessLocation businessLocation,
	            final @ParameterLayout(named="CustomerSubscription") CustomerSubscription customerSubscription,
	            final @ParameterLayout(named="Title") String title,
	            final @ParameterLayout(named="Comment") String comment,
	            final @ParameterLayout(named="Rating", multiLine = 5) BigDecimal rating)
	    {
	        final UserComment userComment = container.newTransientInstance(UserComment.class);
	        //Obtain automatic User Comment commentId
	        autoNumberRepository = new AutoNumberRepository();
	        autoNumberRepository.setContainer(container);
	        long commentId = autoNumberRepository.nextAutoNumber("USER COMMENT", null);
	        userComment.setCommentId(commentId);
	        userComment.setBusinessLocation(businessLocation);
	        userComment.setCustomerSubscription(customerSubscription);
	        userComment.setTitle(title);
	        userComment.setComment(comment);
	        userComment.setRating(rating);
	        container.persistIfNotAlready(userComment);
	        return userComment;
	    }

	    //endregion

	    //region > injected services

	    @javax.inject.Inject
	    DomainObjectContainer container;

	    AutoNumberRepository autoNumberRepository;

	    
	    //endregion


}
