package stockcharts.kafka

import kafka.admin.AdminUtils
import kafka.utils.ZkUtils
import org.slf4j.LoggerFactory
import stockcharts.{Config, KafkaTopic}

import scala.util.Try

trait KafkaSupport {

  private val log = LoggerFactory.getLogger(this.getClass)

  private val sessionTimeoutMs = 10 * 1000
  private val connectionTimeoutMs = 8 * 1000

  def withZkUtils[T](action: ZkUtils => T) = {
    val zkUtils = ZkUtils(Config.ZooKeeper.serverUrl, sessionTimeoutMs, connectionTimeoutMs, isZkSecurityEnabled = false)
    val res = Try(action(zkUtils))
    zkUtils.close()
    res
  }

  def initTopics(topics: Seq[KafkaTopic]) = withZkUtils { zkUtils =>
    topics.foreach { topic =>
      AdminUtils.createTopic(zkUtils, topic.name, topic.partitions, topic.replicationFactor)
      log.debug(s"$topic initialized")
    }
  }

}
