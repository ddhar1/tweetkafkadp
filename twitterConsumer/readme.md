Run pyspark structured streaming consumer with the following command:

```bash
aws emr --profile cincouser \ 
    add-steps --cluster-id j-1DKBHZRVBE8SS \
    --steps Type=spark,Name=job1, \
        Args=[--deploy-mode,cluster,--master,yarn,--conf,spark.yarn.submit.waitAppCompletion=true,--packages,org.apache.spark:spark-sql-kafka-0-10_2.11:2.4.6,s3a://gd-abucket/consumer.py],ActionOnFailure=CONTINUE
```