package awslabs.labs21

import awscala._
import awscala.s3._
import java.util.UUID
import org.joda.time.DateTime
import com.amazonaws.auth._

object Lab21 extends App {
  val classpathCreds = new ClasspathPropertiesFileCredentialsProvider()

  val labCreds = Credentials(classpathCreds.getCredentials().getAWSAccessKeyId(),
    classpathCreds.getCredentials().getAWSSecretKey())

  implicit val s3 = S3(labCreds)

  val buckets: Seq[Bucket] = s3.buckets

  val bucketname = "awslabs" + UUID.randomUUID().toString().substring(0, 8)
  val bucket: Bucket = s3.createBucket(bucketname)

  val summaries: Seq[S3ObjectSummary] = bucket.objectSummaries

  bucket.put("test-image.png", new java.io.File("test-image.png"))

  val s3obj: Option[S3Object] = bucket.getObject("test-image.png")

  s3obj.foreach { obj =>
    println("publicUrl: " + obj.publicUrl) // http://unique-name-xxx.s3.amazonaws.com/sample.txt

    val mytime = DateTime.now.plusMinutes(60)

    println("PresignedUrl: " + obj.generatePresignedUrl(mytime)) // ?Expires=....

    bucket.delete(obj) // or obj.destroy()
  }
}