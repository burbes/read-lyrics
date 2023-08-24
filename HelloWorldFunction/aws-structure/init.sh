aws ecr create-repository --repository-name read-lyric --region us-east-1

aws s3api create-bucket --bucket read-lyrics-bucket --region us-east-1

aws rds create-db-instance \
    --db-name lyricsDB \
    --db-instance-identifier lyricsdb-instance \
    --allocated-storage 20 \
    --db-instance-class db.t2.micro \
    --engine mysql \
    --master-username admin \
    --master-user-password my-secret-pw \
    --publicly-accessible \
    --region us-east-1

(aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 742040112049.dkr.ecr.us-east-1.amazonaws.com)

docker build -t read-lyric .

docker tag read-lyric:latest 742040112049.dkr.ecr.us-east-1.amazonaws.com/read-lyric:latest

docker push 742040112049.dkr.ecr.us-east-1.amazonaws.com/read-lyric:latest



sam package \
    --template-file ../template.yaml \
    --output-template-file packaged.yaml \
    --resolve-s3 read-lyrics-bucket

sam deploy \
    --template-file packaged.yaml \
    --stack-name HelloWorldLambdaStack \
    --capabilities CAPABILITY_IAM \
    --region us-east-1

# Create policy using bucket-policy.json file
aws iam create-policy \
    --policy-name LambdaS3RDSAccessPolicy \
    --policy-document file://policy.json



# Get the full ARN of the role
FULL_ROLE_ARN=$(aws cloudformation describe-stacks --stack-name HelloWorldLambdaStack --query "Stacks[0].Outputs[?OutputKey=='HelloWorldFunctionIamRole'].OutputValue" --output text)

# Extract the role name from the ARN
ROLE_NAME=$(echo $FULL_ROLE_ARN | awk -F/ '{print $NF}')

aws iam attach-role-policy \
    --role-name $ROLE_NAME \
    --policy-arn arn:aws:iam::742040112049:policy/LambdaS3RDSAccessPolicy


FUNCTION_NAME=$(aws cloudformation describe-stacks --stack-name HelloWorldLambdaStack --query "Stacks[0].Outputs[?OutputKey=='HelloWorldFunction'].OutputValue" --output text | awk -F ':' '{print $7}')

aws lambda invoke \
    --function-name $FUNCTION_NAME \
    --payload file://payload_base64.txt \
    response.json

#docker buildx build --platform linux/amd64 -t read-lyric .
#docker tag read-lyric:latest 742040112049.dkr.ecr.us-east-1.amazonaws.com/read-lyric:latest
#docker push 742040112049.dkr.ecr.us-east-1.amazonaws.com/read-lyric:latest
