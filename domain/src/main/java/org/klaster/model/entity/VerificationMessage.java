package org.klaster.model.entity;

/**
 * VerificationMessage
 *
 * @author Nikita Lepesevich
 */

public class VerificationMessage {

  private final User author;
  private final String documentNumber;
  private final String documentName;
  private final FileInfo documentScan;
  private Administrator consideredBy;

  public VerificationMessage(User author, String documentName, String documentNumber, FileInfo documentScan) {
    this.author = author;
    this.documentName = documentName;
    this.documentNumber = documentNumber;
    this.documentScan = documentScan;
  }

  public String getDocumentNumber() {
    return documentNumber;
  }

  public FileInfo getDocumentScan() {
    return documentScan;
  }

  public User getAuthor() {
    return author;
  }

  public Administrator getConsideredBy() {
    return consideredBy;
  }

  public void setConsideredBy(Administrator consideredBy) {
    this.consideredBy = consideredBy;
  }

  public boolean isConsidered() {
    return consideredBy != null;
  }

  public String getDocumentName() {
    return documentName;
  }
}
