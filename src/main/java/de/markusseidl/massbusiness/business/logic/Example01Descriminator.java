package de.markusseidl.massbusiness.business.logic;

import de.markusseidl.massbusiness.business.domain.StoragePart;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * Example01Descriminator
 * <p>
 * Created on 11.08.2016
 */
public final class Example01Descriminator {

    private Example01Descriminator() { }

    public static Object groupByKeyForV1(StoragePart storagePart) {
        return storagePart.getPartType();
    }

    public static Object groupByKeyForV2(StoragePart storagePart) {
        double valueToCompany = 0;
        switch(storagePart.getPartType()) {
            case "Zukaufteil":
                valueToCompany = 2.0;
                break;
            case "Normteil":
                valueToCompany = 1.0;
                break;
            case "Verbrauchsmaterial":
                valueToCompany = 1.0;
                break;
        }

        // max 1            max 100.000                         max 100 --> max 2*100.000*100 = 20.000.000
        valueToCompany *= storagePart.getAmountInStorage() * storagePart.getPricePerUnit();

        // Klassifiziere den Wert für die Firma in 10 Kategorien, jeweils von 0 bis 1, 1 bis 2, usf.
        return (int) Math.ceil( (valueToCompany / 20_000_000.0) * 10.0);
    }

    public static void whereCriteriaForGroupBy(StoragePart sp, CriteriaBuilder cb) {

    }
}
