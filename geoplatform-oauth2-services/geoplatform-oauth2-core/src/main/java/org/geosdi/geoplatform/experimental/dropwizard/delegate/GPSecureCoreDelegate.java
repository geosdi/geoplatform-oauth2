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
package org.geosdi.geoplatform.experimental.dropwizard.delegate;

import java.util.List;
import org.geosdi.geoplatform.core.delegate.api.account.AccountDelegate;
import org.geosdi.geoplatform.core.delegate.api.acl.AclDelegate;
import org.geosdi.geoplatform.core.delegate.api.folder.FolderDelegate;
import org.geosdi.geoplatform.core.delegate.api.layer.LayerDelegate;
import org.geosdi.geoplatform.core.delegate.api.message.MessageDelegate;
import org.geosdi.geoplatform.core.delegate.api.organization.OrganizationDelegate;
import org.geosdi.geoplatform.core.delegate.api.project.ProjectDelegate;
import org.geosdi.geoplatform.core.delegate.api.server.ServerDelegate;
import org.geosdi.geoplatform.core.delegate.api.viewport.ViewportDelegate;
import org.geosdi.geoplatform.core.model.GPAccountProject;
import org.geosdi.geoplatform.core.model.GPBBox;
import org.geosdi.geoplatform.core.model.GPFolder;
import org.geosdi.geoplatform.core.model.GPLayerInfo;
import org.geosdi.geoplatform.core.model.GPMessage;
import org.geosdi.geoplatform.core.model.GPOrganization;
import org.geosdi.geoplatform.core.model.GPProject;
import org.geosdi.geoplatform.core.model.GPRasterLayer;
import org.geosdi.geoplatform.core.model.GPUser;
import org.geosdi.geoplatform.core.model.GPVectorLayer;
import org.geosdi.geoplatform.core.model.GPViewport;
import org.geosdi.geoplatform.core.model.GeoPlatformServer;
import org.geosdi.geoplatform.exception.AccountLoginFault;
import org.geosdi.geoplatform.exception.IllegalParameterFault;
import org.geosdi.geoplatform.exception.ResourceNotFoundFault;
import org.geosdi.geoplatform.gui.shared.GPLayerType;
import org.geosdi.geoplatform.request.InsertAccountRequest;
import org.geosdi.geoplatform.request.PaginatedSearchRequest;
import org.geosdi.geoplatform.request.PutAccountsProjectRequest;
import org.geosdi.geoplatform.request.RequestByAccountProjectIDs;
import org.geosdi.geoplatform.request.SearchRequest;
import org.geosdi.geoplatform.request.folder.InsertFolderRequest;
import org.geosdi.geoplatform.request.folder.WSAddFolderAndTreeModificationsRequest;
import org.geosdi.geoplatform.request.folder.WSDDFolderAndTreeModifications;
import org.geosdi.geoplatform.request.folder.WSDeleteFolderAndTreeModifications;
import org.geosdi.geoplatform.request.layer.InsertLayerRequest;
import org.geosdi.geoplatform.request.layer.WSAddLayerAndTreeModificationsRequest;
import org.geosdi.geoplatform.request.layer.WSAddLayersAndTreeModificationsRequest;
import org.geosdi.geoplatform.request.layer.WSDDLayerAndTreeModificationsRequest;
import org.geosdi.geoplatform.request.layer.WSDeleteLayerAndTreeModificationsRequest;
import org.geosdi.geoplatform.request.message.MarkMessageReadByDateRequest;
import org.geosdi.geoplatform.request.organization.WSPutRolePermissionRequest;
import org.geosdi.geoplatform.request.organization.WSSaveRoleRequest;
import org.geosdi.geoplatform.request.project.ImportProjectRequest;
import org.geosdi.geoplatform.request.project.SaveProjectRequest;
import org.geosdi.geoplatform.request.server.WSSaveServerRequest;
import org.geosdi.geoplatform.request.viewport.InsertViewportRequest;
import org.geosdi.geoplatform.request.viewport.ManageViewportRequest;
import org.geosdi.geoplatform.response.AccountProjectPropertiesDTO;
import org.geosdi.geoplatform.response.FolderDTO;
import org.geosdi.geoplatform.response.GetDataSourceResponse;
import org.geosdi.geoplatform.response.MessageDTO;
import org.geosdi.geoplatform.response.ProjectDTO;
import org.geosdi.geoplatform.response.RasterPropertiesDTO;
import org.geosdi.geoplatform.response.SearchUsersResponseWS;
import org.geosdi.geoplatform.response.ServerDTO;
import org.geosdi.geoplatform.response.ServerDTOContainer;
import org.geosdi.geoplatform.response.ShortAccountDTOContainer;
import org.geosdi.geoplatform.response.ShortLayerDTO;
import org.geosdi.geoplatform.response.ShortLayerDTOContainer;
import org.geosdi.geoplatform.response.UserDTO;
import org.geosdi.geoplatform.response.WSGetAccountProjectsResponse;
import org.geosdi.geoplatform.response.authority.GetAuthoritiesResponseWS;
import org.geosdi.geoplatform.response.authority.GetAuthorityResponse;
import org.geosdi.geoplatform.response.collection.ChildrenFolderStore;
import org.geosdi.geoplatform.response.collection.GuiComponentsPermissionMapData;
import org.geosdi.geoplatform.response.collection.LongListStore;
import org.geosdi.geoplatform.response.collection.TreeFolderElementsStore;
import org.geosdi.geoplatform.response.message.GetMessageResponse;
import org.geosdi.geoplatform.response.role.WSGetRoleResponse;
import org.geosdi.geoplatform.response.viewport.WSGetViewportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Giuseppe La Scaleia - CNR IMAA geoSDI Group
 * @email giuseppe.lascaleia@geosdi.org
 */
