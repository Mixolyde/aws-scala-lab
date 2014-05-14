package awslabs

import awscala._
import com.amazonaws._

object LabUtility {
  def dumpError(ex: Exception) =
    {
      ex match {
        case ase: AmazonServiceException =>

          println("Caught an AmazonServiceException, which means your request made it AWS,");
          println("but was rejected with an error response for some reason.");
          println("Error Message:    " + ase.getMessage());
          println("HTTP Status Code: " + ase.getStatusCode());
          println("AWS Error Code:   " + ase.getErrorCode());
          println("Error Type:       " + ase.getErrorType());
          println("Request ID:       " + ase.getRequestId());
          println("Stack trace:");
          ase.printStackTrace();
        case ace: AmazonClientException =>
          println("Caught an AmazonClientException, which means the client encountered");
          println("a problem without before communicating with the service.");
          println("Error Message: " + ace.getMessage());
          println("Stack trace:");
          ace.printStackTrace();
        case _ =>
          println("Caught exception [" + ex.getClass() + "].");
          println("Error Message: " + ex.getMessage());
          println("Cause: " + ex.getCause());
          println("Stack trace:");
          ex.printStackTrace();
      }

    }

}

