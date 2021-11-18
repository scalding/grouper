package edu.internet2.middleware.grouper.app.provisioning;

public class GrouperProvisioningDiagnosticsSettings {

  /**
   * if select all entities during diagnostics
   */
  private boolean diagnosticsEntitiesAllSelect;

  /**
   * if select all entities during diagnostics
   * @return
   */
  public boolean isDiagnosticsEntitiesAllSelect() {
    return diagnosticsEntitiesAllSelect;
  }


  /**
   * if select all entities during diagnostics
   * @param diagnosticsEntitiesAllSelect
   */
  public void setDiagnosticsEntitiesAllSelect(boolean diagnosticsEntitiesAllSelect) {
    this.diagnosticsEntitiesAllSelect = diagnosticsEntitiesAllSelect;
  }

  /**
   * if select all memberships during diagnostics
   */
  private boolean diagnosticsMembershipsAllSelect;



  /**
   * if select all memberships during diagnostics
   * @return
   */
  public boolean isDiagnosticsMembershipsAllSelect() {
    return diagnosticsMembershipsAllSelect;
  }


  /**
   * if select all memberships during diagnostics
   * @param diagnosticsMembershipsAllSelect
   */
  public void setDiagnosticsMembershipsAllSelect(boolean diagnosticsMembershipsAllSelect) {
    this.diagnosticsMembershipsAllSelect = diagnosticsMembershipsAllSelect;
  }

  /**
   * group name to do diagnostics for
   */
  private String diagnosticsGroupName;

  /**
   * group name to do diagnostics for
   * @return group name
   */
  public String getDiagnosticsGroupName() {
    return diagnosticsGroupName;
  }

  /**
   * group name to do diagnostics for
   * @param diagnosticsGroupName
   */
  public void setDiagnosticsGroupName(String diagnosticsGroupName) {
    this.diagnosticsGroupName = diagnosticsGroupName;
  }

  /**
   * if insert group in diagnostics
   */
  private boolean diagnosticsGroupInsert;

  /**
   * if insert group in diagnostics
   * @return if group insert
   */
  public boolean isDiagnosticsGroupInsert() {
    return this.diagnosticsGroupInsert;
  }


  /**
   * if insert group in diagnostics
   * @param diagnosticsGroupInsert1
   */
  public void setDiagnosticsGroupInsert(boolean diagnosticsGroupInsert1) {
    this.diagnosticsGroupInsert = diagnosticsGroupInsert1;
  }
  
  /**
   * if delete group in diagnostics
   */
  private boolean diagnosticsGroupDelete;

  /**
   * if delete group in diagnostics
   * @return if group delete
   */
  public boolean isDiagnosticsGroupDelete() {
    return this.diagnosticsGroupDelete;
  }


  /**
   * if delete group in diagnostics
   * @param diagnosticsGroupDelete1
   */
  public void setDiagnosticsGroupDelete(boolean diagnosticsGroupDelete1) {
    this.diagnosticsGroupDelete = diagnosticsGroupDelete1;
  }

  /**
   * if select all groups during diagnostics
   */
  private boolean diagnosticsGroupsAllSelect;

  /**
   * if select all groups during diagnostics
   * @return
   */
  public boolean isDiagnosticsGroupsAllSelect() {
    return diagnosticsGroupsAllSelect;
  }


  /**
   * if select all groups during diagnostics
   * @param selectAllGroupsDuringDiagnostics
   */
  public void setDiagnosticsGroupsAllSelect(
      boolean selectAllGroupsDuringDiagnostics) {
    this.diagnosticsGroupsAllSelect = selectAllGroupsDuringDiagnostics;
  }
  
  /**
   * if the entity should be added to the group as a group attribute
   */
  private boolean diagnosticsGroupAttributesMembershipInsert;
  
  /**
   * @return if the entity should be added to the group as a group attribute
   */
  public boolean isDiagnosticsGroupAttributesMembershipInsert() {
    return diagnosticsGroupAttributesMembershipInsert;
  }

  /**
   * if the entity should be added to the group as a group attribute
   * @param diagnosticsGroupAttributesMembershipInsert
   */
  public void setDiagnosticsGroupAttributesMembershipInsert(
      boolean diagnosticsGroupAttributesMembershipInsert) {
    this.diagnosticsGroupAttributesMembershipInsert = diagnosticsGroupAttributesMembershipInsert;
  }

