# Build the project
cd .. && mvn clean package -DskipTests dependency:copy-dependencies -DincludeScope=runtime

# Try to pull the latest image. If it fails, log into ECR.
docker pull 742040112049.dkr.ecr.us-east-1.amazonaws.com/read-lyric:latest || \
(aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 742040112049.dkr.ecr.us-east-1.amazonaws.com)

# Build the Docker image
docker build --build-arg ENVIRONMENT=AWS -t read-lyric .

# Tag the Docker image
docker tag read-lyric:latest 742040112049.dkr.ecr.us-east-1.amazonaws.com/read-lyric:latest

# Push the Docker image
docker push 742040112049.dkr.ecr.us-east-1.amazonaws.com/read-lyric:latest
