package edu.internet2.middleware.grouper.dataField;

import java.sql.Timestamp;

import org.apache.commons.lang3.StringUtils;

import edu.internet2.middleware.grouper.tableIndex.TableIndex;
import edu.internet2.middleware.grouper.tableIndex.TableIndexType;
import edu.internet2.middleware.grouperClient.jdbc.GcDbVersionable;
import edu.internet2.middleware.grouperClient.jdbc.GcPersist;
import edu.internet2.middleware.grouperClient.jdbc.GcPersistableClass;
import edu.internet2.middleware.grouperClient.jdbc.GcPersistableField;
import edu.internet2.middleware.grouperClient.jdbc.GcSqlAssignPrimaryKey;
import edu.internet2.middleware.grouperClient.util.GrouperClientUtils;
import edu.internet2.middleware.grouperClientExt.org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * loader config for grouper data alias
 */
@GcPersistableClass(tableName="grouper_data_alias", defaultFieldPersist=GcPersist.doPersist)
public class GrouperDataAlias implements GcSqlAssignPrimaryKey, GcDbVersionable {

  private Long dataFieldInternalId = null;

  private Long dataRowInternalId = null;

  @GcPersistableField(primaryKey=true, primaryKeyManuallyAssigned=true)
  private long internalId = -1;
  
  private String name;

  private String lowerName;

  private Timestamp createdOn = null;
  
  private String aliasType;

  /**
   * version from db
   */
  @GcPersistableField(persist = GcPersist.dontPersist)
  private GrouperDataAlias dbVersion;

  public String getAliasType() {
    return aliasType;
  }


  
  public void setAliasType(String aliasType) {
    this.aliasType = aliasType;
  }





  public Long getDataRowInternalId() {
    return dataRowInternalId;
  }




  
  public void setDataRowInternalId(Long dataRowInternalId) {
    this.dataRowInternalId = dataRowInternalId;
  }




  public String getLowerName() {
    return lowerName;
  }



  
  public void setLowerName(String lowerName) {
    this.lowerName = lowerName;
  }



  public Long getDataFieldInternalId() {
    return dataFieldInternalId;
  }


  
  public void setDataFieldInternalId(Long dataFieldInternalId) {
    this.dataFieldInternalId = dataFieldInternalId;
  }


  public long getInternalId() {
    return internalId;
  }

  
  public void setInternalId(long internalId) {
    this.internalId = internalId;
  }

  
  public String getName() {
    return name;
  }

  
  public void setName(String name) {
    this.name = name;
  }

  
  public Timestamp getCreatedOn() {
    return createdOn;
  }

  
  public void setCreatedOn(Timestamp createdOn) {
    this.createdOn = createdOn;
  }
  
  
  /**
   * 
   */
  @Override
  public boolean gcSqlAssignNewPrimaryKeyForInsert() {
    if (this.internalId != -1) {
      return false;
    }
    this.internalId = TableIndex.reserveId(TableIndexType.dataAlias);
    return true;
  }


  //########## END GENERATED BY GcDbVersionableGenerate.java ###########
  
  /**
   * deep clone the fields in this object
   */
  @Override
  public GrouperDataAlias clone() {
  
    GrouperDataAlias grouperDataAlias = new GrouperDataAlias();
  
    //dbVersion  DONT CLONE
  
    grouperDataAlias.createdOn = this.createdOn;
    grouperDataAlias.dataFieldInternalId = this.dataFieldInternalId;
    grouperDataAlias.dataRowInternalId = this.dataRowInternalId;
    grouperDataAlias.aliasType = this.aliasType;
    grouperDataAlias.internalId = this.internalId;
    grouperDataAlias.lowerName = this.lowerName;
    grouperDataAlias.name = this.name;
  
    return grouperDataAlias;
  }


  /**
   * db version
   */
  @Override
  public void dbVersionDelete() {
    this.dbVersion = null;
  }


  /**
   * if we need to update this object
   * @return if needs to update this object
   */
  @Override
  public boolean dbVersionDifferent() {
    return !this.equalsDeep(this.dbVersion);
  }


  /**
   * take a snapshot of the data since this is what is in the db
   */
  @Override
  public void dbVersionReset() {
    //lets get the state from the db so we know what has changed
    this.dbVersion = this.clone();
  }


  /**
   *
   */
  public boolean equalsDeep(Object obj) {
    if (this==obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof GrouperDataAlias)) {
      return false;
    }
    GrouperDataAlias other = (GrouperDataAlias) obj;
  
    return new EqualsBuilder()
  
  
      //dbVersion  DONT EQUALS
      .append(this.createdOn, other.createdOn)
      .append(this.dataFieldInternalId, other.dataFieldInternalId)
      .append(this.aliasType, other.aliasType)
      .append(this.dataRowInternalId, other.dataRowInternalId)
      .append(this.internalId, other.internalId)
      .append(this.lowerName, other.lowerName)
      .append(this.name, other.name)
        .isEquals();
  
  }


  public void storePrepare() {
    if (this.createdOn == null) {
      this.createdOn = new Timestamp(System.currentTimeMillis());
    }
    this.lowerName = StringUtils.lowerCase(this.name);
  }


  /**
   * 
   */
  @Override
  public String toString() {
    return GrouperClientUtils.toStringReflection(this, null);
  }

}