  /**
   * if the entity should be removed from the group as a group attribute
   */
  private boolean diagnosticsGroupAttributesMembershipDelete;
  
  /**
   * @return if the entity should be removed from the group as a group attribute
   */
  public boolean isDiagnosticsGroupAttributesMembershipDelete() {
    return diagnosticsGroupAttributesMembershipDelete;
  }

  /**
   * if the entity should be removed from the group as a group attribute
   * @param diagnosticsGroupAttributesMembershipDelete
   */
  public void setDiagnosticsGroupAttributesMembershipDelete(
      boolean diagnosticsGroupAttributesMembershipDelete) {
    this.diagnosticsGroupAttributesMembershipDelete = diagnosticsGroupAttributesMembershipDelete;
  }
  
  /**
   * if delete entity in diagnostics
   */
  private boolean diagnosticsEntityDelete;

  /**
   * if delete entity in diagnostics
   * @return if entity delete
   */
  public boolean isDiagnosticsEntityDelete() {
    return this.diagnosticsEntityDelete;
  }


  /**
   * if delete entity in diagnostics
   * @param diagnosticsEntityDelete1
   */
  public void setDiagnosticsEntityDelete(boolean diagnosticsEntityDelete1) {
    this.diagnosticsEntityDelete = diagnosticsEntityDelete1;
  }
  
  /**
   * if insert entity in diagnostics
   */
  private boolean diagnosticsEntityInsert;

  /**
   * if insert entity in diagnostics
   * @return if entity insert
   */
  public boolean isDiagnosticsEntityInsert() {
    return this.diagnosticsEntityInsert;
  }


  /**
   * if insert entity in diagnostics
   * @param diagnosticsEntityInsert1
   */
  public void setDiagnosticsEntityInsert(boolean diagnosticsEntityInsert1) {
    this.diagnosticsEntityInsert = diagnosticsEntityInsert1;
  }
  
  /**
   * subject id or identifier to do diagnostics for
   */
  private String diagnosticsSubjectIdOrIdentifier;

  /**
   * subject id or identifier to do diagnostics for
   * @return subject id or identifier
   */
  public String getDiagnosticsSubjectIdOrIdentifier() {
    return diagnosticsSubjectIdOrIdentifier;
  }

  /**
   * subject id or identifier to do diagnostics for
   * @param diagnosticsSubjectIdOrIdentifier
   */
  public void setDiagnosticsSubjectIdOrIdentifier(String diagnosticsSubjectIdOrIdentifier) {
    this.diagnosticsSubjectIdOrIdentifier = diagnosticsSubjectIdOrIdentifier;
  }
  
  /**
   * if the group should be added to the entity as an entity attribute
   */
  private boolean diagnosticsEntityAttributesMembershipInsert;
  
  /**
   * @return if the group should be added to the entity as an entity attribute
   */
  public boolean isDiagnosticsEntityAttributesMembershipInsert() {
    return diagnosticsEntityAttributesMembershipInsert;
  }

  /**
   * if the group should be added to the entity as an entity attribute
   * @param diagnosticsEntityAttributesMembershipInsert
   */
  public void setDiagnosticsEntityAttributesMembershipInsert(
      boolean diagnosticsEntityAttributesMembershipInsert) {
    this.diagnosticsEntityAttributesMembershipInsert = diagnosticsEntityAttributesMembershipInsert;
  }

  /**
   * if the group should be removed from the entity as an entity attribute
   */
  private boolean diagnosticsEntityAttributesMembershipDelete;
  
  /**
   * @return if the group should be removed from the entity as an entity attribute
   */
  public boolean isDiagnosticsEntityAttributesMembershipDelete() {
    return diagnosticsEntityAttributesMembershipDelete;
  }

  /**
   * if the group should be removed from the entity as an entity attribute
   * @param diagnosticsEntityAttributesMembershipDelete
   */
  public void setDiagnosticsEntityAttributesMembershipDelete(
      boolean diagnosticsEntityAttributesMembershipDelete) {
    this.diagnosticsEntityAttributesMembershipDelete = diagnosticsEntityAttributesMembershipDelete;
  }
}
