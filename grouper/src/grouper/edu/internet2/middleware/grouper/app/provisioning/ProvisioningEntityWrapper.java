package edu.internet2.middleware.grouper.app.provisioning;

import edu.internet2.middleware.grouperClient.jdbc.tableSync.GcGrouperSyncErrorCode;
import edu.internet2.middleware.grouperClient.jdbc.tableSync.GcGrouperSyncMember;

public class ProvisioningEntityWrapper extends ProvisioningUpdatableWrapper {

  /**
   * if recalcing the entity memberships 
   */
  private boolean recalcEntityMemberships;
  
  /**
   * if recalcing the entity memberships 
   * @return
   */
  public boolean isRecalcEntityMemberships() {
    return recalcEntityMemberships;
  }
  
  /**
   * if recalcing the entity memberships 
   * @param recalcEntityMemberships1
   */
  public void setRecalcEntityMemberships(boolean recalcEntityMemberships1) {
    this.recalcEntityMemberships = recalcEntityMemberships1;
  }

  /**
   * if this is incremental, and syncing memberships for this group
   */
  private boolean incrementalSyncMemberships;
  
  /**
   * if this is incremental, and syncing memberships for this group
   * @return
   */
  public boolean isIncrementalSyncMemberships() {
    return incrementalSyncMemberships;
  }

  /**
   * if this is incremental, and syncing memberships for this group
   * @param incrementalSyncMemberships1
   */
  public void setIncrementalSyncMemberships(boolean incrementalSyncMemberships1) {
    this.incrementalSyncMemberships = incrementalSyncMemberships1;
  }


  private Object matchingId = null;
  
  
  public Object getMatchingId() {
    return matchingId;
  }



  
  public void setMatchingId(Object matchingId) {
    this.matchingId = matchingId;
  }

  /**
   * grouper member id
   */
  private String memberId;
  
  
  
  
  public String getMemberId() {
    return memberId;
  }




  
  public void setMemberId(String memberId) {
    this.memberId = memberId;
  }


  /**
   * sync member id
   */
  private String syncMemberId;

  
  
  /**
   * sync member id
   * @return
   */
  public String getSyncMemberId() {
    return syncMemberId;
  }




  /**
   * sync member id
   * @param syncMemberId
   */
  public void setSyncMemberId(String syncMemberId) {
    this.syncMemberId = syncMemberId;
  }




  public ProvisioningEntityWrapper() {
    super();
  }

  public String toString() {
    return "EntityWrapper@" + Integer.toHexString(hashCode());
  }

  private ProvisioningEntity grouperProvisioningEntity;
  
  private ProvisioningEntity targetProvisioningEntity;

  private ProvisioningEntity grouperTargetEntity;

  private Object targetNativeEntity;
  
  private GcGrouperSyncMember gcGrouperSyncMember;

  /**
   * if this is for a create in target
   */
  private boolean create;

  /**
   * if the grrouperProvisioningGroup side is for a delete.  includes things that are known 
   * to be needed to be deleted.  This is used to retrieve the correct
   * incremental state from the target
   */
  private boolean delete;
  
  
  
  public ProvisioningEntity getGrouperProvisioningEntity() {
    return grouperProvisioningEntity;
  }

  
  public void setGrouperProvisioningEntity(ProvisioningEntity grouperProvisioningEntity) {
    this.grouperProvisioningEntity = grouperProvisioningEntity;
    if (this.grouperProvisioningEntity!=null) {
      this.memberId = this.grouperProvisioningEntity.getId();
      if (this != this.grouperProvisioningEntity.getProvisioningEntityWrapper()) {
        if (this.grouperProvisioningEntity.getProvisioningEntityWrapper() != null) {
          this.getGrouperProvisioner().retrieveGrouperProvisioningData().getProvisioningEntityWrappers().remove(this.grouperProvisioningEntity.getProvisioningEntityWrapper());
        }
        this.grouperProvisioningEntity.setProvisioningEntityWrapper(this);
      }
    }
  }

  
  public ProvisioningEntity getTargetProvisioningEntity() {
    return targetProvisioningEntity;
  }

  
  public void setTargetProvisioningEntity(ProvisioningEntity targetProvisioningEntity) {
    this.targetProvisioningEntity = targetProvisioningEntity;
    if (this.targetProvisioningEntity != null && this != this.targetProvisioningEntity.getProvisioningEntityWrapper()) {
      if (this.targetProvisioningEntity.getProvisioningEntityWrapper() != null) {
        this.getGrouperProvisioner().retrieveGrouperProvisioningData().getProvisioningEntityWrappers().remove(this.targetProvisioningEntity.getProvisioningEntityWrapper());
      }
      this.targetProvisioningEntity.setProvisioningEntityWrapper(this);
    }
  }

  
  public ProvisioningEntity getGrouperTargetEntity() {
    return grouperTargetEntity;
  }

  
  public void setGrouperTargetEntity(ProvisioningEntity grouperTargetEntity) {
    this.grouperTargetEntity = grouperTargetEntity;
    if (this.grouperTargetEntity != null && this != this.grouperTargetEntity.getProvisioningEntityWrapper()) {
      if (this.grouperTargetEntity.getProvisioningEntityWrapper() != null) {
        this.getGrouperProvisioner().retrieveGrouperProvisioningData().getProvisioningEntityWrappers().remove(this.grouperTargetEntity.getProvisioningEntityWrapper());
      }
      this.grouperTargetEntity.setProvisioningEntityWrapper(this);
    }
  }

  
  public Object getTargetNativeEntity() {
    return targetNativeEntity;
  }

  
  public void setTargetNativeEntity(Object targetNativeEntity) {
    this.targetNativeEntity = targetNativeEntity;
  }

  
  public GcGrouperSyncMember getGcGrouperSyncMember() {
    return gcGrouperSyncMember;
  }

  
  public void setGcGrouperSyncMember(GcGrouperSyncMember gcGrouperSyncMember) {
    this.gcGrouperSyncMember = gcGrouperSyncMember;
    if (this.gcGrouperSyncMember != null) {
      this.syncMemberId = this.getGcGrouperSyncMember().getId();
    }
  }

  /**
   * if this is for a create in target
   * @return
   */
  public boolean isCreate() {
    return create;
  }

  /**
   * if the grrouperProvisioningGroup side is for a delete.  includes things that are known 
   * to be needed to be deleted.  This is used to retrieve the correct
   * incremental state from the target
   * @return
   */
  public boolean isDelete() {
    return delete;
  }

  /**
   * if this is for a create in target
   * @param create
   */
  public void setCreate(boolean create) {
    this.create = create;
  }

  /**
   * if the grrouperProvisioningGroup side is for a delete.  includes things that are known 
   * to be needed to be deleted.  This is used to retrieve the correct
   * incremental state from the target
   * @param delete
   */
  public void setDelete(boolean delete) {
    this.delete = delete;
  }

  @Override
  public String objectTypeName() {
    return "entity";
  }
  
  public String toStringForError() {
    
    if (this.grouperTargetEntity != null) {
      return "grouperTargetEntity: " + this.grouperTargetEntity;
    }

    if (this.grouperProvisioningEntity != null) {
      return "grouperProvisioningEntity: " + this.grouperProvisioningEntity;
    }

    if (this.targetProvisioningEntity != null) {
      return "targetProvisioningEntity: " + this.targetProvisioningEntity;
    }
    
    return this.toString();
  }



}
