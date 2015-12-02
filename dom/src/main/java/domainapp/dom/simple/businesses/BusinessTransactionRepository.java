package domainapp.dom.simple.businesses;

import com.google.common.base.Predicate;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;

import java.util.List;

/**
 * Created by Administrator on 10/12/2015.
 */

@DomainService(repositoryFor = BusinessLocation.class, nature = NatureOfService.DOMAIN)
@DomainServiceLayout(named = "Businesses",menuOrder = "60")
public class BusinessTransactionRepository {

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(contributed = Contributed.AS_NEITHER)
    @CollectionLayout(render = RenderType.EAGERLY)
    public List<BusinessTransaction> businessTransactionsByBusiness(Business business){
        return container.allMatches(BusinessTransaction.class, new Predicate<BusinessTransaction>() {
            @Override public boolean apply(final BusinessTransaction input) {
                return input.getBusiness().equals(business);
            }
        });
    }

    //region > injected services

    @javax.inject.Inject
    DomainObjectContainer container;

    //endregion


}
