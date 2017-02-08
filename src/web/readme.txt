
Copy web files to s3:
    aws s3 rm "s3://jlf1/JsonB/web" --recursive
    aws s3 cp "C:\jflight\software\JsonB\src\web" "s3://jlf1/JsonB/web/" --recursive --acl public-read


Url:
    https://s3-us-west-1.amazonaws.com/jlf1/JsonB/web/html/DayReading.htm?month=12&old&psalms
