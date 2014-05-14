package awslabs.labs21

import awscala._
import awscala.s3._

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
	@Override
	public void createBucket(AmazonS3 s3Client, String bucketName, Region region) {
		com.amazonaws.services.s3.model.Region s3Region = com.amazonaws.services.s3.model.Region.US_Standard;

		s3Client.createBucket(bucketName, s3Region);
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
	@Override
	public void putObject(AmazonS3 s3Client, String bucketName, String sourceFileName, String objectKey) {
		File sourceFile = new File(sourceFileName);

		s3Client.putObject(bucketName, objectKey, sourceFile);
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
	@Override
	public void listObjects(AmazonS3 s3Client, String bucketName) {
		ObjectListing listing = s3Client.listObjects(bucketName);

		for (S3ObjectSummary summary : listing.getObjectSummaries()) {
			System.out.println("Summary key: " + summary.getKey());
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
	@Override
	public void makeObjectPublic(AmazonS3 s3Client, String bucketName, String key) {

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
	@Override
	public String generatePreSignedUrl(AmazonS3 s3Client, String bucketName, String key) {
		// TODO: Replace this call to the super class with your own
		// implementation of the method.
		return s3Client.generatePresignedUrl(bucketName, key, new Date(System.currentTimeMillis() + 3600000L)).toString();

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
	@Override
	public void deleteBucket(AmazonS3 s3Client, String bucketName) {

		ObjectListing listing = s3Client.listObjects(bucketName);
		if (listing.getObjectSummaries().size() > 0) {
			// If we got here, the bucket isn't empty, so delete the contents
			// and try again.
			List<KeyVersion> keys = new ArrayList<KeyVersion>();
			for (S3ObjectSummary obj : s3Client.listObjects(bucketName).getObjectSummaries()) {
				// Add the keys to our list of object.
				keys.add(new KeyVersion(obj.getKey()));
			}
			// Create the request to delete the objects.
			DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
			deleteObjectsRequest.withKeys(keys);
			// Submit the delete objects request.
			s3Client.deleteObjects(deleteObjectsRequest);

		}
		s3Client.deleteBucket(bucketName);
	}
}