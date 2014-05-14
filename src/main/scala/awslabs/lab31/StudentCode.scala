package awslabs.lab31

import awscala._, sqs._
import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.Message

class StudentCode extends ILabCode with IOptionalLabCode {
  @Override
  def createQueue(sqsClient: AmazonSQSClient, queueName: String): String = {
    return ""
  }
  @Override
  def getQueueArn(sqsClient: AmazonSQSClient, queueUrl: String): String = {
    return ""
  }

  @Override
  def createTopic(snsClient: AmazonSNSClient, topicName: String): String = {
    "topic"
  }

  @Override
  def createSubscription(snsClient: AmazonSNSClient, queueArn: String, topicArn: String) = {

  }

  @Override
  def publishTopicMessage(snsClient: AmazonSNSClient,
    topicArn: String,
    subject: String,
    message: String) = {

  }
  @Override
  def postToQueue(sqsClient: AmazonSQSClient, queueUrl: String, messageText: String) = {

  }
  @Override
  def readMessages(sqsClient: AmazonSQSClient, queueUrl: String): java.util.List[com.amazonaws.services.sqs.model.Message] = {
    new java.util.ArrayList[com.amazonaws.services.sqs.model.Message]()

  }
  @Override
  def removeMessage(sqsClient: AmazonSQSClient, queueUrl: String, receiptHandle: String) = {

  }
  @Override
  def deleteSubscriptions(snsClient: AmazonSNSClient, topicArn: String) = {

  }
  @Override
  def deleteTopic(snsClient: AmazonSNSClient, topicArn: String) = {

  }
  @Override
  def deleteQueue(sqsClient: AmazonSQSClient, queueUrl: String) = {

  }

  @Override
  def grantNotificationPermission(sqsClient: AmazonSQSClient, queueArn: String, queueUrl: String, topicArn: String) = {

  }

}