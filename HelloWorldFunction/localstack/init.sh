
# Create an s3 bucket
aws --endpoint-url=http://localhost:4566 s3api create-bucket --bucket read-lyrics-bucket

# Create the Role
aws --endpoint-url=http://localhost:4566 iam create-role --role-name lambda-s3-rds-role --assume-role-policy-document file://trust-policy.json

#Attach the policy to the role
aws --endpoint-url=http://localhost:4566 iam create-policy --policy-name lambda-s3-rds-policy --policy-document file://permissions-policy.json
aws --endpoint-url=http://localhost:4566 iam attach-role-policy --role-name lambda-s3-rds-role --policy-arn arn:aws:iam::000000000000:policy/lambda-s3-rds-policy

# Package the Jar
cd .. && mvn clean package -DskipTests dependency:copy-dependencies -DincludeScope=runtime

# Copy the Jar to a S3 Bucket
aws --endpoint-url=http://localhost:4566 s3api create-bucket --bucket my-lambda-jars
aws --endpoint-url=http://localhost:4566 s3 cp ../target/HelloWorld.jar s3://my-lambda-jars/

# Create the function using s3 bucket
aws --endpoint-url=http://localhost:4566 lambda create-function --function-name HelloWorld --code S3Bucket=my-lambda-jars,S3Key=HelloWorld.jar --handler helloworld.App --runtime java11 --role arn:aws:iam::000000000000:role/lambda-s3-rds-role

# Invoke (inside lambda folder)
aws --endpoint-url=http://localhost:4566 lambda invoke --function-name HelloWorld --payload file://payload_base64.txt response.json


#aws --endpoint http://localhost:4566 --profile localstack iam create-role --role-name lambda-execution --assume-role-policy-document "{\"Version\": \"2012-10-17\",\"Statement\": [{ \"Effect\": \"Allow\", \"Principal\": {\"Service\": \"lambda.amazonaws.com\"}, \"Action\": \"sts:AssumeRole\"}]}"
#aws --endpoint http://localhost:4566 --profile localstack iam attach-role-policy --role-name lambda-execution --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
#aws --endpoint http://localhost:4566 --profile localstack lambda create-function --function-name HelloWorld --zip-file fileb://HelloWorld-1.0.jar --handler helloworld.App --runtime java11 --role arn:aws:iam::000000000000:role/lambda-execution
#aws --endpoint http://localhost:4566 --profile localstack lambda invoke --function-name HelloWorld out.txt --log-type Tail
#aws --endpoint-url=http://localhost:4566 lambda invoke --function-name HelloWorld response.json
#aws --endpoint-url=http://localhost:4566 lambda invoke --function-name HelloWorld --payload '{"body": "{}", "headers": {"Content-Type": "application/json"}}' response.json