@Transactional // Give atomicity on DropWizard RS Resources
@Component(value = "gpSecureCoreDelegate")
public class GPSecureCoreDelegate implements SecureCoreDelegate {

    @Autowired
    private AccountDelegate gpAccountDelegate;
    @Autowired
    private AclDelegate gpAclDelegate;
    @Autowired
    private FolderDelegate gpFolderDelegate;
    @Autowired
    private LayerDelegate gpLayerDelegate;
    @Autowired
    private MessageDelegate gpMessageDelegate;
    @Autowired
    private OrganizationDelegate gpOrganizationDelegate;
    @Autowired
    private ProjectDelegate gpProjectDelegate;
    @Autowired
    private ServerDelegate gpServerDelegate;
    @Autowired
    private ViewportDelegate gpViewportDelegate;

    // <editor-fold defaultstate="collapsed" desc="Organization">
    // =========================================================================
    // === Organization
    // =========================================================================
    @Override
    public Long insertOrganization(GPOrganization organization) throws
            IllegalParameterFault {
        return gpOrganizationDelegate.insertOrganization(organization);
    }

    @Override
    public Boolean deleteOrganization(Long organizationID) throws
            ResourceNotFoundFault {
        return gpOrganizationDelegate.deleteOrganization(organizationID);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Account (User and Application)">
    // =========================================================================
    // === Account
    // =========================================================================
    @Override
    public Long insertAccount(InsertAccountRequest insertAccountRequest)
            throws IllegalParameterFault {
        return gpAccountDelegate.insertAccount(insertAccountRequest);
    }

    @Override
    public Long updateUser(GPUser user) throws ResourceNotFoundFault,
            IllegalParameterFault {
        return gpAccountDelegate.updateUser(user);
    }

    @Override
    public Boolean deleteAccount(Long accountID) throws ResourceNotFoundFault {
        return gpAccountDelegate.deleteAccount(accountID);
    }

    @Override
    public GPUser getUserDetail(Long userID) throws ResourceNotFoundFault {
        return gpAccountDelegate.getUserDetail(userID);
    }

    @Override
    public GPUser getUserDetailByUsername(SearchRequest request)
            throws ResourceNotFoundFault {
        return gpAccountDelegate.getUserDetailByUsername(request);
    }

    @Override
    public GPUser getUserDetailByUsernameAndPassword(String username,
            String plainPassword)
            throws ResourceNotFoundFault, IllegalParameterFault,
            AccountLoginFault {
        return gpAccountDelegate.getUserDetailByUsernameAndPassword(
                username, plainPassword);
    }

    @Override
    public UserDTO getShortUser(Long userID) throws ResourceNotFoundFault {
        return gpAccountDelegate.getShortUser(userID);
    }

    @Override
    public UserDTO getShortUserByUsername(SearchRequest request)
            throws ResourceNotFoundFault {
        return gpAccountDelegate.getShortUserByUsername(request);
    }

    @Override
    public SearchUsersResponseWS searchUsers(Long userID,
            PaginatedSearchRequest request) throws ResourceNotFoundFault {
        return gpAccountDelegate.searchUsers(userID, request);
    }

    @Override
    public ShortAccountDTOContainer getAllAccounts() {
        return gpAccountDelegate.getAllAccounts();
    }

    @Override
    public ShortAccountDTOContainer getAccounts(String organization)
            throws ResourceNotFoundFault {
        return gpAccountDelegate.getAccounts(organization);
    }

    @Override
    public Long getAccountsCount(SearchRequest request) {
        return gpAccountDelegate.getAccountsCount(request);
    }

    @Override
    public Long getUsersCount(String organization,
            SearchRequest request) {
        return gpAccountDelegate.getUsersCount(organization, request);
    }

    @Override
    public GetAuthorityResponse getAuthorities(Long accountID)
            throws ResourceNotFoundFault {
        return gpAccountDelegate.getAuthorities(accountID);
    }

    @Override
    public GetAuthoritiesResponseWS getAuthoritiesDetail(String accountNaturalID)
            throws ResourceNotFoundFault {
        return gpAccountDelegate.getAuthoritiesDetail(accountNaturalID);
    }

    @Override
    public void forceTemporaryAccount(Long accountID)
            throws ResourceNotFoundFault {
        gpAccountDelegate.forceTemporaryAccount(accountID);
    }

    @Override
    public void forceExpiredTemporaryAccount(Long accountID)
            throws ResourceNotFoundFault, IllegalParameterFault {
        gpAccountDelegate.forceExpiredTemporaryAccount(accountID);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="AccountProject">
    // =========================================================================
    // === AccountProject
    // =========================================================================
    @Override
    public Long insertAccountProject(GPAccountProject accountProject) throws
            IllegalParameterFault {
        return gpProjectDelegate.insertAccountProject(accountProject);
    }

    @Override
    public Long updateAccountProject(GPAccountProject accountProject)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpProjectDelegate.updateAccountProject(accountProject);
    }

    @Override
    public Boolean deleteAccountProject(Long accountProjectID) throws
            ResourceNotFoundFault {
        return gpProjectDelegate.deleteAccountProject(accountProjectID);
    }

    @Override
    public GPAccountProject getAccountProject(Long accountProjectID) throws
            ResourceNotFoundFault {
        return gpProjectDelegate.getAccountProject(accountProjectID);
    }

    @Override
    public WSGetAccountProjectsResponse getAccountProjectsByAccountID(
            Long accountID) {
        return gpProjectDelegate.getAccountProjectsByAccountID(accountID);
    }

    @Override
    public WSGetAccountProjectsResponse getAccountProjectsByProjectID(
            Long projectID) {
        return gpProjectDelegate.getAccountProjectsByProjectID(projectID);
    }

    @Override
    public GPAccountProject getAccountProjectByAccountAndProjectIDs(
            Long accountID,
            Long projectID)
            throws ResourceNotFoundFault {
        return gpProjectDelegate.getAccountProjectByAccountAndProjectIDs(
                accountID, projectID);
    }

    @Override
    public Long getAccountProjectsCount(Long accountID,
            SearchRequest request)
            throws ResourceNotFoundFault {
        return gpProjectDelegate.getAccountProjectsCount(accountID, request);
    }

    @Override
    public GPAccountProject getDefaultAccountProject(Long accountID) throws
            ResourceNotFoundFault {
        return gpProjectDelegate.getDefaultAccountProject(accountID);
    }

    @Override
    public List<ProjectDTO> searchAccountProjects(Long accountID,
            PaginatedSearchRequest request)
            throws ResourceNotFoundFault {
        return gpProjectDelegate.searchAccountProjects(accountID, request);
    }

    @Override
    public Boolean setProjectOwner(RequestByAccountProjectIDs request)
            throws ResourceNotFoundFault {
        return gpProjectDelegate.setProjectOwner(request);
    }

    @Override
    public GPAccountProject getProjectOwner(Long projectID) throws
            ResourceNotFoundFault {
        return gpProjectDelegate.getProjectOwner(projectID);
    }

    @Override
    public GPProject getDefaultProject(Long accountID) throws
            ResourceNotFoundFault {
        return gpProjectDelegate.getDefaultProject(accountID);
    }

    @Override
    public ProjectDTO getDefaultProjectDTO(Long accountID) throws
            ResourceNotFoundFault {
        return gpProjectDelegate.getDefaultProjectDTO(accountID);
    }

    @Override
    public GPProject updateDefaultProject(Long accountID,
            Long projectID) throws ResourceNotFoundFault {
        return gpProjectDelegate.updateDefaultProject(accountID, projectID);
    }

    @Override
    public Boolean saveAccountProjectProperties(
            AccountProjectPropertiesDTO accountProjectProperties)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpProjectDelegate.saveAccountProjectProperties(
                accountProjectProperties);
    }

