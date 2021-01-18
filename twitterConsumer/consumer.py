from pyspark.sql import SparkSession
import pyspark.sql.functions as f
from pyspark.sql import types as T

# Schema of Tweet and Tweet Metadata from Twitter API
json_schema =StructType([
    StructField("data", 
        StructType([ 
                    StructField( "created_at", StringType(),  nullable = False )
                    , StructField( "id", StringType(),  nullable = False)
                    , StructField("text", StringType(),  nullable = False)
               ])  
        , nullable = False)
    , StructField( "matching_rules",
             ArrayType(StructType([ 
                    StructField("id", StringType(), nullable = False)
                , StructField("tag", StringType(), nullable= False)
                ])
                )
            )
    ])
    
    
spark = SparkSession \
    .builder \
    .appName("StructuredNetworkWordCount") \
    .getOrCreate()

# Start Spark Structured Streaming 
df = spark \
  .readStream \
  .format("kafka") \
  .option("kafka.bootstrap.servers", "34.213.179.50:9092,34.220.78.168:9092,34.210.71.28:9092") \
  .option("subscribe", "twitter_on_stocks") \
  .load()

tweets = df.selectExpr("CAST(value) AS str")

tweets = tweets.select( f.from_json("value", json_schema).alias("value")  )

tweets = tweets.selectExpr( "CAST(value.data.id as BIGINT) AS id", "value.data.created_at AS created_at", "CAST(value.data.text as STRING) AS text", "LOWER(CAST(value.matching_rules.tag[0] AS STRING)) as company"   )

# Count words in the tweet
tweets = tweets.withColumn( "word_count",  f.size(f.split(f.col('text'), ' '))  )

# Save set of data pulled from Kafka every 15 seconds
df.writeStream.format("parquet"). \
    option("checkpointLocation", "s3n://tweets1/checkpoints/"). \
    option("output", "s3n://tweets1/data/")
    outputMode('append'). \
    trigger(processingTime='15 seconds'). \
    partitionBy('company')