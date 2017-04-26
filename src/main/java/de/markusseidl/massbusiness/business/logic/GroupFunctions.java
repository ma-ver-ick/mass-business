package de.markusseidl.massbusiness.business.logic;

import de.markusseidl.massbusiness.business.domain.StoragePart;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * GroupFunctions
 */
public final class GroupFunctions {

  /** Management determined value */
  private static float VALUE_ZUKAUFTEIL = 1.0f;

  /** Management determined value */
  private static float VALUE_NORMTEIL = 0.5f;

  /** Management determined value */
  private static float VALUE_VERBRAUCHSMATERIAL = 0.2f;

  /** Sum for {@link #groupByKeyForV2(StoragePart)} determined by manual calculation for easier code. */
  private static double SUM_V2 = 10_000_000.0;

  private GroupFunctions() {
  }

  /**
   * Returns the group for the first example and for the given storage part.
   */
  public static Object groupByKeyForV1(StoragePart storagePart) {
    return storagePart.getPartType();
  }

  /** Returns a predicate which can be used to select every storage part for the group specified by the given instance of storage part. */
  public static Predicate whereCriteriaForGroupByV1(StoragePart sp, Root<StoragePart> root, CriteriaBuilder cb) {
    return cb.equal(root.get("_partType"), cb.literal(sp.getPartType()));
  }

  /**
   * Creates a predicate that determines all storage parts in the database that are in the same group as the given storage part. This
   * represents the case and if statement as found in {@link #groupByKeyForV2(StoragePart)} in a predicate.
   */
  public static Predicate whereCriteriaForGroupByV2(StoragePart sp, Root<StoragePart> root, CriteriaBuilder cb) {
    float min = (int) (Integer) groupByKeyForV2(sp);
    float max = min + 1;

    Path<String> partType = root.get("_partType");
    Path<Float> amountInStorage = root.get("_amountInStorage");
    Path<Float> pricePerUnit = root.get("_pricePerUnit");

    Expression<Float> p0Inner = cb.toFloat(//
        cb.quot(cb.prod(amountInStorage, cb.prod(pricePerUnit, VALUE_ZUKAUFTEIL)), SUM_V2 / 10.0));
    Predicate p0 =//
        cb.and(cb.equal(partType, cb.literal(StoragePart.TYPE_ZUKAUFTEIL)), cb.between(p0Inner, min, max));

    Expression<Float> p1Inner = cb.toFloat(//
        cb.quot(cb.prod(amountInStorage, cb.prod(pricePerUnit, VALUE_NORMTEIL)), SUM_V2 / 10.0));
    Predicate p1 =//
        cb.and(cb.equal(partType, cb.literal(StoragePart.TYPE_NORMTEIL)), cb.between(p1Inner, cb.literal(min), cb.literal(max)));

    Expression<Float> p2Inner = cb.toFloat(//
        cb.quot(cb.prod(amountInStorage, cb.prod(pricePerUnit, VALUE_VERBRAUCHSMATERIAL)), SUM_V2 / 10.0));
    Predicate p2 =//
        cb.and(cb.equal(partType, cb.literal(StoragePart.TYPE_VERBRAUCHSMATERIAL)), cb.between(p2Inner, cb.literal(min), cb.literal(max)));

    return cb.or(p0, p1, p2);
  }

  /**
   * Determine the group v2 for the given storage part. The group is determined by:
   * value = amount * price * factor. The factor is part specific and the result is binned into 10 groups. The groups are determined
   * arbitrary as the test data max sum is known. In reality the result may be used with fixed group lengths (ex: 1.000 - 2.000, 2.001 -
   * 3.000, and so on) but this would make the implementation much more complex.
   */
  public static Object groupByKeyForV2(StoragePart storagePart) {
    float valueToCompany = 0f;
    switch (storagePart.getPartType()) {
      case "Zukaufteil":
        valueToCompany = VALUE_ZUKAUFTEIL;
        break;
      case "Normteil":
        valueToCompany = VALUE_NORMTEIL;
        break;
      case "Verbrauchsmaterial":
        valueToCompany = VALUE_VERBRAUCHSMATERIAL;
        break;
    }

    // max 1        = max 100.000                        max 100                      --> max 100.000 * 100 = 10.000.000
    valueToCompany *= storagePart.getAmountInStorage() * storagePart.getPricePerUnit();

    // Klassifiziere den Wert für die Firma in 10 Kategorien, jeweils von 0 bis 1, 1 bis 2, usf.
    return (int) Math.floor((valueToCompany / SUM_V2) * 10.0);
  }
}