    @Override
    public ShortAccountDTOContainer getAccountsByProjectID(Long projectID)
            throws ResourceNotFoundFault {
        return gpProjectDelegate.getAccountsByProjectID(projectID);
    }

    @Override
    public ShortAccountDTOContainer getAccountsToShareByProjectID(Long projectID)
            throws ResourceNotFoundFault {
        return gpProjectDelegate.getAccountsToShareByProjectID(projectID);
    }

    @Override
    public Boolean updateAccountsProjectSharing(
            PutAccountsProjectRequest apRequest)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpProjectDelegate.updateAccountsProjectSharing(apRequest);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Project">
    // =========================================================================
    // === Project
    // =========================================================================
    @Override
    public Long saveProject(SaveProjectRequest saveProjectRequest)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpProjectDelegate.saveProject(saveProjectRequest);
    }

    @Override
    public Long insertProject(GPProject project) throws IllegalParameterFault {
        return gpProjectDelegate.insertProject(project);
    }

    @Override
    public Long updateProject(GPProject project)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpProjectDelegate.updateProject(project);
    }

    @Override
    public Boolean deleteProject(Long projectID) throws ResourceNotFoundFault {
        return gpProjectDelegate.deleteProject(projectID);
    }

    @Override
    public GPProject getProjectDetail(Long projectID) throws
            ResourceNotFoundFault {
        return gpProjectDelegate.getProjectDetail(projectID);
    }

    @Override
    public Integer getNumberOfElementsProject(Long projectID) throws
            ResourceNotFoundFault {
        return gpProjectDelegate.getNumberOfElementsProject(projectID);
    }

    @Override
    public void setProjectShared(Long projectID) throws ResourceNotFoundFault {
        gpProjectDelegate.setProjectShared(projectID);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Viewport">
    // =========================================================================
    // === Viewport
    // =========================================================================
    @Override
    public GPViewport getDefaultViewport(Long accountProjectID)
            throws ResourceNotFoundFault {
        return gpViewportDelegate.getDefaultViewport(accountProjectID);
    }

    @Override
    public WSGetViewportResponse getAccountProjectViewports(
            Long accountProjectID) throws ResourceNotFoundFault {
        return gpViewportDelegate.getAccountProjectViewports(
                accountProjectID);
    }

    @Override
    public Long insertViewport(InsertViewportRequest insertViewportReq)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpViewportDelegate.insertViewport(insertViewportReq);
    }

    @Override
    public void replaceViewportList(ManageViewportRequest request)
            throws ResourceNotFoundFault, IllegalParameterFault {
        gpViewportDelegate.replaceViewportList(request);
    }

    @Override
    public void saveOrUpdateViewportList(ManageViewportRequest request)
            throws ResourceNotFoundFault, IllegalParameterFault {
        gpViewportDelegate.saveOrUpdateViewportList(request);
    }

    @Override
    public Long updateViewport(GPViewport viewport)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpViewportDelegate.updateViewport(viewport);
    }

    @Override
    public Boolean deleteViewport(Long viewportID) throws ResourceNotFoundFault {
        return gpViewportDelegate.deleteViewport(viewportID);
    }

    @Override
    public GPViewport getViewportById(Long idViewport) throws
            ResourceNotFoundFault, IllegalParameterFault {
        return gpViewportDelegate.getViewportById(idViewport);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Folder">
    // =========================================================================
    // === Folder
    // =========================================================================
    @Override
    public Long insertFolder(InsertFolderRequest insertFolderRequest)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpFolderDelegate.insertFolder(insertFolderRequest);
    }

    @Override
    public Long updateFolder(GPFolder folder)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpFolderDelegate.updateFolder(folder);
    }

    @Override
    public Boolean deleteFolder(Long folderID) throws ResourceNotFoundFault {
        return gpFolderDelegate.deleteFolder(folderID);
    }

    @Override
    public Long saveFolderProperties(Long folderID,
            String name,
            boolean checked,
            boolean expanded)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpFolderDelegate.saveFolderProperties(folderID, name,
                checked, expanded);
    }

    @Override
    public Long saveAddedFolderAndTreeModifications(
            WSAddFolderAndTreeModificationsRequest sftModificationRequest)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpFolderDelegate.saveAddedFolderAndTreeModifications(
                sftModificationRequest);
    }

    @Override
    public Boolean saveDeletedFolderAndTreeModifications(
            WSDeleteFolderAndTreeModifications sdfModificationRequest)
            throws ResourceNotFoundFault {
        return gpFolderDelegate.saveDeletedFolderAndTreeModifications(
                sdfModificationRequest);
    }

    @Override
    public Boolean saveDragAndDropFolderAndTreeModifications(
            WSDDFolderAndTreeModifications sddfTreeModificationRequest)
            throws ResourceNotFoundFault {
        return gpFolderDelegate.saveDragAndDropFolderAndTreeModifications(
                sddfTreeModificationRequest);
    }

    @Override
    public FolderDTO getShortFolder(Long folderID) throws ResourceNotFoundFault {
        return gpFolderDelegate.getShortFolder(folderID);
    }

    @Override
    public GPFolder getFolderDetail(Long folderID) throws ResourceNotFoundFault {
        return gpFolderDelegate.getFolderDetail(folderID);
    }

    @Override
    public ChildrenFolderStore getChildrenFolders(Long folderID) {
        return gpFolderDelegate.getChildrenFolders(folderID);
    }

    @Override
    public TreeFolderElementsStore getChildrenElements(Long folderID) {
        return gpFolderDelegate.getChildrenElements(folderID);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Folder / Project">
    @Override
    public ProjectDTO getProjectWithRootFolders(Long projectID,
            Long accountID)
            throws ResourceNotFoundFault {
        return gpProjectDelegate.getProjectWithRootFolders(projectID,
                accountID);
    }

    @Override
    public ProjectDTO getProjectWithExpandedFolders(Long projectID,
            Long accountID)
            throws ResourceNotFoundFault {
        return gpProjectDelegate.getProjectWithExpandedFolders(projectID,
                accountID);
    }

    @Override
    public ProjectDTO exportProject(Long projectID) throws ResourceNotFoundFault {
        return gpProjectDelegate.exportProject(projectID);
    }

    @Override
    public Long importProject(ImportProjectRequest impRequest)
            throws IllegalParameterFault, ResourceNotFoundFault {
        return gpProjectDelegate.importProject(impRequest);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Layer (Raster and Vector)">
    // =========================================================================
    // === Layer
    // =========================================================================
    @Override
    public Long insertLayer(InsertLayerRequest layerRequest) throws
            IllegalParameterFault {
        return gpLayerDelegate.insertLayer(layerRequest);
    }

    @Override
    public Long updateRasterLayer(GPRasterLayer layer)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpLayerDelegate.updateRasterLayer(layer);
    }

    @Override
    public Long updateVectorLayer(GPVectorLayer layer)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpLayerDelegate.updateVectorLayer(layer);
    }

    @Override
    public Boolean deleteLayer(Long layerID)
            throws ResourceNotFoundFault {
        return gpLayerDelegate.deleteLayer(layerID);
    }

    @Override
    public Long saveAddedLayerAndTreeModifications(
            WSAddLayerAndTreeModificationsRequest addLayerRequest)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpLayerDelegate.saveAddedLayerAndTreeModifications(
                addLayerRequest);
    }

    @Override
    public LongListStore saveAddedLayersAndTreeModifications(
            WSAddLayersAndTreeModificationsRequest addLayersRequest)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpLayerDelegate.saveAddedLayersAndTreeModifications(
                addLayersRequest);
    }

    @Override
    public Boolean saveDeletedLayerAndTreeModifications(
            WSDeleteLayerAndTreeModificationsRequest deleteLayerRequest)
            throws ResourceNotFoundFault {
        return gpLayerDelegate.saveDeletedLayerAndTreeModifications(
                deleteLayerRequest);
    }

    @Override
    public Boolean saveCheckStatusLayerAndTreeModifications(Long layerID,
            boolean checked) throws ResourceNotFoundFault {
        return gpLayerDelegate.saveCheckStatusLayerAndTreeModifications(
                layerID, checked);
    }

    @Override
    public Boolean saveDragAndDropLayerAndTreeModifications(
            WSDDLayerAndTreeModificationsRequest ddLayerReq)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpLayerDelegate.saveDragAndDropLayerAndTreeModifications(
                ddLayerReq);
    }

    @Override
    public Boolean saveLayerProperties(RasterPropertiesDTO layerProperties)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpLayerDelegate.saveLayerProperties(layerProperties);
    }

    @Override
    public GPRasterLayer getRasterLayer(Long layerID)
            throws ResourceNotFoundFault {
        return gpLayerDelegate.getRasterLayer(layerID);
    }

    @Override
    public GPVectorLayer getVectorLayer(Long layerID)
            throws ResourceNotFoundFault {
        return gpLayerDelegate.getVectorLayer(layerID);
    }

    @Override
    public ShortLayerDTOContainer getLayers(Long projectID) {
        return gpLayerDelegate.getLayers(projectID);
    }

    @Override
    public GPBBox getBBox(Long layerID) throws ResourceNotFoundFault {
        return gpLayerDelegate.getBBox(layerID);
    }

    @Override
    public GPLayerInfo getLayerInfo(Long layerID) throws ResourceNotFoundFault {
        return gpLayerDelegate.getLayerInfo(layerID);
    }

    @Override
    public ShortLayerDTO getShortLayer(Long layerID) throws
            ResourceNotFoundFault {
        return gpLayerDelegate.getShortLayer(layerID);
    }

    @Override
    public GPLayerType getLayerType(Long layerID) throws ResourceNotFoundFault {
        return gpLayerDelegate.getLayerType(layerID);
    }

    @Override
    public GetDataSourceResponse getLayersDataSourceByProjectID(Long projectID)
            throws ResourceNotFoundFault {
        return gpLayerDelegate.getLayersDataSourceByProjectID(projectID);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ACL">
    // =========================================================================
    // === ACL
    // =========================================================================    
    @Override
    public WSGetRoleResponse getAllRoles(String organization) throws
            ResourceNotFoundFault {
        return gpAclDelegate.getAllRoles(organization);
    }

    @Override
    public GuiComponentsPermissionMapData getAccountPermission(Long accountID)
            throws ResourceNotFoundFault {
        return this.gpAclDelegate.getAccountPermission(accountID);
    }

    @Override
    public GuiComponentsPermissionMapData getRolePermission(String role,
            String organization)
            throws ResourceNotFoundFault {
        return gpAclDelegate.getRolePermission(role, organization);
    }

    @Override
    public Boolean updateRolePermission(
            WSPutRolePermissionRequest putRolePermissionReq)
            throws ResourceNotFoundFault {
        return gpAclDelegate.updateRolePermission(putRolePermissionReq);
    }

    @Override
    public Boolean saveRole(WSSaveRoleRequest saveRoleReq) throws
            IllegalParameterFault {
        return gpAclDelegate.saveRole(saveRoleReq);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Server">
    // =========================================================================
    // === Server
    // =========================================================================
    @Override
    public Long insertServer(GeoPlatformServer server) {
        return gpServerDelegate.insertServer(server);
    }

    @Override
    public Long updateServer(GeoPlatformServer server)
            throws ResourceNotFoundFault, IllegalParameterFault {
        return gpServerDelegate.updateServer(server);
    }

    @Override
    public Boolean deleteServer(Long idServer) throws ResourceNotFoundFault {
        return gpServerDelegate.deleteServer(idServer);
    }

    @Override
    public GeoPlatformServer getServerDetail(Long idServer)
            throws ResourceNotFoundFault {
        return gpServerDelegate.getServerDetail(idServer);
    }

    @Override
    public ServerDTO getShortServer(String serverUrl) throws
            ResourceNotFoundFault {
        return gpServerDelegate.getShortServer(serverUrl);
    }

    @Override
    public ServerDTOContainer getAllServers(String organizationName) throws
            ResourceNotFoundFault {
        return gpServerDelegate.getAllServers(organizationName);
    }

    @Override
    public GeoPlatformServer getServerDetailByUrl(String serverUrl)
            throws ResourceNotFoundFault {
        return gpServerDelegate.getServerDetailByUrl(serverUrl);
    }

    @Override
    public ServerDTO saveServer(WSSaveServerRequest saveServerReq)
            throws IllegalParameterFault {
        return gpServerDelegate.saveServer(saveServerReq);
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Message">
    // =========================================================================
    // === Message
    // =========================================================================
    @Override
    public Long insertMessage(GPMessage message) throws ResourceNotFoundFault,
            IllegalParameterFault {
        return gpMessageDelegate.insertMessage(message);
    }

    @Override
    public Boolean insertMultiMessage(MessageDTO messageDTO) throws
            ResourceNotFoundFault {
        return gpMessageDelegate.insertMultiMessage(messageDTO);
    }

    @Override
    public Boolean deleteMessage(Long messageID) throws ResourceNotFoundFault {
        return gpMessageDelegate.deleteMessage(messageID);
    }

    @Override
    public GPMessage getMessageDetail(Long messageID) throws
            ResourceNotFoundFault {
        return gpMessageDelegate.getMessageDetail(messageID);
    }

    @Override
    public GetMessageResponse getAllMessagesByRecipient(Long recipientID) throws
            ResourceNotFoundFault {
        return gpMessageDelegate.getAllMessagesByRecipient(recipientID);
    }

    @Override
    public GetMessageResponse getUnreadMessagesByRecipient(Long recipientID)
            throws ResourceNotFoundFault {
        return gpMessageDelegate.getUnreadMessagesByRecipient(recipientID);
    }

    @Override
    public Boolean markMessageAsRead(Long recipientID) throws
            ResourceNotFoundFault {
        return gpMessageDelegate.markMessageAsRead(recipientID);
    }

    @Override
    public Boolean markAllMessagesAsReadByRecipient(Long recipientID) throws
            ResourceNotFoundFault {
        return gpMessageDelegate.markAllMessagesAsReadByRecipient(
                recipientID);
    }

    @Override
    public Boolean markMessagesAsReadByDate(
            MarkMessageReadByDateRequest markMessageAsReadByDateReq)
            throws ResourceNotFoundFault {
        return gpMessageDelegate.markMessagesAsReadByDate(
                markMessageAsReadByDateReq);
    }
    // </editor-fold>
}
