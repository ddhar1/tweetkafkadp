Before uploading to  consumer to s3, modify the following variables
* `S3_CHECKPOINT_LOCATION`: is the location where spark structured streaming will record kafka offset consumption progress
* `S3_PARQUET_SINK`: is the location in S3 where the parquet files will be saved to 
* `BOOTSTRAP_SERVERS`: url:host of kafka brokers

When ready to run consumer, Upload the consumer to a location in S3. Then run pyspark structured streaming consumer with the following command. Make sure to replace `s3a://gd-abucket/consumer.py` with wherever you put the consumer, as well as `j-C1ZX17BF180B` with the cluster you made with terraform. You can either look the cluster in the tf state file.

```bash
aws emr --profile cincouser add-steps --cluster-id j-C1ZX17BF180B --steps Type=spark,Name=job1,Args=[--deploy-mode,cluster,--master,yarn,--conf,spark.yarn.submit.waitAppCompletion=true,--packages,org.apache.spark:spark-sql-kafka-0-10_2.12:3.0.1,s3a://gd-abucket/consumer.py],ActionOnFailure=CONTINUE
```

