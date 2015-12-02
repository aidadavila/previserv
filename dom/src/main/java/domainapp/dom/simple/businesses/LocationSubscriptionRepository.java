/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package domainapp.dom.simple.businesses;

import domainapp.dom.simple.applications.Application;
import domainapp.dom.simple.applications.SubscriptionStatus;
import domainapp.dom.simple.applications.SubscriptionType;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;

import javax.inject.Inject;
import java.util.List;

@DomainService(repositoryFor = LocationSubscription.class,
            nature = NatureOfService.VIEW_CONTRIBUTIONS_ONLY)
@DomainServiceLayout(menuOrder = "60")
public class LocationSubscriptionRepository {

    //region > title
    public TranslatableString title() {
        return TranslatableString.tr("Location Subscriptions");
    }
    //endregion


    //region > listAll (action)
    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<LocationSubscription> listAllLocationSubscriptions() {
        return container.allInstances(LocationSubscription.class);
    }
    //endregion



    //region > addSubscription (action)
    public static class CreateDomainEvent extends ActionDomainEvent<LocationSubscriptionRepository> {
        public CreateDomainEvent(final LocationSubscriptionRepository source, final Identifier identifier, final Object... arguments) {
            super(source, identifier, arguments);
        }
    }

    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(name = "locationSubscriptions",sequence = "2")
    public BusinessLocation addSubscription(
            final @ParameterLayout(named="Business Location") BusinessLocation businessLocation,
            final @ParameterLayout(named="Application") Application application,
            final @ParameterLayout(named="Subscription Type") SubscriptionType subscriptionType){
        final LocationSubscription obj = container.newTransientInstance(LocationSubscription.class);
        obj.setBusinessLocation(businessLocation);
        obj.setApplication(application);
        obj.setSubscriptionType(subscriptionType);
        obj.setSubscriptionTime(clockService.nowAsDateTime());
        obj.setStatus(SubscriptionStatus.ACTIVE);
        container.persistIfNotAlready(obj);
        return obj.getBusinessLocation();
    }

    //endregion

    //region > injected services

    @Inject
    DomainObjectContainer container;

    @Inject
    private ClockService clockService;

    //endregion
}
