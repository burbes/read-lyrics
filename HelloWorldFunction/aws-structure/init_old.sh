
# Create ECR repository
aws ecr create-repository --repository-name read-lyric

#S3 Bucket
aws s3api create-bucket --bucket read-lyrics-bucket --region us-east-1

# RDS Mysql Setup
aws rds create-db-instance \
    --db-instance-identifier lyricsdb \
    --db-instance-class db.t2.micro \
    --engine mysql \
    --master-username admin \
    --master-user-password my-secret-pw \
    --allocated-storage 20 \
    --region us-east-1

# Deploying
sam package \
    --template-file ../template.yaml \
    --output-template-file packaged.yaml \
    --resolve-s3 read-lyrics-bucket

# Deploy the Lambda Function
sam deploy \
    --template-file ../template.yaml \
    --stack-name HelloWorldLambdaStack \
    --capabilities CAPABILITY_IAM \
    --region us-east-1


# API Gateway Setup
# Get the full ARN of the role
FULL_ROLE_ARN=$(aws cloudformation describe-stacks --stack-name HelloWorldLambdaStack --query "Stacks[0].Outputs[?OutputKey=='HelloWorldFunctionIamRole'].OutputValue" --output text)

# Extract the role name from the ARN
ROLE_NAME=$(echo $FULL_ROLE_ARN | awk -F/ '{print $NF}')

# Attach the S3 read-only policy to the role
aws iam attach-role-policy \
    --role-name $ROLE_NAME \
    --policy-arn arn:aws:iam::aws:policy/AmazonS3ReadOnlyAccess

# Attach the RDS full access policy to the role
aws iam attach-role-policy \
    --role-name $ROLE_NAME \
    --policy-arn arn:aws:iam::aws:policy/AmazonRDSDataFullAccess


