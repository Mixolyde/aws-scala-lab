package awslabs.lab31

import awscala._, sqs._
import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.Message
import scala.collection.JavaConverters._

import com.amazonaws.auth.policy._
import com.amazonaws.auth.policy.actions.SQSActions
import com.amazonaws.auth.policy.conditions.ConditionFactory;
import com.amazonaws.services.sqs.model._
import com.amazonaws.auth._

class StudentCode extends ILabCode with IOptionalLabCode {
  val classpathCreds = new ClasspathPropertiesFileCredentialsProvider().getCredentials()
  implicit val credentials = Credentials(classpathCreds.getAWSAccessKeyId(),
    classpathCreds.getAWSSecretKey())
  val sqs = SQS(credentials)

  @Override
  def createQueue(sqsClient: AmazonSQSClient, queueName: String): String = {
    val result = sqsClient.createQueue(queueName)
    result getQueueUrl
  }
  @Override
  def getQueueArn(sqsClient: AmazonSQSClient, queueUrl: String): String = {
    val attr = "QueueArn"

    val result = sqsClient.getQueueAttributes(queueUrl, List(attr).asJava)
    result.getAttributes().get(attr);
  }

  @Override
  def createTopic(snsClient: AmazonSNSClient, topicName: String): String = {
    val result = snsClient.createTopic(topicName)
    result.getTopicArn()
  }

  @Override
  def createSubscription(snsClient: AmazonSNSClient, queueArn: String, topicArn: String) = {
    snsClient.subscribe(topicArn, "sqs", queueArn)

  }

  @Override
  def publishTopicMessage(snsClient: AmazonSNSClient,
    topicArn: String,
    subject: String,
    message: String) = {

    snsClient.publish(topicArn, message, subject)

  }
  @Override
  def postToQueue(sqsClient: AmazonSQSClient, queueUrl: String, messageText: String) = {
    sqs.send(Queue(queueUrl), messageText)

  }
  @Override
  def readMessages(sqsClient: AmazonSQSClient, queueUrl: String): java.util.List[com.amazonaws.services.sqs.model.Message] = {
    val msgs = sqs.receive(Queue(queueUrl))
    msgs.asJava.asInstanceOf[java.util.List[com.amazonaws.services.sqs.model.Message]]

  }

  @Override
  def removeMessage(sqsClient: AmazonSQSClient, queueUrl: String, receiptHandle: String) = {
    sqs.deleteMessage(queueUrl, receiptHandle)
  }

  @Override
  def deleteSubscriptions(snsClient: AmazonSNSClient, topicArn: String) = {
    val subs = snsClient.listSubscriptionsByTopic(topicArn).getSubscriptions()

    for (sub <- subs.asScala) { snsClient.unsubscribe(sub.getSubscriptionArn()) }

  }
  @Override
  def deleteTopic(snsClient: AmazonSNSClient, topicArn: String) = {
    snsClient.deleteTopic(topicArn)

  }
  @Override
  def deleteQueue(sqsClient: AmazonSQSClient, queueUrl: String) = {
    sqs.delete(Queue(queueUrl))

  }

  @Override
  def grantNotificationPermission(sqsClient: AmazonSQSClient, queueArn: String, queueUrl: String, topicArn: String) = {
    val statement = new com.amazonaws.auth.policy.Statement(Effect.Allow)
      .withActions(SQSActions.SendMessage)
      .withPrincipals(new Principal("*"))
      .withConditions(ConditionFactory.newSourceArnCondition(topicArn))
      .withResources(new com.amazonaws.auth.policy.Resource(queueArn));

    val policy = new com.amazonaws.auth.policy.Policy("SubscriptionPermission").withStatements(statement);

    val attributes = new java.util.HashMap[String, String]();
    attributes.put("Policy", policy.toJson());

    // Create the request to set the queue attributes for policy
    val setQueueAttributesRequest = new SetQueueAttributesRequest().withQueueUrl(queueUrl)
      .withAttributes(attributes);

    // Set the queue policy
    sqsClient.setQueueAttributes(setQueueAttributesRequest);
  }

}