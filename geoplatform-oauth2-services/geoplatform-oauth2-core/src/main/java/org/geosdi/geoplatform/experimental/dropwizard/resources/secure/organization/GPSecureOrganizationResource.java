/*
 *  geo-platform
 *  Rich webgis framework
 *  http://geo-platform.org
 * ====================================================================
 *
 * Copyright (C) 2008-2014 geoSDI Group (CNR IMAA - Potenza - ITALY).
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. This program is distributed in the 
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 * A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. You should have received a copy of the GNU General 
 * Public License along with this program. If not, see http://www.gnu.org/licenses/ 
 *
 * ====================================================================
 *
 * Linking this library statically or dynamically with other modules is 
 * making a combined work based on this library. Thus, the terms and 
 * conditions of the GNU General Public License cover the whole combination. 
 * 
 * As a special exception, the copyright holders of this library give you permission 
 * to link this library with independent modules to produce an executable, regardless 
 * of the license terms of these independent modules, and to copy and distribute 
 * the resulting executable under terms of your choice, provided that you also meet, 
 * for each linked independent module, the terms and conditions of the license of 
 * that module. An independent module is a module which is not derived from or 
 * based on this library. If you modify this library, you may extend this exception 
 * to your version of the library, but you are not obligated to do so. If you do not 
 * wish to do so, delete this exception statement from your version. 
 *
 */
package org.geosdi.geoplatform.experimental.dropwizard.resources.secure.organization;

import io.dropwizard.auth.Auth;
import java.security.Principal;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.geosdi.geoplatform.core.model.GPOrganization;
import org.geosdi.geoplatform.services.rs.path.GPServiceRSPathConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
@Path(value = GPServiceRSPathConfig.GP_SECURE_ORGANIZATION_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Component(value = "secureOrganizationResource")
public class GPSecureOrganizationResource extends BaseOrganizationResource {

    private static final Logger logger = LoggerFactory.getLogger(
            GPSecureOrganizationResource.class);

    @POST
    @Path(value = GPServiceRSPathConfig.INSERT_ORGANIZATION_PATH)
    @Override
    public Long insertOrganization(@Auth Principal principal,
            GPOrganization organization) throws Exception {
        logger.debug("\n\n@@@@@@@@@@@@@@Executing secure insertOrganization - "
                + "Principal : {}\n\n", principal.getName());
        return super.insertOrganization(organization);
    }

    @Path(value = GPServiceRSPathConfig.DELETE_ORGANIZATION_PATH)
    @DELETE
    @Override
    public Boolean deleteOrganization(@Auth Principal principal,
            @PathParam(value = "organizationID") Long organizationID)
            throws Exception {
        logger.debug("\n\n@@@@@@@@@@@@@@Executing secure deleteOrganization - "
                + "Principal : {}\n\n", principal.getName());
        return super.deleteOrganization(organizationID);
    }
}
