#!/bin/bash

# Disable AWS CLI pager
export AWS_PAGER=""

cd .. && mvn clean package dependency:copy-dependencies -DincludeScope=runtime -DskipTests

if [ "$1" == "init" ]; then
    # Create Lambda Function
    aws --endpoint http://localhost:4566 --profile localstack lambda create-function --function-name HelloWorld --zip-file fileb://target/HelloWorld.jar --handler helloworld.App --runtime java11 --role arn:aws:iam::000000000000:role/lambda-execution
else
    # Update Lambda Function
    pwd && aws --endpoint-url=http://localhost:4566 lambda update-function-code --function-name HelloWorld --zip-file fileb://target/HelloWorld.jar --publish
fi

# Invoke
aws --endpoint-url=http://localhost:4566 lambda invoke --function-name HelloWorld --payload file://scripts/payload_base64.txt ./scripts/out/response.json

cat scripts/out/response.json
