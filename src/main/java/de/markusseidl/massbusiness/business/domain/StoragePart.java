package de.markusseidl.massbusiness.business.domain;

import javax.persistence.*;

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
    private float _amountInStorage;

    // Additional parameters / properties of the storage part omitted.

    public StoragePart() {
    }

    public StoragePart(String partId, String partType, float amountInStorage) {
        _partId = partId;
        _partType = partType;
        _amountInStorage = amountInStorage;
    }

    public String getPartId() {
        return _partId;
    }

    public float getAmountInStorage() {
        return _amountInStorage;
    }

    public void setAmountInStorage(float amountInStorage) {
        _amountInStorage = amountInStorage;
    }

    public String getPartType() {
        return _partType;
    }

    public void setPartType(String partType) {
        _partType = partType;
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
