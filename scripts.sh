
aws --endpoint-url=http://localhost:4566 s3api create-bucket --bucket myamazingbucket --profile localstack
aws --endpoint-url=http://localhost:4566 s3 cp munra.rtf s3://myamazingbucket/ --profile localstack
aws --endpoint-url=http://localhost:4566 ecr create-repository --repository-name read-lyrics --profile localstack
#aws --endpoint-url=http://localhost:4566 lambda create-function --function-name myFunction --role dummy --handler index.handler --code ImageUri=localhost:4566/read-lyrics:latest --profile localstack
#aws --endpoint-url=http://localhost:4566 lambda invoke --function-name myFunction out.txt --profile localstack
#cat out.txt

# Cleanup
#aws --endpoint-url=http://localhost:4566 lambda delete-function --function-name myFunction --profile localstack
#aws --endpoint-url=http://localhost:4566 ecr delete-repository --repository-name read-lyrics --profile localstack
#aws --endpoint-url=http://localhost:4566 s3 rb s3://myamazingbucket --force --profile localstack


