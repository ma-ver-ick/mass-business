package de.markusseidl.massbusiness.business.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * StoragePart / Lagerteil
 */
@Entity
@Table(name = "T_STORAGE_PART")
public class StoragePart {

  public static final String TYPE_ZUKAUFTEIL = "Zukaufteil";
  public static final String TYPE_NORMTEIL = "Normteil";
  public static final String TYPE_VERBRAUCHSMATERIAL = "Verbrauchsmaterial";

  /** Business key used in the company to uniquely identify a single part. */
  @Id
  @Column(name = "PART_ID")
  private String _partId;

  /** Type of the part, for possible values see above. */
  @Column(name = "PART_TYPE")
  private String _partType;

  /** The amount in stock of this stock item. */
  @Column(name = "AMOUNT")
  private int _amountInStorage;

  /** Price of one unit (piece). */
  @Column(name = "PRICE_UNIT")
  private float _pricePerUnit;

  public StoragePart() {
  }

  public StoragePart(String partId, String partType, int amountInStorage, float pricePerUnit) {
    _partId = partId;
    _partType = partType;
    _amountInStorage = amountInStorage;
    _pricePerUnit = pricePerUnit;
  }

  public String getPartId() {
    return _partId;
  }

  public int getAmountInStorage() {
    return _amountInStorage;
  }

  public void setAmountInStorage(int amountInStorage) {
    _amountInStorage = amountInStorage;
  }

  public String getPartType() {
    return _partType;
  }

  public void setPartType(String partType) {
    _partType = partType;
  }

  public float getPricePerUnit() {
    return _pricePerUnit;
  }

  public void setPricePerUnit(float pricePerUnit) {
    _pricePerUnit = pricePerUnit;
  }

  @Override
  public int hashCode() {
    return _partId.hashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    StoragePart that = (StoragePart) o;

    return _partId.equals(that._partId);

  }

  @Override
  public String toString() {
    return "StoragePart{" + "_partId='" + _partId + '\'' + ", _partType='" + _partType + '\'' + ", _amountInStorage=" + _amountInStorage
        + ", _pricePerUnit=" + _pricePerUnit + '}';
  }
}
