import com.mongodb.casbah.Imports.MongoClient
import com.mongodb.casbah.MongoClientURI
import com.mongodb.casbah.commons.MongoDBObject
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient

import java.net.InetAddress

case class Game(id: Int, name: String, description: String, publisher: String, publishTime:String,  platforms: String, genres: String, tags: String, price: String, imgUrl: String)

case class Rating()

case class Tag()

/**
 * MongoDB的连接配置
 *
 * @param uri MongoDB的连接
 * @param db  MongoDB操作的数据库
 */
case class MongoConfig(uri: String, db: String)

/**
 * ElasticSearch的连接配置
 *
 * @param httpHosts      http的主机列表
 * @param transportHosts transport的主机列表
 * @param index          需要操作的索引
 * @param clustername    ES集群的名称
 */
case class ESConfig(httpHosts: String, transportHosts: String, index: String,
                    clustername: String)

/**
 * 加载数据
 */
object DataLoader {

  // 数据集的绝对路径
  val GAME_DATA_PATH = "C:\\Users\\STio\\IdeaProjects\\com.syh.whattoplay\\recommender\\DataLoader\\src\\main\\resources\\steam.csv"

  // MongoDB中的表名
  val MONGODB_GAME_COLLECTION = "Game"
  //  ES中的表名
  val ES_GAME_INDEX = "Game"

  def main(args: Array[String]): Unit = {

    /**
     * 设置配置信息
     */
    val config = Map(
      "spark.cores" -> "local[*]",
      "mongo.uri" -> "mongodb://127.0.0.1:27017/recommender",
      "mongo.db" -> "recommender",
      "es.httpHosts" -> "127.0.0.1:9200",
      "es.transportHosts" -> "127.0.0.1:9300",
      "es.index" -> "recommender",
      "es.cluster.name" -> "elasticsearch"
    )

    /**
     * 创建spark配置
     */
    // 创建sparkConf配置
    val sparkConf = new SparkConf().setMaster(config.get("spark.cores").get).setAppName("DataLoader")
    // 创建sparkSession
    val spark = SparkSession.builder().config(sparkConf).getOrCreate()

    // 隐式转化
    import spark.implicits._

    /**
     * 加载数据集
     */
    val gameRDD = spark.sparkContext.textFile(GAME_DATA_PATH)
//    val descRDD = spark.sparkContext.textFile(DESC_DATA_PATH)
//    val mediaRDD = spark.sparkContext.textFile(MEDIA_DATA_PATH)

    /**
     * 将RDD转换为DataFrame
     */

    // 将movieRDD转化为DataFrame
    val gameDF = gameRDD.map(item => {
      // 通过,分割数据
      val attr = item.split(",")
      Game(attr(2).toInt, attr(4).trim, attr(0).trim, attr(8).trim,
        attr(7).trim, attr(5).trim, attr(1).trim, attr(9).trim, attr(6).trim, attr(3).trim)
    }).toDF()


    import org.apache.spark.sql.functions._

    implicit val mongoConfig = MongoConfig(config.get("mongo.uri").get, config.get("mongo.db").get)

    // 将DF数据存入MongoDB
//    storeDataInMongoDB(gameDF)

    /**
     * 声明ES配置的隐式参数
     */
    implicit val esConfig = ESConfig(
      config.get("es.httpHosts").get,
      config.get("es.transportHosts").get,
      config.get("es.index").get,
      config.get("es.cluster.name").get)

    // 将处理后的新数据存入ES
    storeDataInES(gameDF)(esConfig)

    // 关闭spark连接
    spark.stop()

  }

  /**
   * 将数据写入MongoDB
   *
   * @param gameDF
   * @param ratingDF
   * @param tagDF
   * @param mongoConfig
   */
  def storeDataInMongoDB(gameDF: DataFrame)
                        (implicit mongoConfig: MongoConfig): Unit = {
    /**
     * 新建MongoDB连接
     */
    val mongoClient = MongoClient(MongoClientURI(mongoConfig.uri))

    /**
     * 如果Mongodb中表已经存在则删除否则创建
     */
    mongoClient(mongoConfig.db)(MONGODB_GAME_COLLECTION).dropCollection()
//    mongoClient(mongoConfig.db)(MONGODB_RATING_COLLECTION).dropCollection()
//    mongoClient(mongoConfig.db)(MONGODB_TAG_COLLECTION).dropCollection()

    /**
     * 将DF数据写入Mongodb数据库
     */
    gameDF
      .write
      .option("uri", mongoConfig.uri)
      .option("collection", MONGODB_GAME_COLLECTION)
      .mode("overwrite")
      .format("com.mongodb.spark.sql")
      .save()

//    ratingDF
//      .write
//      .option("uri", mongoConfig.uri)
//      .option("collection", MONGODB_RATING_COLLECTION)
//      .mode("overwrite")
//      .format("com.mongodb.spark.sql")
//      .save()
//
//    tagDF
//      .write
//      .option("uri", mongoConfig.uri)
//      .option("collection", MONGODB_TAG_COLLECTION)
//      .mode("overwrite")
//      .format("com.mongodb.spark.sql")
//      .save()

    /**
     * 对数据库建立索引
     */
    mongoClient(mongoConfig.db)(MONGODB_GAME_COLLECTION).createIndex(MongoDBObject("mid" -> 1))
//    mongoClient(mongoConfig.db)(MONGODB_RATING_COLLECTION).createIndex(MongoDBObject("mid" -> 1))
//    mongoClient(mongoConfig.db)(MONGODB_RATING_COLLECTION).createIndex(MongoDBObject("uid" -> 1))
//    mongoClient(mongoConfig.db)(MONGODB_TAG_COLLECTION).createIndex(MongoDBObject("mid" -> 1))
//    mongoClient(mongoConfig.db)(MONGODB_TAG_COLLECTION).createIndex(MongoDBObject("uid" -> 1))

    /**
     * MongoDB关闭连接
     */
    mongoClient.close()
  }

  /**
   * 将数据存入Elasticsearch
   *
   * @param movieWithTagsDF
   * @param esConfig
   */
  def storeDataInES(gameDF: DataFrame)(implicit esConfig: ESConfig): Unit = {
    /**
     * 新建ES配置
     */
    val settings: Settings = Settings.builder()
      .put("cluster.name", esConfig.clustername).build()

    /**
     * 新建ES客户端
     */
    val esClient = new PreBuiltTransportClient(settings)
    // 将TransportHosts添加到esClient中
    val REGEX_HOST_PORT = "(.+):(\\d+)".r
    esConfig.transportHosts.split(",").foreach {
      case REGEX_HOST_PORT(host: String, port: String) => {
        esClient.addTransportAddress(new
            InetSocketTransportAddress(InetAddress.getByName(host), port.toInt))
      }
    }

    /**
     * 清除ES中遗留的数据
     */
    if (esClient.admin().indices().exists(new IndicesExistsRequest(esConfig.index)).actionGet().isExists) {
      esClient.admin().indices().delete(new DeleteIndexRequest(esConfig.index))
    }

    esClient.admin().indices().create(new CreateIndexRequest(esConfig.index))

    /**
     * 将数据写入ES
     */
    gameDF
      .write
      .option("es.nodes", esConfig.httpHosts)
      .option("es.http.timeout", "100m")
      .option("es.mapping.id", "id")
      .mode("overwrite")
      .format("org.elasticsearch.spark.sql")
      .save(esConfig.index + "/" + ES_GAME_INDEX)
  }

}