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
package domainapp.app;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.apache.isis.applib.AppManifest;
import org.apache.isis.applib.fixturescripts.FixtureScript;

import domainapp.dom.DomainAppDomainModule;
import domainapp.fixture.DomainAppFixtureModule;

/**
 * Bootstrap the application.
 */
public class DomainAppAppManifest implements AppManifest {

    /**
     * Load all services and entities found in (the packages and subpackages within) these modules
     */
    @Override
    public List<Class<?>> getModules() {
        return Arrays.asList(
                org.isisaddons.wicket.gmap3.cpt.service.Gmap3ServiceModule.class,// to enable Isis gmap3 add-on module using google maps with LocationLookupService
                org.isisaddons.module.security.SecurityModule.class, // to enable Isis security add-on module
                org.isisaddons.module.excel.ExcelModule.class, // to enable Isis Excel import export module
                DomainAppDomainModule.class,  // domain (entities and repositories)
                DomainAppFixtureModule.class, // fixtures
                DomainAppAppModule.class      // home page service etc
        );
    }

    /**
     * No additional services.
     */
    @Override
    public List<Class<?>> getAdditionalServices() {
        return Arrays.asList(
                org.isisaddons.module.security.dom.password.PasswordEncryptionServiceUsingJBcrypt.class,
                org.isisaddons.module.security.dom.permission.PermissionsEvaluationServiceAllowBeatsVeto.class
        );
    }

    /**
     * Use shiro for authentication.
     *
     * <p>
     *     NB: this is ignored for integration tests, which always use "bypass".
     * </p>
     */
    @Override
    public String getAuthenticationMechanism() {
        return "bypass";
    }

    /**
     * Use shiro for authorization.
     *
     * <p>
     *     NB: this is ignored for integration tests, which always use "bypass".
     * </p>
     */
    @Override
    public String getAuthorizationMechanism() {
        return "bypass";
    }

    /**
     * No fixtures.
     */
    @Override
    public List<Class<? extends FixtureScript>> getFixtures() {
        return Collections.emptyList();
    }

    /**
     * No overrides.
     */
    @Override
    public Map getConfigurationProperties() {
        return ImmutableMap.of(
                "isis.reflector.facets.include", "org.isisaddons.module.security.facets.TenantedAuthorizationFacetFactory");
    }

}
