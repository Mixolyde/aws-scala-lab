package awslabs.lab21

import awscala._
import awscala.s3._
import com.amazonaws.services.s3.model.CannedAccessControlList

/**
 * Project: Lab2.1
 *
 * The primary purpose of this lab is to gain experience working with S3
 * programmatically.
 */
class StudentCode {
  /**
   * Use the provided S3 client object to create the specified bucket. Hint:
   * Use the createBucket() method of the client object. If the region is
   * anything other than us-east-1, it needs to be explicitly specified in the
   * request.
   *
   * @param s3Client
   *            The S3 client object.
   * @param bucketName
   *            The name of the bucket to create.
   */
  def createBucket(s3Client: S3, bucketName: String, region: Region) {
    s3Client.at(region)
    s3Client.createBucket(bucketName)
  }

  /**
   * Upload the provided item to the specified bucket. Hint: Use the
   * putObject() method of the client object.
   *
   * @param s3Client
   *            The S3 client object.
   * @param bucketName
   *            The name of the target bucket.
   * @param sourceFile
   *            The name of the file to upload.
   * @param objectKey
   *            The key to assign to the new S3 object.
   */
  def putObject(s3Client: S3, bucketName: String, sourceFileName: String, objectKey: String) {
    val sourceFile = new File(sourceFileName)

    s3Client.putObject(bucketName, objectKey, sourceFile)
  }

  /**
   * List the contents of the specified bucket by writing the object key and
   * item size to the console. Hint: Use the listObjects() method of the
   * client object.
   *
   * @param s3Client
   *            The S3 client object.
   * @param bucketName
   *            The name of the bucket containing the objects to list.
   */
  def listObjects(s3Client: S3, bucketName: String) {
    val listing = s3Client.objectSummaries(Bucket(bucketName))
    for (summary <- listing) {
      println("Summary key: " + summary.getKey())
    }

  }

  /**
   * Change the ACL for the specified object to make it publicly readable.
   * Hint: Call the setObjectAcl() method of the client object. Use the
   * CannedAccessControlList enumeration to set the ACL for the object to
   * PublicRead.
   *
   * @param s3Client
   *            The S3 client object.
   * @param bucketName
   *            The name of the bucket containing the object.
   * @param key
   *            The key used to identify the object.
   */
  def makeObjectPublic(s3Client: S3, bucketName: String, key: String) {

    s3Client.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);
  }

  /**
   * Create and return a pre-signed URL for the specified item. Set the URL to
   * expire one hour from the moment it was generated. Hint: Use the
   * generatePresignedUrl() method of the client object.
   *
   * @param s3Client
   *            The S3 client object.
   * @param bucketName
   *            The name of the bucket containing the object.
   * @param key
   *            The key used to identify the object.
   * @return The pre-signed URL for the object.
   */
  def generatePreSignedUrl(s3Client: S3, bucketName: String, key: String) {
    // TODO: Replace this call to the super class with your own
    // implementation of the method.
    s3Client.generatePresignedUrl(bucketName, key,
      new java.util.Date(System.currentTimeMillis() + 3600000L)).toString();

  }

  /**
   * Delete the specified bucket. You will use the deleteBucket() method of
   * the client object to delete the bucket, but first you will need to delete
   * the bucket contents. To delete the contents, you will need to list the
   * objects and delete them individually (DeleteObject() method) or as a
   * batch (DeleteObjects() method).
   *
   * The purpose of this task is to gain experience writing applications that
   * remove unused AWS resources in an automated manner.
   *
   * @param s3Client
   *            The S3 client object.
   * @param bucketName
   *            The name of the bucket to delete.
   */
  def deleteBucket(s3Client: S3, bucketName: String) {
    for (summary <- s3Client.objectSummaries(Bucket(bucketName))) {
      println("Deleting key: " + summary.getKey())
      s3Client.deleteObject(bucketName, summary.getKey())
    }

    s3Client.deleteBucket(bucketName);
  }
}