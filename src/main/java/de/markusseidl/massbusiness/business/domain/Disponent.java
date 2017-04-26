package de.markusseidl.massbusiness.business.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Disponent / Disponent für ein Lagerteil
 */
@Entity
@Table(name = "T_DISPONENT")
public class Disponent {

  /**
   * Business key used in the company to uniquely identify a single part.
   */
  @Id
  @Column(name = "ID")
  private String _id;

  /** First name of the disponent. */
  @Column(name = "FIRST_NAME")
  private String _firstName;

  /** Last name of the disponent. */
  @Column(name = "LAST_NAME")
  private String _lastName;

  /** Start number of the storage part under the supervision of this disponent (inclusive). */
  @Column(name = "START_PART_NUMBER")
  private String _startPartNumber;

  /** End number of the storage part under the supervision of this disponent (inclusive). */
  @Column(name = "END_PART_NUMBER")
  private String _endPartNumber;

  public String getId() {
    return _id;
  }

  public void setId(String id) {
    _id = id;
  }

  public String getFirstName() {
    return _firstName;
  }

  public void setFirstName(String firstName) {
    _firstName = firstName;
  }

  public String getLastName() {
    return _lastName;
  }

  public void setLastName(String lastName) {
    _lastName = lastName;
  }

  public String getStartPartNumber() {
    return _startPartNumber;
  }

  public void setStartPartNumber(String startPartNumber) {
    _startPartNumber = startPartNumber;
  }

  public String getEndPartNumber() {
    return _endPartNumber;
  }

  public void setEndPartNumber(String endPartNumber) {
    _endPartNumber = endPartNumber;
  }

  @Override
  public String toString() {
    return "Disponent{" + "_id='" + _id + '\'' + ", _startPartNumber='" + _startPartNumber + '\'' + ", _endPartNumber='" + _endPartNumber
        + '\'' + '}';
  }
}
