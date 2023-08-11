
aws --endpoint-url=http://localhost:4566 s3api create-bucket --bucket myamazingbucket --profile localstack
aws --endpoint-url=http://localhost:4566 s3 cp munra.rtf s3://myamazingbucket/ --profile localstack
aws --endpoint-url=http://localhost:4566 ecr create-repository --repository-name read-lyrics --profile localstack