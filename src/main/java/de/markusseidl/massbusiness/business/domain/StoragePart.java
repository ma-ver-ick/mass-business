package de.markusseidl.massbusiness.business.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * StoragePart / Lagerteil
 * <p>
 * Created on 17.07.2016
 */
@Entity
@Table(name = "T_STORAGE_PART")
public class StoragePart {

    // /** Surrogate primary key of the entity */
    // private long _id;

    /**
     * Business key used in the company to uniquely identify a single part
     */
    @Id
    @Column(name = "PART_ID")
    private String _partId;

    @Column(name = "PART_TYPE")
    private String _partType;

    @Column(name = "AMOUNT")
    private int _amountInStorage;

    @Column(name = "PRICE_UNIT")
    private float _pricePerUnit;

    public StoragePart() { }

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoragePart that = (StoragePart) o;

        return _partId.equals(that._partId);

    }
}
