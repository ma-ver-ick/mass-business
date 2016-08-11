package de.markusseidl.massbusiness.business.logic;

import de.markusseidl.massbusiness.business.domain.StoragePart;

/**
 * Example01Descriminator
 * <p>
 * Created on 11.08.2016
 */
public final class Example01Descriminator {

    private Example01Descriminator() { }

    public static Object groupByKeyFor(StoragePart storagePart) {
        return storagePart.getPartType();
    }

}
